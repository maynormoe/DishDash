package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.DishFlavor;
import com.maynormoe.takeout.mapper.DishFlavorMapper;
import com.maynormoe.takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
