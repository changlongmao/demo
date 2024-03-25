package com.example.demo.testMain.cglib;

/**
 * @author ChangLF 2024/01/16
 */
public class TestCglib {

    public static void main(String[] args) {
        ProxyCglib proxy = new ProxyCglib();
        //通过生成子类的方式创建代理类
        SayHello proxyImp = (SayHello)proxy.getProxy(SayHello.class);
        proxyImp.say();
        Thread.currentThread().getContextClassLoader();
    }
}
