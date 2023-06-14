package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.dto.DishDto;
import com.maynormoe.takeout.dto.SetmealDto;
import com.maynormoe.takeout.entity.Category;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.entity.Setmeal;
import com.maynormoe.takeout.entity.SetmealDish;
import com.maynormoe.takeout.service.CategoryService;
import com.maynormoe.takeout.service.DishService;
import com.maynormoe.takeout.service.SetmealDishService;
import com.maynormoe.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private DishService dishService;


    /**
     * 新增菜单
     *
     * @param setmealDto 菜单传输对象
     * @return Results<SetmealDto>
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Results<SetmealDto> save(@RequestBody SetmealDto setmealDto) {
        log.info("添加套餐{}", setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        log.info("添加套餐成功");
        return Results.success(null);
    }

    /**
     * @param page     当前页数
     * @param pageSize 分页
     * @param name     套餐名称
     * @return Results<Page < Setmeal>>
     */
    @GetMapping("/page")
    public Results<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        // 分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        // 条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(!StringUtils.isEmpty(name), Setmeal::getName, name);
        //排序构造器
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            // 根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return Results.success(setmealDtoPage);
    }

    /**
     * 根据id查询套餐及关联菜品信息
     *
     * @param id 套餐id
     * @return Results<SetmealDto>
     */
    @GetMapping("/{id}")
    public Results<SetmealDto> getById(@PathVariable Long id) {
        log.info("查询id为{}的套餐信息", id);
        SetmealDto setmealDish = setmealService.getByIdWithSetmealDish(id);
        log.info("查询成功");
        return Results.success(setmealDish);
    }

    /**
     * 修改套餐
     *
     * @param setmealDto 菜单传输对象
     * @return Results<SetmealDto>
     */
    @PutMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Results<SetmealDto> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改id为{}的套餐信息{}", setmealDto.getId().toString(), setmealDto.toString());
        setmealService.updateWithSetmealDishById(setmealDto);
        log.info("修改套餐成功");
        return Results.success(null);
    }


    /**
     * 修改套餐状态
     *
     * @param status 状态
     * @param ids    id数组
     * @return Results<Setmeal>
     */
    @PostMapping("/status/{status}")
    public Results<Setmeal> updateStatus(@PathVariable Integer status, @RequestParam Long[] ids) {
        log.info("修改id为{}的套餐状态为{}", Arrays.toString(ids), status.toString());
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return Results.success(null);
    }


    /**
     * 删除套餐
     *
     * @param ids ids
     * @return Results<Setmeal>
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Results<Setmeal> delete(@RequestParam Long[] ids) throws Exception {
        log.info("删除id为{}的套餐数据", Arrays.toString(ids));
        setmealService.removeWithSetmealDish(ids);
        log.info("删除成功");
        return Results.success(null);
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public Results<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return Results.success(list);
    }

    /**
     * 根据套餐id查询套餐明细
     *
     * @param id 套餐id
     * @return Results<SetmealDto>
     */
    @GetMapping("/dish/{id}")
    public Results<List<DishDto>> getSetmealDish(@PathVariable Long id) {
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);

        // 查询套餐下的菜品
        List<SetmealDish> setmealDishlist = setmealDishService.list(setmealDishLambdaQueryWrapper);

        List<DishDto> list = setmealDishlist.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        return Results.success(list);
    }
}
