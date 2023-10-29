package com.summer.web.controller.userManager;

import com.summer.service.impl.PlateformUserService;
import com.summer.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private PlateformUserService plateformUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 前处理，执行handler之前执行此方法。
        //返回true，放行	false：拦截
        //从请求头中拿到token
        String token = request.getHeader("Authorization");
        //取到token，
        String user = plateformUserService.getUserByToken(Constant.TOKEN + token);
        //没有取到用户信息。登录过期，直接放行。
        if (StringUtils.isBlank(user)) {
            return true;
        }
        //把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行
        // request.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
