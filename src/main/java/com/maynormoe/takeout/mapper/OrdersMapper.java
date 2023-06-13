package com.maynormoe.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maynormoe.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Maynormoe
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
