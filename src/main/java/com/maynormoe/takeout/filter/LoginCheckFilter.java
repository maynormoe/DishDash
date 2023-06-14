package com.maynormoe.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.maynormoe.takeout.common.Results;
import com.maynormoe.takeout.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查登录
 *
 * @author Maynormoe
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    /**
     * 路径匹配器
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求： {}", request.getRequestURI());
        // 获取本次请求url
        String requestUrl = request.getRequestURI();

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        // 判断本次请求需不要处理
        boolean check = check(urls, requestUrl);
        if (check) {
            log.info("本次请求{}不需要处理", requestUrl);
            filterChain.doFilter(request, response);
            return;
        }
        // 判断登录状态，已登录直接放行(后台管理)
        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("用户已经登录,用户id{}", empId);
            filterChain.doFilter(request, response);
            return;
        }

        // 判断登录状态，已登录直接放行(移动端)
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            log.info("用户已经登录,用户id{}", userId);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //未登录响应数据
        response.getWriter().write(JSON.toJSONString(Results.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配
     *
     * @param requestUrl 拦截的url
     * @param urls       默认放行的url
     * @return boolean
     */
    public boolean check(String[] urls, String requestUrl) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match) {
                return true;
            }
        }
        return false;
    }

    ;
}
