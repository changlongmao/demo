package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Chang
 * @Description 日期格式工具类
 * @Date 2020/12/6 21:07
 **/
public class DateUtil {

    /**
     * 辅助将字符串的日期转换为Date格式
     * @author Chang
     * @date 2021/8/26 11:15
     **/
    private static class DateFormatPattern {

        private final String pattern;

        private final String regex;

        public DateFormatPattern(String pattern, String regex) {
            this.pattern = pattern;
            this.regex = regex;
        }

        public String getPattern() {
            return pattern;
        }

        public boolean matchPattern(String date) {
            return date.matches(regex);
        }

    }

    public static final String timeZone = "GMT+8";

    public static final List<DateFormatPattern> patternList = new ArrayList<>();

    static {
        patternList.add(new DateFormatPattern("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy-MM-dd HH:mm", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy-MM-dd HH", "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy-MM-dd", "\\d{4}-\\d{1,2}-\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy-MM", "\\d{4}-\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy/MM/dd HH:mm:ss", "\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy/MM/dd HH:mm", "\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy/MM/dd HH", "\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy/MM/dd", "\\d{4}/\\d{1,2}/\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy/MM", "\\d{4}/\\d{1,2}"));
        patternList.add(new DateFormatPattern("yyyy年MM月dd日", "\\d{4}年\\d{1,2}月\\d{1,2}日"));
        patternList.add(new DateFormatPattern("yyyy年MM月", "\\d{4}年\\d{1,2}月"));
        patternList.add(new DateFormatPattern("dd-MM-yyyy HH:mm:ss", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("dd-MM-yyyy HH:mm", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}"));
        patternList.add(new DateFormatPattern("dd-MM-yyyy HH", "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}"));
        patternList.add(new DateFormatPattern("dd-MM-yyyy", "\\d{1,2}-\\d{1,2}-\\d{4}"));
        patternList.add(new DateFormatPattern("MM-yyyy", "\\d{1,2}-\\d{4}"));
        patternList.add(new DateFormatPattern("yyyyMMddHHmmss", "\\d{14}"));
        patternList.add(new DateFormatPattern("yyyyMMddHHmm", "\\d{12}"));
        patternList.add(new DateFormatPattern("yyyyMMddHH", "\\d{10}"));
        patternList.add(new DateFormatPattern("yyyyMMdd", "\\d{8}"));
        patternList.add(new DateFormatPattern("yyyyMM", "\\d{6}"));
        patternList.add(new DateFormatPattern("yyyy", "\\d{4}"));
        patternList.add(new DateFormatPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[-+]\\d{4}"));// 2012-03-15T13:48:49.733+0800
    }

    /**
     * 解析时间，把指定格式的日期/时间转化为时间类型。目前支持的格式有: <br>
     * yyyy-MM-dd HH:mm:ss <br>
     * yyyy-MM-dd HH:mm <br>
     * yyyy-MM-dd HH <br>
     * yyyy-MM-dd <br>
     * yyyy-MM <br>
     * dd-MM-yyyy HH:mm:ss <br>
     * dd-MM-yyyy HH:mm <br>
     * dd-MM-yyyy HH <br>
     * dd-MM-yyyy <br>
     * MM-yyyy <br>
     * yyyyMMddHHmmss <br>
     * yyyyMMddHHmm <br>
     * yyyyMMddHH <br>
     * yyyyMMdd <br>
     * yyyyMM <br>
     * yyyy <br>
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ (2012-03-15T13:48:49.733+0800) <br>
     * yyyy-MM-dd'T'HH:mm:ss.SSSXZ (2012-03-15T13:48:49.733+08:00) <br>
     *
     * @param sDate 待解析的时间
     * @return 解析所得时间，如果不满足指定的格式类型，则返回null
     */
    public static Date parseDate(String sDate) {
        if (sDate == null)
            return null;
        for (DateFormatPattern pattern : patternList) {
            if (pattern.matchPattern(sDate)) {
                SimpleDateFormat parser = new SimpleDateFormat(pattern.getPattern());
                try {
                    return parser.parse(sDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Cannot parse String to Date: " + sDate);
    }

    /**
     * 取得指定格式表示的日期/时间
     *
     * @param date    待格式化的日期/时间
     * @param dateFormat 目标格式
     * @return 按目标格式格式化后的日期/时间，如果时间或者目标格式为null则返回null
     */
    public static String getDateStr(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }
        return dateFormat.df.format(date);
    }

    /**
     * 取得以格式yyyy-MM-dd表示的日期
     *
     * @param date 待格式化的日期
     * @return yyyy-MM-dd格式化的日期
     */
    public static String getShortDate(Date date) {
        return getDateStr(date, DateFormat.SHORT_DATE_PATTERN_LINE);
    }

    /**
     * 取得以格式yyyyMMdd表示的日期
     *
     * @param date 待格式化的日期
     * @return yyyyMMdd格式化的日期
     */
    public static String getDateYMD(Date date) {
        return getDateStr(date, DateFormat.SHORT_DATE_PATTERN_NONE);
    }

    /**
     * 取得指定日期的开始点，值为yyyy-MM-dd HH:mm:ss:000，如2011-09-07的开始时间为2011-09-07 00:00:00:000
     *
     * @param d 待操作的日期
     * @return 指定的日期的开始时间点
     */
    public static Date getDayBegin(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 取得指定日期的结束点，值为yyyy-MM-dd 23:59:59:999，如2011-09-07的结束时间为2011-09-07 23:59:59:999
     *
     * @param d 待操作的日期
     * @return 指定的日期的结束时间点
     */
    public static Date getDayEnd(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 判断一个时间是否介于另外两个时间之间（包括等于）
     *
     * @param beginTime
     * @param endTime
     * @param theTime
     * @return
     */
    public static boolean isBetween(Date beginTime, Date endTime, Date theTime) {
        if (theTime == null)
            return false;
        boolean flag = true;
        if (beginTime != null)
            flag = beginTime.getTime() <= theTime.getTime();
        if (flag && endTime != null)
            flag = endTime.getTime() >= theTime.getTime();
        return flag;
    }

    /**
     * 得到n天前或者n天后的时间
     *
     * @param date--传入时间
     * @param days--正的是n天后的,负的是n天之前的
     * @return 处理后的时间
     */
    public static Date getDayAfterOrBefore(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);//当前时间减去或加上n天
        return calendar.getTime();
    }

    /**
     * @Param: beginDate
     * @Param: endDate
     * @Author Chang
     * @Description 获取两个时间相差天数
     * @Date 2020/10/28 15:33
     * @Return long
     **/
    public static long getDayBetween(Date beginDate, Date endDate) {
        long day = 0;
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * @Param: date
     * @Author Chang
     * @Description 获取当前季度
     * @Date 2020/11/19 8:38
     * @Return int
     **/
    public static int getQuarter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        if (month <= 3)
            return 1;
        else if (month <= 6)
            return 2;
        else if (month <= 9)
            return 3;
        else
            return 4;
    }

    /**
     * 得到n小时前或者n小时后的时间
     *
     * @param date--传入时间
     * @param hours--正的是n小时后的,负的是n小时之前的
     * @return 处理后的时间
     */
    public static Date getHourAfterOrBefore(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
        return calendar.getTime();
    }

    /**
     * 得到n个月前或者n个月后的时间
     *
     * @param date--传入时间
     * @param months--正的是n月后的,负的是n月之前的
     * @return 处理后的时间
     */
    public static Date getMonthAfterOrBefore(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);//当前时间减去或加上n个月
        return calendar.getTime();
    }

    /**
     * 得到n年前或者n年后的时间
     *
     * @param date--传入时间
     * @param years--正的是n年后的,负的是n年之前的
     * @return 处理后的时间
     */
    public static Date getYearAfterOrBefore(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);//当前时间减去或加上n年
        return calendar.getTime();
    }

    /**
     * 获取传入月份的月初时间
     * @param date
     * @author Chang
     * @date 2021/8/26 11:22
     * @return java.util.Date
     **/
    public static Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (1 - index));
        return calendar.getTime();
    }

    /**
     * 获取传入月份的月末时间
     * @param date
     * @author Chang
     * @date 2021/8/26 11:23
     * @return java.util.Date
     **/
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (-index));
        return calendar.getTime();
    }

    /**
     * 获取当前日期是星期几，周日为0
     *
     * @param date
     * @return 日期是星期几
     */
    public static int getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * Date 转 LocalDate
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate parseLocalDate(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthdayStr 生日
     * @return 年龄
     */
    public static int getAgeByBirthday(String birthdayStr) {
        LocalDate endDate = LocalDate.now();
        Period period = Period.between(parseLocalDate(parseDate(birthdayStr)), endDate);
        return period.getYears();
    }

    /**
     * 辅助将Date转换为需要的时间字符串格式
     * @author Chang
     * @date 2021/8/26 11:15
     **/
    public enum DateFormat {
        /**
         * 获取年，月，日
         */
        DATE_YEAR("yyyy"),
        DATE_MONTH("MM"),
        DATE_DAY("dd"),
        DATE_YEAR_MONTH("yyyyMM"),
        DATE_YEAR_MONTH_STRING("yyyy-MM"),

        /**
         * 自定义时间格式
         */
        CUSTOM_DATE_DAY_MONTH("MM-dd"),
        CUSTOM_TIME_HOUR_MINUTES("HH:mm"),
        CUSTOM_DATE_PATTERN_NONE("yyMMdd"),
        CUSTOM_TIME_HOUR_MINUTES_SECONDS("HH:mm:ss"),
        CUSTOM_INVITATIONS_MINUTES("MM月dd日 HH:mm"),
        CUSTOM_DATE_PATTERN_WORD("yyyy年MM月"),
        CUSTOM_DATE_MINUTES("MM月dd日 HH:mm"),

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),
        SHORT_DATE_PATTERN_WORD("yyyy年MM月dd日"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_LINE_WITHOUT_SECONDS("yyyy-MM-dd HH:mm"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH_WITHOUT_SECONDS("yyyy/MM/dd HH:mm"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
        LONG_DATE_PATTERN_UNSIGN("yyyyMMddHHmmss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC("(yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        private transient SimpleDateFormat df;

        DateFormat(String pattern) {
            df = new SimpleDateFormat(pattern);
        }

    }

}
