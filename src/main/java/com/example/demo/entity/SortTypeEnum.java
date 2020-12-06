package com.example.demo.entity;

/**
 * 排序枚举
 *
 * @author Antikvo
 */
public enum SortTypeEnum {

    /**
     * 更新时间
     */
    UPDATE_TIME("1","更新时间"),
    FINANCING_AMOUNT("2","融资金额");
    private String id;
    private String msg;
    SortTypeEnum(String id, String msg){
        this.id = id;
        this.msg = msg;
    }

    public static String getMsgById(String id){
        for (SortTypeEnum value : values()) {
            if(value.id.equals(id)){
                return value.msg;
            }
        }
        return "";
    }
    public static SortTypeEnum getById(String id){
        for (SortTypeEnum value : values()) {
            if(value.id.equals(id)){
                return value;
            }
        }
        return null;
    }


    public String getId(){
        return id;
    }

}
