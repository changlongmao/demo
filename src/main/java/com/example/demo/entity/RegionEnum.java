package com.example.demo.entity;

/**
 * @Title: RegionEnum
 * @Description: 项目区域辨识枚举类
 * @Author Cui
 * @Date 2020/4/22
 */
public enum RegionEnum {
    /**
     *  苍南
     */
    CANG_NAN("CANG_NAN"),

    /**
     *  衢州
     */
    QU_ZHOU("QU_ZHOU"),
    /**
     *  龙湾
     */
    LONG_WAN("LONG_WAN"),
    /**
     *  瓯海
     */
    OU_HAI("OU_HAI"),
    /**
     *  永嘉
     */
    YONG_JIA("YONG_JIA"),
    /**
     * 浦江
     */
    PU_JIANG("PU_JIANG");

    private String value;

    RegionEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static RegionEnum getByValue (String value){
        for (RegionEnum regionEnum : values()) {
            if (regionEnum.value.equals(value)) {
                return regionEnum;
            }
        }
        return null;
    }

}
