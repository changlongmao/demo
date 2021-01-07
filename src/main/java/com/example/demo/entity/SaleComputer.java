package com.example.demo.entity;

@FunctionalInterface
public interface SaleComputer {

    public String sale(double money);


    default void show(){
        System.out.println("defaultMethod");
    }


}
