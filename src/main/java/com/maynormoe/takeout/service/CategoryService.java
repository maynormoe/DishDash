package com.maynormoe.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maynormoe.takeout.entity.Category;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除分类数据
     * @param id id
     */
    public void remove(Long id) throws Exception;
}
