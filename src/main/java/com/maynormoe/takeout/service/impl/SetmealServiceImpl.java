package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.dto.SetmealDto;
import com.maynormoe.takeout.entity.DishFlavor;
import com.maynormoe.takeout.entity.Setmeal;
import com.maynormoe.takeout.entity.SetmealDish;
import com.maynormoe.takeout.mapper.SetmealMapper;
import com.maynormoe.takeout.service.SetmealDishService;
import com.maynormoe.takeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Customizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maynormoe
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 保存套餐与菜品信息
     *
     * @param setmealDto 套餐传输对象
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品关联信息
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 根据id查询套餐和相关菜品信息
     *
     * @param id 套餐id
     * @return
     */
    @Override
    public SetmealDto getByIdWithSetmealDish(Long id) {
        // 查询套餐基本信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        // 根据套餐id查询套餐对应菜品列表
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>();
        // 条件构造器
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        //排序构造器
        setmealDishLambdaQueryWrapper.orderByAsc(SetmealDish::getSort).orderByDesc(SetmealDish::getUpdateTime);

        List<SetmealDish> setmealDishlist = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishlist);
        return setmealDto;
    }

    /**
     * 根据id更新套餐数据
     *
     * @param setmealDto 套餐传输对象
     */
    @Transactional
    @Override
    public void updateWithSetmealDishById(SetmealDto setmealDto) {
        // 修改套餐基本信息
        this.updateById(setmealDto);

        // 清理当前套餐对应的菜品数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        // 保存新的套餐关联菜品数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> list = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(list);
    }

    /**
     * 删除套餐和关联的菜品数据
     *
     * @param ids id数组
     */
    @Transactional
    @Override
    public void removeWithSetmealDish(Long[] ids) throws Exception {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(setmealLambdaQueryWrapper);

        if (count > 0) {
            throw new Exception("套餐正在售卖中不能删除");
        }
        // 如果状态为停售，可以删除
        this.removeByIds(Arrays.asList(ids));

        //删除关系表中数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<SetmealDish>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}
