package com.example.demo.util;

import com.example.demo.entity.ParseTextResumeDto;
import com.example.demo.enums.ParseResumeFieldEnum;
import com.example.demo.enums.TalentDegreeEnum;
import com.example.demo.enums.TalentSexEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 解析文本简历工具类
 * 返回字段在{@link ParseTextResumeDto} {@link ParseResumeFieldEnum}中维护
 *
 * @author ChangLF 2022-02-10
 */
@Slf4j
public class ParseTextResumeUtil {

    /**
     * 需要解析的文本标题
     */
    public static final List<String> nameList = Arrays.asList("姓名", "名字");

    public static final List<String> mobileList = Arrays.asList("手机", "手机号", "手机号码", "联系方式", "联系电话", "电话");

    public static final List<String> sexList = Collections.singletonList("性别");

    public static final List<String> ageList = Arrays.asList("年龄", "年纪");

    public static final List<String> degreeList = Collections.singletonList("学历");

    public static final List<String> idCardList = Arrays.asList("身份证号码", "身份证号", "身份证", "证件号码", "证件号");

    /**
     * 解析姓名时，这些带有姓氏可能被解析为名字又可能不是名字，解析优先级降低
     */
    public static final List<String> unNameList = Arrays.asList("高中", "姓名", "性别", "年龄", "年纪", "学历");

    /**
     * 符号标志位，文本标题之后需连接符号标识为有效标题
     */
    public static final List<String> signList = Arrays.asList("：", ":", "");

    /**
     * 换行符兼容
     * +和十，十有时候会被误认为加号，当作分隔符误用，做兼容
     */
    public static final List<String> lineList = Arrays.asList("\r\n", "\n", "\r", "\t", "\f", "，", "；", ";", ",", "。", "|", "｜", "\\", "/", "➕", "+", "十");

    /**
     * 用于去除字符串中所有标点符号
     */
    public static final String regEx = "[\n\r\t\f`~～·!！@#￥$%…^&*()（）+=|、{}【】「」':：;；,\\[\\].<>《》/?？—_\\-‘”“’。， ]";

    /**
     * 年龄正则
     */
    private static final Pattern AGE_PATTERN = Pattern.compile("^[0-9]{2}$");

    /**
     * 所有集合的总值，key为{@link ParseResumeFieldEnum}，需与 {@link ParseTextResumeDto}中字段名对应
     */
    public static final Map<ParseResumeFieldEnum, List<String>> allParseMap = new EnumMap<>(ParseResumeFieldEnum.class);

    static {
        allParseMap.put(ParseResumeFieldEnum.NAME, nameList);
        allParseMap.put(ParseResumeFieldEnum.MOBILE, mobileList);
        allParseMap.put(ParseResumeFieldEnum.SEX, sexList);
        allParseMap.put(ParseResumeFieldEnum.AGE, ageList);
        allParseMap.put(ParseResumeFieldEnum.DEGREE, degreeList);
        allParseMap.put(ParseResumeFieldEnum.ID_CARD, idCardList);
    }

    /**
     * 解析简历
     *
     * @param beginResumeText 简历文本内容
     * @return com.joyowo.turing.supplier.app.javabean.dto.ParseTextResumeDto
     * @author Chang
     * @date 2022/2/10 16:50
     **/
    public static ParseTextResumeDto parseResume(String beginResumeText) {
        if (StringUtils.isBlank(beginResumeText)) {
            return null;
        }
        // 去除文本中空格
        String resumeText = beginResumeText.replaceAll(" ", "");
        // 将文本切割为最小分割单元存储
        List<String> resultList = new ArrayList<>();

        // 遍历循环文本
        List<Integer> indexList = new ArrayList<>();
        // 先将文本根据换行符分段
        for (String line : lineList) {
            getTextIndex(indexList, resumeText, line, 2);
        }
        allParseMap.forEach((key, list) -> {
            list.forEach(format -> {
                signList.forEach(sign -> {
                    // 判断是否有符合条件的文本，获取其对应位置索引
                    String signTest = format + sign;
                    // 遍历同一个文本出现多次的索引位置
                    getTextIndex(indexList, resumeText, signTest, 1);
                });
            });
        });
        // 对符合条件的索引进行排序，并将原始文本按照索引切割存入resultList
        indexList.sort(Comparator.comparingInt(a -> a));
        if (CollectionUtils.isEmpty(indexList)) {
            resultList.add(resumeText);
        } else {
            for (int i = 0; i < indexList.size(); i++) {
                Integer start = indexList.get(i);
                String result;
                if (i + 1 == indexList.size()) {
                    result = resumeText.substring(start);
                } else if (i == 0 && start != 0) {
                    result = resumeText.substring(0, start);
                } else {
                    result = resumeText.substring(start, indexList.get(i + 1));
                }
                // 将截取结果存入集合，空集合或者只有符号的不要
                if (StringUtils.isNotBlank(result) && !lineList.contains(result)) {
                    resultList.add(result);
                }
            }
        }
        ParseTextResumeDto parseTextResumeDto = new ParseTextResumeDto();
        allParseMap.forEach((key, list) -> {
            list.forEach(format -> {
                signList.forEach(sign -> {
                    resultList.forEach(result -> {
                        // 判断是否有符合条件的文本，获取其对应位置索引
                        String str = format + sign;
                        int indexOf = result.indexOf(str);
                        if (indexOf != -1) {
                            String resultSub = result.substring(indexOf + str.length());
                            // 获取解析结果存入dto中
                            getResultParse(resultSub, key, parseTextResumeDto);
                        }
                    });
                });
            });
        });
        // 若解析内容失败，尝试补偿解析简历，使用原始文本补偿解析
        compensateParse(beginResumeText, parseTextResumeDto, resultList);
        log.info("文本简历解析结果：{}, 解析前内容为：{}", JsonUtils.toJson(parseTextResumeDto), beginResumeText);
        return parseTextResumeDto;
    }

