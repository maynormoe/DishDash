package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.dto.OrdersDto;
import com.maynormoe.takeout.entity.OrderDetail;
import com.maynormoe.takeout.entity.Orders;
import com.maynormoe.takeout.service.OrderDetailService;
import com.maynormoe.takeout.service.OrdersService;
import com.maynormoe.takeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    @GetMapping("/page")
    public Results<Page<Orders>> page(Integer page, Integer pageSize, Long number, String beginTime, String endTime) {
        // 分页构造器
        Page<Orders> pageInfo = new Page<Orders>(page, pageSize);

        // 条件构造器
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<Orders>();
        ordersLambdaQueryWrapper.like(number != null, Orders::getNumber, number);
        ordersLambdaQueryWrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, ordersLambdaQueryWrapper);
        return Results.success(pageInfo);
    }

    /**
     * 用户下单
     *
     * @param orders 菜单
     * @return Results<Orders>
     */
    @PostMapping("/submit")
    public Results<Orders> submit(@RequestBody Orders orders) throws Exception {
        log.info("id为{}的用户下单{}", BaseContext.getCurrentId(), orders);
        ordersService.submit(orders);
        return Results.success(null);
    }

    /**
     * 根据订单id获取订单明细
     *
     * @param orderId 订单id
     * @return List<OrderDetail>
     */
    public List<OrderDetail> getOrderListByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> ordersdetailLambdaQueryWrapper = new LambdaQueryWrapper<OrderDetail>();
        ordersdetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> list = orderDetailService.list(ordersdetailLambdaQueryWrapper);
        return list;
    }

    @GetMapping("/userPage")
    public Results<Page<OrdersDto>> userPage(Integer page, Integer pageSize) {
        // 分页构造器
        Page<Orders> pageInfo = new Page<Orders>(page, pageSize);
        Page<OrdersDto> pageDto = new Page<OrdersDto>(page, pageSize);

        // 获取用户id
        Long userId = BaseContext.getCurrentId();
        // 条件构造器
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<Orders>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, userId);
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, ordersLambdaQueryWrapper);

        // 通过订单id查询对应的orderDetail
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<OrderDetail>();

        // 对OrderDto进行属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            Long orderId = item.getId();
            List<OrderDetail> orderList = getOrderListByOrderId(orderId);
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setOrderDetails(orderList);
            return ordersDto;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        pageDto.setRecords(ordersDtoList);
        return Results.success(pageDto);
    }


    /**
     * 修改订单状态
     *
     * @param orders 订单状态
     * @return Results<Orders>
     */
    @PutMapping
    public Results<Orders> updateStatus(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return Results.success(null);
    }

    /**
     * 再来一单
     *
     * @return Results<OrdersDto>
     */
    @PostMapping("/again")
    public Results<String> again(@RequestBody HashMap<String, String> map) {
        log.info("再来一单{}", map.get("id"));
        ordersService.again(map);
        return Results.success(null);
    }
}

