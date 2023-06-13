package com.maynormoe.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maynormoe.takeout.dto.DishDto;
import com.maynormoe.takeout.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品信息和对应口味信息
     *
     * @param dishDto 菜品传输对象
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品和口味数据
     *
     * @return DishDto
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息
     *
     * @param dishDto 菜品传输对象
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     *
     * @param ids id数组
     */
    public void removeWithDish(Long[] ids) throws Exception;
}
