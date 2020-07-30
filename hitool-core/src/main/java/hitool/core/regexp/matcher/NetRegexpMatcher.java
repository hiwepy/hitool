package hitool.core.regexp.matcher;

/**
 * 使用正则表达式验证【网络相关字符串】或提取数据
 */
public class NetRegexpMatcher extends StringRegexpMatcher{

	public NetRegexpMatcher() {
		super("net");
	}

	public NetRegexpMatcher(int mask) {
		super("net", mask);
	}

}
