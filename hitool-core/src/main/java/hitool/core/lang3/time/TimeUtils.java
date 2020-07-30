/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hitool.core.lang3.StringUtils;

/**
 * 
 * <b>时间工具</b>
 * <p>格式字符串"EEEE-MMMM-dd-yyyy"中 EEEE是星期, MMMM是月, dd是日, yyyy是年.</p>  
 * <pre>
 *  SimpleDateFormat bartDateFormat = new SimpleDateFormat("EE-MM-dd-yyyy",Locale.ENGLISH);
 *	Date date = new Date();
 *	System.out.println(bartDateFormat.format(date));
 *	//EEEE-MMMM-dd-yyyy:Monday-July-25-2016
 *	//EEEE-MM-dd-yyyy:Monday-07-25-2016
 *	//EE-MMMM-dd-yyyy:Mon-July-25-2016
 *	//EE-MM-dd-yyyy:Mon-07-25-2016
 * </pre>
 */
public abstract class TimeUtils {

	protected static ConcurrentMap<String, SimpleDateFormat> COMPLIED_FORMAT = new ConcurrentHashMap<String, SimpleDateFormat>();
	
    /** 时间格式：hh24 */
    public static final String TIME_FORMAT_24HOUR = "hh24";
    /** 时间格式：hh12 */
    public static final String TIME_FORMAT_12HOUR = "hh12";
	/** 时间格式：dd*/
 	public static final String TIME_FORMAT_DD = "dd";
 	/** 时间格式：EE */
    public static final String TIME_FORMAT_EE = "EE";
    /** 时间格式：EEEE */
    public static final String TIME_FORMAT_EEEE = "EEEE";
    /** 时间格式：MM*/
    public static final String TIME_FORMAT_MM = "MM";
 	/** 时间格式：MMMM*/
    public static final String TIME_FORMAT_MMMM = "MMMM";
    /** 时间格式：yy*/
 	public static final String TIME_FORMAT_YY = "yyyy";
 	 /** 时间格式：yyyy*/
 	public static final String TIME_FORMAT_YYYY = "yyyy";
	/** 短时间格式：HH:mm*/
 	public static final String SHORT_TIME_FORMAT = "HH:mm";
 	/** 短时间格式：HH时mm分*/
 	public static final String SHORT_TIME_FORMAT_CN = "HH时mm分";
 	/** 时间格式：HH:mm:ss */
 	public static final String TIME_FORMAT = "HH:mm:ss"; 
	/** 时间格式：HH时mm分ss秒 */
 	public static final String TIME_FORMAT_CN = "HH时mm分ss秒"; 
 	/** 时间格式,主要是针对timestamp：HH:mm:ss:SS */
	public static final String TIME_LONGFORMAT = "HH:mm:ss:SS"; 
 	/** 时间格式,主要是针对timestamp：HH时mm分ss秒SS毫秒 */
	public static final String TIME_LONGFORMAT_CN = "HH时mm分ss秒SS毫秒";
    /** 时间格式：h:m:s a */
    public static final String TIME_FORMAT_SECOND = "h:m:s a";
    /** 时间格式：a h时m分s秒 */
    public static final String TIME_FORMAT_SECOND_CN = "a h时m分s秒";
    /** 时间格式：h:m a */
    public static final String TIME_FORMAT_MINUTE = "h:m a";
    /** 时间格式：a h时m分 */
    public static final String TIME_FORMAT_MINUTE_CN = "a h时m分";
    /** 时间格式：HHmmss*/
 	public static final String TIMESTAMP_SECOND = "HHmmss";
 	/** 时间格式：HHmmssSSS*/
 	public static final String TIMESTAMP_MICROSECOND = "HHmmssSSS";
 	/** 时间格式：yyyyMMdd*/
 	public static final String TIMESTAMP_DAY = "yyyyMMdd";
 	/** 时间格式：yyyyMMddHHmmss*/
 	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
 	/** 时间格式：yyyyMMddHHmmssSSS*/
 	public static final String TIMESTAMP_LONGFORMAT = "yyyyMMddHHmmssSSS";
    
	protected static Map<String,Integer> TIME_POWERS = new HashMap<String, Integer>();
	private static final Pattern regex = Pattern.compile("^([1-9][0-9]*\\.?[0-9]*)\\s*([smhd]{1})$");
	
