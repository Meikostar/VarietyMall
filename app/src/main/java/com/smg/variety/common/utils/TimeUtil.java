package com.smg.variety.common.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: howie
 * Date: 13-5-11
 * Time: 下午4:09
 */
public class TimeUtil {



    /*将字符串转为时间戳*/
    public static long getStringToDates(String time) {
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = new Date();
        try{
            date = sdf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static String getTimeFormat(long total_seconds) {
        String timeStr;
        if (total_seconds <= 0) {
            timeStr = "00,00,00,00";
        } else {
            long days = Math.abs(total_seconds / (24 * 60 * 60));
            long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
            long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
            long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
            timeStr = forMatString(days) + "," + forMatString(hours) + "," + forMatString(minutes) + "," + forMatString(seconds);
        }
        return timeStr;
    }

    public static String formatToFileName(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
        return format.format(new Date(time));
    }
    private static String forMatString(long days) {
        String str;
        if (days < 10) {
            str = "0" + days;
        } else {
            str = "" + days;
        }
        return str;
    }

    public static String formatToFileDi(long time){
        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - time)/1000;

        long minutes = Math.abs(seconds/60);
        long hours = Math.abs(minutes/60);
        long days = Math.abs(hours/24);



        if ( seconds <= 15 )
        {
            return "刚刚";
        }
        else if ( seconds < 120 )
        {
            return"1分钟前";
        }
        else if ( minutes < 60 )
        {
            return minutes+"分钟前";
        }
        else if ( minutes < 120 )
        {
            return "一小时前";
        }
        else if ( hours < 24 )
        {
            return hours +"小时前";
        }
        else if ( hours < 24 * 2 )
        {
            return "昨天";
        }

        else
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(new Date(time));
            return dateString;
        }

    }
    public static String timeAgo(String timeStr)
    {
        Date date = null;
        try
        {
            SimpleDateFormat format =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss Z" );
            date = format.parse(timeStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStamp)/1000;

        long minutes = Math.abs(seconds/60);
        long hours = Math.abs(minutes/60);
        long days = Math.abs(hours/24);



        if ( seconds <= 15 )
        {
            return "刚刚";
        }
        else if ( seconds < 60 )
        {
            return seconds+"秒前";
        }
        else if ( seconds < 120 )
        {
            return"1分钟前";
        }
        else if ( minutes < 60 )
        {
            return minutes+"分钟前";
        }
        else if ( minutes < 120 )
        {
            return "一小时前";
        }
        else if ( hours < 24 )
        {
            return hours +"小时前";
        }
        else if ( hours < 24 * 2 )
        {
            return "一天前";
        }
        else if ( days < 30 )
        {
            return days+"天前" ;
        }
        else
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(date);
            return dateString;
        }

    }

    public static String timeLeft(String timeStr)
    {
        Date date = null;
        try
        {
            SimpleDateFormat format =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss Z" );
            date = format.parse(timeStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp)/1000;

        if (total_seconds <= 0)
        {
            return  "";
        }

        long days = Math.abs(total_seconds/(24*60*60));

        long hours = Math.abs((total_seconds - days*24*60*60)/(60*60));
        long minutes = Math.abs((total_seconds - days*24*60*60 - hours*60*60)/60);
        long seconds = Math.abs((total_seconds - days*24*60*60 - hours*60*60 -minutes*60));
        String leftTime;
        if (days > 0)
        {
            leftTime = days+"天" + hours + "小时" + minutes +"分" +seconds+"秒";
        }
        else if (hours > 0)
        {
            leftTime = hours + "小时" + minutes +"分" +seconds+"秒";
        }
        else if (minutes > 0)
        {
            leftTime = minutes +"分" +seconds+"秒";
        }
        else if (seconds > 0)
        {
            leftTime = seconds+"秒";
        }
        else
        {
            leftTime = "0秒";
        }

         return leftTime;
    }
    public static String formatTtimes(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }
    public static String formatTtimeName(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(new Date(time));
    }
    public static String formatTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return format.format(new Date(time));
    }
    public static String formatTimes(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return format.format(new Date(time));
    }
    public static String formatChatTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date(time));
    }
    public static String formatChatTimes(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm.ss");
        return format.format(new Date(time));
    }
    public static String formatYearTimes(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date(time));
    }

    public static String formatHourTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return format.format(new Date(time));
    }
    public static String formatHoursTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }
    public static String formatMinterTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm");
        return format.format(new Date(time));
    }
    public static String formatMsTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("ss");
        return format.format(new Date(time));
    }
    public static String formatRedTimes(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(time));
    }
    public static String formatTtimeNames(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(new Date(time));
    }

    public static String formatToMf(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM.dd ## HH:mm:ss");
        return format.format(new Date(time));
    }
    public static String formatToYear(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time));
    }
    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = sdf.parse(time);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
