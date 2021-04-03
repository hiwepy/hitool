/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;

import hitool.core.lang3.Assert;
import hitool.core.lang3.StringUtils;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	/** 短日时间格式：HH:mm */
	public static final String SHORT_TIME_FORMAT = "HH:mm";
	/** 短日时间格式：HH:mm */
	public static final SimpleDateFormat FORMAT_SHORT_TIME = new SimpleDateFormat(SHORT_TIME_FORMAT);
	/** 短日时间格式：HH时mm分 */
	public static final String SHORT_TIME_FORMAT_CN = "HH时mm分";
	/** 短日时间格式：HH时mm分 */
	public static final SimpleDateFormat FORMAT_SHORT_TIME_CN = new SimpleDateFormat(SHORT_TIME_FORMAT_CN);
	/** 时间格式：HH:mm:ss */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/** 时间格式：HH:mm:ss */
	public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(TIME_FORMAT);
	/** 时间格式：HH时mm分ss秒 */
	public static final String TIME_FORMAT_CN = "HH时mm分ss秒";
	/** 时间格式：HH时mm分ss秒 */
	public static final SimpleDateFormat FORMAT_TIME_CN = new SimpleDateFormat(TIME_FORMAT_CN);
	/** 时间格式,主要是针对timestamp：HH:mm:ss:SS */
	public static final String TIME_LONGFORMAT = "HH:mm:ss:SS";
	/** 时间格式,主要是针对timestamp：HH:mm:ss:SS */
	public static final SimpleDateFormat FORMAT_LONGTIME = new SimpleDateFormat(TIME_LONGFORMAT);
	/** 时间格式,主要是针对timestamp：HH时mm分ss秒SS毫秒 */
	public static final String TIME_LONGFORMAT_CN = "HH时mm分ss秒SS毫秒";
	/** 时间格式,主要是针对timestamp：HH时mm分ss秒SS毫秒 */
	public static final SimpleDateFormat FORMAT_LONGTIME_CN = new SimpleDateFormat(TIME_LONGFORMAT_CN);

	/** 短日期格式：yyyy-MM-dd */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 短日期格式：yyyy-MM-dd */
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(DATE_FORMAT);
	/** 短日期格式：yyyy/MM/dd */
	public static final String DATE_FORMAT_TWO = "yyyy/MM/dd";
	/** 短日期格式：yyyy/MM/dd */
	public static final SimpleDateFormat FORMAT_DATE_TWO = new SimpleDateFormat(DATE_FORMAT_TWO);
	/** 短日期格式：yyyy年MM月dd日 */
	public static final String DATE_FORMAT_CN = "yyyy年MM月dd日";
	/** 短日期格式：yyyy年MM月dd日 */
	public static final SimpleDateFormat FORMAT_DATE_CN = new SimpleDateFormat(DATE_FORMAT_CN);

	/** 日期格式：yyyy-MM */
	public static final String MONTH_FORMAT = "yyyy-MM";
	/** 日期格式：yyyy-MM */
	public static final SimpleDateFormat FORMAT_MONTH = new SimpleDateFormat(MONTH_FORMAT);
	/** 日期格式：yyyy/MM */
	public static final String MONTH_FORMAT_TWO = "yyyy/MM";
	/** 日期格式：yyyy/MM */
	public static final SimpleDateFormat FORMAT_MONTH_TWO = new SimpleDateFormat(MONTH_FORMAT_TWO);
	/** 日期格式：yyyy年MM月 */
	public static final String MONTH_FORMAT_CN = "yyyy年MM月";
	/** 日期格式：yyyy年MM月 */
	public static final SimpleDateFormat FORMAT_MONTH_CN = new SimpleDateFormat(MONTH_FORMAT_CN);
	/** 日期格式：EEE, dd MMM yyyy */
	public static final String EEE_DATE_FORMAT = "EEE, dd MMM yyyy";
	/** 日期格式：EEE, dd MMM yyyy */
	public static final SimpleDateFormat FORMAT_EEE_DATE = new SimpleDateFormat(EEE_DATE_FORMAT);
	/** 日期格式：dd MMM yyyy */
	public static final String MMM_DATE_FORMAT = "dd MMM yyyy";
	/** 日期格式：dd MMM yyyy */
	public static final SimpleDateFormat FORMAT_MMM_DATE = new SimpleDateFormat(MMM_DATE_FORMAT);
	/** 日期格式：dd MMM yyyy HH:mm:ss */
	public static final String MMM_DATE_TIME_FORMAT = MMM_DATE_FORMAT + " " + TIME_FORMAT;
	/** 日期格式：dd MMM yyyy HH:mm:ss */
	public static final SimpleDateFormat FORMAT_MMM_DATE_TIME = new SimpleDateFormat(MMM_DATE_TIME_FORMAT);

	/** 日期格式：yyyy-MM-dd HH:mm */
	public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + SHORT_TIME_FORMAT;
	/** 日期格式：yyyy-MM-dd HH:mm */
	public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat(DATE_TIME_FORMAT);
	/** 日期格式：yyyy/MM/dd HH:mm */
	public static final String DATE_TIME_FORMAT_TWO = DATE_FORMAT_TWO + " " + SHORT_TIME_FORMAT;
	/** 日期格式：yyyy年MM月dd日 HH:mm */
	public static final SimpleDateFormat FORMAT_DATE_TIME_TWO = new SimpleDateFormat(DATE_TIME_FORMAT_TWO);
	/** 日期格式：yyyy年MM月dd日 HH:mm */
	public static final String DATE_TIME_FORMAT_CN = DATE_FORMAT_CN + " " + SHORT_TIME_FORMAT;
	/** 日期格式：yyyy年MM月dd日 HH:mm */
	public static final SimpleDateFormat FORMAT_DATE_TIME_CN = new SimpleDateFormat(DATE_TIME_FORMAT_CN);

	/** 长日期格式：yyyy-MM-dd HH:mm:ss */
	public static final String DATE_LONGFORMAT = DATE_FORMAT + " " + TIME_FORMAT;
	/** 长日期格式：yyyy-MM-dd HH:mm:ss */
	public static final SimpleDateFormat FORMAT_LONGDATE = new SimpleDateFormat(DATE_LONGFORMAT);
	/** 长日期格式：yyyy/MM/dd HH:mm:ss */
	public static final String DATE_LONGFORMAT_TWO = DATE_FORMAT_TWO + " " + TIME_FORMAT;
	/** 长日期格式：yyyy/MM/dd HH:mm:ss */
	public static final SimpleDateFormat FORMAT_LONGDATE_TWO = new SimpleDateFormat(DATE_LONGFORMAT_TWO);
	/** 长日期格式：yyyy年MM月dd日 HH:mm:ss */
	public static final String DATE_LONGFORMAT_DATE_CN = DATE_FORMAT_CN + " " + TIME_FORMAT;
	/** 长日期格式：yyyy年MM月dd日 HH:mm:ss */
	public static final SimpleDateFormat FORMAT_LONGDATE_DATE_CN = new SimpleDateFormat(DATE_LONGFORMAT_DATE_CN);
	/** 长日期格式：yyyy年MM月dd日 HH时mm分ss秒 */
	public static final String DATE_LONGFORMAT_TIME_CN = DATE_FORMAT_CN + " " + TIME_FORMAT_CN;
	/** 长日期格式：yyyy年MM月dd日 HH时mm分ss秒 */
	public static final SimpleDateFormat FORMAT_LONGDATE_TIME_CN = new SimpleDateFormat(DATE_LONGFORMAT_TIME_CN);

	/** 长日期格式,主要是针对timestamp：yyyy-MM-dd HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT = DATE_FORMAT + " " + TIME_LONGFORMAT;
	/** 长日期格式,主要是针对timestamp：yyyy-MM-dd HH:mm:ss.SSS */
	public static final SimpleDateFormat FORMAT_TIMESTAMP = new SimpleDateFormat(TIMESTAMP_FORMAT);
	/** 长日期格式,主要是针对timestamp：yyyy/MM/dd HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT_TWO = DATE_FORMAT_TWO + " " + TIME_LONGFORMAT;
	/** 长日期格式,主要是针对timestamp：yyyy/MM/dd HH:mm:ss.SSS */
	public static final SimpleDateFormat FORMAT_TIMESTAMP_TWO = new SimpleDateFormat(TIMESTAMP_FORMAT_TWO);
	/** 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT_DATE_CN = DATE_FORMAT_CN + " " + TIME_LONGFORMAT;
	/** 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH:mm:ss.SSS */
	public static final SimpleDateFormat FORMAT_TIMESTAMP_DATE_CN = new SimpleDateFormat(TIMESTAMP_FORMAT_DATE_CN);
	/** 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH时mm分ss秒SS毫秒 */
	public static final String TIMESTAMP_FORMAT_TIME_CN = DATE_FORMAT_CN + " " + TIME_LONGFORMAT_CN;
	/** 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH时mm分ss秒SS毫秒 */
	public static final SimpleDateFormat FORMAT_TIMESTAMP_TIME_CN = new SimpleDateFormat(TIMESTAMP_FORMAT_TIME_CN);

	/**
	 * 默认验证日期正则表达式
	 */
	protected static final String DATE_PATTERN = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS 格式正则
	 */
	protected static final String DATE_TIMESTAMP_PATTERN = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$";
	/**
	 * yyyy-MM-dd HH:mm:ss格式正则
	 */
	protected static final String DATE_LONGFORMAT_PATTERN = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy/MM/dd HH:mm:ss格式正则
	 */
	protected static final String DATE_LONGFORMAT_PATTERN_TWO = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy-MM-dd HH:mm格式正则
	 */
	protected static final String DATE_TIME_PATTERN = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy-MM-dd格式正则
	 */
	protected static final String DATE_DAY_PATTERN = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$";
	/**
	 * yyyy/MM/dd格式正则
	 */
	protected static final String DATE_DAY_PATTERN_TWO = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2}$";

	/**
	 * 根据给出的字符串格式，获取相应的日期格式化对象
	 * 
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(String format) {
		return getDateFormat(format, Locale.getDefault());
	}

	public static SimpleDateFormat getDateFormat(String format, Locale locale) {
		return new SimpleDateFormat(format, locale);
	}

	/**
	 * 获取长日期格式：yyyy-MM-dd HH:mm:ss 格式对象
	 */
	public static SimpleDateFormat getLongDateFormat() {
		return getDateFormat(DATE_LONGFORMAT);
	}

	/**
	 * 将日期格式化成字符串：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            将被格式的Date对象
	 * @return 被格式化后的日期字符串形式
	 */
	public static String format(Date date) {
		return getDateFormat(DATE_LONGFORMAT).format(date);
	}

	/**
	 * 根据传入的日期格式将日期化成相应格式的字符串，如：
	 * <ul>
	 * <li>yyyy-MM-dd HH:mm:ss
	 * <li>yyyy-MM-dd
	 * <li>yyyy/MM/dd HH:mm:ss
	 * <li>yyyy/MM/dd
	 * </ul>
	 * 
	 * @param date
	 *            : 待格式化的时间对象
	 * @param format
	 *            ：时间格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		Assert.notNull(date, " date must not be null ");
		Assert.hasText(pattern, " pattern must not be null ");
		return getDateFormat(pattern).format(date);
	}

	/**
	 * 将long类型的日期格式化成yyyy-MM-dd HH:mm:ss格式的日期字符串形式
	 * 
	 * @param date
	 *            将被格式化的long类型日期
	 * @return 格式化后的字符串形式日期
	 */
	public static String format(long date) {
		return getDateFormat(DATE_LONGFORMAT).format(date);
	}

	/**
	 * 根据传入的日期格式将系统当前日期对象进行格式后返回
	 * 
	 * @param format
	 *            ：时间格式
	 * @return
	 */
	public static String format(String pattern) {
		Assert.hasText(pattern, " pattern must not be null ");
		return getDateFormat(pattern).format(new Date());
	}

	/**
	 * 将日期格式化成字符串
	 * 
	 * @param date
	 *            将被格式的Date对象
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		Assert.notNull(date, " date must not be null ");
		return getDateFormat(DATE_FORMAT).format(date);
	}

	public static String formatDateTime(Date date) {
		Assert.notNull(date, " date must not be null ");
		return getDateFormat(DATE_LONGFORMAT).format(date);
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss） public static String formatDateTime(Date
	 * date) { return formatDate(date, DATE_LONGFORMAT); }
	 */

	/**
	 * 将一个long型的数值转换为指定格式的日期字符串
	 * 
	 * @param time
	 *            要转换的long值
	 * @return 转换后的字符串
	 */
	public static String formatTime(long time, String format) {
		return getDateFormat(format).format(time);
	}

	/**
	 * 得到格式化后的日期，格式为:yyyy年MM月dd日，如2016年07月31日
	 * 
	 * @param currDate
	 *            要格式化的日期
	 * @see #getFormatDate(java.util.Date, String)
	 * @return String 返回格式化后的日期，默认格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static String formatDate_CN(Date date) {
		Assert.notNull(date, " date must not be null ");
		return getDateFormat(DATE_FORMAT_CN).format(date);
	}

	public static String formatDateTime_CN(Date date) {
		Assert.notNull(date, " date must not be null ");
		return getDateFormat(DATE_LONGFORMAT_TIME_CN).format(date);
	}

	// 取得当前系统的日期
	public static Date formatDate(String format) {
		Assert.hasText(format, " format must not be null ");
		try {
			Date now = new Date(System.currentTimeMillis());
			String datestr = getDateFormat(format).format(now).toString();
			// /System.out.println(test);
			return getDateFormat(format).parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将日期类型转换成指定格式的日期字符串
	 * 
	 * @param date
	 * @param format
	 * @return ：
	 */
	public static String formatDate(Date date, String format) {
		Assert.notNull(date, " date must not be null ");
		Assert.hasText(format, " format must not be null ");
		return getDateFormat(format).format(date);
	}

	/**
	 * 按照给定的格式style将指定的日期值转换成字符串。
	 * 
	 * @param date
	 *            : 待转换的日期
	 * @param style
	 *            : 指定转化类型,style参数取静态常量LONG、MEDIUM和SHORT的值
	 * @param loc
	 *            ：字符定义对象
	 * @return 格式化后的日期字符串
	 * @throws IllegalArgumentException
	 *             : style模板不符合格式时报异常
	 */
	public static String formatDate(Date date, int style, Locale loc) {
		if (style < 1 || style > 3) {
			throw new IllegalArgumentException("parameter is invalid.");
		}
		String newDate = "";
		if (loc == null) {
			loc = Locale.getDefault();
		}
		if (date != null) {
			DateFormat df = DateFormat.getDateInstance(style, loc);
			newDate = df.format(date);
		}
		return newDate;
	}

	/**
	 * 按照给定的格式模板将指定的日期值转换成字符串。
	 * 
	 * @param date
	 *            : 待转换的日期
	 * @param format
	 *            : 指定转化格式字符串,例如：yyyy-MM-dd
	 * @param locale
	 *            : 字符定义对象
	 * @return 格式化后的日期字符串
	 * @throws IllegalArgumentException
	 *             : pattern模板不符合格式时报异常
	 */
	public static String formatDate(Date date, String format, Locale locale) {
		Assert.notNull(date, " date must not be null ");
		Assert.hasText(format, " format must not be null ");
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return new SimpleDateFormat(format, locale).format(date);
	}

	/**
	 * 将日期字符串解析成日期对象，支持一下格式<br/>
	 * <p>
	 * <ul>
	 * <li>yyyy-MM-dd HH:mm:ss.m</li>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy/MM/dd HH:mm:ss</li>
	 * <li>yyyy/MM/dd</li>
	 * </ul>
	 * </p>
	 * 
	 * @param datestr
	 *            字符串形式的日期
	 * @return 字符串格式日期经相应的格式格式化后的Date形式<br/>
	 *         如果传入的日期跟内置日期格式不匹配,抛出异常
	 * @throws Exception
	 */
	public static Date parse(String datestr) throws Exception {
		Date date = null;
		try {
			Pattern p1 = Pattern.compile(DATE_LONGFORMAT_PATTERN);
			Matcher m1 = p1.matcher(datestr);
			if (m1.matches()) {
				date = getDateFormat(DATE_LONGFORMAT).parse(datestr);
			}

			if (date == null) {
				Pattern p2 = Pattern.compile(DATE_DAY_PATTERN);
				Matcher m2 = p2.matcher(datestr);
				if (m2.matches()) {
					date = getDateFormat(DATE_FORMAT).parse(datestr);
				}
			}

			if (date == null) {
				Pattern p3 = Pattern.compile(DATE_LONGFORMAT_PATTERN_TWO);
				Matcher m3 = p3.matcher(datestr);
				if (m3.matches()) {
					date = getDateFormat(DATE_LONGFORMAT_TWO).parse(datestr);
				}
			}

			if (date == null) {
				Pattern p4 = Pattern.compile(DATE_DAY_PATTERN_TWO);
				Matcher m4 = p4.matcher(datestr);
				if (m4.matches()) {
					date = getDateFormat(DATE_FORMAT_TWO).parse(datestr);
				}
			}

			if (date == null) {
				Pattern p5 = Pattern.compile(DATE_TIME_PATTERN);
				Matcher m5 = p5.matcher(datestr);
				if (m5.matches()) {
					date = getDateFormat(DATE_TIME_FORMAT).parse(datestr);
				}
			}

			if (date == null) {
				Pattern p6 = Pattern.compile(DATE_TIMESTAMP_PATTERN);
				Matcher m6 = p6.matcher(datestr);
				if (m6.matches()) {
					date = getDateFormat(TIMESTAMP_FORMAT).parse(datestr);
				}
			}

		} catch (ParseException e) {
			throw new Exception("非法日期字符串，解析失败：" + datestr, e);
		}
		return date;
	}

	public static Date parseDate(String datestr) {
		return parseDate(datestr, DATE_FORMAT);
	}

	public static Date parseDateTime(String datestr) {
		return parseDate(datestr, DATE_LONGFORMAT);
	}

	public static Date parseDateTime_CN(String datestr) {
		return parseDate(datestr, DATE_LONGFORMAT_TIME_CN);
	}

	public static Date parseDate_CN(String datestr) {
		return parseDate(datestr, DATE_FORMAT_CN);
	}

	/**
	 * 根据指定的格式strFormat,将给定的字符串形式的日期转换成date类型
	 * 
	 * @param datestr
	 *            : 要转换的日期字符串
	 * @param format
	 *            : 日期格式
	 * @return 转换以后的日期
	 */
	public static Date parseDate(String datestr, String format) {
		Assert.hasText(datestr, " datestr must not be null ");
		Assert.hasText(format, " format must not be null ");
		Date newDate = null;
		try {
			newDate = getDateFormat(format).parse(datestr);
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}

	/**
	 * 两个时间之间的天数
	 */
	public static long getDiffDays(String date1, String date2) {
		if (date1 == null || date1.equals("")) {
			return 0;
		}
		if (date2 == null || date2.equals("")) {
			return 0;
		}
		// 转换为标准时间
		SimpleDateFormat myFormatter = getDateFormat(DATE_FORMAT);
		long day = 0;
		try {
			Date date = myFormatter.parse(date1);
			Date mydate = myFormatter.parse(date2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
		}
		return day;
	}

	/**
	 * 将2007-12-1变成2007-12-01。将2007-9-1变为2007-09-01。
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("null")
	public static String getFormatDateV2(String date) {
		if (date == null) {
			return date;
		}
		String[] datearr = StringUtils.split(date, "-");
		StringBuffer ret = new StringBuffer();

		if (datearr == null && datearr.length < 3) {
			return date;
		}

		ret.append(datearr[0]);
		ret.append("-");
		ret.append(Integer.parseInt(datearr[1]) < 10 ? "0" + datearr[1] : datearr[1]);
		ret.append("-");
		ret.append(Integer.parseInt(datearr[2]) < 10 ? "0" + datearr[2] : datearr[2]);
		return ret.toString();

	}

	/**
	 * 从时间串中获取小时数。
	 * 
	 * @param datestr
	 *            "2007-10-12 13:25:00"
	 * @return
	 * @throws ParseException
	 */
	public static int getHourFromTimeString(String datestr) throws ParseException {
		if (StringUtils.isBlank(datestr)) {
			return 0;
		}
		Date date = getDateFormat(DATE_LONGFORMAT).parse(datestr);
		datestr = getDateFormat(DATE_LONGFORMAT).format(date);
		return Integer.parseInt(datestr.substring(datestr.length() - 8, datestr.length() - 6));
	}

	public static String getDayBegin(String datestr) throws ParseException {
		Assert.hasText(datestr, " datestr must not be null ");
		Date date = getDateFormat(DATE_FORMAT).parse(datestr);
		datestr = getDateFormat(DATE_FORMAT).format(date);
		return datestr + " 00:00:00";
	}

	/**
	 * 时间查询时,结束时间的 23:59:59
	 * 
	 * @throws ParseException
	 */
	public static String getDayEnd(String datestr) throws ParseException {
		Assert.hasText(datestr, " datestr must not be null ");
		Date date = getDateFormat(DATE_FORMAT).parse(datestr);
		datestr = getDateFormat(DATE_FORMAT).format(date);
		return datestr + " 23:59:59";
	}

	/**
	 * 得到格式化后的当前系统日期，格式为yyyy-MM-dd，如2006-02-15
	 * 
	 * @see #getFormatDate(Date)
	 * @return String 返回格式化后的当前服务器系统日期，格式为yyyy-MM-dd，如2006-02-15
	 */
	public static String getCurrDate() {
		return formatDate(new Date(), DATE_FORMAT);
	}

	/**
	 * 取得当前日期，并将其转换成格式为"dateFormat"的字符串 例子：假如当前日期是 2003-09-24 9:19:10，则：
	 * 
	 * <pre>
	 *  getCurrDate(&quot;yyyyMMdd&quot;)=&quot;20030924&quot;
	 *  getCurrDate(&quot;yyyy-MM-dd&quot;)=&quot;2003-09-24&quot;
	 *  getCurrDate(&quot;yyyy-MM-dd HH:mm:ss&quot;)=&quot;2003-09-24 09:19:10&quot;
	 * </pre>
	 * 
	 * @param dateFormatString
	 *            日期格式字符串
	 * @return String
	 */
	public static String getCurrDate(String format) {
		return formatDate(new Date(), format);
	}

	/**
	 * 得到格式化后的当前系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 * 
	 * @see #getFormatDateTime(Date)
	 * @return String 返回格式化后的当前服务器系统时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
	 */
	public static String getCurrDateTime() {
		return formatDate(new Date(), DATE_LONGFORMAT);
	}

	/**
	 * 得到格式化后的当前系统日期，格式为yyyy年MM月dd日，如2006年02月15日
	 * 
	 * @see #getFormatDate(Date, String)
	 * @return String 返回当前服务器系统日期，格式为yyyy年MM月dd日，如2006年02月15日
	 */
	public static String getCurrDate_CN() {
		return formatDate(new Date(), DATE_FORMAT_CN);
	}

	/**
	 * 得到格式化后的当前系统时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 * 
	 * @see #getFormatDateTime(Date, String)
	 * @return String 返回格式化后的当前服务器系统时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
	 */
	public static String getCurrDateTime_CN() {
		return formatDate(new Date(), DATE_LONGFORMAT_DATE_CN);
	}

	/**
	 * 进行时段格式转换，对于输入的48位的01串，将进行如下操作：
	 * <li>1.先将输入中每个0变成两个0，每个1变成2个1，形成一个96位的二进制串。</li>
	 * <li>2.将上述的96位的二进制串分成3组，每组32位。</li>
	 * <li>3.将每个32位的二进制串转换成一个8位的16进制串。</li>
	 * <li>4.将3个8位的16进制串合并成一个串，中间以","分割。</li>
	 * 
	 * @param timespan
	 *            一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 * @return 一个16进制串，每位间以","分割。如："3fffcfff,ffffffff,fffffffc"
	 */
	public static String getHexTimeFromBinary(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String ret = "";
		String tmp = "";
		for (int i = 0; i < timespan.length(); i++) {
			tmp += timespan.charAt(i);
			tmp += timespan.charAt(i);
			// tmp += i;
			if ((i + 1) % 16 == 0) {
				if (!ret.equals("")) {
					ret += ",";
				}
				Long t = Long.parseLong(tmp, 2);
				String hexStr = Long.toHexString(t);
				if (hexStr.length() < 8) {
					int length = hexStr.length();
					for (int n = 0; n < 8 - length; n++) {
						hexStr = "0" + hexStr;
					}
				}

				ret += hexStr;
				tmp = "";
			}
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，将输入的26位的2进制串转换成48位的二进制串。
	 * 
	 * @param timespan
	 *            一个16进制串，每位间以","分割。如："3fffcfff,ffffffff,fffffffc"
	 * @return 一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 */
	public static String getBinaryTimeFromHex(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String tmp = "";
		String ret = "";
		String[] strArr = timespan.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String binStr = Long.toBinaryString(Long.parseLong(strArr[i], 16));
			if (binStr.length() < 32) {
				int length = binStr.length();
				for (int n = 0; n < 32 - length; n++) {
					binStr = "0" + binStr;
				}
			}
			tmp += binStr;
		}

		for (int i = 0; i < 48; i++) {
			ret += tmp.charAt(i * 2);
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，将输入的32位的10进制串转换成48位的二进制串。
	 * 
	 * @param timespan
	 *            一个16进制串，每位间以","分割。如："1234567890,1234567890,1234567890c"
	 * @return 一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 */
	public static String getBinaryTimeFromDec(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String tmp = "";
		String ret = "";
		String[] strArr = timespan.split(",");
		for (int i = 0; i < strArr.length; i++) {
			String binStr = Long.toBinaryString(Long.parseLong(strArr[i], 10));
			if (binStr.length() < 32) {
				int length = binStr.length();
				for (int n = 0; n < 32 - length; n++) {
					binStr = "0" + binStr;
				}
			}
			tmp += binStr;
		}

		for (int i = 0; i < 48; i++) {
			ret += tmp.charAt(i * 2);
		}

		return ret;
	}

	/**
	 * 进行时段格式转换，对于输入的48位的01串，将进行如下操作：
	 * <li>1.先将输入中每个0变成两个0，每个1变成2个1，形成一个96位的二进制串。</li>
	 * <li>2.将上述的96位的二进制串分成3组，每组32位。</li>
	 * <li>3.将每个32位的二进制串转换成一个10位的10进制串。</li>
	 * <li>4.将3个8位的16进制串合并成一个串，中间以","分割。</li>
	 * 
	 * @param timespan
	 *            一个48位的二进制串，如："011111111011111111111111111111111111111111111110"
	 * @return 一个16进制串，每位间以","分割。如："1234567890,1234567890,1234567890"
	 */
	public static String getDecTimeFromBinary(String timespan) {
		if (timespan == null || timespan.equals("")) {
			return "";
		}

		String ret = "";
		String tmp = "";
		for (int i = 0; i < timespan.length(); i++) {
			tmp += timespan.charAt(i);
			tmp += timespan.charAt(i);
			// tmp += i;
			if ((i + 1) % 16 == 0) {
				if (!ret.equals("")) {
					ret += ",";
				}
				Long t = Long.parseLong(tmp, 2);
				String decStr = Long.toString(t);
				if (decStr.length() < 10) {
					int length = decStr.length();
					for (int n = 0; n < 10 - length; n++) {
						decStr = "0" + decStr;
					}
				}

				ret += decStr;
				tmp = "";
			}
		}

		return ret;
	}

	/**
	 * 使用正则表达式判定日期
	 */
	public static boolean isDate(String date, String expression) {
		// 使用正则表达式进行判定
		if (expression == null || expression.trim().length() == 0) {
			expression = DATE_PATTERN;
		}
		Pattern pattern;
		// System.out.println(expression);
		pattern = Pattern.compile(expression, 2);
		Matcher matcher = pattern.matcher(date.trim());
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String datestr) {
		return isFormat(datestr, DATE_FORMAT) || isFormat(datestr, DATE_FORMAT_TWO) || isFormat(datestr, DATE_FORMAT_CN)
				|| isFormat(datestr, MONTH_FORMAT) || isFormat(datestr, MONTH_FORMAT_TWO)
				|| isFormat(datestr, MONTH_FORMAT_CN) || isFormat(datestr, DATE_TIME_FORMAT)
				|| isFormat(datestr, DATE_TIME_FORMAT_TWO) || isFormat(datestr, DATE_TIME_FORMAT_CN)
				|| isFormat(datestr, DATE_LONGFORMAT) || isFormat(datestr, DATE_LONGFORMAT_TWO)
				|| isFormat(datestr, DATE_LONGFORMAT_DATE_CN) || isFormat(datestr, DATE_LONGFORMAT_TIME_CN)
				|| isFormat(datestr, TIMESTAMP_FORMAT) || isFormat(datestr, TIMESTAMP_FORMAT_TWO)
				|| isFormat(datestr, TIMESTAMP_FORMAT_DATE_CN) || isFormat(datestr, TIMESTAMP_FORMAT_TIME_CN);
	}

	public static boolean isFormat(String datestr, String format) {
		Assert.hasText(datestr, " datestr must not be null ");
		Assert.hasText(format, " format must not be null ");
		SimpleDateFormat sdf = getDateFormat(format);
		// 这个的功能是不让系统自动转换 不把1996-13-3 转换为1997-3-1
		sdf.setLenient(false);
		try {
			sdf.parse(datestr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	public static void main(String[] args) {
		System.out.println(isDate("2016-05-5"));
		System.out.println("转换后的字符串:" + formatTime(1274812130218L, "yyyy-MM-dd HH:mm:ss"));

	}

}