	static {
        
        //时间格式
		COMPLIED_FORMAT.put(TIME_FORMAT_24HOUR, new SimpleDateFormat(TIME_FORMAT_24HOUR ));
        COMPLIED_FORMAT.put(TIME_FORMAT_12HOUR, new SimpleDateFormat(TIME_FORMAT_12HOUR ));
        COMPLIED_FORMAT.put(TIME_FORMAT_DD, new SimpleDateFormat(TIME_FORMAT_DD ));
        COMPLIED_FORMAT.put(TIME_FORMAT_EE, new SimpleDateFormat(TIME_FORMAT_EE ));
        COMPLIED_FORMAT.put(TIME_FORMAT_EEEE, new SimpleDateFormat(TIME_FORMAT_EEEE ));
        COMPLIED_FORMAT.put(TIME_FORMAT_MM, new SimpleDateFormat(TIME_FORMAT_MM ));
        COMPLIED_FORMAT.put(TIME_FORMAT_MMMM, new SimpleDateFormat(TIME_FORMAT_MMMM ));
        COMPLIED_FORMAT.put(TIME_FORMAT_YY, new SimpleDateFormat(TIME_FORMAT_YY ));
        COMPLIED_FORMAT.put(TIME_FORMAT_YYYY, new SimpleDateFormat(TIME_FORMAT_YYYY ));
        COMPLIED_FORMAT.put(SHORT_TIME_FORMAT, new SimpleDateFormat(SHORT_TIME_FORMAT ));
        COMPLIED_FORMAT.put(SHORT_TIME_FORMAT_CN, new SimpleDateFormat(SHORT_TIME_FORMAT_CN ));
        COMPLIED_FORMAT.put(TIME_FORMAT, new SimpleDateFormat(TIME_FORMAT ));
        COMPLIED_FORMAT.put(TIME_FORMAT_CN, new SimpleDateFormat(TIME_FORMAT_CN ));
        COMPLIED_FORMAT.put(TIME_LONGFORMAT, new SimpleDateFormat(TIME_LONGFORMAT ));
        COMPLIED_FORMAT.put(TIME_LONGFORMAT_CN, new SimpleDateFormat(TIME_LONGFORMAT_CN ));
        COMPLIED_FORMAT.put(TIME_FORMAT_SECOND, new SimpleDateFormat(TIME_FORMAT_SECOND ));
        COMPLIED_FORMAT.put(TIME_FORMAT_SECOND_CN, new SimpleDateFormat(TIME_FORMAT_SECOND_CN ));
        COMPLIED_FORMAT.put(TIME_FORMAT_MINUTE, new SimpleDateFormat(TIME_FORMAT_MINUTE ));
        COMPLIED_FORMAT.put(TIME_FORMAT_MINUTE_CN, new SimpleDateFormat(TIME_FORMAT_MINUTE_CN ));
        COMPLIED_FORMAT.put(TIMESTAMP_SECOND, new SimpleDateFormat(TIMESTAMP_SECOND ));
        COMPLIED_FORMAT.put(TIMESTAMP_MICROSECOND, new SimpleDateFormat(TIMESTAMP_MICROSECOND ));
        COMPLIED_FORMAT.put(TIMESTAMP_DAY, new SimpleDateFormat(TIMESTAMP_DAY ));
        COMPLIED_FORMAT.put(TIMESTAMP_FORMAT, new SimpleDateFormat(TIMESTAMP_FORMAT ));
        COMPLIED_FORMAT.put(TIMESTAMP_LONGFORMAT, new SimpleDateFormat(TIMESTAMP_LONGFORMAT ));
        /*
	        (s:秒钟,m:分钟,h:小时,d:天)
	        1秒=1000毫秒(ms), 
			1毫秒=1／1000秒(s)； 
			1秒=1000000 微秒(μs), 
			1微秒=1／1000000秒(s)； 
			1秒=1000000000 纳秒(ns),
			1纳秒=1／1000000000秒(s)； 
			1秒=1000000000000皮秒 
			1皮秒==1/1000000000000秒
		*/
		TIME_POWERS.put("s", 1000);//一秒
		TIME_POWERS.put("m", 60 * 1000);//一分钟
		TIME_POWERS.put("h", 60 * 60 * 1000);//一小时
		TIME_POWERS.put("d", 24 * 60 * 60 * 1000);//一天
	}
	
