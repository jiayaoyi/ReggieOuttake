package com.jia.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jia.reggie.common.BaseContext;
import com.jia.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求:{}", request.getRequestURI());
        String requestURL = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        //判断需要处理的请求
        boolean check = check(urls, requestURL);
        //不需要处理的请求直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        //判断登录状态
        //1.桌面端登录
        if (request.getSession().getAttribute("employee") != null) {

            Long empId = (Long) request.getSession().getAttribute("employee");
            log.info("用户id{}", empId);
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        //2.移动端登录
        if (request.getSession().getAttribute("user") != null) {

            Long userId = (Long) request.getSession().getAttribute("user");
            log.info("用户id{}", userId);
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        //输出流方式相应前后端数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public Boolean check(String[] urls, String requestURL) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match == true) {
                return true;
            }
        }
        return false;
    }
}
