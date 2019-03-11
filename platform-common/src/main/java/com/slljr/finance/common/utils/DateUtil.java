package com.slljr.finance.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slljr.finance.common.enums.DateStyle;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.util.Calendar.WEEK_OF_MONTH;
import static java.util.Calendar.WEEK_OF_YEAR;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal();
    private static final Object object = new Object();

    public DateUtil() {
    }

    /**
     * "2018-08-14 15:33""
     * 通过日期字符串返回SimpleDateFormat 对象
     * @param pattern
     * @return
     * @throws RuntimeException
     */
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = (SimpleDateFormat)threadLocal.get();
        if(dateFormat == null) {
            Object var2 = object;
            synchronized(object) {
                if(dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }

        dateFormat.applyPattern(pattern);
        return dateFormat;
    }


    //1：年 2：月，要加1 3：今年第几周 4：这个月第几周 5：日 6：今年第几天 7：一周中的第几天（有的以周日为开始）

    //8:某月中第几周
    private static int getInteger(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if(date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }

        return num;
    }

    /**
     * 在给定的日期和类型上相加（例如：date==》"2018-08-14 15:32"，dateType：1（年），amount：2）
     * 结果为 2020-08-14 15:32
     * @param date 日期字符串
     * @param dateType 类型
     * @param amount
     * @return
     */
    private static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateStyle dateStyle = getDateStyle(date);
        if(dateStyle != null) {
            Date myDate = StringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = DateToString(myDate, dateStyle);
        }

        return dateString;
    }

    /**
     * 当前日期 2018-08-14 15:32
     * 在给定的日期和类型上相加（例如：date==》new Date()，dateType：1（年），amount：2）
     * 结果为 2020-08-14 15:32
     * @param date 日期字符串
     * @param dateType 类型
     * @param amount
     * @return
     */
    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }

        return myDate;
    }


    /**
     * 通过时间戳获取Date对象
     *
     * @param timestamps
     * @return
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0L;
        HashMap map = new HashMap();
        ArrayList absoluteValues = new ArrayList();
        if(timestamps != null && timestamps.size() > 0) {
            if(timestamps.size() > 1) {
                for(int minAbsoluteValue = 0; minAbsoluteValue < timestamps.size(); ++minAbsoluteValue) {
                    for(int j = minAbsoluteValue + 1; j < timestamps.size(); ++j) {
                        long timestampsLastTmp = Math.abs(((Long)timestamps.get(minAbsoluteValue)).longValue() - ((Long)timestamps.get(j)).longValue());
                        absoluteValues.add(Long.valueOf(timestampsLastTmp));
                        long[] timestampTmp = new long[]{((Long)timestamps.get(minAbsoluteValue)).longValue(), ((Long)timestamps.get(j)).longValue()};
                        map.put(Long.valueOf(timestampsLastTmp), timestampTmp);
                    }
                }

                long var13 = -1L;
                if(!absoluteValues.isEmpty()) {
                    var13 = ((Long)absoluteValues.get(0)).longValue();

                    for(int var14 = 1; var14 < absoluteValues.size(); ++var14) {
                        if(var13 > ((Long)absoluteValues.get(var14)).longValue()) {
                            var13 = ((Long)absoluteValues.get(var14)).longValue();
                        }
                    }
                }

                if(var13 != -1L) {
                    long[] var15 = (long[])map.get(Long.valueOf(var13));
                    long dateOne = var15[0];
                    long dateTwo = var15[1];
                    if(absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo)?dateOne:dateTwo;
                    }
                }
            } else {
                timestamp = ((Long)timestamps.get(0)).longValue();
            }
        }

        if(timestamp != 0L) {
            date = new Date(timestamp);
        }

        return date;
    }

    /**
     * 判断是否是日期格式
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        boolean isDate = false;
        if(date != null && getDateStyle(date) != null) {
            isDate = true;
        }

        return isDate;
    }

    /**
     * YYYY_MM_DD("yyyy-MM-dd", false),
     *获取枚举的YYYY_MM_DD
     * @param date
     * @return
     */
    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        HashMap map = new HashMap();
        ArrayList timestamps = new ArrayList();
        DateStyle[] accurateDate = DateStyle.values();
        int len$ = accurateDate.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            DateStyle style = accurateDate[i$];
            if(!style.isShowOnly()) {
                Date dateTmp = null;
                if(date != null) {
                    try {
                        ParsePosition e = new ParsePosition(0);
                        dateTmp = getDateFormat(style.getValue()).parse(date, e);
                        if(e.getIndex() != date.length()) {
                            dateTmp = null;
                        }
                    } catch (Exception var10) {
                        logger.debug("获取日期字符串的日期风格失败！", var10);
                    }
                }

                if(dateTmp != null) {
                    timestamps.add(Long.valueOf(dateTmp.getTime()));
                    map.put(Long.valueOf(dateTmp.getTime()), style);
                }
            }
        }

        Date var11 = getAccurateDate(timestamps);
        if(var11 != null) {
            dateStyle = (DateStyle)map.get(Long.valueOf(var11.getTime()));
        }

        return dateStyle;
    }

    /**
     * 日期字符串转Date日期
     *
     * @param date
     * @return
     */
    public static Date StringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return StringToDate(date, dateStyle);
    }

    /**
     * 将日期字符串转化为日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date StringToDate(String date, String pattern) {
        Date myDate = null;
        if(date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception var4) {
                logger.debug("将日期字符串转化为日期失败！", var4);
            }
        }

        return myDate;
    }


    /**
     *
     * @param date 2018-08-15 15:21
     * @param dateStyle DateStyle.YYYY_MM_DD_HH_MM
     * @return
     */
    public static Date StringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if(dateStyle != null) {
            myDate = StringToDate(date, dateStyle.getValue());
        }

        return myDate;
    }

    /**
     * 把date日期转化为指定的字符串日期格式
     * new Date(),"yyyy年MM月dd日 HH:mm:ss"
     * @param date
     * @param pattern
     * @return
     */
    public static String DateToString(Date date, String pattern) {
        String dateString = null;
        if(date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception var4) {
                logger.debug("将日期转化为日期字符串失败！", var4);
            }
        }

        return dateString;
    }

    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if(dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }

        return dateString;
    }

    public static String StringToString(String date, String newPattern) {
        DateStyle oldDateStyle = getDateStyle(date);
        return StringToString(date, oldDateStyle, newPattern);
    }

    public static String StringToString(String date, DateStyle newDateStyle) {
        DateStyle oldDateStyle = getDateStyle(date);
        return StringToString(date, oldDateStyle, newDateStyle);
    }

    public static String StringToString(String date, String olddPattern, String newPattern) {
        return DateToString(StringToDate(date, olddPattern), newPattern);
    }

    public static String StringToString(String date, DateStyle olddDteStyle, String newParttern) {
        String dateString = null;
        if(olddDteStyle != null) {
            dateString = StringToString(date, olddDteStyle.getValue(), newParttern);
        }

        return dateString;
    }

    public static String StringToString(String date, String olddPattern, DateStyle newDateStyle) {
        String dateString = null;
        if(newDateStyle != null) {
            dateString = StringToString(date, olddPattern, newDateStyle.getValue());
        }

        return dateString;
    }

    public static Long getNow(){

        Long time = System.currentTimeMillis()/1000;
        return time;
    }


    public static Long getSecond(Long second){
        if (null == second) return 0L;
        Long time = second/1000;
        return time;
    }

    public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
        String dateString = null;
        if(olddDteStyle != null && newDateStyle != null) {
            dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
        }

        return dateString;
    }

    /**
     * 时间戳转日期
     *
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy.MM.dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }


    /*当前日期年份添加  例：2018-10-15 15:32 加1 2019-10-15 15:32*/
    public static String addYear(String date, int yearAmount) {
        return addInteger((String)date, 1, yearAmount);
    }

    //当前年份加n年
    public static Date addYear(Date date, int yearAmount) {
        return addInteger((Date)date, 1, yearAmount);
    }
    //添加月
    public static String addMonth(String date, int monthAmount) {
        return addInteger((String)date, 2, monthAmount);
    }
    //添加月
    public static Date addMonth(Date date, int monthAmount) {
        return addInteger((Date)date, 2, monthAmount);
    }
    //添加天
    public static String addDay(String date, int dayAmount) {
        return addInteger((String)date, 5, dayAmount);
    }
    //添加天
    public static Date addDay(Date date, int dayAmount) {
        return addInteger((Date)date, 5, dayAmount);
    }

    public static String addHour(String date, int hourAmount) {
        return addInteger((String)date, 11, hourAmount);
    }

    public static Date addHour(Date date, int hourAmount) {
        return addInteger((Date)date, 11, hourAmount);
    }

    public static String addMinute(String date, int minuteAmount) {
        return addInteger((String)date, 12, minuteAmount);
    }

    public static Date addMinute(Date date, int minuteAmount) {
        return addInteger((Date)date, 12, minuteAmount);
    }

    public static String addSecond(String date, int secondAmount) {
        return addInteger((String)date, 13, secondAmount);
    }

    public static Date addSecond(Date date, int secondAmount) {
        return addInteger((Date)date, 13, secondAmount);
    }

    public static int getYear(String date) {
        return getYear(StringToDate(date));
    }

    public static int getYear(Date date) {
        return getInteger(date, 1);
    }

    public static int getMonth(String date) {
        return getMonth(StringToDate(date));
    }

    public static int getMonth(Date date) {
        return getInteger(date, 2) + 1;
    }

    public static int getDay(String date) {
        return getDay(StringToDate(date));
    }

    public static int getDay(Date date) {
        return getInteger(date, 5);
    }

    public static int getHour(String date) {
        return getHour(StringToDate(date));
    }

    public static int getHour(Date date) {
        return getInteger(date, 11);
    }

    public static int getMinute(String date) {
        return getMinute(StringToDate(date));
    }

    public static int getMinute(Date date) {
        return getInteger(date, 12);
    }

    public static int getSecond(String date) {
        return getSecond(StringToDate(date));
    }

    public static int getSecond(Date date) {
        return getInteger(date, 13);
    }

    public static String getDate(String date) {
        return StringToString(date, DateStyle.YYYY_MM_DD);
    }

    public static String getDate(Date date) {
        return DateToString(date, DateStyle.YYYY_MM_DD);
    }

    public static String getTime(String date) {
        return StringToString(date, DateStyle.HH_MM_SS);
    }

    public static String getTime(Date date) {
        return DateToString(date, DateStyle.HH_MM_SS);
    }



    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(StringToDate(date), StringToDate(otherDate));
    }

    public static int getIntervalDays(Date date, Date otherDate) {
        int num = -1;
        Date dateTmp = StringToDate(getDate(date), DateStyle.YYYY_MM_DD);
        Date otherDateTmp = StringToDate(getDate(otherDate), DateStyle.YYYY_MM_DD);
        if(dateTmp != null && otherDateTmp != null) {
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
            num = (int)(time / 86400000L);
        }

        return num;
    }

    /**
     * 获取指定日期之前或者之后偏移量个的日期
     *
     * @param type   类型【1：年；2：月；3：日；4：时；5：分；6：秒】
     * @param day    指定的时间
     * @param offset 日期偏移量，正数表示延后，负数表示天前
     * @return java.util.Date
     * @author uncle.quentin
     * @date 2019/2/25 15:07
     * @version 1.0
     */
    public static Date getDateByOffset(int type, Date day, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        switch (type) {
            //年
            case 1:
                c.add(Calendar.YEAR, offset);
                break;
            //月
            case 2:
                c.add(Calendar.MONTH, offset);
                break;
            //日
            case 3:
                c.add(Calendar.DATE, offset);
                break;
            //时
            case 4:
                c.add(Calendar.HOUR, offset);
                break;
            //分
            case 5:
                c.add(Calendar.MINUTE, offset);
                break;
            default:
                break;
        }

        return c.getTime();
    }

    public static void main(String[] args) {
        String s = "2018-10-15 15:32";
        Date date = StringToDate(s);

        date = getDateByOffset(3,date,8);

        System.out.println(DateToString(date, "yyyy-MM-dd HH:mm:ss"));
    }
}
 




