package com.example.demo.entity;


import java.io.ObjectStreamException;

/**
 * @ClassName: Singleton
 * @Description: 单例模式
 * @Author: Chang
 * @Date: 2021/02/20 14:19
 **/
public class Singleton {

    private static volatile Singleton instance;

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

    public Object readResolve() throws ObjectStreamException {
        return getInstance();
    }
}
