package hitool.core.regexp.matcher;
/**
 * 使用正则表达式验证【时间相关字符串】或提取数据
 */
public class DateRegexpMatcher extends StringRegexpMatcher {

	public DateRegexpMatcher() {
		super("date");
	}

	public DateRegexpMatcher(int mask) {
		super("date", mask);
	}

	public String getYear(String dateStr) {
		return "";
	}

	public String getMonth(String dateStr) {
		return "";
	}

	public String getDay(String dateStr) {
		return "";
	}

	public String getHours(String dateStr) {
		return "";
	}

	public String getMinutes(String dateStr) {
		return "";
	}

	public String getSeconds(String dateStr) {
		return "";
	}

	public String getMilliseconds(String dateStr) {
		return "";
	}

}
