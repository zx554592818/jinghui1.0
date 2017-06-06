package com.example.jinghui.model;

/**
 * Created by zhaoxin on 2017-05-08.
 */

public class TimeFormat {
    private static TimeFormat timeFormat;

    public TimeFormat() {
    }

    public String getDayTime(String string) {
        //填数据去掉小时
        String dayTime = string.substring(0, 10);
        return dayTime;
    }

    public String getHourTime(String string) {
        //因为数据库存的时间格式问题 去掉微秒
        String hourTime = string.substring(0, 19);
        String newTm = hourTime.replace(" ", "\n"); //将返回时间的字符串格式替换掉
        return newTm;
    }

    public static TimeFormat getInstance() {
        if (timeFormat == null) {
            timeFormat = new TimeFormat();
        }
        return timeFormat;
    }

}
