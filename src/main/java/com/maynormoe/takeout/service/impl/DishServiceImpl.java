package com.maynormoe.takeout.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.mapper.DishMapper;
import com.maynormoe.takeout.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
