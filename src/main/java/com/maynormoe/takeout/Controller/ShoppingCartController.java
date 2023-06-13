package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.dto.DishDto;
import com.maynormoe.takeout.entity.Category;
import com.maynormoe.takeout.entity.ShoppingCart;
import com.maynormoe.takeout.service.ShoppingCartService;
import com.maynormoe.takeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart 购物车数据
     * @return Results<ShoppingCart>
     */
    @PostMapping("/add")
    public Results<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据{}", shoppingCart);

        // 设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 查询当前菜品或套餐是否在购物车里
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            // 添加到购物券的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            // 添加的是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        if (shoppingCartOne != null) {
            Integer number = shoppingCartOne.getNumber();
            shoppingCartOne.setNumber(number + 1);
            shoppingCartOne.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(shoppingCartOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
        }

        return Results.success(shoppingCartOne);
    }

    /**
     * 查询用户购物车
     *
     * @return Results<ShoppingCart>
     */
    @GetMapping("/list")
    public Results<List<ShoppingCart>> list() {
        log.info("查看购物车..");
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Results.success(list);
    }

    /**
     * 减少购物车菜品
     *
     * @param shoppingCart 购物车
     * @return Results<ShoppingCart>
     */
    @PostMapping("/sub")
    public Results<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (dishId != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
            if (shoppingCartOne.getNumber() == 1) {
                shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
            } else {
                shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
                shoppingCartService.updateById(shoppingCartOne);
            }
            return Results.success(shoppingCartOne);
        }
        if (setmealId != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
            if (shoppingCartOne.getNumber() == 1) {
                shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
            } else {
                shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
                shoppingCartService.updateById(shoppingCartOne);
            }
            return Results.success(shoppingCartOne);
        }
        return Results.error("操作异常");
    }


    /**
     * 清空购物车
     *
     * @return Results<ShoppingCart>
     */
    @DeleteMapping("/clean")
    public Results<ShoppingCart> clean() {
        log.info("id为{}的用户清空购物车", BaseContext.getCurrentId());
        shoppingCartService.clean();
        return Results.success(null);
    }

}
