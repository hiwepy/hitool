package hitool.core.regexp.matcher;

/**
 * 使用正则表达式验证【身份证相关字符串】或提取数据
 */
public class SpecialRegexpMatcher extends StringRegexpMatcher {

	public SpecialRegexpMatcher() {
		super("special");
	}

	public SpecialRegexpMatcher(int mask) {
		super("special", mask);
	}
}
