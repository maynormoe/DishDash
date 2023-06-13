package com.maynormoe.takeout.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.dto.DishDto;
import com.maynormoe.takeout.entity.Category;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.entity.DishFlavor;
import com.maynormoe.takeout.service.CategoryService;
import com.maynormoe.takeout.service.DishFlavorService;
import com.maynormoe.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maynormoe
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param page     page
     * @param pageSize 记录数
     * @param name     菜品名称
     * @return Results<Page < Dish>>
     */
    @GetMapping("/page")
    public Results<Page<DishDto>> page(Integer page, Integer pageSize, String name) {
        log.info("分页查询菜品数据page{},pageSize{}", page, pageSize);
        // 分页构造器
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>();
        // 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        // 添加过滤条件
        dishLambdaQueryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);

        // 添加排序条件
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, dishLambdaQueryWrapper);

        // 对象拷贝 忽略recordsList
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();


        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            // 根据Id查询对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);
        return Results.success(dishDtoPage);
    }


    /**
     * 批量根据id删除菜品和关联口味信息
     *
     * @return Results<Page < Dish>>
     */
    @DeleteMapping
    public Results<Dish> deleteById(Long[] ids) throws Exception {
        log.info("删除id为{}的菜品数据", Arrays.toString(ids));
        dishService.removeWithDish(ids);
        log.info("删除成功");
        return Results.success(null);
    }

    /**
     * 新增菜品
     *
     * @param dishDto 菜品数据对象
     * @return Results<DishDto>
     */
    @PostMapping
    public Results<DishDto> save(@RequestBody DishDto dishDto) {
        log.info("添加菜品数据{}", dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        log.info("新增菜品成功");
        return Results.success(null);
    }

    /**
     * @param id 菜品id
     * @return Results<DishDto>
     */
    @GetMapping("/{id}")
    public Results<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Results.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto 菜品数据对象
     * @return Results<DishDto>
     */
    @PutMapping
    public Results<DishDto> updateById(@RequestBody DishDto dishDto) {
        log.info("修改id为{}的菜品数据{}", dishDto.getId().toString(), dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        log.info("修改菜品成功");
        return Results.success(null);
    }

    /**
     * @param ids    id数组
     * @param status 菜品状态 1起售 2禁售
     * @return Results<Dish>
     */
    @PostMapping("status/{status}")
    public Results<Dish> updateStatusByIds(Long[] ids, @PathVariable Integer status) {
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return Results.success(null);
    }

    /**
     * 根据条件查询对应菜品菜品数据
     *
     * @param dish 菜品
     * @return Results<List < Dish>>
     */
    @GetMapping("/list")
    public Results<List<DishDto>> list(Dish dish) {
        // 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);
        // 排序构造器
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            // 根据Id查询对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            // 当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<DishFlavor>();
            dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishDtoLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return Results.success(dishDtoList);
    }
}
