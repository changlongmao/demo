package com.example.demo.entity;


public enum UserTypeEnum {

    /**
     * 用户类型
     */
    UNKNOWN_TYPE(0),
    PRODUCT(1),
    AMOUNT_PRODUCT(2);

    private Integer value;

    UserTypeEnum(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return value;
    }
}
