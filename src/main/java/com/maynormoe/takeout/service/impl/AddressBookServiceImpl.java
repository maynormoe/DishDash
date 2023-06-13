package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.AddressBook;
import com.maynormoe.takeout.mapper.AddressMapper;
import com.maynormoe.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressMapper, AddressBook> implements AddressBookService {
}
