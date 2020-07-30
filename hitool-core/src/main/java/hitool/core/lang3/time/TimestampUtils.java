/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
	
}
