package com.example.demo.entity;

/**
 * @ClassName: Adapter
 * @Description:
 * @Author: Chang
 * @Date: 2021/02/20 14:56
 **/
public class Adapter extends YuanKongChaZhuo implements AdpaterInter{

    @Override
    public void charge_byBianKong() {
        System.out.println("扁孔插座供电中......");
    }
}
