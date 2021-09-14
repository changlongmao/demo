package com.example.demo.entity;

/**
 * 枚举基接口
 *
 * @author Spark
 * @deprecated 代替：com.joyowo.smarthr.core.ancestor.EnumAncestor
 */
@Deprecated
public interface BaseEnum {

    /**
     * 获取code值
     */
    String getCode();

    /**
     * 获取描述
     */
    String getDesc();

}
