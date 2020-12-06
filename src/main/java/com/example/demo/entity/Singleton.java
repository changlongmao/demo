package com.example.demo.entity;

public class Singleton {
    private Singleton(){}

    public static Singleton getInstance(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static final Singleton instance = new Singleton();
    }
}
