package com.example.demo.entity;

public class HelloA {
    public HelloA(int x){//构造函数
        System.out.println("A的构造函数:"+x);
    }
    {//构造代码块
        System.out.println("A的构造代码块");
    }
    static {//静态代码块
        System.out.println("A的静态代码块");
    }
    public static void main(String[] args) {
        HelloA a=new HelloA(1);
        HelloA b=new HelloA(2);
    }
}