    /**
     * 获取finalResumeText文本中出现signTest的索引位置存入indexList中
     *
     * @param type 1为有效标识需留下，2为无效标识，需整个截掉
     */
    private static void getTextIndex(List<Integer> indexList, String finalResumeText, String signTest, int type) {
        int startIndex = 0;
        int index = 0;
        while (index != -1) {
            // 从指定的索引位置开始，返回第一次出现指定子字符串在此字符串中的索引位置
            index = finalResumeText.indexOf(signTest, startIndex);
            if (index != -1) {
                if (type == 1) {
                    indexList.add(index);
                } else if (type == 2) {
                    indexList.add(index);
                    indexList.add(index + 1);
                }
            }
            startIndex = index + 1;
        }
    }

    /**
     * 根据字段名解析文本
     *
     * @param result             已处理的文本内容
     * @param fieldEnum          对应的字段枚举
     * @param parseTextResumeDto 返回的结果存储
     * @author Chang
     * @date 2022/2/11 15:35
     **/
    private static void getResultParse(String result, ParseResumeFieldEnum fieldEnum, ParseTextResumeDto parseTextResumeDto) {
        // 对截取后的文本去除所有标点符号
        result = result.replaceAll(regEx, "");
        switch (fieldEnum) {
            case NAME:
                if (parseTextResumeDto.getName() != null) {
                    return;
                }
                String surName = ChineseNameUtil.getSurName(result);
                if (StringUtils.isNotBlank(surName)) {
                    parseTextResumeDto.setName(surName);
                }
                break;
            case MOBILE:
                if (parseTextResumeDto.getMobile() != null) {
                    return;
                }
                if (!StringUtil.isNotMobile(result)) {
                    parseTextResumeDto.setMobile(result);
                }
                break;
            case AGE:
                if (parseTextResumeDto.getAge() != null) {
                    return;
                }
                try {
                    if (result.endsWith("岁")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    int age = Integer.parseInt(result);
                    if (age >= 0 && age <= 999) {
                        parseTextResumeDto.setAge(age);
                    }
                } catch (NumberFormatException e) {
                    log.warn("简历解析年龄异常", e);
                }
                break;
            case SEX:
                if (parseTextResumeDto.getSex() != null) {
                    return;
                }
                if (result.contains(TalentSexEnum.MAN.getTitle())) {
                    parseTextResumeDto.setSex(Byte.valueOf(TalentSexEnum.MAN.getCode()));
                } else if (result.contains(TalentSexEnum.WOMAN.getTitle())) {
                    parseTextResumeDto.setSex(Byte.valueOf(TalentSexEnum.WOMAN.getCode()));
                }
                break;
            case DEGREE:
                if (parseTextResumeDto.getDegree() != null) {
                    return;
                }
                TalentDegreeEnum degreeEnum = TalentDegreeEnum.getEnumByTitle(result);
                if (degreeEnum != null) {
                    parseTextResumeDto.setDegree(Byte.valueOf(degreeEnum.getCode()));
                }
                break;
            case ID_CARD:
                if (parseTextResumeDto.getIdCard() != null) {
                    return;
                }
                if (StringUtil.isIDCard(result)) {
                    parseTextResumeDto.setIdCard(result);
                    parseTextResumeDto.setSex(Byte.valueOf(StringUtil.getSexFromIdCard(result).getCode()));
                    int age = StringUtil.getAgeByIdCard(result);
                    parseTextResumeDto.setAge(age);
                }
            default:
        }
    }

    /**
     * 补偿解析简历
     *
     * @param resumeText         简历文本
     * @param parseTextResumeDto 解析结果
     * @param resultList         文本切割后集合
     */
    private static void compensateParse(String resumeText, ParseTextResumeDto parseTextResumeDto, List<String> resultList) {
        // 去除所有标点符号的文本简历
        for (int i = 0; i < resultList.size(); i++) {
            resultList.set(i, resultList.get(i).replaceAll(regEx, ""));
        }
        // 讲根据空格隔开的文本放入resultList后面补充解析
        resultList.addAll(Arrays.asList(resumeText.split(" ")));
        // 若文本中只有姓名则识别出
        if (parseTextResumeDto.getName() == null) {
            String surName = ChineseNameUtil.getSurName(resumeText);
            if (surName != null && surName.equals(resumeText)) {
                parseTextResumeDto.setName(surName);
                return;
            }
        }
        for (String result : resultList) {
            // 补偿提取文本中手机号
            if (parseTextResumeDto.getMobile() == null) {
                String mobile = StringUtil.getMobile(result);
                if (StringUtil.isNotEmpty(mobile)) {
                    int indexOf = result.indexOf(mobile);
                    int startIndex = Math.max(indexOf - 1, 0);
                    int endIndex = Math.min(indexOf + 12, result.length());
                    // 确保手机号附近没有其她数字，保证其为手机号
                    if (result.substring(startIndex, endIndex).replaceAll("\\D", "").length() == mobile.length()) {
                        parseTextResumeDto.setMobile(mobile);
                    }
                }
            }
            // 因身份证号校验标准较高，若能提取则认为有效
            if (parseTextResumeDto.getIdCard() == null) {
                String idCard = StringUtil.getIdCard(result);
                if (StringUtil.isNotEmpty(idCard)) {
                    parseTextResumeDto.setIdCard(idCard);
                    parseTextResumeDto.setSex(Byte.valueOf(StringUtil.getSexFromIdCard(idCard).getCode()));
                    int age = StringUtil.getAgeByIdCard(result);
                    parseTextResumeDto.setAge(age);
                }
            }
        }
        // 若手机号提取成功，则尝试提取姓名
        if (parseTextResumeDto.getMobile() != null && parseTextResumeDto.getName() == null) {
            for (String result : resultList) {
                String surName = ChineseNameUtil.getSurName(result);
                if (StringUtil.isNotEmpty(surName) && result.equals(surName)) {
                    parseTextResumeDto.setName(surName);
                    if (!unNameList.contains(surName)) {
                        break;
                    }
                }
            }
            if (parseTextResumeDto.getName() == null) {
                for (String result : resultList) {
                    int indexOf = result.indexOf(parseTextResumeDto.getMobile());
                    if (indexOf != -1) {
                        int startIndex = Math.max(indexOf - 4, 0);
                        int endIndex = Math.min(indexOf + 15, result.length());
                        String name = result.substring(startIndex, endIndex);
                        parseTextResumeDto.setName(ChineseNameUtil.getSurName(name));
                        break;
                    }
                }
            }
        }
        // 若姓名+手机号提取成功，则尝试提取性别
        if (parseTextResumeDto.getMobile() != null && parseTextResumeDto.getName() != null && parseTextResumeDto.getSex() == null) {
            if (resumeText.contains(TalentSexEnum.MAN.getTitle())) {
                parseTextResumeDto.setSex(Byte.valueOf(TalentSexEnum.MAN.getCode()));
            } else if (resumeText.contains(TalentSexEnum.WOMAN.getTitle())) {
                parseTextResumeDto.setSex(Byte.valueOf(TalentSexEnum.WOMAN.getCode()));
            }
        }
        // 若姓名+手机号提取成功，则尝试提取学位
        if (parseTextResumeDto.getMobile() != null && parseTextResumeDto.getName() != null && parseTextResumeDto.getDegree() == null) {
            Arrays.stream(TalentDegreeEnum.values())
                    .filter(e -> resumeText.contains(e.getTitle()))
                    .findFirst()
                    .ifPresent(degreeEnum -> parseTextResumeDto.setDegree(Byte.valueOf(degreeEnum.getCode())));
        }
        // 若姓名+手机号提取成功，则尝试提取年龄
        if (parseTextResumeDto.getMobile() != null && parseTextResumeDto.getName() != null && parseTextResumeDto.getAge() == null) {
            try {
                int indexOf = resumeText.indexOf("岁");
                if (indexOf != -1) {
                    String ageStr = resumeText.substring(indexOf - 2, indexOf);
                    int age = Integer.parseInt(ageStr);
                    if (age >= 0 && age <= 999) {
                        parseTextResumeDto.setAge(age);
                    }
                }
            } catch (Exception e) {
                log.warn("简历解析年龄异常", e);
            }
            if (parseTextResumeDto.getAge() == null) {
                try {
                    resultList.stream().filter(e -> AGE_PATTERN.matcher(e).matches()).findFirst().ifPresent(e -> parseTextResumeDto.setAge(Integer.valueOf(e)));
                } catch (Exception e) {
                    log.warn("简历解析年龄异常", e);
                }
            }
        }
    }
}
