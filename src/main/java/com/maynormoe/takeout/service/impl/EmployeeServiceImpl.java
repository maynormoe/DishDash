package com.maynormoe.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maynormoe.takeout.entity.Employee;
import com.maynormoe.takeout.mapper.EmployeeMapper;
import com.maynormoe.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Maynormoe
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
