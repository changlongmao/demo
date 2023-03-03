package com.example.demo.testMain;

import com.example.demo.entity.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ChangLF 2023-02-19
 */
public class BeanGarbageCollectionDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class（配置类）
        applicationContext.register(User.class);
        // 启动 Spring 应用上下文
        applicationContext.refresh();
        System.out.println("Spring 应用上下文已启动");
        //依赖查找
        User bean = applicationContext.getBean(User.class);
        System.out.println(bean.toString());
        // 关闭 Spring 应用上下文
        applicationContext.close();
//        Thread.sleep(5000L);
        // 强制触发 GC, 当然这个方法不是每次都必然会被调用的, 因此这里也是加了些线程睡眠的代码
        bean=null;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(applicationContext.getBeanFactory());
        });
        thread.start();
//        System.gc();
//        Thread.sleep(5000L);
    }

}
