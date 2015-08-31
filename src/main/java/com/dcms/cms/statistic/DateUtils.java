package com.dcms.cms.statistic;


import org.apache.commons.lang.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dailinyi on 2015/5/28.
 */
public class DateUtils {
    public static final String SIMPLE_DATE = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间转化成标准的yyyy-MM-dd HH:mm:ss格式
     * 如果为空返回空字符串
     * @param date
     * @return 格式为yyyy-MM-dd HH:mm:ss的string串
     */
    public static String dateToStr(Date date) {
        if(date == null){
            return new String();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_TIME);
        String dateString = formatter.format(date);
        return dateString;
    }



    /**
     * 将时间转化成标准的yyyy-MM-dd格式
     * 如果为空,返回空字符串
     * @param date
     * @return 格式为yyyy-MM-dd的string串
     */
    public static String dateToStrShort(Date date) {
        if(date == null){
            return new String();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE);
        String dateString = formatter.format(date);
        return dateString;
    }



    /**
     * 获取当前时间
     * @return 格式为yyyy-MM-dd HH:mm:ss的string串
     */
    public static String getCurrentDate(){
        return dateToStr(new Date());
    }

    /**
     * 获取当前时间
     * @return 格式为yyyy-MM-dd的string串
     */
    public static String getCurrentDateShort(){
        return dateToStrShort(new Date());
    }

    /**
     * String转换为Date
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_TIME);
        SimpleDateFormat shortFormatter = new SimpleDateFormat(SIMPLE_DATE);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = null;
        //先以标准格式转换,如果异常,则以短格式转换 ,均异常则报错,返回空
        try {
            strtodate = formatter.parse(strDate, pos);
            if (strtodate == null){
                strtodate = shortFormatter.parse(strDate,pos);
            }
        } catch (Exception e) {
            try {
                strtodate = shortFormatter.parse(strDate,pos);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return strtodate;
    }





    /* 获取当天0点 */
    public static Date getCurrentDayZeroHour(){
        return strToDate(getCurrentDateShort());
    }



}
