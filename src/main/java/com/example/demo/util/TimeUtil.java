package com.example.demo.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于JDK8 time包的时间工具类
 *
 * @author wanglei
 * @date 2017.12.12
 * @since v1.0.0
 */
public final class TimeUtil {
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
        CUSTOM_TIME_MINUTES("mm"),
        CUSTOM_DATE_PATTERN_NONE("yyMMdd"),
        CUSTOM_TIME_HOUR_MINUTES_SECONDS("HH:mm:ss"),
        CUSTOM_DATE_PATTERN_WORD("yyyy年MM月"),
        CUSTOM_DATE_PATTERN_SPOT("yyyy.MM.dd"),


        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),
        SHORT_DATE_PATTERN_WORD("yyyy年MM月dd日"),
        SHORT_DATE_PATTERN_WORD_NO_YEAR("MM月dd日"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_LINE_WITHOUT_SECONDS("yyyy-MM-dd HH:mm"),
        LONG_DATE_PATTERN_LINE_WITHOUT_HOUR("yyyy-MM-dd HH"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH_WITHOUT_SECONDS("yyyy/MM/dd HH:mm"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
        LONG_DATE_PATTERN_UNSIGN("yyyyMMddHHmmss"),
        LONG_DATE_PATTERN_LINE_ZH("yyyy年MM月dd日 HH:mm"),

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
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;

    /**
     * 获取默认时区: Asia/Shanghai
     */
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    /**
     * 获取默认时区偏移量: +8
     */
    public static final ZoneOffset DEFAULT_ZONE_OFF_SET = ZoneOffset.of("+8");

    private TimeUtil() {
        // no construct function
    }

    /**
     * 时间转 String
     *
     * @param time 待转时间
     * @return String
     */
    private static String parseTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    /**
     * 时间转 String
     *
     * @param time 待转时间
     * @param format 时间格式
     * @return String
     */
    private static String parseTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * int 转时间
     *
     * @param time 待转时间
     * @return LocalDateTime
     */
    private static LocalDateTime parseTime(int time) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), DEFAULT_ZONE);
    }

    private static LocalDateTime parseTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(time), DEFAULT_ZONE);
    }

    /**
     * int 转时间
     *
     * @param time 待转时间
     * @return LocalDateTime
     */
    private static LocalDate parseTimeToDate(int time) {
        return parseTime(time).toLocalDate();
    }

    /**
     * 时间转 int
     *
     * @param time 待转时间
     * @return int
     */
    public static int parseTimeToInt(LocalDateTime time) {
        return (int)time.toEpochSecond(DEFAULT_ZONE_OFF_SET);
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
     * LocalDateTime 转为 Date
     */
    private static Date parseTimeToDate(LocalDateTime time){
        ZonedDateTime zdt = time.atZone(DEFAULT_ZONE);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取当前时间(秒)
     *
     * @return int
     */
    public static int getCurrentTime() {
        return parseTimeToInt(LocalDateTime.now());
    }

    /**
     * 获取当前时间(秒)
     *
     * @return int
     */
    public static long getCurrentTimeLong() {
        return parseTimeToLong(LocalDateTime.now());
    }

    /**
     * 获取对应天的开始时间
     */
    public static Date getDateCurrentTime() {
        return parseTimeToDate(LocalDateTime.now());
    }

    /**
     * int 转时间String
     *
     * @param time 待转时间
     * @param format 时间格式
     * @return String
     */
    public static String parseTime(int time, TimeFormat format) {
        return parseTime(parseTime(time), format);
    }

    public static String parseTime(long time, TimeFormat format) {
        return parseTime(parseTime(time), format);
    }

    /**
     * 时间格式的字符串转成int
     *
     * @param dateStr 日期
     * @return 时间戳
     */
    public static int parseDateStrToInt(String dateStr) {
        DateTimeFormatter dateTimeFormatter = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;
        return parseTimeToInt(LocalDate.parse(dateStr, dateTimeFormatter).atTime(LocalTime.now()));
    }

    /**
     * 时间格式的字符串转成int
     *
     * @param dateStr 日期
     * @return 时间戳
     */
    public static int parseTimeStrToInt(String dateStr) {
        DateTimeFormatter dateTimeFormatter = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
        return parseTimeToInt(LocalDateTime.parse(dateStr, dateTimeFormatter));
    }

    public static long parseTimeStrToLong(String dateStr) {
        DateTimeFormatter dateTimeFormatter = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
        return parseTimeToLong(LocalDateTime.parse(dateStr, dateTimeFormatter));
    }

    /**
     * 时间加 (正数为加，负数为减)
     *
     * @param time 待处理时间
     * @param num   数量
     * @param temporalUnit 时间单位 ChronoUnit.DAYS，ChronoUnit.YEARS，ChronoUnit.MONTHS
     * @return int
     */
    public static int timePlus(int time, int num, TemporalUnit temporalUnit) {
        return parseTimeToInt(parseTime(time).plus(num, temporalUnit));
    }

    /**
     * 获取相差天数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 0相等 <0前者大 >后者大
     */
    public static int daysBetween(int startTime, int endTime) {
        LocalDate start = parseTimeToDate(startTime);
        LocalDate end = parseTimeToDate(endTime);
        return (int)start.until(end, ChronoUnit.DAYS);
    }

    /**
     * 判断时间格式是否正确
     * @param dateStr 日期
     * @param datePattern 格式
     * @return true 正确
     */
    public static boolean isRightDateStr(String dateStr, TimeFormat datePattern) {
        String newDateStr;
        try {
            newDateStr = parseTime(parseDateStrToInt(dateStr), datePattern);
        } catch (Exception e) {
            return false;
        }
        return dateStr.equals(newDateStr);
    }

    private static LocalDate getLocalDate(String timeStr, TimeFormat datePattern){
        DateTimeFormatter strToDateFormatter = datePattern.formatter;
        TemporalAccessor dateTemporal = strToDateFormatter.parse(timeStr);
        return LocalDate.from(dateTemporal);
    }

    /**
     * 把一个时间戳类型的数据转换成 LocalDate
     */
    public static LocalDate parseTimeToLocalDate(Integer time){
        LocalDateTime localDateTime = parseTime(time);
        return localDateTime.toLocalDate();
    }

    /**
     * 计算传入时间戳与当前时间差值 大于今天是正值 小于是负值
     */
    public static long betweenNowDays(int time) {
        return LocalDate.now().until(parseTime(time), ChronoUnit.DAYS);
    }

    /**
     * 计算当前日期与传入时间戳差值 小于今天是正值 大于是负值
     */
    public static long betweenReverseNowDays(int time) {
        return parseTime(time).toLocalDate().until(LocalDate.now(), ChronoUnit.DAYS);
    }

    /**
     * 计算传入时间戳与当前时间差值 大于今天是正值 小于是负值
     */
    public static long betweenNowSeconds(int time) {
        return LocalDateTime.now().until(parseTime(time), ChronoUnit.SECONDS);
    }

    /**
     * 计算两个时间戳相差多少小时
     *
     * @param startTime 小值
     * @param endTime   大值
     * @return int 差值小于1小时记为0，小于两个小时记为1
     */
    public static int betweenHours(int startTime, int endTime) {
        return (int) parseTime(startTime).until(parseTime(endTime), ChronoUnit.HOURS);
    }

    /**
     * 计算传入时间戳和当天凌晨相差的分钟数
     */
    public static int betweenMinuteOfWeeHours(int time) {
        LocalTime zero = LocalTime.of(0, 0, 0);
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, zero);
        return (int) localDateTime.until(parseTime(time), ChronoUnit.MINUTES);
    }

    /**
     * 计算两个时间戳分钟的差值
     */
    public static int betweenMinute(int startTime, int endTime) {
        return (int) parseTime(startTime).until(parseTime(endTime), ChronoUnit.MINUTES);
    }

    /**
     * 获取时间戳的小时和分钟
     */
    public static String getHourAndMinute(int time) {
        LocalDateTime localDateTime = parseTime(time);
        return TimeFormat.CUSTOM_TIME_HOUR_MINUTES.formatter.format(localDateTime);
    }

    /**
     * 获取时间戳月份和日期，带年份
     */
    public static String getYearMonthAndDay(int time) {
        LocalDateTime localDateTime = parseTime(time);
        return TimeFormat.SHORT_DATE_PATTERN_LINE.formatter.format(localDateTime);
    }

    /**
     * 获取当前的年月日数据
     *
     * @return string
     */
    static String getNowYearMonthDay() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return TimeFormat.CUSTOM_DATE_PATTERN_NONE.formatter.format(localDateTime);
    }

    /**
     * 获取当前年月日加上传入天数的yyyyMMdd
     */
    public static String getYearMonthDay(Integer day) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plus(day, ChronoUnit.DAYS);
        return TimeFormat.SHORT_DATE_PATTERN_NONE.formatter.format(localDateTime);
    }

    /**
     * 将时间戳 转换成当天 00:00:00 的时间戳
     */
    public static int getTimeStart(int time) {
        String startDateStr = TimeUtil.parseTime(time, TimeFormat.SHORT_DATE_PATTERN_LINE)+" 00:00:00";
        return TimeUtil.parseTimeStrToInt(startDateStr);
    }

    /**
     * 将时间戳 转换成当天 12:00:00 的时间戳
     */
    public static int getTimeMiddle(int time) {
        String startDateStr = TimeUtil.parseTime(time, TimeFormat.SHORT_DATE_PATTERN_LINE)+" 12:00:00";
        return TimeUtil.parseTimeStrToInt(startDateStr);
    }

    /**
     * 将时间戳 转换成当天 23:59:59 的时间戳
     */
    public static int getTimeEnd(int time) {
        String endDateStr = TimeUtil.parseTime(time, TimeFormat.SHORT_DATE_PATTERN_LINE)+" 23:59:59";
        return TimeUtil.parseTimeStrToInt(endDateStr);
    }

    /**
     * 判断过期时间点和当前执行时间点差值 一分钟以内都算在可执行范围之内
     * @param time 短期职位最后一天的时间戳
     */
    public static int isPositionExpired(int time){
        LocalDateTime localDateTime = LocalDateTime.of(parseTime(time).plus(1,  ChronoUnit.DAYS).toLocalDate(), LocalTime.MIN);
        return (int)LocalDateTime.now().until(localDateTime, ChronoUnit.SECONDS);
    }

    /**
     * 把传入的时间戳的时和秒用，参数替换
     */
    public static int getDateTimeByCustomParam(int dateTime, String time){
        String[] hourAndMinute = time.split(":");
        LocalTime zero = LocalTime.of(Integer.parseInt(hourAndMinute[0]), Integer.parseInt(hourAndMinute[1]), 0);
        LocalDateTime localDateTime = LocalDateTime.of(parseTime(dateTime).toLocalDate(), zero);
        return (int)localDateTime.toEpochSecond(DEFAULT_ZONE_OFF_SET);
    }

    /**
     * 把传入的时间戳的时和分用参数替换
     */
    public static int getDateTimeByCustomParam(LocalDate dateTime, String time){
        String[] hourAndMinute = time.split(":");
        LocalTime zero = LocalTime.of(Integer.parseInt(hourAndMinute[0]), Integer.parseInt(hourAndMinute[1]), 0);
        LocalDateTime localDateTime = LocalDateTime.of(dateTime, zero);
        return (int)localDateTime.toEpochSecond(DEFAULT_ZONE_OFF_SET);
    }

    /**
     * 获取昨天的最早时刻时间戳
     */
    public static int getYesterdayStartTime(){
        return parseTimeToInt(LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(-1), LocalTime.MIN));
    }

    /**
     * 获取昨天最晚时刻时间戳
     */
    public static int getYesterdayEndTime(){
        return parseTimeToInt(LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(-1), LocalTime.MAX));
    }

    /**
     * 获取时间格式对应那天的开始和结束时间
     * @param time yyyyMMdd
     */
    public static Map<String, Integer> getDayOfStartOrEndTime(String time){
        LocalDate localDate = getLocalDate(time, TimeFormat.SHORT_DATE_PATTERN_NONE);
        Map<String, Integer> map = new HashMap<>();
        map.put("startTime", parseTimeToInt(LocalDateTime.of(localDate, LocalTime.MIN)));
        map.put("endTime", parseTimeToInt(LocalDateTime.of(localDate, LocalTime.MAX)));
        return map;
    }

    /**
     * 获取传入时间对应天的开始时间
     * @param plusNum 时间天的加减值
     */
    public static int getNowDayStartTime(int plusNum){
        LocalDate localLastDate = LocalDate.now().plusDays(plusNum);
        return parseTimeToInt(LocalDateTime.of(localLastDate, LocalTime.MIN));
    }

    /**
     * 获取传入时间对应天的开始时间
     * @param time 传入的时间戳
     * @param plusNum 时间天的加减值
     */
    public static int getDayStartTime(int time, int plusNum){
        LocalDate localLastDate = parseTime(time).toLocalDate().plusDays(plusNum);
        return parseTimeToInt(LocalDateTime.of(localLastDate, LocalTime.MIN));
    }

    public static Date getPlusDayDate(int plusNum) {
        int time = getTimeEnd(getCurrentTime());
        Instant instant = parseTime(time).plusDays(plusNum).atZone(DEFAULT_ZONE).toInstant();
        return Date.from(instant);
    }

    public static LocalDate getPlusDayDate2(int plusNum) {
        LocalDate now = LocalDate.now();
        return now.plusDays(plusNum);
    }

    public static Date getPlusHoursDate(int hours) {
        int time = getTimeEnd(getCurrentTime());
        Instant instant = parseTime(time).plusHours(hours).atZone(DEFAULT_ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 根据当前时间获取月份加减的开始时间
     */
    public static int getMonthStartTime(int plusNum){
        LocalDate localLastDate = LocalDate.now().plusMonths(plusNum);
        return parseTimeToInt(LocalDate.of(localLastDate.getYear(), localLastDate.getMonth(), 1).atStartOfDay());
    }

    /**
     * 获取传入时间对应月份的开始时间
     * @param time 传入的时间戳
     * @param plusNum 时间月份的加减值
     */
    public static int getMonthStartTime(int time, int plusNum){
        LocalDate localLastDate = parseTime(time).toLocalDate().plusMonths(plusNum);
        return parseTimeToInt(LocalDate.of(localLastDate.getYear(), localLastDate.getMonth(), 1).atStartOfDay());
    }

    /**
     * 获取以当天为基准，加减相应天数的具体时间点的时间戳
     * @param plusNum 加减天数
     * @param hour 整点时间（24小时制）
     */
    public static int getDayTimeByNow(int plusNum, int hour){
        LocalDate localDate = LocalDateTime.now().toLocalDate().plusDays(plusNum);
        return parseTimeToInt(LocalDateTime.of(localDate, LocalTime.of(hour, 0, 0)));
    }

    public static String beforeOneHourToNowDate() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return  df.format(calendar.getTime());
    }

    /**
     * 获取传入时间对应的yyyy-MM-dd HH:mm:ss
     */
    public static String parseTimeToStr(Long time){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), DEFAULT_ZONE);
        return DEFAULT_DATETIME_FORMATTER.format(localDateTime);
    }

    /**
     * 获取时间戳的分钟
     */
    public static String getMinute(Long time) {
        LocalDateTime localDateTime = parseTime(time);
        return TimeFormat.CUSTOM_TIME_MINUTES.formatter.format(localDateTime);
    }

    /**
     * 获取上个月最后一天的 23:59:59
     */
    public static Long getLastMonthTimeEnd() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return TimeUtil.parseTimeToLong(LocalDateTime.of(lastMonth.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX));
    }

    /**
     * 获取几个月前的一号
     *
     * @param subtract 几个月
     */
    public static LocalDate getMonthsAgoStartDate(Long subtract) {
        LocalDate monthsAgo = LocalDate.now().minusMonths(subtract);
        return monthsAgo.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期的零点（00:00:00）
     *
     * @param day 日期
     */
    public static Long getStartTimeOfDay(LocalDate day) {
        return TimeUtil.parseTimeToLong(LocalDateTime.of(day, LocalTime.MIN));
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthdayStr 生日字符串
     * @return 计算了生日的周岁
     */
    public static int getAgeByBirthday(String birthdayStr) {
        LocalDate endDate = LocalDate.now();
        Period period = Period.between(getLocalDate(birthdayStr, TimeFormat.SHORT_DATE_PATTERN_NONE), endDate);
        return period.getYears();
    }

    /**
     * 根据生日获取日期间隔
     * @param birthdayStr 生日字符串
     * @return Period
     */
    public static Period getPeriodByBirthday(String birthdayStr){
        LocalDate endDate = LocalDate.now();
        Period period = Period.between(getLocalDate(birthdayStr, TimeFormat.SHORT_DATE_PATTERN_NONE), endDate);
        return period;
    }
}
