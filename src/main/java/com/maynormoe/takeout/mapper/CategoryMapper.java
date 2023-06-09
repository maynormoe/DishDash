package com.maynormoe.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maynormoe.takeout.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Maynormoe
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
