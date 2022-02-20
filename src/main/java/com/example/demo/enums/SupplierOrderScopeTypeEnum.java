package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author ChangLF 2021-12-17
 */
@Getter
@AllArgsConstructor
public enum SupplierOrderScopeTypeEnum {

    ALL("1", "可操作全部订单"),

    PART("2", "分配指定订单"),

    NONE("3", "暂不分配订单")

    ;

    @JsonValue
    private final String code;

    private final String title;

    /**
     * 判断参数code是否是一个有效的枚举
     */
    public static boolean valid(String code) {
        return Arrays.stream(values()).anyMatch(anEnum -> anEnum.getCode().equals(code));
    }

    /**
     * 获取code对应的枚举
     */
    @JsonCreator
    public static SupplierOrderScopeTypeEnum of(Byte code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code.toString())).findFirst().orElse(null);
    }


    /**
     * 获取code对应的title
     */
    public static String getTitleByCode(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).map(SupplierOrderScopeTypeEnum::getTitle).findFirst().orElse(null);
    }

}