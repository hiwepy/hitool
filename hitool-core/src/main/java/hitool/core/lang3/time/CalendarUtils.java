/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hitool.core.lang3.Assert;

/**
 * 日历操作辅助类：提供常规日历相关操作方法
 */
@SuppressWarnings("static-access")
public abstract class CalendarUtils {

	private static int[][] seasons = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };

	private static int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
	private static int MaxDate; // 一月最大天数
	private static int MaxYear; // 一年最大天数
	public static final int LONG = DateFormat.LONG;// 长日期型
	public static final int MEDIUM = DateFormat.MEDIUM;// 中日期型
	public static final int SHORT = DateFormat.SHORT;// 短日期型

	public enum Datepart {
		YY, MM, DD
	}

	/**
	 * 比较两个日期(不包括时间)的大小 方法的实现逻辑描述（如果是接口方法可以不写）：
	 * 
	 * @param date1
	 * @param date2
	 * @return 0代表d1等于d2; -1代表d1小于d2; 1代表d1大于d2
	 */
	public static int compareDateIgnoreTime(Date date1, Date date2) {
		Assert.notNull(date1, " date1 must not be null ");
		Assert.notNull(date2, " date2 must not be null ");
		int discrepancy = 0;
		int result = compareYearAndMonth(date1, date2);
		if (result != 0) {
			return result;
		}
		Calendar cal1 = getCalendar();
		Calendar cal2 = getCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		discrepancy = cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		return 0;
	}

	// date1加上compday天数以后的日期与当前时间比较，如果大于当前时间返回true，否则false
	public static Boolean compareDay(Date date1, int compday) {
		if (date1 == null) {
			return false;
		}
		Date dateComp = getDateBeforeOrAfter(date1, compday);
		Date nowdate = new Date();
		if (dateComp.after(nowdate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 比较时间(不包括日期) 方法的实现逻辑描述（如果是接口方法可以不写）：
	 * 
	 * @param date1
	 * @param date2
	 * @return 0代表d1等于d2; -1代表d1小于d2; 1代表d1大于d2
	 */
	public static int compareTime(Date date1, Date date2) {
		Assert.notNull(date1, " date1 must not be null ");
		Assert.notNull(date2, " date2 must not be null ");
		int discrepancy = 0;
		Calendar cal1 = getCalendar();
		Calendar cal2 = getCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		discrepancy = cal1.get(Calendar.HOUR_OF_DAY) - cal2.get(Calendar.HOUR_OF_DAY);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		discrepancy = cal1.get(Calendar.MINUTE) - cal2.get(Calendar.MINUTE);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		discrepancy = cal1.get(Calendar.SECOND) - cal2.get(Calendar.SECOND);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		return 0;
	}

	/**
	 * 比较日期的年月 方法的实现逻辑描述（如果是接口方法可以不写）：
	 * 
	 * @param date1
	 * @param date2
	 * @return 0代表d1等于d2; -1代表d1小于d2; 1代表d1大于d2
	 */
	public static int compareYearAndMonth(Date date1, Date date2) {
		Assert.notNull(date1, " date1 must not be null ");
		Assert.notNull(date2, " date2 must not be null ");
		Calendar cal1 = getCalendar();
		Calendar cal2 = getCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int discrepancy = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		discrepancy = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
		if (discrepancy != 0) {
			return discrepancy < 0 ? -1 : 1;
		}
		return 0;
	}

	/**
	 * 计算两个日期之间的相隔的月。
	 * 
	 * @param startDate
	 *            : 开始日期
	 * @param endDate
	 *            : 结束日期
	 * @return 如果enddate>startdate，返回一个大于0的整数，否则返回0
	 */
	public static int getMonthDiff(Date startDate, Date endDate) {
		int k = 0;
		GregorianCalendar temp = new GregorianCalendar();
		temp.setTime(startDate);
		temp.set(GregorianCalendar.MILLISECOND, 0);
		temp.add(GregorianCalendar.DAY_OF_MONTH, 1);
		int day = temp.getActualMaximum(GregorianCalendar.DATE);
		GregorianCalendar endCal = new GregorianCalendar();
		endCal.setTime(endDate);
		endCal.set(GregorianCalendar.MILLISECOND, 0);
		endCal.add(GregorianCalendar.DAY_OF_MONTH, 1);
		while (temp.getTime().before(endCal.getTime())) {
			k++;
			day = temp.getActualMaximum(GregorianCalendar.DATE);
			temp.add(GregorianCalendar.DAY_OF_MONTH, day);
		}
		return k;
	}

	/**
	 * 获得当前日期
	 * 
	 * @return Date实例
	 */
	public static Date getDate() {
		Calendar cal = getCalendar();
		return cal.getTime();
	}

	/**
	 * 获得当前系统的时间戳
	 * 
	 * @return 从1970-1-1到现在的毫秒数
	 */
	public static long getTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 将时间转成yyyy-MM-dd字符形式的Long类型
	 */
	public static long getTimeInMillis(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	/**
	 * 在给定的日期点上加入指定的月
	 * 
	 * @param date
	 *            给定的日期点
	 * @param days
	 *            天数，正数为向后；负数为向前
	 * @return 返回改变后的时间点
	 */
	public static Date getNewMonthDate(Date date, int months) {
		if (date == null) {
			return date;
		}
		Calendar cal = getCalendar();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(year, month + months, day);
		return cal.getTime();
	}

	/**
	 * 把日期对象加减年、月、日后得到新的日期对象
	 * 
	 * @param date
	 *            需要加减年、月、日的日期对象
	 * @param depart
	 *            年 、月、日
	 * @param number加减因子
	 * @return 新的日期对象
	 */
	public static Date getNewDate(Date date, Datepart datepart, int number) {
		Calendar cal = getCalendar();
		cal.setTime(date);
		if (Datepart.YY.equals(datepart)) {
			cal.add(Calendar.YEAR, number);
		} else if (Datepart.MM.equals(datepart)) {
			cal.add(Calendar.MONTH, number);
		} else if (Datepart.DD.equals(datepart)) {
			cal.add(Calendar.DATE, number);
		}
		return cal.getTime();
	}

	/**
	 * 在给定的日期点上加入指定的天数
	 * 
	 * @param date
	 *            给定的日期点
	 * @param days
	 *            天数，正数为向后；负数为向前
	 * @return 返回改变后的时间点
	 */
	public static Date getNewDate(Date date, int days) {
		if (date == null) {
			return date;
		}
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(year, month, day + days);
		return cal.getTime();
	}

	/**
	 * 在当前的日期点上加入指定的天数
	 * 
	 * @param days
	 *            天数，正数为向后；负数为向前
	 * @return 返回改变后的时间
	 */
	public static Date getNewDate(int days) {
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(year, month, day + days);
		return cal.getTime();
	}

	/**
	 * 在给定的日期点上加入指定的天数
	 * 
	 * @param date
	 *            给定的日期点
	 * @param days
	 *            天数，正数为向后；负数为向前
	 * @return 返回改变后的时间点
	 */
	public static String getNewDate(String dateStr, String format, int days) {
		if (dateStr == null) {
			return "";
		}
		Date date = DateUtils.parseDate(dateStr, format);
		date = getNewDate(date, days);
		return DateUtils.formatDate(date, format);
	}

	// add by messi(chencao)
	/**
	 * 把参数日期的时,分,秒清零,返回下一天 如:参数为2006-08-22 12:33 那么该方法返回 2006-08-23 00:00 ×
	 */
	public static Date getNextDay(Date day) {
		Calendar nowC = getClearCalendar(day);
		nowC.add(Calendar.DATE, 1);
		return nowC.getTime();
	}

	/**
	 * 获得下周星期一的日期
	 */
	public static String getNextMonday() {
		weeks++;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得下周星期日的日期
	 */
	public static String getNextSunday() {

		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得下个月第一天的日期
	 * 
	 * @return
	 */
	public static String getNextMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = getCalendar();
		lastDate.add(Calendar.MONTH, 1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获得下个月最后一天的日期
	 * 
	 * @return
	 */
	public static String getNextMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = getCalendar();
		lastDate.add(Calendar.MONTH, 1);// 加一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获得明年最后一天的日期
	 * 
	 * @return
	 */
	public static String getNextYearEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = getCalendar();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获得明年第一天的日期
	 * 
	 * @return
	 */
	public static Date getNextYearFirst() {
		Calendar lastDate = getCalendar();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		return lastDate.getTime();
	}

	/**
	 * 把参数日期的时,分,秒清零,返回前一天 如:参数为2006-08-22 12:33 那么该方法返回 2006-08-21 00:00
	 */
	public static Date getPreDay(Date day) {
		Calendar nowC = getClearCalendar(day);
		nowC.add(Calendar.DATE, -1);
		return nowC.getTime();
	}

	/**
	 * 获得参数指定月份的前一个月，参数格式："200811",或"200801"，返回格式："200810",或"200712"
	 */
	public static String getPreMonth(String month) {
		if (month == null || month.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar cal = getCalendar();
		try {
			cal.setTime(sdf.parse(month));
			cal.add(cal.MONTH, -1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 获得上月最后一天的日期
	 */
	public static Date getPreMonthEnd() {
		Calendar lastDate = getCalendar();
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		return lastDate.getTime();
	}

	/**
	 * 获得上月第一天的日期
	 */
	public static Date getPreMonthBegin() {
		Calendar lastDate = getCalendar();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		return lastDate.getTime();
	}

	/**
	 * 获得上周星期日的日期
	 * 
	 * @return
	 */
	public static String getPreSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得上周星期一的日期
	 * 
	 * @return
	 */
	public static String getPreWeekday() {
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上年最后一天的日期
	public static String getPreYearEnd() {
		weeks--;
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	// 获得上年第一天的日期 *
	public static Date getPreYearBegin() {

		Calendar nowC = getCalendar();

		nowC.set(Calendar.DAY_OF_YEAR, 1);
		nowC.set(Calendar.DAY_OF_MONTH, 1);
		nowC.set(Calendar.MONTH, 0);
		nowC.set(Calendar.HOUR, 0);
		nowC.set(Calendar.HOUR_OF_DAY, 0);
		nowC.set(Calendar.MINUTE, 0);
		nowC.set(Calendar.SECOND, 0);
		nowC.set(Calendar.MILLISECOND, 0);

		return nowC.getTime();
	}

	public static Date getLastSecPreDay(Date day) {
		Calendar nowC = getClearCalendar(day);
		nowC.set(Calendar.SECOND, -1);
		return nowC.getTime();
	}

	/**
	 * 将时间转成yyyy-MM-dd的Date类型
	 */
	public static Date getDateBegin(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 增加,减少天数
	 */
	public static Date updateDay(Date date, int changday) {
		Calendar c = getCalendar();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_MONTH);
		day += changday;
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}

	/**
	 * 取得时间对应的星期,从星期天开始
	 */
	public static int getWeekFromSunDay(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取得时间对应的星期,从星期一开始
	 */
	public static int getWeekFromMonday(Date date) {
		int week = 0;
		int temp = 0;
		temp = getWeekFromSunDay(date);
		if (temp == 1) {
			week = 7;
		} else {
			week = temp - 1;
		}
		return week;
	}

	/**
	 * 获取本周的第一天的日期,从星期一开始，星期日结束
	 */
	public static Date getBeginDateOfWeek() {
		Calendar cal = getCalendar();
		int amount = cal.get(Calendar.DAY_OF_WEEK);
		if (amount == 1) {
			cal.add(Calendar.DAY_OF_MONTH, -6);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, 2 - amount);
		}
		return cal.getTime();
	}

	/**
	 * 获取本周的最后天的日期,从星期一开始，星期日结束
	 */
	public static Date getEndDateOfWeek() {
		Calendar cal = getCalendar();
		int amount = cal.get(Calendar.DAY_OF_WEEK);
		if (amount != 1) {
			cal.add(Calendar.DAY_OF_MONTH, 8 - amount);
		}
		return cal.getTime();
	}

	/**
	 * 获取本月的第一天
	 */
	public static Date getBeginDayOfMonth() {
		return getBeginDayOfMonth(new Date());
	}

	public static Date getBeginDayOfMonth(Date date) {
		Calendar cal = getCalendar();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 取得Calendar实例
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	public static Calendar getCalendar(Locale locale) {
		return GregorianCalendar.getInstance(locale);
	}

	public static Calendar getClearCalendar(Date date) {
		Calendar clearC = getCalendar(Locale.getDefault());
		clearC.setTime(date);
		clearC.clear(Calendar.HOUR);
		clearC.clear(Calendar.HOUR_OF_DAY);
		clearC.clear(Calendar.MINUTE);
		clearC.clear(Calendar.SECOND);
		clearC.clear(Calendar.MILLISECOND);
		return clearC;
	}

	public static Calendar getNowCalendar(Date date) {
		Calendar nowC = getCalendar();
		nowC.setTime(date);
		nowC.set(Calendar.HOUR, 0);
		nowC.set(Calendar.HOUR_OF_DAY, 0);
		nowC.set(Calendar.MINUTE, 0);
		nowC.set(Calendar.SECOND, 0);
		nowC.set(Calendar.MILLISECOND, 0);
		return nowC;
	}

	/**
	 * 将给定日期的时分秒和毫秒清零
	 * 
	 * @param date
	 *            给定的日期
	 * @return
	 */
	public static Date getClearDate(Date date) {
		if (date == null) {
			return date;
		}
		return getClearCalendar(date).getTime();
	}

	/**
	 * 获取本月的最后一天
	 */
	public static Date getEndDayOfMonth() {
		Calendar cal = getCalendar();
		cal.add(cal.MONTH, 1);
		cal.set(cal.DATE, 1);
		cal.add(cal.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 获取指定月份的最后一天
	 */
	public static Date getEndDayOfMonth(String specMonth) {
		if (specMonth == null || specMonth.trim().length() == 0) {
			return null;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar cal = getCalendar();
		try {
			date = sdf.parse(specMonth);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(cal.MONTH, 1);
		cal.set(cal.DATE, 1);
		cal.add(cal.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 获取本周的第一天的日期,从星期天开始，星期一结束 方法的实现逻辑描述:
	 * 
	 * @return
	 * @author yjx 新增日期：2008-8-19
	 * @author yjx 修改日期：2008-8-19
	 */
	public static String getStartDateOfWeek() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		int z; // 日期属性：日
		String strY = null;
		String strZ = null;
		Calendar localTime = getCalendar();
		// Modified by 陈艺武 2009-01-16
		int amount = localTime.get(Calendar.DAY_OF_WEEK);
		localTime.add(Calendar.DAY_OF_MONTH, 1 - amount);
		/*
		 * if (amount == 1) { localTime.add(Calendar.DAY_OF_MONTH, -6); } else {
		 * localTime.add(Calendar.DAY_OF_MONTH, 2 - amount); }
		 */

		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);

		z = localTime.get(Calendar.DATE);
		strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
		return x + "-" + strY + "-" + strZ;
	}

	/**
	 * 获取本周的最后一天的日期,从星期天开始，星期一结束 方法的实现逻辑描述:
	 * 
	 * @return
	 * @author yjx 新增日期：2008-8-19
	 * @author yjx 修改日期：2008-8-19
	 */
	public static String getEndDateOfWeekend() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		int z; // 日期属性：日
		String strY = null;
		String strZ = null;
		Calendar localTime = getCalendar();

		int amount = localTime.get(Calendar.DAY_OF_WEEK);
		localTime.add(Calendar.DAY_OF_MONTH, 7 - amount);
		/*
		 * if (amount != 1) { localTime.add(Calendar.DAY_OF_MONTH, 8 - amount); }
		 */

		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		z = localTime.get(Calendar.DATE);

		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
		return x + "-" + strY + "-" + strZ;
	}

	/**
	 * 得到当前月份月初 格式为：xxxx-yy-zz (eg: 2008-08-19) 方法的实现逻辑描述:
	 * 
	 * @return
	 * @author yjx 新增日期：2008-8-19
	 * @author yjx 修改日期：2008-8-19
	 */
	public static String getMonthFromTheFirstDay() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		Calendar localTime = getCalendar(); // 当前日期
		String strY = null;
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-01";
	}

	/**
	 * 得到当前月份月底 格式为：xxxx-yy-zz (eg: 2008-08-31) 方法的实现逻辑描述:
	 * 
	 * @return
	 * @author yjx 新增日期：2008-8-19
	 * @author yjx 修改日期：2008-8-19
	 */
	public static String getMonthFromTheEndDay() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		Calendar localTime = getCalendar(); // 当前日期
		String strY = null;
		String strZ = null;
		boolean leap = false;
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-" + strZ;
	}

	public static Date getDateBefore(int beforeDay) {
		Calendar today = getCalendar();
		today.add(today.DATE, beforeDay);
		today.set(today.HOUR_OF_DAY, 0);
		today.set(today.MINUTE, 0);
		today.set(today.SECOND, 0);
		today.set(today.MILLISECOND, 0);
		return today.getTime();
	}

	/**
	 * 
	 * 
	 * 功能描述：计算两个日期之间的月差数。
	 * 
	 * @param startdate
	 * @param enddate
	 * @return 如果enddate>startdate，返回一个大于0的整数，
	 *         如果enddate小于startdate，返回一个小于0的整数，enddate=startdate则返回0
	 * 
	 */
	public static int getMonthDiff2(Date startdate, Date enddate) {
		int k = 0;
		GregorianCalendar temp = new GregorianCalendar();

		temp.setTime(startdate);
		temp.set(GregorianCalendar.MILLISECOND, 0);
		temp.add(GregorianCalendar.DAY_OF_MONTH, 1);
		int day = temp.getActualMaximum(GregorianCalendar.DATE);

		GregorianCalendar endCal = new GregorianCalendar();
		endCal.setTime(enddate);
		endCal.set(GregorianCalendar.MILLISECOND, 0);
		endCal.add(GregorianCalendar.DAY_OF_MONTH, 1);

		while (temp.getTime().before(endCal.getTime())) {
			k++;
			day = temp.getActualMaximum(GregorianCalendar.DATE);
			temp.add(GregorianCalendar.DAY_OF_MONTH, day);
		}
		if (k == 0) {
			while (temp.getTime().after(endCal.getTime())) {
				day = endCal.getActualMaximum(GregorianCalendar.DATE);
				endCal.add(GregorianCalendar.DAY_OF_MONTH, day);
				if (temp.getTime().after(endCal.getTime())) {
					k--;
				}
			}
		}
		return k;
	}

	/**
	 * 得到当前小时的第多少分钟
	 */
	public static int getMinutes() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.MINUTE);
	}

	/**
	 * 取得日期格式中的分钟。
	 * 
	 * @param date
	 *            源日期对象
	 * @return 源日期对象中的分钟信息
	 */
	public static int getMinutes(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 得到当前分钟的第多少秒钟
	 */
	public static int getSeconds() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期与本周日相差的天数
	 * 
	 * @return
	 */
	private static int getMondayPlus() {
		Calendar cd = getCalendar();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 获得本周一的日期
	 * 
	 * @return
	 */
	public static String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	public static int getMonthPlus() {
		Calendar cd = getCalendar();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	/**
	 * 获得本年第一天的日期
	 */
	public static String getYearFirst() {
		int yearPlus = getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	/**
	 * 获得本年有多少天
	 */
	public static int getYearDays() {
		Calendar cd = getCalendar();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	/**
	 * 获得本年最后一天的日期
	 */
	public static String getYearEnd() {
		return TimeUtils.getYear() + "-12-31";
	}

	private static int getYearPlus() {
		Calendar cd = getCalendar();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	public static int getSeason() {
		return getSeason(getMonth());
	}

	public static int getSeason(int month) {
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		return season;
	}

	/**
	 * 获得本季度第一天
	 */
	public static String getBeginDateOfSeason() {
		return TimeUtils.getYear() + "-" + seasons[getSeason(getMonth()) - 1][0] + "-01";
	}

	public static String getBeginDateOfSeason(int month) {
		return TimeUtils.getYear() + "-" + seasons[getSeason(month) - 1][0] + "-01";
	}

	/**
	 * 获得本季度最后一天
	 */
	public static String getEndDayOfSeason(int month) {
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int end_month = array[season - 1][2];

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);

		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value + "-" + end_month + "-" + end_days;
		return seasonDate;

	}

	/**
	 * 得到本日的上月时间 如果当日为2007-9-1,那么获得2007-8-1
	 * 
	 * 
	 */
	public static Date getDateBeforeMonth() {
		Calendar cal = getCalendar();
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	public static Date getDateBeforeDay() {
		Calendar cal = getCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.getTime();
	}

	/**
	 * 得到系统当前日期的前或者后几天
	 * 
	 * @param iDate
	 *            如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
	 * @see java.util.Calendar#add(int, int)
	 * @return Date 返回系统当前日期的前或者后几天
	 */
	public static Date getDateBeforeOrAfter(int iDate) {
		Calendar cal = getCalendar();
		cal.add(Calendar.DAY_OF_MONTH, iDate);
		return cal.getTime();
	}

	/**
	 * 得到日期的前或者后几天
	 * 
	 * @param iDate
	 *            如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
	 * @see java.util.Calendar#add(int, int)
	 * @return Date 返回参数<code>curDate</code>定义日期的前或者后几天
	 */
	public static Date getDateBeforeOrAfter(Date curDate, int iDate) {
		Calendar cal = getCalendar();
		cal.setTime(curDate);
		cal.add(Calendar.DAY_OF_MONTH, iDate);
		return cal.getTime();
	}

	/**
	 * 得到格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see java.util.Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static Date getFirstDayOfMonth() {
		Calendar cal = getCalendar();
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}

	/**
	 * 得到格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see java.util.Calendar#getMinimum(int)
	 * @return String 返回格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static Date getFirstDayOfNextMonth() {
		Calendar cal = getCalendar();
		cal.add(Calendar.MONTH, +1);
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}

	/**
	 * 得到格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see java.util.Calendar#getMinimum(int)
	 * @return String 返回格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
	 */
	public static Date getFirstDayOfMonth(Date currDate) {
		Calendar cal = getCalendar();
		cal.setTime(currDate);
		int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		return cal.getTime();
	}

	/**
	 * 得到格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see java.util.Calendar#getMinimum(int)
	 * @see #getFormatDate(Date, String)
	 * @return String 返回格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
	 */
	public static Date getLastDayOfMonth(Date currDate) {
		Calendar cal = getCalendar();
		cal.setTime(currDate);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return cal.getTime();
	}

	/**
	 * 得到当月最后一天日期对象
	 * 
	 * @see java.util.Calendar#getMinimum(int)
	 */
	public static Date getLastDayOfMonth() {

		Calendar lastDate = getCalendar();

		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		/*
		 * int lastDay = lastDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		 * lastDate.set(Calendar.DAY_OF_MONTH, lastDay);
		 */
		return lastDate.getTime();
	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 得到日期的前或者后几小时
	 * 
	 * @param iHour
	 *            如果要获得前几小时日期，该参数为负数； 如果要获得后几小时日期，该参数为正数
	 * @see java.util.Calendar#add(int, int)
	 * @return Date 返回参数<code>curDate</code>定义日期的前或者后几小时
	 */
	public static Date getDateBeforeOrAfterHours(Date curDate, int iHour) {
		Calendar cal = getCalendar();
		cal.setTime(curDate);
		cal.add(Calendar.HOUR_OF_DAY, iHour);
		return cal.getTime();
	}

	/**
	 * 方法用途描述: 验证是否是前三天或当天的其中一天 实现逻辑描述: 如下
	 * 
	 * @param date
	 * @return
	 * @author 陈艺武 新增日期：2008-10-31
	 * @author 你的姓名 修改日期：2008-10-31
	 */
	public static boolean isAvaildPeriod(Date date) {
		Calendar cal = getCalendar();
		int curDateOfYear = cal.get(cal.DAY_OF_YEAR);

		cal.setTime(date);
		int selectDateOfYear = cal.get(cal.DAY_OF_YEAR);
		int betValue = curDateOfYear - selectDateOfYear;

		if (betValue >= 0 && betValue <= 3)
			return true;
		else
			return false;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear2(int year) {
		return new GregorianCalendar().isLeapYear(year);
	}

	/**
	 * 判断输入年份是否为闰年 方法的实现逻辑描述:
	 * 
	 * @param year
	 * @return
	 * @author yjx 新增日期：2008-8-19
	 * @author yjx 修改日期：2008-8-19
	 */
	private static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	/**
	 * 判断日期是否在当前周内
	 * 
	 * @param curDate
	 * @param compareDate
	 * @return
	 */
	public static boolean isSameWeek(Date curDate, Date compareDate) {
		if (curDate == null || compareDate == null) {
			return false;
		}

		String datestr = DateUtils.formatDate(curDate);
		Calendar calSun = getCalendar();
		calSun.setTime(DateUtils.parseDate(datestr));
		calSun.set(Calendar.DAY_OF_WEEK, 1);

		Calendar calNext = getCalendar();
		calNext.setTime(calSun.getTime());
		calNext.add(Calendar.DATE, 7);

		Calendar calComp = getCalendar();
		calComp.setTime(compareDate);
		if (calComp.after(calSun) && calComp.before(calNext)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getDay(String datestr) {
		Date date = DateUtils.parseDate(datestr);
		return getDay(date);
	}

	public static int getDay(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 日期转化为大小写
	 */
	public static String getDate_CN(Date date) {
		Calendar ca = getCalendar();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		return NumberUtils.numToUpper(year) + "年" + NumberUtils.monthToUppder(month) + "月"
				+ NumberUtils.dayToUppder(day) + "日";
	}

	/**
	 * 得到内置日期的格式<br/>
	 * <b>得到的内容如下:<br/>
	 * </b>
	 * <ul>
	 * <li>0->yyyy-MM-dd</li>
	 * <li>1->yyyy年MM月dd日</li>
	 * <li>2->yyyy年MM月</li>
	 * <li>3->MM月dd日</li>
	 * <li>7->yyyy-MM-dd</li>
	 * <li>8->EEE(周次 如 周一)</li>
	 * </ul>
	 * 
	 * @return 返回日期格式的map集合
	 */
	public static Map<String, String> getDateFormatMap() {
		Map<String, String> dateFormatMap = new HashMap<String, String>();
		dateFormatMap.put("0", "yyyy-MM-dd");
		dateFormatMap.put("1", "yyyy年MM月dd日");
		dateFormatMap.put("2", "yyyy年MM月");
		dateFormatMap.put("3", "MM月dd日");
		dateFormatMap.put("7", "yyyy-MM-dd");
		dateFormatMap.put("8", "EEE");

		// "EEE-MMMM-dd-yyyy"

		return dateFormatMap;
	}

	/**
	 * 将给定的日期按照传入的内置日期格式码对日期进行转化,转成字符串格式
	 * 
	 * @param date
	 *            将被转化的日期
	 * @param formatCode
	 *            内置日期格式码,详情如下<br/>
	 *            <ul>
	 *            <li>0->yyyy-MM-dd</li>
	 *            <li>1->yyyy年MM月dd日</li>
	 *            <li>2->yyyy年MM月</li>
	 *            <li>3->MM月dd日</li>
	 *            <li>7->yyyy-MM-dd</li>
	 *            <li>8->EEE(周次 如 周一)</li>
	 *            <li>4->二0一二年八月二十五日</li>
	 *            <li>5->二0一二年八月</li>
	 *            <li>6->八月二十五日</li>
	 *            </ul>
	 * @return 格式化后的日期
	 * 
	 */
	public static String getDate_CN(Date date, String formatCode) {
		Map<String, String> dateFormatMap = getDateFormatMap();
		if (!"4".equals(formatCode) && !"5".equals(formatCode) && !"6".equals(formatCode)) {
			String format = dateFormatMap.get(formatCode);
			return DateUtils.getDateFormat(format).format(date);
		} else {
			StringBuilder dateString = new StringBuilder();
			String y = getYear_CN(String.valueOf(getYear(date)));
			String m = getMonth_CN(getMonth(date));
			String day = getMonth_CN(getDay(date));
			if ("4".equals(formatCode)) {
				dateString.append(y).append("年").append(m).append("月").append(day).append("日");
			} else if ("5".equals(formatCode)) {
				dateString.append(y).append("年").append(m).append("月");
			} else if ("6".equals(formatCode)) {
				dateString.append(m).append("月").append(day).append("日");
			}
			return dateString.toString();
		}
	}

	public static String getDay_CN(int day) {
		if (day < 10) {
			return getYear_CN(String.valueOf(day));
		} else {
			return getMonthDay_CN(getYear_CN(String.valueOf(day)));
		}
	}

	/**
	 * 取得日期格式中的小时。24小时制
	 */
	public static int getHour(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 取得字符串日期中的小时。
	 */
	public static int getHour(String datestr) {
		return getHour(DateUtils.parseDateTime(datestr));
	}

	/**
	 * 得到当前小时的第多少分钟
	 */
	public static int getHours() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获得当前月份
	 */
	public static int getMonth() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取得日期对象中的月
	 * 
	 * @param date
	 *            源日期对象
	 * @return month 源日期对象中的月份信息
	 */
	public static int getMonth(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getMonth(String datestr) {
		Date date = DateUtils.parseDate(datestr);
		return getMonth(date);
	}

	/**
	 * 将日期的数字形式转成汉字 12转成一十二
	 */
	public static String getMonth_CN(String datestr) {
		return getMonth_CN(getMonth(datestr));
	}

	/**
	 * 将日期的数字形式转成汉字 12转成一十二
	 * 
	 * @param month
	 *            数字形式日期
	 * @return 汉字日期
	 */
	public static String getMonth_CN(Date date) {
		return getMonth_CN(getMonth(date));
	}

	public static String getMonth_CN(int month) {
		if (month < 10) {
			return getYear_CN(String.valueOf(month));
		} else {
			return getMonthDay_CN(getYear_CN(String.valueOf(month)));
		}
	}

	public static int getMonthDay() {
		return getMonthDay(new Date());
	}

	public static int getMonthDay(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getMonthDay(String datestr) throws Exception {
		return getMonthDay(DateUtils.parseDateTime(datestr));
	}

	public static String getMonthDay_CN(String date) {
		return getMonthDay_CN(getMonth(date));
	}

	public static String getMonthDay_CN(Date date) {
		return getMonthDay_CN(getMonth(date));
	}

	public static String getMonthDay_CN(int monthDay) {
		if (monthDay < 10) {
			return getYear_CN(String.valueOf(monthDay));
		} else {
			return getMonthDay_CN(getYear_CN(String.valueOf(monthDay)));
		}
	}

	public static int getMonthWeek() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 取得字符串日期中的月
	 * 
	 * @param date
	 *            源日期
	 * @return month 源日期中的月份信息
	 * @throws Exception
	 */
	public static int getMonthWeek(String datestr) {
		return getMonthWeek(DateUtils.parseDateTime(datestr));
	}

	/**
	 * 取得日期对象中的月
	 * 
	 * @param date
	 *            源日期对象
	 * @return month 源日期对象中的月份信息
	 */
	public static int getMonthWeek(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 将日期的数字形式转成汉字 12转成一十二
	 * 
	 * @param month
	 *            数字形式日期
	 * @return 汉字日期
	 */
	public static String getMonthWeek_CN(String date) {
		return getMonth_CN(getMonth(date));
	}

	public static String getMonthWeek_CN() {
		return getMonthWeek_CN(getMonthWeek());
	}

	/**
	 * 将日期的数字形式转成汉字 12转成一十二
	 * 
	 * @param month
	 *            数字形式日期
	 * @return 汉字日期
	 */
	public static String getMonthWeek_CN(Date date) {
		return getMonthWeek_CN(getMonthWeek(date));
	}

	/**
	 * 将日期的数字形式转成汉字 12转成一十二
	 * 
	 * @param month
	 *            数字形式日期
	 * @return 汉字日期
	 */
	public static String getMonthWeek_CN(int monthWeek) {
		return "第" + NumberUtils.numToUpper(monthWeek) + "周";
	}

	/**
	 * 获得两个时间点之间相差的天数
	 * 
	 * @param date1
	 *            开始时间点
	 * @param date2
	 *            结束时间点
	 * @return 具体的天数
	 */
	public static int getOffsetDays(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return 0;
		}
		return (int) ((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
	}

	/**
	 * 根据日期偏移天数取得日期<br/>
	 * offset > 0 ,往后延迟offset天， <br/>
	 * offset < 0 向前推进 offset天 <br/>
	 * 
	 * @param date
	 *            :基日期
	 * @param offset
	 *            :日期天数偏移量
	 * @return 偏移后的日期
	 */
	public static Date getOffsetDate(Date date, int offset) {
		if (date == null) {
			return date;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		return calendar.getTime();
	}

	/**
	 * 取得日期格式中的分钟。
	 * 
	 * @param date
	 *            源日期对象
	 * @return 源日期对象中的秒信息
	 */
	public static int getSecond(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 */
	public static String getWeek(String datestr) {
		// 再转换为时间
		Date date = DateUtils.parseDate(datestr);
		Calendar c = getCalendar();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return TimeUtils.getWeek(c.getTime());
	}

	/**
	 * 代表含义：一周中的第几天，对应星期几，第一天为星期日，于此类推。
	 * 
	 * <pre>
	 * 		星期日:Calendar.SUNDAY=1
	 * 		星期一:Calendar.MONDAY=2
	 * 		星期二:Calendar.TUESDAY=3
	 * 		星期三:Calendar.WEDNESDAY=4
	 * 		星期四:Calendar.THURSDAY=5
	 * 		星期五:Calendar.FRIDAY=6
	 * 		星期六:Calendar.SATURDAY=7
	 * </pre>
	 * 
	 * @return
	 */
	public static int getWeekDay() {
		return getWeekDay(new Date());
	}

	public static int getWeekDay(Date date) {
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getWeekDay(String datestr) {
		return getWeekDay(DateUtils.parseDateTime(datestr));
	}

	public static String getWeekDay_CN() {
		return getWeekDay_CN(getWeekDay());
	}

	public static String getWeekDay_CN(Date date) {
		return getWeekDay_CN(getWeekDay(date));
	}

	public static String getWeekDay_CN(String datestr) {
		return getWeekDay_CN(getWeekDay(datestr));
	}

	public static String getWeekDay_CN(int weekDay) {
		return "星期" + NumberUtils.numToUpper(weekDay);
	}

	/**
	 * 将经 getYearForChinese(String dateString) 转化后的类似于 一二 这样的月转成 一十二
	 * 
	 * @param monthChinese
	 *            月份中文
	 * @return 将转换后的月份返回
	 */
	public static String getMonthDay_CNe(String monthChinese) {
		String[] ss = monthChinese.split("");
		StringBuilder sb = new StringBuilder();
		sb.append(ss[1]).append("十").append(ss[2]);
		return sb.toString().replace("0", "");
	}

	public static int getYear() {
		Calendar cal = getCalendar();
		return cal.get(Calendar.YEAR);
	}

	public static int getYear(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getYear(String datestr) throws Exception {
		return getYear(DateUtils.parseDate(datestr));
	}

	public static String getYear_CN() {
		return getYear_CN(TimeUtils.getYear());
	}

	public static String getYear_CN(Date date) {
		return getYear_CN(TimeUtils.getYear(date));
	}

	public static String getYear_CN(String dateStr) {
		StringBuilder dataChinese = new StringBuilder();
		String[] dateArray = dateStr.split("");
		for (String s1 : dateArray) {
			if ("0".equals(s1)) {
				dataChinese.append("〇");
			} else if ("1".equals(s1)) {
				dataChinese.append("一");
			} else if ("2".equals(s1)) {
				dataChinese.append("二");
			} else if ("3".equals(s1)) {
				dataChinese.append("三");
			} else if ("4".equals(s1)) {
				dataChinese.append("四");
			} else if ("5".equals(s1)) {
				dataChinese.append("五");
			} else if ("6".equals(s1)) {
				dataChinese.append("六");
			} else if ("7".equals(s1)) {
				dataChinese.append("七");
			} else if ("8".equals(s1)) {
				dataChinese.append("八");
			} else if ("9".equals(s1)) {
				dataChinese.append("九");
			} else {
				dataChinese.append(s1);
			}
		}

		return dataChinese.toString();
	}

	/**
	 * 计算指定日期+addMonth月+15号 返回格式"2008-02-15"
	 * 
	 * @param date
	 * @param addMonth
	 * @param monthDay
	 * @return
	 */
	public static Date getSpecDate(Date date, int addMonth, int monthDay) {
		Calendar cal = getCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, addMonth);
		cal.set(Calendar.DAY_OF_MONTH, monthDay);
		return cal.getTime();
	}

	/**
	 * 获取本周一日期
	 */
	public static String getMonday() {
		return "";
	}

	/**
	 * 获取本周二日期
	 */
	public static String getTuesday() {
		return "";
	}

	/**
	 * 获取本周三日期
	 */
	public static String getWednesday() {
		return "";
	}

	/**
	 * 获取本周四日期
	 */
	public static String getThursday() {
		return "";
	}

	/**
	 * 获取本周五日期
	 */
	public static String getFriday() {
		return "";
	}

	/**
	 * 获取本周六日期
	 */
	public static String getSaturday() {
		int mondayPlus = getMondayPlus();
		Calendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取本周日日期
	 */
	public static String getSunday() {
		return "";
	}

	/**
	 * 工作日 获得本周星期日的日期
	 * 
	 * @return
	 */
	public static String getWeekday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 将小时化成分钟,再加分钟时间
	 */
	public static int toMinutes(Date date) {
		Calendar c = getCalendar();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前日期 格式为：xxxx-yy-zz (eg: 2008-08-19) 方法的实现逻辑描述:
	 */
	public static String today() {
		int x; // 日期属性：年
		int y; // 日期属性：月
		int z; // 日期属性：日

		Calendar localTime = getCalendar(); // 当前日期
		String strY = null;
		String strZ = null;
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		z = localTime.get(Calendar.DATE);
		// int hour = localTime.get(Calendar.HOUR);
		// int minute = localTime.get(Calendar.MINUTE);
		// int seconds = localTime.get(Calendar.SECOND);
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
		return x + "-" + strY + "-" + strZ;
		// return x + "-" + strY + "-" + strZ + " " + hour
		// +":"+minute+":"+seconds;
	}

	public static void main(String[] args) {

		System.out.println(DateUtils.formatDateTime(getPreYearBegin()));

		Calendar cal = getCalendar();

		/*
		 * Calendar.WEEK_OF_YEAR Calendar.WEEK_OF_MONTH Calendar.DAY_OF_YEAR :
		 * 获得今天在本年的第几天 Calendar.DAY_OF_WEEK_IN_MONTH : 某月中第几周，
		 * 按这个月1号算，1号起就是第1周，8号起就是第2周。以月份天数为标准 Calendar.DAY_OF_MONTH :
		 * 日历式的第几周（例如今天是8-21，是八月的第四周） Calendar.DAY_OF_WEEK : 获得今天在本周的第几天
		 */
		System.out.println("WEEK_OF_YEAR:" + cal.get(Calendar.WEEK_OF_YEAR));
		System.out.println("WEEK_OF_MONTH:" + cal.get(Calendar.WEEK_OF_MONTH));
		System.out.println("DAY_OF_YEAR:" + cal.get(Calendar.DAY_OF_YEAR));
		System.out.println("DAY_OF_WEEK_IN_MONTH:" + cal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		System.out.println("DAY_OF_MONTH:" + cal.get(Calendar.DAY_OF_MONTH));
		System.out.println("DAY_OF_WEEK:" + cal.get(Calendar.DAY_OF_WEEK));

		System.out.println(getYear_CN("2015"));
		System.out.println(getMinutes());
		System.out.println(getSeconds());
		/** ***********Test of convertTimeSpan***************** */
		// System.out.println(getLastDayOfMonth(new Date()));
		System.out.println(getSpecDate(new Date(), 2, 15));

		/*
		 * CalendarUtil tt = new CalendarUtil();
		 * 
		 * System.out.println("获取当天日期:" + tt.getNowTime("yyyy/MM-dd"));
		 * System.out.println("获取本周一日期:" + tt.getMondayOFWeek());
		 * System.out.println("获取本周日的日期~:" + tt.getWeekday());
		 * System.out.println("获取上周一日期:" + tt.getPreWeekday());
		 * System.out.println("获取上周日日期:" + tt.getPreWeekSunday());
		 * System.out.println("获取下周一日期:" + tt.getNextMonday());
		 * System.out.println("获取下周日日期:" + tt.getNextSunday());
		 * System.out.println("获得相应周的周六的日期:" + tt.getNowTime("yyyy-MM-dd"));
		 * System.out.println("获取本月第一天日期:" + tt.getFirstDayOfMonth());
		 * System.out.println("获取本月最后一天日期:" + tt.getDefaultDay());
		 * System.out.println("获取上月第一天日期:" + tt.getPreMonthFirst());
		 * System.out.println("获取上月最后一天的日期:" + tt.getPreMonthEnd());
		 * System.out.println("获取下月第一天日期:" + tt.getNextMonthFirst());
		 * System.out.println("获取下月最后一天日期:" + tt.getNextMonthEnd());
		 * System.out.println("获取本年的第一天日期:" + tt.getYearFirst());
		 * System.out.println("获取本年最后一天日期:" + tt.getYearEnd());
		 * System.out.println("获取去年的第一天日期:" + tt.getPreYearFirst());
		 * System.out.println("获取去年的最后一天日期:" + tt.getPreYearEnd());
		 * System.out.println("获取明年第一天日期:" + tt.getNextYearFirst());
		 * System.out.println("获取明年最后一天日期:" + tt.getNextYearEnd());
		 * System.out.println("获取本季度第一天:" + tt.getThisSeasonFirstTime(11));
		 * System.out.println("获取本季度最后一天:" + tt.getThisSeasonFinallyTime(11));
		 * System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:" +
		 * CalendarUtil.getTwoDay("2008-12-1", "2008-9-29"));
		 * System.out.println("获取当前月的第几周：" + tt.getWeekOfMonth());
		 * System.out.println("获取当前年份：" + tt.getYear()); System.out.println("获取当前月份：" +
		 * tt.getMonth()); System.out.println("获取今天在本年的第几天：" + tt.getDayOfYear());
		 * System.out.println("获得今天在本月的第几天(获得当前日)：" + tt.getDayOfMonth());
		 * System.out.println("获得今天在本周的第几天：" + tt.getDayOfWeek());
		 * System.out.println("获得半年后的日期：" +
		 * tt.convertDateToString(tt.getTimeYearNext()));
		 * System.out.println("获得本年有多少天 ：" + tt.getMaxYear());
		 */
	}

}