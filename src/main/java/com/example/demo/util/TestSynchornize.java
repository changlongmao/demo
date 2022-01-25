package com.example.demo.util;

/**
 * @author ChangLF 2021-12-31
 */
public class TestSynchornize {

    /**
     * 修饰静态方法
     */
    public synchronized static void test1() {
        System.out.println("test1");
    }

    /**
     * 修饰实例方法
     */
    public synchronized void test2(){
        System.out.println("test2");
    }

    /**
     * 修饰代码块
     */
    public void test3(){
        synchronized (this){
            System.out.println("test3");
        }
    }
}
