package com.summer.web.controller.userManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 登录拦截器配置
 */
@Configuration
public class LoginInterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //所有已api开头的访问都要进入LoginInterceptor拦截器进行登录验证，并排除login接口(全路径)。必须写成链式，分别设置的话会创建多个拦截器。
        //必须写成getLoginInterceptor()，否则LoginInterceptor中的@Autowired会无效
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/user/login");
        super.addInterceptors(registry);
    }

}
