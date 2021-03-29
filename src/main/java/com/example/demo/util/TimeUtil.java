package com.example.demo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

/**
 * 基于JDK8 time包的时间工具类
 *
 * @author wanglei
 * @date 2017.12.12
 * @since v1.0.0
 */
public final class TimeUtil {

    /**
     * 1年12个月
     */
    private static final int MONTH_OF_YEAR = 12;
    /**
     * 获取默认时区: Asia/Shanghai
     */
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");
    /**
     * 获取默认时区偏移量: +8
     */
    private static final ZoneOffset DEFAULT_ZONE_OFF_SET = ZoneOffset.of("+8");

    private TimeUtil() {
        // no construct function
    }

    /**
     * 时间转 String
     *
     * @param time   待转时间
     * @param format 时间格式
     * @return String
     */
    public static String parseTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * long 转时间
     *
     * @param time 待转时间
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), DEFAULT_ZONE);
    }

    /**
     * long 转时间
     *
     * @param time 待转时间
     * @return LocalDateTime
     */
    public static LocalDate parseTimeToDate(long time) {
        return parseTime(time).toLocalDate();
    }

    /**
     * 时间转 long
     *
     * @param time 待转时间
     * @return long
     */
    public static long parseTimeToLong(LocalDateTime time) {
        return time.toEpochSecond(DEFAULT_ZONE_OFF_SET);
    }

    /**
     * 获取当前时间(秒)
     *
     * @return long
     */
    public static long getCurrentTime() {
        return parseTimeToLong(LocalDateTime.now());
    }

    /**
     * long 转时间String
     *
     * @param time   待转时间
     * @param format 时间格式
     * @return String
     */
    public static String parseTime(long time, TimeFormat format) {
        return parseTime(parseTime(time), format);
    }

    /**
     * 时间格式的字符串转成long
     *
     * @param dateStr 日期
     * @return 时间戳
     */
    public static long parseDateStrToLong(String dateStr) {
        DateTimeFormatter dateTimeFormatter = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;
        return parseTimeToLong(LocalDate.parse(dateStr, dateTimeFormatter).atTime(LocalTime.now()));
    }

    /**
     * 时间格式的字符串转成long
     *
     * @param timeStr 时间
     * @return 时间戳
     */
    public static long parseTimeStrToLong(String timeStr) {
        DateTimeFormatter dateTimeFormatter = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
        return parseTimeToLong(LocalDateTime.parse(timeStr, dateTimeFormatter));
    }

    /**
     * 获取相差天数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 0相等 <0前者大 >后者大
     */
    public static long daysBetween(long startTime, long endTime) {
        LocalDate start = parseTimeToDate(startTime);
        LocalDate end = parseTimeToDate(endTime);
        return start.until(end, ChronoUnit.DAYS);
    }

    /**
     * 获取相差月份
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 0相等 <0前者大 >0后者大
     */
    public static int monthsBetween(long startTime, long endTime) {
        LocalDate start = parseTimeToDate(startTime);
        LocalDate end = parseTimeToDate(endTime);
        return (end.getYear() - start.getYear()) * 12 + (end.getMonthValue() - start.getMonthValue());
    }

    /**
     * 获取时间差额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return Duration
     */
    public static Duration timeBetween(long startTime, long endTime) {
        return Duration.between(parseTime(startTime), parseTime(endTime));
    }

    /**
     * 判断时间格式是否正确
     *
     * @param dateStr     日期
     * @param datePattern 格式
     * @return true 正确
     */
    public static boolean isRightDateStr(String dateStr, TimeFormat datePattern) {
        String newDateStr;
        try {
            newDateStr = parseTime(parseDateStrToLong(dateStr), datePattern);
        } catch (Exception e) {
            return false;
        }

        return dateStr.equals(newDateStr);
    }

    private static LocalDate getLocalDate(String timeStr, TimeFormat datePattern) {
        DateTimeFormatter strToDateFormatter = datePattern.formatter;
        TemporalAccessor dateTemporal = strToDateFormatter.parse(timeStr);
        return LocalDate.from(dateTemporal);
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthdayStr 生日字符串
     * @return 计算了生日的周岁
     */
    public static int getAgeByBirthday(String birthdayStr, TimeFormat datePattern) {
        LocalDate endDate = LocalDate.now();
        Period period = Period.between(getLocalDate(birthdayStr, datePattern), endDate);
        return period.getYears();
    }

    /**
     * 将时间戳 转换成当天 00:00:00 的时间戳
     *
     */
    public static long getTimeStart(long time) {
        String startDateStr = TimeUtil.parseTime(time, TimeFormat.SHORT_DATE_PATTERN_LINE) + " 00:00:00";
        return TimeUtil.parseTimeStrToLong(startDateStr);
    }

    /**
     * 将时间戳 转换成当天 23:59:59 的时间戳
     *
     */
    public static long getTimeEnd(long time) {
        String endDateStr = TimeUtil.parseTime(time, TimeFormat.SHORT_DATE_PATTERN_LINE) + " 23:59:59";
        return TimeUtil.parseTimeStrToLong(endDateStr);
    }

    /**
     * 根据年龄获取生日的范围
     * 例：以今天2019-01-11为例子，30岁，的生日范围应该是 1988-01-12 ~ 1989-01-11
     *
     * @param queryAge 需要计算生日的年龄
     * @param isMax    是否是去获取生日的最大值
     * @return int
     */
    public static int getBirthdayByAge(int queryAge, Boolean isMax) {
        LocalDate nowTime = LocalDate.now();
        if (isMax) {
            nowTime = nowTime.plusYears(-queryAge);
        } else {
            nowTime = nowTime.plusYears(-(queryAge + 1)).plusDays(1);
        }
        return Integer.parseInt(TimeFormat.SHORT_DATE_PATTERN_NONE.formatter.format(nowTime));
    }

    /**
     * 获取N天后的时间
     *
     * @param day 几天后 负数则为几天前
     * @return 时间戳
     */
    public static long getNDays(int day) {
        String format = LocalDateTime.now().plus(day, ChronoUnit.DAYS).format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter) + " 23:59:59";
        return parseTimeStrToLong(format);
    }

    /**
     * 获取工作时限
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工作时限(1位小数)
     */
    public static BigDecimal getWorkYear(String startDate, String endDate) {
        Long startTime = parseDateStrToLong(startDate);
        Long endTime = parseDateStrToLong(endDate);
        return getWorkYear(startTime, endTime);
    }

    /**
     * 获取工作时限
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 工作时限(1位小数)
     */
    public static BigDecimal getWorkYear(Long startDate, Long endDate) {
        return BigDecimal.valueOf(TimeUtil.monthsBetween(startDate, endDate)).divide(BigDecimal.valueOf(MONTH_OF_YEAR), 1, RoundingMode.HALF_UP);
    }

    /**
     * 获取今天星期几
     */
    public static String getDayOfTheWeek(long time) {
        String[][] strArray = {{"MONDAY", "一"}, {"TUESDAY", "二"}, {"WEDNESDAY", "三"}, {"THURSDAY", "四"}, {"FRIDAY", "五"}, {"SATURDAY", "六"}, {"SUNDAY", "日"}};

        LocalDate currentDate = parseTimeToDate(time);
        String k = String.valueOf(currentDate.getDayOfWeek());
        //获取行数
        for (String[] strings : strArray) {
            if (k.equals(strings[0])) {
                k = strings[1];
                break;
            }
        }
        return "周" + k;
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {
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

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    /**
     * X分钟
     */
    public static final int MINUTES_10 = 10;
    public static final int MINUTES_60 = 60;

    /**
     * X小时
     */
    public static final int HOURS_1 = 1;
    public static final int HOURS_24 = 24;

    /**
     * X天
     */
    public static final int DAYS_AGO_1 = 1;
    public static final int DAYS_AGO_2 = 2;
    public static final int DAYS_AGO_7 = 7;
    public static final int DAYS_AGO_14 = 14;
    public static final int DAYS_AGO_30 = 30;

    /**
     * X月
     */
    public static final int MONTHS_1 = 1;
    public static final int MONTHS_6 = 6;
    public static final int MONTHS_12 = 12;

}
