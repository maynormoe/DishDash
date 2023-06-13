package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.ShoppingCart;
import com.maynormoe.takeout.mapper.ShoppingCartMapper;
import com.maynormoe.takeout.service.ShoppingCartService;
import com.maynormoe.takeout.utils.BaseContext;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        this.remove(shoppingCartLambdaQueryWrapper);
    }
}
