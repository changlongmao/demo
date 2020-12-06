package com.example.demo.entity;

public interface SaleComputer {

    public String sale(double money);

    default void show(){};
}
