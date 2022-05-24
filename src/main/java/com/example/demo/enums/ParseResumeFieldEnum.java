package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author ChangLF 2022-02-11
 */
@Getter
@AllArgsConstructor
public enum ParseResumeFieldEnum implements EnumAncestor<String> {

    /**
     * 简历解析字段名维护
     */
    NAME("name", "名称"),

    MOBILE("mobile", "手机号码"),

    SEX("sex", "性别"),

    AGE("age", "年龄"),

    DEGREE("degree", "学位"),

    ID_CARD("idCard", "身份证号"),

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
    public static ParseResumeFieldEnum of(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * 获取code对应的title
     */
    public static String getTitleByCode(Byte code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(String.valueOf(code))).map(ParseResumeFieldEnum::getTitle).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return asJavabean().toString();
    }

}