/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hitool.core.format.Format;

/*
 *  时间格式化成字符串
 */
public class LocalDateFormat implements Format{

	private String pattern = null; 
	private Locale loc = null;
	private int style = -1;
	
	private static Calendar cal = null;
	private static SimpleDateFormat sdf = null;
	public static final int LONG = DateFormat.LONG;// 长日期型
	public static final int MEDIUM = DateFormat.MEDIUM;// 中日期型
	public static final int SHORT = DateFormat.SHORT;// 短日期型
	
	// 缺省短日期格式
	public final static String DEFAULT_YEAR_FORMAT = "yyyy";
	public final static String DEFAULT_MONTH_FORMAT = "yyyy-MM";
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String CHINA_YEAR_FORMAT = "yyyy年";
	public final static String CHINA_MONTH_FORMAT = "yyyy年MM月";
	public final static String CHINA_DATE_FORMAT = "yyyy年MM月dd日";
	// 时间格式
	public final static String TIME_FORMAT = "HH:mm:ss";
	// 时间格式,主要是针对timestamp
	public final static String TIME_LONGFORMAT = "HH:mm:ss:SS";
	// 缺省长日期格式
	public final static String DEFAULT_LONG_DATE_FORMAT = DEFAULT_DATE_FORMAT + " " + TIME_FORMAT;
	// 缺省长日期格式,主要是针对timestamp
	public final static String DEFAULT_LONG_DATE_LONGFORMAT = DEFAULT_DATE_FORMAT + " " + TIME_LONGFORMAT;
	
	public String format(Date fmtObj) {
		if (pattern!=null) {
			sdf = new SimpleDateFormat(pattern);
			if (loc != null) {
				sdf = new SimpleDateFormat(pattern, loc);
			}
		}
		if (style != -1) {
			DateFormat df = DateFormat.getDateInstance(style, loc);
			return df.format(fmtObj);
		}
		return sdf.format(fmtObj);
	}
	
	public String format(Date date, String pattern) {
		return format(date, pattern, null);
	}
	
	/*
	 * 按照给定的格式模板将指定的日期值转换成字符串
	 * @param date: 待转换的日期
	 * @param pattern: 指定转化格式字符串,例如：yyyy-MM-dd
	 * @param loc: 字符定义对象
	 * @return 格式化后的日期字符串
	 */
	public String format(Date date,String pattern, Locale loc) {
		this.pattern = pattern;
		this.loc = (loc==null)?Locale.getDefault():loc;
		return format(date);
	}

	/*
	 * 按照给定的格式style将指定的日期值转换成字符串
	 * @param date: 待转换的日期
	 * @param style: 指定转化类型,style参数取静态常量LONG、MEDIUM和SHORT的值
	 * @param loc ：字符定义对象
	 * @return 格式化后的日期字符串
	 */
	public String format(Date date, int style, Locale loc) {
		if (style >= 1 && style <= 3) {
			this.style = style;
			this.loc = (loc==null)?Locale.getDefault():loc;
			return format(date);
		}else{
			return null;
		}
	}
	/*
	 * 将一个long型的数值转换为指定格式的日期字符串
	 * 
	 * @param time
	 *            要转换的long值
	 * @return 转换后的字符串
	 */
	public static String formatTime(long time, String formatType) {
		cal = Calendar.getInstance();
		sdf = new SimpleDateFormat(formatType);
		cal.setTime(new Date(time));
		return sdf.format(cal.getTime());
	}
	
	public static void main(String[] args) {
		LocalDateFormat c = new LocalDateFormat();
		System.out.println("转换后的字符串:" + c.formatTime(1274812130218L, "yyyy-MM-dd HH:mm:ss"));
		/*
		 * CalendarUtil tt = new CalendarUtil();
		 * 
		 * System.out.println("获取当天日期:" + tt.getNowTime("yyyy/MM-dd"));
		 * System.out.println("获取本周一日期:" + tt.getMondayOFWeek());
		 * System.out.println("获取本周日的日期~:" + tt.getCurrentWeekday());
		 * System.out.println("获取上周一日期:" + tt.getPreviousWeekday());
		 * System.out.println("获取上周日日期:" + tt.getPreviousWeekSunday());
		 * System.out.println("获取下周一日期:" + tt.getNextMonday());
		 * System.out.println("获取下周日日期:" + tt.getNextSunday());
		 * System.out.println("获得相应周的周六的日期:" + tt.getNowTime("yyyy-MM-dd"));
		 * System.out.println("获取本月第一天日期:" + tt.getFirstDayOfMonth());
		 * System.out.println("获取本月最后一天日期:" + tt.getDefaultDay());
		 * System.out.println("获取上月第一天日期:" + tt.getPreviousMonthFirst());
		 * System.out.println("获取上月最后一天的日期:" + tt.getPreviousMonthEnd());
		 * System.out.println("获取下月第一天日期:" + tt.getNextMonthFirst());
		 * System.out.println("获取下月最后一天日期:" + tt.getNextMonthEnd());
		 * System.out.println("获取本年的第一天日期:" + tt.getCurrentYearFirst());
		 * System.out.println("获取本年最后一天日期:" + tt.getCurrentYearEnd());
		 * System.out.println("获取去年的第一天日期:" + tt.getPreviousYearFirst());
		 * System.out.println("获取去年的最后一天日期:" + tt.getPreviousYearEnd());
		 * System.out.println("获取明年第一天日期:" + tt.getNextYearFirst());
		 * System.out.println("获取明年最后一天日期:" + tt.getNextYearEnd());
		 * System.out.println("获取本季度第一天:" + tt.getThisSeasonFirstTime(11));
		 * System.out.println("获取本季度最后一天:" + tt.getThisSeasonFinallyTime(11));
		 * System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:" +
		 * CalendarUtil.getTwoDay("2008-12-1", "2008-9-29"));
		 * System.out.println("获取当前月的第几周：" + tt.getWeekOfMonth());
		 * System.out.println("获取当前年份：" + tt.getYear());
		 * System.out.println("获取当前月份：" + tt.getMonth());
		 * System.out.println("获取今天在本年的第几天：" + tt.getDayOfYear());
		 * System.out.println("获得今天在本月的第几天(获得当前日)：" + tt.getDayOfMonth());
		 * System.out.println("获得今天在本周的第几天：" + tt.getDayOfWeek());
		 * System.out.println("获得半年后的日期：" +
		 * tt.convertDateToString(tt.getTimeYearNext()));
		 * System.out.println("获得本年有多少天 ：" + tt.getMaxYear());
		 */
	}

	@Override
	public <T> String format(String message, T bean) {
		
		return message;
	}

	@Override
	public String format(String message, String... replaceParas) {
		
		
		return message;
	}

	@Override
	public String format(String message, String regex, String[] replaceParas) {
		
		
		return message;
	}
}
