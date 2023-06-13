package com.maynormoe.takeout.dto;

import com.maynormoe.takeout.entity.OrderDetail;
import com.maynormoe.takeout.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Maynormoe
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto extends Orders {
    // 订单明细列表
    private List<OrderDetail> orderDetails;
}
