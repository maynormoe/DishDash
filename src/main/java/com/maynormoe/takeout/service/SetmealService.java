package com.maynormoe.takeout.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.maynormoe.takeout.dto.SetmealDto;
import com.maynormoe.takeout.entity.Setmeal;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存套餐与菜品信息
     * @param setmealDto 套餐传输对象
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id查询套餐和相关菜品信息
     *
     * @param id 套餐id
     * @return
     */
    public SetmealDto getByIdWithSetmealDish(Long id);

    /**
     * 根据id更新套餐数据
     * @param setmealDto 套餐传输对象
     */
    public void updateWithSetmealDishById(SetmealDto setmealDto);

    /**
     * 删除套餐和关联的菜品数据
     * @param ids id数组
     */
    public void removeWithSetmealDish(Long[] ids) throws Exception;
}