	/**
	 * 根据给出的字符串格式，获取相应的时间格式化对象
	 */
	public static SimpleDateFormat getTimeFormat(String format) {
		if (StringUtils.isNotEmpty(format)) {
			SimpleDateFormat ret = COMPLIED_FORMAT.get(format);
			if (ret != null) {
				return ret;
			}
			ret = new SimpleDateFormat(format);
			SimpleDateFormat existing = COMPLIED_FORMAT.putIfAbsent(format, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	/**
	 * 得到字符时间对应的时间Ingeter值：如 5s 对应值为 50000
	 */
	public static Integer getTimeMillis(String value) {
		if (value == null){
			return -1;
		}
        Matcher matcher = regex.matcher(value.toLowerCase());
		if(matcher.matches()){
			if(matcher.group(1)!=null&&matcher.group(2)!=null){
				Integer num = Integer.valueOf(matcher.group(1));
				Integer mult = TIME_POWERS.get(matcher.group(2));
				return Integer.valueOf(num * mult);
			}
		}
		return 0;
	}
	
	private static Calendar getBeginCalendar() {
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 *  取得当前天零晨时间
	 */
	public static long getBeginTimeOfDay() {
		return getBeginCalendar().getTimeInMillis();
	}
	
	/**
	 *  <b>取得当前天偏移指定天数后的零晨时间 </b>
	 *  <p>offset > 0 ,往后延迟offset天， </p>
	 *  offset < 0 向前推进 offset天 <br/>
	 */
	public static long getBeginTimeOfDay(int offset) {
		Calendar c = getBeginCalendar();
		c.add(Calendar.DAY_OF_YEAR, offset);
		return c.getTimeInMillis();
	}
	
	public static String getTime(long time, String format) {
		return TimeUtils.getTimeFormat(format).format(new Timestamp(time));
	}

    /**
	 * 取得当前时间
	 */
	public static long getNow() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 指定格式的当前时间 
	 */
	public static String getNow(String format) {
		return getTimeFormat(format).format(new Date());
	}
	
	/** 当前年份：yyyy */
	public static String getYear() {
		return getYear(new Date());
	}
	
	/** 指定日期所在年的年份：yyyy */
	public static String getYear(Date date) {
		return getTimeFormat(TIME_FORMAT_YYYY).format(date);
	}
	
	/** 当前月份：MM */
	public static String getMonth() {
		return getMonth(new Date());
	}
	
	/** 指定日期所在月的月份：MM */
	public static String getMonth(Date date) {
		return getTimeFormat(TIME_FORMAT_MM).format(date);
	}
	
	/** 当前周几：EEEE  */
	public static String getWeek() {
		return getWeek(new Date());
	}
	
	/** 指定日期周几：EEEE  */
	public static String getWeek(Date date) {
		return getTimeFormat(TIME_FORMAT_EEEE).format(date);
	}
	
	/** 当月第n天：dd */
	public static String getDay() {
		return getDay(new Date());
	}
	
	/** 指定日期所在月的第n天：dd */
	public static String getDay(Date date) {
		return getTimeFormat(TIME_FORMAT_DD).format(date);
	}

	/** 当前小时：hh24 */
	public static String getHour24() {
		return getHour24(new Date());
	}
	
	/** 指定日期小时：hh24 */
	public static String getHour24(Date date) {
		return getTimeFormat(TIME_FORMAT_24HOUR).format(date);
	}
	
	/** 当前小时：hh12 */
	public static String getHour12() {
		return getHour12(new Date());
	}
	
	/** 指定日期当前小时：hh12 */
	public static String getHour12(Date date) {
		return getTimeFormat(TIME_FORMAT_12HOUR).format(date);
	}
    
	public static void main(String[] args) {
		System.out.println(TimeUtils.getTimeMillis("2d"));
		System.out.println(TimeUtils.getBeginTimeOfDay(2) - TimeUtils.getBeginTimeOfDay());
	}
	
}