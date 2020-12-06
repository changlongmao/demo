package com.example.demo.config;


import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.example.demo.jwt.AuthArgumentResolver;
import com.example.demo.jwt.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web 配置类
 *
 * @author antikvo
 * @since 2019-07-30
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthArgumentResolver userArgumentResolver;

    /**
     * 增加对rest api鉴权的spring mvc拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

}
