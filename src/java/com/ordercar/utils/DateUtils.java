package com.ordercar.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @author zlzhaoe
 * @version [版本号, 2017-5-10下午3:02:08 ]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DateUtils
{
    
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private static final String DATETIME_PATTERN_NO_MSE = "yyyy-MM-dd HH:mm";
    
    private static final String DATETIME_PAT = "yyyy-MM-dd";
    
    private static final String DATETIME_MONTH_PAT = "yyyy-MM";
    
    /**
     * 日期转为字符串
     *
     * @param value
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String doConvertToString(Object value)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        String result = null;
        if (value instanceof Date)
        {
            result = simpleDateFormat.format(value);
        }
        return result;
    }
    /**
     * 日期转为字符串
     *
     * @param value
     * @return yyyy-MM-dd
     */
    public static String convertToString(Object value)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PAT);
        String result = null;
        if (value instanceof Date)
        {
            result = simpleDateFormat.format(value);
        }
        return result;
    }
    /**
     * 日期转为字符串
     *
     * @param value
     * @return yyyy-MM
     */
    public static String convertToMonthString(Object value)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_MONTH_PAT);
        String result = null;
        if (value instanceof Date)
        {
            result = simpleDateFormat.format(value);
        }
        return result;
    }
    /**
     * 比较是否滞后于系统时间
     *
     * @param value
     * @return
     */
    public static boolean afterToSystemDate(Object value)
    {
        boolean result = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN_NO_MSE);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse((String)value);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        result = date.after(new Date());
        return result;
    }
    
    public static boolean afterToSysDateInterval(String value, int minutes)
    {
        Date date = null;
        date = new Date(Long.valueOf(value));
        Date sysDate = new Date();
        long between = (sysDate.getTime() - date.getTime()) / 1000;// 除以1000是为了转换成秒
        return between > minutes;
    }
    
    /**
     * 获取当前时间的毫秒数
     * 
     * @return
     */
    public static String getCurrentTimeMillis()
    {
        return System.currentTimeMillis() + "";
    }
    
    /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(Date date)
    {// 可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return c.getTime();
    }
    
    
    /**
     * 两个时间之间相差距离多少天
     * 
     * @param one 时间参数 1：
     * @param two 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2)
        throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try
        {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2)
            {
                diff = time2 - time1;
            }
            else
            {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return days;
    }
    
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try
        {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2)
            {
                day = 0;
                hour = 0;
                min = 0;
                sec = 0;
            }
            else
            {
                diff = time1 - time2;
                day = diff / (24 * 60 * 60 * 1000);
                hour = (diff / (60 * 60 * 1000) - day * 24);
                min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
                sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            }
           
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(Date str1, Date str2)
    {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long time1 = str1.getTime();
        long time2 = str2.getTime();
        long diff;
        if (time1 < time2)
        {
            diff = time2 - time1;
        }
        else
        {
            diff = time1 - time2;
        }
        // day = diff / (24 * 60 * 60 * 1000);
        // hour = (diff / (60 * 60 * 1000) - day * 24);
        // min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return sec + "";
    }
    
    /**
	 * 计算2个时间相差的分钟数
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return min
	 * @throws ParseException 
	 */
	public static long getMins(String str1, String str2){
		long min = 0;
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date begin = dfs.parse(str1);
			Date end = dfs.parse(str2);
			long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
			min=between/60;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return min;
	}
	/**
     * 两个时间相差距离秒
     * 
	 * @throws ParseException 
     */
    public static long getDistance(String str1, String str2) throws ParseException{
        long sec = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        Date date1 = simpleDateFormat.parse(str1);
        Date date2 = simpleDateFormat.parse(str2);
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long diff;
        if (time1 < time2){
            diff = time2 - time1;
        }
        else{
            diff = time1 - time2;
        }
        sec = (diff / 1000);
        return sec;
    }
	 /** 
     * 获取过去第几天的日期 
     * 
     * @param past 
     * @return 
     */  
    public static String getPastDate(int past) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String result = format.format(today);  
        return result;  
    } 
	/**
	 * 获取2个时间内的所有时间，包含dBegin，dEnd
	 * @return
	 * @throws ParseException 
	 */
	public static List<String> findDates(String begin, String end) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date dBegin = dateFormat.parse(begin);
	    Date dEnd = dateFormat.parse(end);
		List lDate = new ArrayList();
		lDate.add(begin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(convertToString(calBegin.getTime()));
		}
		return lDate;
	}
	/* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
    	String res = "";
    	try{
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           long lt = new Long(s);
           Date date = new Date(lt);
           res = simpleDateFormat.format(date);   
       }catch (Exception e) {
		// TODO: handle exception
    	   res = "";
       }
        return res;
    }
    /**
     * 获取上周周一（第一天是周一）
     *
     * @return
     */
    public static String getPreviousMonday() {
        Calendar cal = Calendar.getInstance();
        // 将每周第一天设为星期一，默认是星期天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, -1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return convertToString(cal.getTime());
    }
    /**
     * 获取上周周日（第一天是周一）
     * @return
     */
    public static String getSunday() {
        Calendar cal = Calendar.getInstance();
        //将每周第一天设为星期一，默认是星期天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, -1*7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return convertToString(cal.getTime());
    }
    /**
     * 上个月第一天
     * @return
     */
    public static String getLastMonthOneDay() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MONTH, -1);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
        return convertToString(calendar.getTime());
    }
    /**
     * 上个月最后天
     * @return
     */
    public static String getLastMonthLastDay() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.DAY_OF_MONTH, 1); 
    	calendar.add(Calendar.DATE, -1);
    	return convertToString(calendar.getTime());
    }
}
