package com.common.lib.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者:zh
 * 时间:12/1/18 10:30 AM
 * 描述:
 */
public class DateUtil {

    private static final Calendar mCalendar;

    static {
        //保留两位
        sDfmon = new DecimalFormat("#00");
        mCalendar = Calendar.getInstance();
    }

    private static DecimalFormat sDfmon;


    public static int getCurYear() {
        return mCalendar.get(Calendar.YEAR);
    }


    public static int getCurMon() {
        return mCalendar.get(Calendar.MONTH) + 1;
    }


    public static String getCurNetYearMon() {
        return DateUtil.getCurYear() + DateUtil.getNetworkMon(DateUtil.getCurMon());

    }

    public static String getNetworkMon(Object obj) {
        return sDfmon.format(obj);
    }


    public static int getCurDay() {
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }


    public static String getDay(String str) {
        try {
            String[] split = str.split("-");
            if (split.length > 1) {
                return split[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }


    public static String getWeek(String str) {
        int dayForWeek = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(str));
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dayForWeek(dayForWeek);
    }


    public static String dayForWeek(int dayForWeek) {
        switch (dayForWeek) {
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


    public static String getBeforeSixMonth() {
        Date dNow = new Date(); //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -6); //设置为前3月
        dBefore = calendar.getTime(); //得到前3月的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore); //格式化前3月的时间
//        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        System.out.println("前3个月的时间是：" + defaultStartDate);
//        System.out.println("生成的时间是：" + defaultEndDate);
        return defaultStartDate;
    }


    /**
     * @param strDate yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getMdHM(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatMdHM = new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date date = format.parse(strDate);
            String MdHM = formatMdHM.format(date);
            return MdHM;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * @param strDate yyyy-MM-dd
     * @return
     */
    public static String getYMDdes(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatMdHM = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = format.parse(strDate);
            String MdHM = formatMdHM.format(date);
            return MdHM;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }








    /**
     * @param strDate yyyy-MM-dd
     * @return
     */
    public static String getMd(String strDate) {
        try {
            String[] split = strDate.split("-");
            if (split.length > 1) {
                return split[0] + "-" + split[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    /**
     * @param millions
     * @return yyyy-MM-dd
     */
    public static String getYMd(long millions) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millions);

        try {
            String ymd = format.format(date);
            return ymd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @return
     */
    public static String[] getYMDArr(long millions) {
        try {
            if (millions>0) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(millions);
                String ymd = format.format(date);
                String[] split = ymd.split("-");
                if (split.length > 2) {
                    return split;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param millions
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYMdMHS(long millions) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millions);

        try {

            String ymd = format.format(date);
            return ymd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}
