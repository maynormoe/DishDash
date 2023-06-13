package com.maynormoe.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maynormoe.takeout.entity.ShoppingCart;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */
@Service
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 清空购物车
     */
    public void clean();
}
