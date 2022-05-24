package com.example.demo.util;


import com.example.demo.enums.TalentSexEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanglei
 * @since 05.17.2017
 */
public final class StringUtil {

    /**
     * 手机号正则表达式
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9][0-9]{9}$");

    private static final Pattern NAME_PATTERN = Pattern.compile("[\\u4E00-\\u9FA5|·]{2,15}");

    private static final Pattern CHINESE_ID = Pattern.compile("^\\d{17}[\\d|X|x]$");

    private static final Pattern MAIL_PATTERN = Pattern.compile("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*");

    private static final Pattern GET_MOBILE_PATTERN = Pattern.compile("1[3-9][0-9]{9}");

    private static final Pattern ID_NUMBER_PATTERN = Pattern.compile("[1-9][0-9]{5}(19|20)[0-9]{2}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9]|(3[0-1])))[0-9]{3}[0-9xX]");

    /**
     * 年龄1-999的正则表达式
     */
    private static final Pattern AGE_PATTERN = Pattern.compile("^[1-9][0-9]{0,2}$");

    private StringUtil() {

    }

    public static boolean isNotMobile(String mobile) {
        return !StringUtil.isNotEmpty(mobile) || !MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 获取字符串中的手机号
     * @param mobile 一段字符串
     * @return 其中第一条手机号
     */
    public static String getMobile(String mobile) {
        if (StringUtil.isNotEmpty(mobile)) {
            Matcher matcher = GET_MOBILE_PATTERN.matcher(mobile);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }

    /**
     * 获取字符串中的身份证号
     * @param idCard 字符串
     * @author Chang
     * @date 2022/3/25 17:24
     * @return java.lang.String 手机号
     **/
    public static String getIdCard(String idCard) {
        if (StringUtil.isNotEmpty(idCard)) {
            Matcher matcher = ID_NUMBER_PATTERN.matcher(idCard);
            while (matcher.find()) {
                String group = matcher.group();
                if (isIDCard(group)) {
                    return group;
                }
            }
        }
        return null;
    }

    public static boolean isAge(String age) {
        return StringUtil.isNotEmpty(age) && AGE_PATTERN.matcher(age).matches();
    }


    /**
     * 邮箱格式校验
     * @param mail 邮箱
     * @author Chang
     * @date 2021/12/20 15:38
     * @return boolean
     **/
    public static boolean isMail(String mail) {
        return StringUtil.isNotEmpty(mail) && MAIL_PATTERN.matcher(mail).matches();
    }

    /**
     * 判断是否为空
     *
     * @param str 字符串
     * @return true表示为空，false表示不为空
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "".equals(str.trim());
    }

    /**
     * 判断是否不为空值
     *
     * @param str 字符串
     * @return true表示不为空值，false表示为空值
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 姓名格式校验
     *
     * @param name 姓名
     * @return true表示格式校验通过，false表示不通过
     */
    public static boolean isCheckName(String name) {
        return isNotEmpty(name) && name.length() >= 2 && name.length() <= 15 && NAME_PATTERN.matcher(name).matches();

    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取脱敏手机号
     * @param phone 手机号
     * @author Chang
     * @date 2021/12/20 15:41
     * @return java.lang.String
     **/
    public static String phoneAddMark(String phone) {
        if (isNotEmpty(phone)) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }

    public static String bankCardNoAddMark(String cardNo) {
        if (isNotEmpty(cardNo) && cardNo.length() >= 10) {
            StringBuilder sb = new StringBuilder();
            int number = cardNo.length() - 10;
            for (int i = 0; i < number; i++) {
                sb.append("*");
            }

            return new StringBuilder(cardNo).replace(6, cardNo.length() - 4, sb.toString()).toString();
        }

        return cardNo;
    }

    public static String bankCardNoDivision(String cardNo) {
        if (isNotEmpty(cardNo) && cardNo.length() >= 5) {
            int number = cardNo.length() / 4;
            StringBuilder sb = new StringBuilder(cardNo);

            for (int i = 0; i < number; i ++){
                int index = (i + 1) * 4;
                if( i > 0){
                   index += i;
                }
                sb.insert(index, " ");
            }

            return sb.toString();
        }
        return cardNo;
    }

    public static boolean isName(String name) {
        return isNotEmpty(name) && NAME_PATTERN.matcher(name).matches();
    }

    /**
     * 身份证验证
     *
     * @param certNo 号码内容
     * @return 是否有效 null和"" 都是false
     */
    public static boolean isIDCard(String certNo) {
        if (certNo == null || certNo.length() != 18 || !CHINESE_ID.matcher(certNo).matches()) {
            return false;
        }

        final int birthDay = Integer.parseInt(certNo.substring(6, 14));

        //校验年份
        final int iyear = birthDay / 10000;
        if (iyear <= 1900 || iyear >= LocalDate.now().getYear()) {
            return false;
        }

        //校验天数
        final int iday = birthDay % 100;
        if (iday <= 0 || iday >= 32) {
            return false;
        }

        //校验月份
        final int imonth = birthDay % 10000 / 100;
        if (imonth <= 0 || imonth >= 13) {
            return false;
        }

        //校验位数
        int power = 0;
        final char[] cs = certNo.toUpperCase().toCharArray();
        int[] powerArray = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int[] parityBit = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int indexRemark = cs.length - 1;
        for (int i = 0; i < indexRemark; i++) {
            power += (cs[i] - '0') * powerArray[i];
        }

        return cs[indexRemark] == parityBit[power % 11];
    }

    /**
     * 从身份证中获取性别
     * @param idCard 身份证
     * @author Chang
     * @date 2022/3/31 10:21
     * @return com.joyowo.turing.supplier.app.enums.TalentSexEnum
     **/
    public static TalentSexEnum getSexFromIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return null;
        }
        int sex = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));
        if (sex % 2 == 0) {
            return TalentSexEnum.WOMAN;
        } else {
            return TalentSexEnum.MAN;
        }
    }

    /**
     * 从身份证中获取性别
     *
     * @param idCard 身份证
     * @return com.joyowo.turing.supplier.app.enums.TalentSexEnum
     * @author Chang
     * @date 2022/3/31 10:21
     **/
    public static Byte getSexCodeFromIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return null;
        }
        int sex;
        try {
            sex = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));
        } catch (Exception e) {
            return null;
        }
        if (sex % 2 == 0) {
            return TalentSexEnum.WOMAN.getCodeNum();
        } else {
            return TalentSexEnum.MAN.getCodeNum();
        }
    }

    /**
     * 根据身份证号码计算年龄
     *
     * @param idNumber 考虑到了15位身份证，但不一定存在
     */
    public static int getAgeByIdCard(String idNumber) {
        return getAgeByDate(getBirthdayByIdCard(idNumber));
    }

    /**
     * 根据身份证号码计算年龄
     *
     * @param idNumber 考虑到了15位身份证，但不一定存在
     */
    public static Date getBirthdayByIdCard(String idNumber) {
        String dateStr;
        if (idNumber.length() == 15) {
            dateStr = "19" + idNumber.substring(6, 12);
        } else if (idNumber.length() == 18) {
            dateStr = idNumber.substring(6, 14);
        } else {//默认是合法身份证号，但不排除有意外发生
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

    }

    public static int getAgeByDate(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();

        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return 0;
        }

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);

        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;

        if (monthNow <= monthBirthday) {
            if (monthNow == monthBirthday) {
                if (dayOfMonthNow < dayOfMonthBirthday) {
                    age--;
                }
            } else {
                age--;
            }
        }

        return age;
    }

    /**
     * 判断字符串中含有某个字符的次数
     *
     * @author zengcy
     * @date 2021/11/24
     */
    public static int contains(String str, String character) {
        int count = 0;
        while (str.contains(character)) {
            str = str.substring(str.indexOf(character) + 1);
            ++count;
        }
        return count;
    }

    /**
     * 获取手机号码的脱敏展示
     * @param mobile 入参
     * @return 返回值
     */
    public static String getMobileHide(String mobile){
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 将Str转list
     * @param str 入参
     * @return 返回值
     */
    public static List<String> stringToList(String str){
        String[] strs = str.split(",");
        return Arrays.asList(strs);
    }
}