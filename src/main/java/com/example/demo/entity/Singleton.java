package com.example.demo.entity;


/**
 * @ClassName: Singleton
 * @Description: 单例模式
 * @Author: Chang
 * @Date: 2021/02/20 14:19
 **/
public class Singleton {

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
