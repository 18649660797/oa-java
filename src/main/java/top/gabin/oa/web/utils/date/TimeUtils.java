/**
 * Copyright (c) 2015 云智盛世
 * Created with TimeUtils.
 */
package top.gabin.oa.web.utils.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author linjiabin  on  15/12/14
 */
public class TimeUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date parseDate(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            if (StringUtils.isBlank(date)) {
                return null;
            }
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
        }
        return "";
    }

    public static boolean isBetween(Date source, Date a, Date b) {
        if (source.getTime() == a.getTime() || source.getTime() == b.getTime()) {
            return true;
        }
        if (source.getTime() > a.getTime() && source.getTime() < b.getTime()) {
            return true;
        }
        return false;
    }

    public static boolean beforeOrEqual(Date a1, Date b1) {
        if (a1.getTime() == b1.getTime()) {
            return true;
        }
        if (a1.getTime() < b1.getTime()) {
            return true;
        }
        return false;
    }

    public static boolean afterOrEqual(Date a1, Date b1) {
        if (a1.getTime() == b1.getTime()) {
            return true;
        }
        if (a1.getTime() > b1.getTime()) {
            return true;
        }
        return false;
    }

    public static long getMinutes(Date a1, Date b1) {
        return (a1.getTime() - b1.getTime()) / 1000 / 60;
    }

}
