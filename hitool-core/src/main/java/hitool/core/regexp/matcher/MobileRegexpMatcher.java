package hitool.core.regexp.matcher;

/**
 * 使用正则表达式验证【通讯号码相关字符串】或提取数据
 */
public class MobileRegexpMatcher extends StringRegexpMatcher{

	public MobileRegexpMatcher() {
		super("mobile");
	}

	public MobileRegexpMatcher(int mask) {
		super("mobile", mask);
	}

}
