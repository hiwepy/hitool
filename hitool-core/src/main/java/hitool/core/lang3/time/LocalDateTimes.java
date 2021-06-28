package hitool.core.lang3.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class LocalDateTimes {

	private LocalDateTimes() {
	}

	public static String formatYMD(LocalDateTime date) {
		return format(date, DateFormats.YYYYMMDD);
	}

	public static String formatYMDHMS(LocalDateTime date) {
		return format(date, DateFormats.YYYYMMDDHHMMSS);
	}

	public static String format(LocalDateTime date, String pattern) {
		return date == null ? null : getStringFormat(date, pattern);
	}

	public static LocalDateTime format(String date, String pattern) {
		return date == null ? null : getDateFormat(date, pattern);
	}

	/**
	 * 日期解析字符串
	 * 
	 * @return String
	 */
	public static String getStringFormat(LocalDateTime date, String pattern) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return date.format(format);
	}

	/**
	 * 字符串解析日期
	 * 
	 * @return LocalDateTime
	 */
	public static LocalDateTime getDateFormat(String date, String pattern) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(date, format);
	}
	
	/**
	 * @return 当天开始时间 00:00
	 */
	public static LocalDateTime firstTimeOfDay() {
		return LocalDate.now().atStartOfDay();
	}

	/**
	 * @return 当天最后时间 23:59:59
	 */
	public static LocalDateTime lastTimeOfDay() {
		return LocalDate.now().atTime(23, 59, 59);
	}

	/**
	 * @return 明天日期
	 */
	public static LocalDate getTomorrow() {
		return LocalDate.now().plusDays(1);
	}

	/**
	 * @return 本月第一天
	 */
	public static LocalDate firstDayOfThisMonth() {
		return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * @return 本月最后一天
	 */
	public static LocalDate lastDayOfMonth() {
		return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
     * 判断当前日期是否在两个日期期间内
     * @param before
     * @param after
     * @return true or false
     */
    public static boolean twoDatePeriod(LocalDateTime before,LocalDateTime after){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(before) && now.isBefore(after);
    }

}
