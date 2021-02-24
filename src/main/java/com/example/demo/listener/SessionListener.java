package com.example.demo.listener;

import org.springframework.context.ApplicationEvent;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @ClassName: SessionListener
 * @Description:
 * @Author: Chang
 * @Date: 2021/02/24 14:49
 **/
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("---------------------------->session创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("---------------------------->session销毁");
    }
}
