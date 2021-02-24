package com.example.demo.listener;

import com.example.demo.entity.PushOrderEvent;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ApplicationListener
 * @Description:
 * @Author: Chang
 * @Date: 2021/02/24 15:16
 **/
@Slf4j
@Component
public class TestApplicationListener implements ApplicationListener<PushOrderEvent> {

    @Override
    public void onApplicationEvent(PushOrderEvent event) {
        log.info("{}用户下了一个订单{}", event.getUserId(), event.getOrderNo());
    }
}
