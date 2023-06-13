package com.maynormoe.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maynormoe.takeout.entity.AddressBook;
import com.maynormoe.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Maynormoe
 */

@Mapper
public interface AddressMapper extends BaseMapper<AddressBook> {
}
