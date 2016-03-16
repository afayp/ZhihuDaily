package com.app.pfh.zhihudaily.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String getTime(String date){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
        try {
            Date date1 = sdf1.parse(date);
            date1.setTime(date1.getTime() - 24 * 60 * 60);
            String time = sdf2.format(date1);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "11月11日";
    }
}
