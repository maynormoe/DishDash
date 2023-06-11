package com.maynormoe.takeout.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.dto.DishDto;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.entity.DishFlavor;
import com.maynormoe.takeout.mapper.DishMapper;
import com.maynormoe.takeout.service.DishFlavorService;
import com.maynormoe.takeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maynormoe
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，保存口味数据
     *
     * @param dishDto 菜单数据传输对象
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品基本信息
        this.save(dishDto);

        //获取菜品id
        Long dishId = dishDto.getId();

        // 菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //批量保存菜品口味数据
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品和口味数据
     *
     * @return DishDto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜单基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    /**
     * 更新菜品信息
     *
     * @param dishDto 菜品传输对象
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto);
        // 清理当前菜品对应口味数据

        //构造条件
        LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<DishFlavor>();
        dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishDtoLambdaQueryWrapper);
        // 添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
