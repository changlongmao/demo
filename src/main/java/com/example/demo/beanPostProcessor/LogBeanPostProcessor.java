package com.example.demo.beanPostProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ChangLF 2024/01/17
 */
@Slf4j
@Component
public class LogBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        log.info("bean:{}，beanName: {}", bean, beanName);
        return bean;
    }
}
