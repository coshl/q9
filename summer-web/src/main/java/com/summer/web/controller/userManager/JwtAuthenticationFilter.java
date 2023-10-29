package com.summer.web.controller.userManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlatformUser;
import com.summer.api.service.IPlateformUserService;
import com.summer.util.Constant;
import com.summer.util.IPUtils;
import com.summer.util.JwtUtil;
import com.summer.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * jwt令牌检验
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IPlateformUserService plateformUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String ipAdress = IPUtils.getIpAddr(request);
        log.info("jwt令牌检验requestIp:{}，request 目标接口地址:{}",ipAdress,request.getServletPath());
        try {
            if (isProtectedUrl(request) && !isExceededUrl(request)) {
                String token = request.getHeader("Authorization");
                log.info("JwtAuthenticationFilter getToken=" + token);
                //检查jwt令牌, 如果令牌不合法或者过期, 里面会直接抛出异常, 下面的catch部分会直接返回
                Map<String, Object> map = JwtUtil.validateToken(token);
                log.info("JwtAuthenticationFilter map=" + map);
            }

        String token = request.getHeader("Authorization");
        log.info("JwtAuthenticationFilter getToken=" + token);
        //判断用户是否登录
        PlatformUser platformUser = null;
        if (redisUtil.hasKey(Constant.TOKEN + token)) {
            String user = redisUtil.get(Constant.TOKEN + token);
            platformUser = JSONObject.parseObject(user, PlatformUser.class);
            log.info("jwt获取redis平台用户信息:{}", user);
            String phoneNum = platformUser.getPhoneNumber();
            PlatformUser loginInfo = plateformUserService.findByPhoneNumber(phoneNum);
            log.info("jwt获取mysql平台用户信息:{}", JSONObject.toJSONString(loginInfo));
            log.info("loginInfo.getPhoneNumber()={}", loginInfo.getPhoneNumber());
            String userTokenKey = Constant.PLATEFROM_USER_TOKEN + loginInfo.getPhoneNumber();
            redisUtil.set(userTokenKey, token, Constant.PLATEFROM_TOKEN_EXPIRE);
            //将用户信息放到缓存中
            redisUtil.set(Constant.TOKEN + token, JSON.toJSONString(loginInfo), Constant.PLATEFROM_TOKEN_EXPIRE);
        }
        //如果jwt令牌通过了检测, 那么就把request传递给后面的RESTful api
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

    }


    //我们只对地址 /api 开头的api检查jwt. 不然的话登录/login也需要jwt
    private boolean isProtectedUrl(HttpServletRequest request) {
        return pathMatcher.match("/user/**", request.getServletPath());
    }

    private boolean isExceededUrl(HttpServletRequest request) {
        return pathMatcher.match("/api/user/login", request.getServletPath());
    }

}
