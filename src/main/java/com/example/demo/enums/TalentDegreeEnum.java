package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author jicai 2021-09-02
 */
@Getter
@AllArgsConstructor
public enum TalentDegreeEnum implements EnumAncestor<String> {

    PRIMARY_SCHOOL    ("0","小学"),
    MIDDLE            ("1","初中"),
    SPECIAL_SCHOOL    ("2","中专"),
    HIGH              ("3","高中"),
    JUNIOR            ("4","大专"),
    UNDER             ("5","本科"),
    MASTER            ("6","硕士"),
    MBA               ("7","MBA"),
    EMBA              ("8","EMBA"),
    DOCTOER           ("9","博士"),
    POST_DOCTORAL     ("10","博士后");

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
    public static TalentDegreeEnum of(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    public static TalentDegreeEnum getEnumByTitle(String title) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getTitle().equals(title)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return asJavabean().toString();
    }

}