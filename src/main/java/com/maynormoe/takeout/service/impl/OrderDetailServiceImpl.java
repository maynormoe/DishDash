package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.OrderDetail;
import com.maynormoe.takeout.mapper.OrderDetailMapper;
import com.maynormoe.takeout.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
