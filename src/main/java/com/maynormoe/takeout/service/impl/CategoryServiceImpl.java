package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.Category;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.entity.Setmeal;
import com.maynormoe.takeout.mapper.CategoryMapper;
import com.maynormoe.takeout.mapper.DishMapper;
import com.maynormoe.takeout.mapper.SetmealMapper;
import com.maynormoe.takeout.service.CategoryService;
import com.maynormoe.takeout.service.DishService;
import com.maynormoe.takeout.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Maynormoe
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类数据
     *
     * @param id id
     */
    @Override
    public void remove(Long id) throws Exception {

        //分类关联菜品，抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);

        // 分类关联套餐，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        //无关联，直接删除
        if (count > 0) {
            // 关联了菜品抛出异常
            throw new Exception("当前分类已经关联了菜品，不能删除");
        }


        if (count2 > 0) {
            //关联套餐抛出异常
            throw new Exception("当前分类已经关联了套餐，不能删除");
        }

        // 直接删除

        super.removeById(id);
    }
}
