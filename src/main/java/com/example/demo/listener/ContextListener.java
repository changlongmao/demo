package com.example.demo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @ClassName: ContextListener
 * @Description:
 * @Author: Chang
 * @Date: 2021/02/24 14:51
 **/
@WebListener
public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("---------------------------->ServletContext创建");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("---------------------------->ServletContext销毁");
    }
}
