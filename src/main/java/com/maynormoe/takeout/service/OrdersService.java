package com.maynormoe.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maynormoe.takeout.entity.Orders;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Maynormoe
 */

@Service
public interface OrdersService extends IService<Orders> {
    /**
     * 提交菜单
     * @param orders 菜单
     */
    public void submit(Orders orders) throws Exception;

    /**
     * 再来一单
     */
    public void again(HashMap<String,String> map);
}
