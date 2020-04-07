package com.jimetec.xunji.util;

import com.common.lib.utils.TimeUtils;

import java.util.Calendar;

/**
 * 作者:capTain
 * 时间:2019-07-30 17:50
 * 描述:
 */
public class DateTimeEntity {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public static DateTimeEntity now() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new DateTimeEntity(year, month, day, hour, minute);
    }

    public static DateTimeEntity getSixHourAgo() {
        Calendar calendar = Calendar.getInstance();

//        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -6);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new DateTimeEntity(year, month, day, hour, minute);
    }

    public long getCurrentTimeMillis(){
//        yyyy-MM-dd HH:mm:ss


        return  TimeUtils.string2Millis(year+"-"+getTwo(month)+"-"+getTwo(day)+" "+getTwo(hour)+":"+getTwo(minute)+":"+"00");
    }


    public String  getTwo(int num){

        if (num>9){
            return num+"";
        }else {
            return "0"+num;
        }
    }





    public static DateTimeEntity hundredYearsOnFuture() {
        DateTimeEntity entity = now();
        entity.setYear(entity.getYear() + 100);
        return entity;
    }

    public DateTimeEntity(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public DateTimeEntity(int year, int month, int day, int hour, int minute) {
        this(year, month, day, hour, minute, 0);
    }

    public DateTimeEntity(String year, String month, String day, String hour, String minute) {
        this(
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day),
                Integer.valueOf(hour),
                Integer.valueOf(minute),
                0);
    }



    public void   updateDateTime(String year, String month, String day, String hour, String minute) {
        updateDateTime(
                Integer.valueOf(year),
                Integer.valueOf(month),
                Integer.valueOf(day),
                Integer.valueOf(hour),
                Integer.valueOf(minute),
                0);
    }


    public void  updateDateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }



    public String getYMD(){
        return year+"-"+getTwo(month)+"-"+getTwo(day);
    }
    public String getHM(){
        return getTwo(hour)+":"+getTwo(minute)+":00";
    }


    public DateTimeEntity(int month, int day, int hour, int minute) {
        this(Calendar.getInstance().get(Calendar.YEAR), month, day, hour, minute, 0);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
