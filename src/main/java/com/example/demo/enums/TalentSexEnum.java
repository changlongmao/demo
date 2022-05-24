package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author jicai 2021-09-01
 */
@Getter
@AllArgsConstructor
public enum TalentSexEnum implements EnumAncestor<String> {

    MAN("1", (byte) 1, "男"),

    WOMAN("2", (byte) 2, "女"),

    ;

    @JsonValue
    private final String code;

    private final Byte codeNum;

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
    public static TalentSexEnum of(String code) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCode().equals(code)).findFirst().orElse(null);
    }

    public static TalentSexEnum of(Byte codeNum) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getCodeNum().compareTo(codeNum) == 0).findFirst().orElse(null);
    }

    /**
     * 根据名称获取枚举
     */
    public static TalentSexEnum getEnumByTitle(String title) {
        return Arrays.stream(values()).filter(anEnum -> anEnum.getTitle().equals(title)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return asJavabean().toString();
    }

}