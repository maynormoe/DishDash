package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.Setmeal;
import com.maynormoe.takeout.mapper.SetmealMapper;
import com.maynormoe.takeout.service.SetmealService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
