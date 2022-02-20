package com.example.demo.enums;

/**
 * 项目状态枚举
 *
 * @author Antikvo
 */
public enum ProductStatusEnum {

    /**
     * 更新时间
     */

    FAIL(-1,"审核不通过"),
    EDIT(0,"编辑中"),
    PENDING_REVIEW(1,"待审核"),
    SUCCESS(3,"审核成功"),
    ONLINE(4,"已上线");
    private Integer id;
    private String msg;
    ProductStatusEnum(Integer id, String msg){
        this.id = id;
        this.msg = msg;
    }

    public static String getMsgById(Integer id){
        for (ProductStatusEnum value : values()) {
            if(value.id.equals(id)){
                return value.msg;
            }
        }
        return "";
    }
    public static ProductStatusEnum getById(Integer id){
        for (ProductStatusEnum value : values()) {
            if(value.id.equals(id)){
                return value;
            }
        }
        return null;
    }


    public Integer getId(){
        return id;
    }
    public String getName(){
        return this.name();
    }

}
