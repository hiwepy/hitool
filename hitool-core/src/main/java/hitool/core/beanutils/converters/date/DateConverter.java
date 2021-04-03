package hitool.core.beanutils.converters.date;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.Converter;

@SuppressWarnings({"unchecked","rawtypes"})
public class DateConverter implements Converter{
	
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
	
	protected String pattern = null; 
	protected Locale loc = null;
	protected int style = -1;
	protected Calendar cal = null;
	protected SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_LONG_DATE_FORMAT);
	

	/*
	 * 将指定的字符串转换成日期
	 * 
	 * @param dateStr
	 *            : 待转换的日期符串,以yyyy-MM-dd模板进行转换
	 * @return 返回标准的日期格式yyyy-MM-dd,与字符串dateStr对应的date对象
	 * @throws ParseStringException
	 */
	public Date parseStrToDate(String dateStr) throws ParseException {
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		}
	}
	

	/*
	 * 按照不同的格式模板将指定的字符串转换成日期。
	 * 
	 * @param date
	 *            : 待转换的日期符串
	 * @param pattern
	 *            : 字符串的格式模板,例如:yyyy-MM-dd HH:mm:ss
	 * @return 与字符串dateStr对应的date对象
	 * @throws ParseStringException
	 */
	public static Date parseStrToDate(String date, String pattern)
			throws ParseException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(date);
		} catch (ParseException e) {
			throw e;
		}
	}
	

	/*
	 * 将字符串转成时间Date
	 * 
	 * @throws ParseException
	 */
	public static Date changeStrToDate(String dateStr, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	/*
	 * 从ResultSet中的到查询结果的列
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public String[] getColNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String[] colNames = new String[count];
		for (int i = 1; i <= count; i++) {
			colNames[i - 1] = rsmd.getColumnLabel(i);
		}
		return colNames;
	}

	/*
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	
	/*
	 * 将指定格式的字符串转换成日期类型
	 * 
	 * @param date
	 *            待转换的日期字符串
	 * @param dateFormat
	 *            日期格式字符串
	 * @return
	 */
	public  Date convertStrToDate(String dateStr, String dateFormat) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	public static void main(String[] args) {
		DateConverter c = new DateConverter();
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


	public Calendar getCal() {
		return cal;
	}


	public void setCal(Calendar cal) {
		this.cal = cal;
	}


	public SimpleDateFormat getSdf() {
		return sdf;
	}


	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}




	@Override
	public Object convert(Class type, Object value) {
		
		 // TODO Auto-generated method stub return null;
		return null;
	}



	
	
}
