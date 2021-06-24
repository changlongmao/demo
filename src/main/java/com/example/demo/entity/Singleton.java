package com.example.demo.entity;


import lombok.Data;

import java.io.ObjectStreamException;

/**
 * @ClassName: Singleton
 * @Description: 单例模式
 * @Author: Chang
 * @Date: 2021/02/20 14:19
 **/
@Data
public class Singleton {

    private String str;

    public static volatile Singleton instance = new Singleton();

    private Singleton() {
        System.out.println("初始化实例");
    }

    static {
        instance.setStr("有值");
    }

//    public static Singleton getInstance() {
//        if (instance == null) {
//            synchronized (Singleton.class) {
//                if (instance == null) {
//                    instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }
}
