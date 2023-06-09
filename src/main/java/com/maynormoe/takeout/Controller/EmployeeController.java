package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.Employee;
import com.maynormoe.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Maynormoe
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;


    /**
     * 员工登录
     *
     * @param employee 员工
     * @param request  请求
     * @return Results<Employee>
     */
    @PostMapping("/login")
    public Results<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //        1.md5加密处理
        String password = employee.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        //        2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());

        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);

        //        3.判断有没有查询结果
        if (emp == null) {
            return Results.error("用户或密码错误");
        }
//        4.密码比对
        if (!emp.getPassword().equals(md5Password)) {
            return Results.error("用户或密码错误");
        }
        if (emp.getStatus() == 0) {
            return Results.error("账号已冻结,无法登录");
        }

        // 5.登录成功
        request.getSession().setAttribute("employee", emp.getId());

        return Results.success(emp);
    }

    @PostMapping("/logout")
    public Results<Employee> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Results.success(null);
    }


    @PostMapping
    public Results<Employee> add(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，{}", employee.toString());
        // 设置初始密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        log.info("新增员工成功");

        return Results.success(null);
    }

    @GetMapping("/page")
    public Results<Page<Employee>> page(Integer page, Integer pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> pageInfo = new Page<Employee>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);

        // 执行查询
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return Results.success(pageInfo);
    }


    /**
     *  根据id修改员工信息
     * @return Results<Employee>
     */
    @PutMapping
    public Results<Employee> update(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("修改员工{}的信息为{}", employee.getId(), employee.toString());
        employeeService.updateById(employee);
        return Results.success(null);
    }


    /**
     * 根据id查询员工信息
     * @param id 员工id
     * @return Results<Employee>
     */
    @GetMapping("/{id}")
    public Results<Employee> getById(@PathVariable String id) {
        Employee emp = employeeService.getById(id);
        return Results.success(emp);
    }
}
