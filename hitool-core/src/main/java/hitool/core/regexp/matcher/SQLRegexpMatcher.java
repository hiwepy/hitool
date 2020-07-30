package hitool.core.regexp.matcher;

/**
 * 使用正则表达式验证【SQL语句相关字符串】或提取数据
 */
public class SQLRegexpMatcher extends StringRegexpMatcher{

	public SQLRegexpMatcher() {
		super("sql");
	}

	public SQLRegexpMatcher(int mask) {
		super("sql", mask);
	}
}
