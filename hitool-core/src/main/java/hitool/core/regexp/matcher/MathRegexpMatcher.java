package hitool.core.regexp.matcher;

/**
 * 使用正则表达式验证【数字字符串】或提取数据
 */
public class MathRegexpMatcher extends StringRegexpMatcher{

	public MathRegexpMatcher() {
		super("math");
	}

	public MathRegexpMatcher(int mask) {
		super("math", mask);
	}
}
