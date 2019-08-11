package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeUtil {

    private static String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

    public static String getDateByUnix(long unixTime){
        Date date = new Date(unixTime);
        DateFormat dateFormat = new SimpleDateFormat(defaultDateFormat);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static long getUnixDateByStr(String dateStr) throws ParseException {
        DateFormat dfDateFormat = new SimpleDateFormat(defaultDateFormat);
        Date date = dfDateFormat.parse(dateStr);
        long unixTime = date.getTime();
        return unixTime;
    }

    //生成随机时间,过去的时间，用于出生日期使用
    public static String getRandomDate(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //c.add(Calendar.YEAR,new Random().nextInt(50)-50);
        c.add(Calendar.DAY_OF_YEAR,new Random().nextInt(20000)-20000);
        return sf.format(c.getTime());
    }

    //生成当天时间
    public static String getCurrentDate(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return sf.format(c.getTime());
    }

    //生成下个月的今天
    public static String getTodayNextMonth(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,1);
        return sf.format(c.getTime());
    }


    public static void main(String[] args) throws ParseException {
        //getRandomTime();
        //System.out.println(getRandomTime());
        System.out.println("getDateByUnix----"+getDateByUnix(631123200000l));
        System.out.println("getUnixDateByStr---"+getUnixDateByStr("1990-01-01 00:00:00"));
        System.out.println("getCurrentDate----" +getCurrentDate());
    }
}
