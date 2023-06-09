package com.maynormoe.takeout.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.Dish;
import com.maynormoe.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Maynormoe
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    /**
     * 分页查询
     *
     * @param page     page
     * @param pageSize 记录数
     * @param name     菜品名称
     * @return Results<Page < Dish>>
     */
    @GetMapping("/page")
    public Results<Page<Dish>> page(Integer page, Integer pageSize, String name) {
        log.info("分页查询菜品数据page{},pageSize{}", page, pageSize);
        // 分页构造器
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);

        dishService.page(pageInfo, dishLambdaQueryWrapper);
        return Results.success(pageInfo);
    }


    /**
     * 批量根据id删除菜品
     *
     * @return Results<Page < Dish>>
     */
    @DeleteMapping
    public Results<Dish> deleteById(Long[] ids) {
        log.info("删除id为{}的菜品数据", Arrays.toString(ids));
        for (Long id : ids) {
            dishService.removeById(id);
        }
        log.info("删除成功");
        return Results.success(null);
    }
}
