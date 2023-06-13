package com.maynormoe.takeout.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.entity.User;
import com.maynormoe.takeout.service.UserService;
import com.maynormoe.takeout.utils.BaseContext;
import com.maynormoe.takeout.utils.RandomNameUtil;
import com.maynormoe.takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author Maynormoe
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/sendMsg")
    public Results<User> sendMsg(@RequestBody User user, HttpSession httpSession) {

        // 获取用户手机号
        String phone = user.getPhone();
        if (!StringUtils.isEmpty(phone)) {
            // 生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码{}", code);
            //  利用阿里云短信服务发验证码
            //  SMSUtils.sendMessage("", "", phone, code);
            // 将手机号和验证码存session中
            httpSession.setAttribute(phone, code);
            return Results.success(null);
        }
        return Results.error("短信发送失败");
    }

    @PostMapping("/login")
    public Results<User> login(@RequestBody HashMap<String, String> map, HttpSession httpSession) {
        log.info(map.toString());
        // 获取手机号
        String phone = map.get("phone");
        // 获取用户输入的验证码
        String code = map.get("code");
        String codeInSession = (String) httpSession.getAttribute(phone);

        // 验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            // 判断用户是否存在
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<User>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                user = new User();
                // 生成随机用户名
                String randomChineseName = RandomNameUtil.getRandomChineseName(4);
                user.setName(randomChineseName);
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user", user.getId());
            return Results.success(user);
        }

        return Results.error("登录失败");
    }

    @PostMapping("/loginout")
    public Results<User> logout(HttpSession httpSession) {
        log.info("id为{}的用户退出登录", BaseContext.getCurrentId());
        // 获取当前登录用户id
        Long userId = BaseContext.getCurrentId();
        // 获取用户登录手机号
        User user = userService.getById(userId);
        String phone = user.getPhone();
        // 在session中移除手机号和验证码
        httpSession.removeAttribute(phone);
        // 在seeion中移除用户id
        httpSession.removeAttribute("user");
        return Results.success(null);
    }
}
