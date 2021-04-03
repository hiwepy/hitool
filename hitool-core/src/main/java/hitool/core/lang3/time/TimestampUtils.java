/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 */
public abstract class TimestampUtils extends TimeUtils {

	/**
	 * 取得当前系统时间戳
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
    
    /**
     * 获取指定时间的时间戳 等同于DateUtils.getFomratCurrentDate(date,"yyyyMMddHHmmssSSS")
     */
    public static String getTimestamp(Date date) {
    	StringBuilder buffer = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        buffer.append(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) < 9) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DATE) < 10) {
            buffer.append(0);
        } 
        buffer.append(calendar.get(Calendar.DATE));
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.HOUR_OF_DAY));
        if (calendar.get(Calendar.MINUTE) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.SECOND) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.SECOND));
        buffer.append(calendar.get(Calendar.MILLISECOND));
        return buffer.toString();
    }
    
    /**
     * 取得当前时间（数据库时间搓）
     */
	public static Timestamp getTimestamp(long time) {
		return new Timestamp(time);
	}
	
	/**
	 * 取得指定时间的时间搓
	 */
	public static Timestamp getTimestamp(String time) throws ParseException {
		return getTimestamp(DateUtils.getDateFormat(TIMESTAMP_LONGFORMAT).parse(time).getTime());
	}
	
	/**
	 * 取得当前时间（数据库时间搓）
	 */
	public static Timestamp getTimestamp(String time, String format) throws ParseException {
		return getTimestamp(TimeUtils.getTimeFormat(format).parse(time).getTime());
	}
	
	/**
	 * https://blog.csdn.net/bsr1983/article/details/84411990
	 * @param amount
	 * @return
	 */
	public static Timestamp fromUnixTime(long amount) {
		return new Timestamp(amount * 1000L);
	}
	
	public static int getAgeFromUnixTime(long amount) {
		return getAgeFromUnixTime(Locale.getDefault(), amount);
	}
	
	public static int getAgeFromUnixTime(Locale locale, long amount) {
		return getAgeFromUnixTime(TimeZone.getDefault(), locale, amount);
	}
	
	public static int getAgeFromUnixTime(TimeZone zone, Locale locale, long amount) {
		
		Calendar birth = Calendar.getInstance();
		birth.setTimeInMillis(fromUnixTime(amount).getTime());
		
		// 当前时间
		Calendar now = Calendar.getInstance();
		
		/* 如果生日大于当前日期，则抛出异常：出生日期不能大于当前日期 */
		if (birth.after(now)) {
			throw new IllegalArgumentException("The birthday is after Now,It's unbelievable");
		}
		/* 取出当前年月日 */
		int yearNow = now.get(Calendar.YEAR);
		int monthNow = now.get(Calendar.MONTH) + 1;
		int dayNow = now.get(Calendar.DAY_OF_MONTH);
		/* 取出出生年月日 */
		int yearBirth = birth.get(Calendar.YEAR);
		int monthBirth = birth.get(Calendar.MONTH) + 1;
		int dayBirth = birth.get(Calendar.DAY_OF_MONTH);
		/* 大概年龄是当前年减去出生年 */
		int age = yearNow - yearBirth;
		/* 如果出当前月小与出生月，或者当前月等于出生月但是当前日小于出生日，那么年龄age就减一岁 */
		if (monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)) {
			age--;
		}
		return age;
	}
	
	public static long getUnixTimeFromDate(Date date) {
		return getUnixTimeFromDate(TimeZone.getDefault(), date);
	}
	
	public static long getUnixTimeFromDate(TimeZone zone, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(zone);
		return calendar.getTimeInMillis();
	}

	public static void main(String[] args) {

		System.out.println(TimestampUtils.fromUnixTime(400326583)); // 1982-09-08 17:49:43
		System.out.println(TimestampUtils.fromUnixTime(-28800));
		
		System.out.println(TimestampUtils.getAgeFromUnixTime(400326583)); // 2002-09-07 00:00:00.000000
		System.out.println(TimestampUtils.getAgeFromUnixTime(-28800));
		
		System.out.println(TimestampUtils.getUnixTimeFromDate(new Date()));
		
	}
}
