package com.example.demo.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Chang
 * @Description 日期格式工具类
 * @Date 2020/12/6 21:07
 **/
public class DateUtil {

    public static final String[] HOURS = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
            "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    public static final String[] DAYS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    public static final String[] MONTHS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
//	public static final Integer[] ARR_24 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//	public static final Integer[] ARR_31 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//	public static final Integer[] ARR_12 = {0,0,0,0,0,0,0,0,0,0,0,0};

    private static class DateFormatPattern {

        private String pattern;

        private String regex;

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

    private static final Date MIN_ALLOWED_DATE = new Date(-30609820800000L);
    public static final String XML_GREGORIAN_CALENDAR_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXZ";

    private static final List<DateFormatPattern> patternList = new ArrayList<DateFormatPattern>();

    private static volatile String lastUniqueTimeStamp = DateUtil.getFormattedDate(new Date(), "yyyyMMddhhmmssSSS");
    private static volatile long lastUniqueMilliseconds = new Date().getTime();

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Object uniqueTimeStampLock = new Object();
    private static final Object uniqueMillisecondsLock = new Object();

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
        patternList.add(new DateFormatPattern(XML_GREGORIAN_CALENDAR_PATTERN, "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}[-+]\\d{2}:\\d{2}"));// 2012-03-15T13:48:49.733+08:00

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
                if (pattern.getPattern().equals(XML_GREGORIAN_CALENDAR_PATTERN)) {
                    return parseXMLGregorianCalendar(sDate).getTime();
                } else {
                    SimpleDateFormat parser = new SimpleDateFormat(pattern.getPattern());
                    try {
                        return parser.parse(sDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        throw new RuntimeException("Cannot parse String to Date: " + sDate);
    }

    public static Calendar parseXMLGregorianCalendar(String sDate) {
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(sDate);
            return xmlGCalendar.toGregorianCalendar();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取得指定日期的开始点，值为yyyy-MM-dd HH:mm:ss:000，如2011-09-07的开始时间为2011-09-07
     * 00:00:00:000
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
     * 取得指定日期的结束点，值为yyyy-MM-dd 23:59:59:999，如2011-09-07的结束时间为2011-09-07
     * 23:59:59:999
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
     * 取得以格式yyyy-MM-dd表示的日期
     *
     * @param date 待格式化的日期
     * @return yyyy-MM-dd格式化的日期
     */
    public static String getDateYMD(Date date) {
        return shortSdf.format(date);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return getFormattedDate(new Date(), "yyyyMMdd");
    }

    /**
     * 取得指定格式表示的日期/时间
     *
     * @param date    待格式化的日期/时间
     * @param pattern 目标格式
     * @return 按目标格式格式化后的日期/时间，如果时间或者目标格式为null则返回null
     */
    public static String getFormattedDate(Date date, String pattern) {
        if (date == null || pattern == null)
            return null;
        if (XML_GREGORIAN_CALENDAR_PATTERN.equals(pattern)) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return toXMLGregorianCalendarFormat(calendar);
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 把Calendar对象转成符合W3C XML Schema 1.0 标准推荐规范的词法格式
     *
     * @param calendar
     * @return
     */
    public static String toXMLGregorianCalendarFormat(Calendar calendar) {
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            GregorianCalendar gCalendar = new GregorianCalendar(calendar.getTimeZone());
            gCalendar.setTime(calendar.getTime());
            XMLGregorianCalendar xmlGCalendar = df.newXMLGregorianCalendar(gCalendar);
            return xmlGCalendar.toXMLFormat();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取一个yyyyMMddHHmmssSSS格式的时间戳，只保证单JVM唯一
     *
     * @return
     */
    public static String getUniqueTimeStamp() {
        synchronized (uniqueTimeStampLock) {
            String s;
            do {
                s = DateUtil.getFormattedDate(new Date(), "yyyyMMddHHmmssSSS");
            } while (s.equals(lastUniqueTimeStamp));
            lastUniqueTimeStamp = s;
            return s;
        }
    }

    /**
     * 取一个系统时间毫秒数作为时间戳，只保证单JVM唯一
     *
     * @return
     */
    public static long getUniqueMilliseconds() {
        synchronized (uniqueMillisecondsLock) {
            long next;
            do {
                next = new Date().getTime();
            } while (next == lastUniqueMilliseconds);
            lastUniqueMilliseconds = next;
            return next;
        }
    }

    /**
     * 删除某日期中的秒和毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateSecond(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 删除某日期中的毫秒
     *
     * @param d
     * @return
     */
    public static Date truncateMillisecond(Date d) {
        if (d == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
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
     * 返回一个日期据当前的时间间隔，表示为自然语言意义上的x年y月z天
     *
     * @param dob 始点日期
     * @return 返回的整数数组表示始点日期据当前的时间间隔。[0]位表示年，[1]位表示月，[2]位表示日。
     */
    public static int[] getInterval(Date dob) {
        Calendar now = Calendar.getInstance();
        Calendar dobc = Calendar.getInstance();
        dobc.setTimeInMillis(dob.getTime());
        int sYears = dobc.get(Calendar.YEAR);
        int sMonths = dobc.get(Calendar.MONTH);
        int sDays = dobc.get(Calendar.DAY_OF_MONTH);
        int eYears = now.get(Calendar.YEAR);
        int eMonths = now.get(Calendar.MONTH);
        int eDays = now.get(Calendar.DAY_OF_MONTH);
        int intervalYear;
        int intervalMonth;
        int intrevalDay;
        intervalYear = eYears - sYears;
        intervalMonth = eMonths - sMonths;
        if (intervalMonth < 0) {
            intervalMonth += 12;
            --intervalYear;
        }
        dobc.add(Calendar.YEAR, intervalYear);
        dobc.add(Calendar.MONTH, intervalMonth);
        sDays = dobc.get(Calendar.DAY_OF_MONTH);
        intrevalDay = eDays - sDays;
        if (intrevalDay < 0) {
            --intervalMonth;
            if (intervalMonth < 0) {
                --intervalYear;
                intervalMonth += 12;
            }
            dobc.setTimeInMillis(dob.getTime());
            dobc.add(Calendar.YEAR, intervalYear);
            dobc.add(Calendar.MONTH, intervalMonth);
            intrevalDay = dobc.getActualMaximum(Calendar.DATE) - dobc.get(Calendar.DATE) + eDays;
        }
        return new int[]{intervalYear, intervalMonth, intrevalDay};
    }

    /**
     * 获取同环比图表的legends
     *
     * @param timeType    1日2月3年
     * @param compareType 1同比 2环比
     * @param interval    往前推几天/几月
     * @return
     */
    public static List<String> getLegends(Integer timeType, Integer compareType, Integer interval) {
        List<String> retList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        if (timeType == 1) {
            cal.add(Calendar.DAY_OF_MONTH, 0 - interval);
            retList.add(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
            if (compareType == 1) {
                cal.add(Calendar.YEAR, -1);
            } else {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            retList.add(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
        } else if (timeType == 2) {
            cal.add(Calendar.MONTH, 0 - interval);
            retList.add(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月");
            if (compareType == 1) {
                cal.add(Calendar.YEAR, -1);
            } else {
                cal.add(Calendar.MONTH, -1);
            }
            retList.add(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月");
        } else if (timeType == 3) {
            cal.add(Calendar.MONTH, 0 - interval);
            retList.add(cal.get(Calendar.YEAR) + "年");
            retList.add((cal.get(Calendar.YEAR) - 1) + "年");
        }
        return retList;
    }

    /**
     * 得到n天前或者n天后的时间
     *
     * @param date--传入时间
     * @param days--正的是n天后的,负的是n天之前的
     * @param format--日期格式转换("yyyy-MM-dd HH:mm:ss")
     * @return 处理后的时间
     */
    public static String getDayAfterOrBefore(Date date, int days, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);//当前时间减去或加上n天
        Date dateChange = calendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(dateChange);
        return dateStr;
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
     * @Author Chang
     * @Description 当前季度开始时间
     * @Date 2020/11/19 8:46
     * @Return java.util.Date
     **/
    public static String getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        String now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = shortSdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 获取YYYY格式
     *
     * @return 时间字符串
     */
    public static String getYear() {
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return 时间字符串
     */
    public static String getDay() {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return 时间字符串
     */
    public static String getDay(Date date, String format) {
        SimpleDateFormat sdfDay = new SimpleDateFormat(format);
        return sdfDay.format(date);
    }

    /**
     * 获取YYYY-MM格式
     *
     * @return 时间字符串
     */
    public static String getMonth() {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM");
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return 时间字符串
     */
    public static String getTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfTime.format(new Date());
    }

    public static Date getTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    /**
     * 校验日期是否合法
     *
     * @param s 时间
     * @return 校验结果，TRUE：合法，FASLE：非法
     */
    public static boolean isValidDate(String s, String format) {
        if (s.length() != format.length()) {
            return false;
        }
        DateFormat fmt = new SimpleDateFormat(format);
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static String toyyyyMMdd(String s) {
        Date date = DateUtil.fomatDateLocal(s, "yyyy-MM-dd HH:mm:ss");
        return DateUtil.getDays(date);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return 时间字符串
     */
    public static String getDays(Date date) {
        SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
        return sdfDays.format(date);
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     *
     * @return 时间字符串
     */
    public static String getFileTime() {
        SimpleDateFormat sdfFileTime = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdfFileTime.format(new Date());
    }

    public static String getDateTime(Date date, String format) {
        DateFormat fmt = new SimpleDateFormat(format);
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return fmt.format(date);
    }

    public static String getDateTimeCST(Date date, String format) {
        return getDateTime(date, format, "GMT+8");
    }

    public static String getDateTime(Date date, String format, String zone) {
        DateFormat fmt = new SimpleDateFormat(format);
        if (zone == null) {
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        } else {
            fmt.setTimeZone(TimeZone.getTimeZone(zone));
        }
        return fmt.format(date);
    }

    public static String getDateTime(Timestamp time, String format) {
        DateFormat fmt = new SimpleDateFormat(format);
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return fmt.format(new Date(time.getTime()));
    }

    public static String getDateTimeCST(Timestamp time, String format) {
        return getDateTime(time, format, "GMT+8");
    }

    public static String getDateTime(Timestamp time, String format, String zone) {
        DateFormat fmt = new SimpleDateFormat(format);
        if (zone == null) {
            fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        } else {
            fmt.setTimeZone(TimeZone.getTimeZone(zone));
        }
        return fmt.format(new Date(time.getTime()));
    }

    public static Timestamp getTimestampCST() {
        return Timestamp.valueOf(getDateTimeCST(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static Timestamp getTimestampCST(Date date) {
        if (date == null) {
            return null;
        }
        return Timestamp.valueOf(getDateTimeCST(date, "yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static String formatTimestampToString(Timestamp timestamp) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tsStr = sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public static String formatTimestampToStringByPattern(Timestamp timestamp, String pattern) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(pattern);
        try {
            tsStr = sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public static Timestamp getTimestampCST(String date, String format) {
        if (date == null) {
            return null;
        }
        return getTimestampCST(fomatDateLocal(date, format));
    }

    /**
     * 日期比较
     *
     * @param s 时间1
     * @param e 时间2
     * @return boolean 如果s大于等于e 返回true 否则返回false
     * @author chang
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @param date 时间
     * @return 日期
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //依照传入的时间格式处理时间
    public static Date fomatDate(String date, String format) {
        DateFormat fmt = new SimpleDateFormat(format);
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date fomatDateLocal(String date, String format) {
        DateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SepTime(String time) {
        StringBuilder temp = new StringBuilder(time);
        temp.insert(4, ":");
        temp.insert(2, ":");
        return temp.toString();
    }

    /**
     * 校验日期是否合法
     *
     * @param s 时间
     * @return 校验结果，TRUE：合法，FASLE：非法
     */
    public static boolean isValidDate(String s) {
        if (s.length() != 10) {
            return false;
        }
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * 时间相减得到天数
     *
     * @param beginDateStr 开始时间
     * @param endDateStr   结束时间
     * @return long 天数
     * @author chang
     */
    public static long getDaySub(final String beginDateStr, final String endDateStr) {
        long day = 0;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = format.parse(beginDateStr);
            Date endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {

        }
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 时间相减得到分钟
     *
     * @param beginDateStr 开始时间
     * @param endDateStr   结束时间
     * @return long 天数
     * @author chang
     */
    public static long getMinuteSub(final String beginDateStr, final String endDateStr) {
        long minute = 0;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginDate = format.parse(beginDateStr);
            Date endDate = format.parse(endDateStr);
            minute = (endDate.getTime() - beginDate.getTime()) / (60 * 1000);
        } catch (ParseException e) {

        }
        // System.out.println("相隔的天数="+day);

        return minute;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days n天
     * @return n天之后的日期
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n小时前或者n小时后的时间
     *
     * @param date--传入时间
     * @param hours--正的是n小时后的,负的是n小时之前的
     * @param format--日期格式转换("yyyy-MM-dd HH:mm:ss")
     * @return 处理后的时间
     */
    public static String getHourAfterOrBefore(Date date, int hours, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
        Date dateChange = calendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(dateChange);
        return dateStr;
    }

    /**
     * 得到n个月前或者n个月后的时间
     *
     * @param date--传入时间
     * @param months--正的是n月后的,负的是n月之前的
     * @param format--日期格式转换("yyyy-MM-dd HH:mm:ss")
     * @return 处理后的时间
     */
    public static String getMonthAfterOrBefore(Date date, int months, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);//当前时间减去或加上n个月
        Date dateChange = calendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(dateChange);
        return dateStr;
    }

    /**
     * 得到n年前或者n年后的时间
     *
     * @param date--传入时间
     * @param years--正的是n年后的,负的是n年之前的
     * @param format--日期格式转换("yyyy-MM-dd HH:mm:ss")
     * @return 处理后的时间
     */
    public static String getYearAfterOrBefore(Date date, int years, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);//当前时间减去或加上n年
        Date dateChange = calendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(dateChange);
        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days n天
     * @return n天之后是周几
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    public static String getMonthStart() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (1 - index));
        return sdf.format(calendar.getTime());
    }

    //	public static void main(String args[]) {
//		// 月初
//		System.out.println("月初" + getMonthStart());
//		// 月末
//		System.out.println("月末" + getMonthEnd());
//		//下一天
//		System.out.println("下一天" + getNext());
//	}
    public static String getMonthEnd() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (-index));
        return sdf.format(calendar.getTime());
    }

    public static Date getMonthEnd(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.fomatDateLocal(date + "-01", "yyyy-MM-dd"));
        calendar.add(Calendar.MONTH, 1);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (-index));
        return calendar.getTime();
    }

    public static String getNext() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return sdf.format(calendar.getTime());
    }

    public static String getNow() {
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        return sdfDay.format(new Date());
    }

    /**
     * 得到一个时间点的前面(负值)或者后面(正值)time分钟
     *
     * @param date
     * @param time
     * @return
     */
    public static String getAfterOrBefore(Timestamp date, int time) {
        Date newDate = new Date(date.getTime() + (1000 * 60 * time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDateStr = dateFormat.format(newDate);
        return newDateStr;
    }

    public static String getDateForDV(Date date, int sec) {
        Date newDate = new Date(date.getTime() + (1000 * sec));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDateStr = dateFormat.format(newDate);
        return newDateStr;
    }

    /**
     * 获取当前日期是星期几，周日为0
     *
     * @param date
     * @return 日期是星期几
     */
    public static int getWeekOfDate(Date date) {
//        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /**
     * 获取所提供日期当月天数
     *
     * @param date
     * @return 当月天数
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
