package com.example.jinghui.util;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 赵欣 on 2017/2/28.
 */

public class DateTimeFormatUtil {
    // n天前的日期
    public static String getDateBefore(int day) {
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(now
                .getTime());
        return dateString;
    }

    public static String currentTime() {
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        return dateString;
    }

    //当前时分秒
    public static String currentDayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String dateString = formatter.format(curDate);
        return dateString;
    }

    //当前年月日
    public static String lastDay() {
        String dateString = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date());
        return dateString;
    }

    public static int getYear() {
        Date date = new Date();
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        return year;
    }

    public static int getMonth() {
        Date date = new Date();
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
        return month;
    }

    public static int getDay() {
        Date date = new Date();
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        return day;
    }

    //得到2小时以前的年月日时分秒
    public static String getBeforeTwoHour() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 2);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    //获取当前星期几
    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        //i=1的时候是星期天 i=7的时候是星期6
        switch (i) {
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            default:
                return "";
            case 3:
                return "星期二";

            case 4:
                return "星期三";

            case 5:
                return "星期四";

            case 6:
                return "星期五";

            case 7:
                return "星期六";
        }
    }

    //得到当前时间的前面一个星期的日期
    public static String getBeforeOneWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 6);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
    }

    //当天时间的前面一个月
    public static String lastMonth() {
        Date date = new Date();
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1;
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        if (month == 0) {
            year -= 1;
            month = 12;
        } else if (day > 28) {
            if (month == 2) {
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                    day = 29;
                } else
                    day = 28;
            } else if ((month == 4 || month == 6 || month == 9 || month == 11)
                    && day == 31) {
                day = 30;
            }
        }
        String y = year + "";
        String m = "";
        String d = "";
        if (month < 10)
            m = "0" + month;
        else
            m = month + "";
        if (day < 10)
            d = "0" + day;
        else
            d = day + "";

        return y + "-" + m + "-" + d;
    }

}