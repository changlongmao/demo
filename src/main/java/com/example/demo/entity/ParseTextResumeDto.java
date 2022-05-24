package com.example.demo.entity;

import lombok.Data;

/**
 * 文本简历解析返回结果
 * @author ChangLF 2022-02-09
 */
@Data
public class ParseTextResumeDto {

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别 1 男 2 女
     * 枚举名jy-turing-suppliers:talentSex
     */
    private Byte sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 学历 0小学 1初中 2中专 3高中 4大专 5本科 6硕士 7MBA 8EMBA 9博士 10博士后
     * 枚举名jy-turing-suppliers:talentDegree
     */
    private Byte degree;

    /**
     * 身份证号
     */
    private String idCard;
}
