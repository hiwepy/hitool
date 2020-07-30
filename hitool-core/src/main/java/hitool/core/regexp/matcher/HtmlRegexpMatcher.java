package hitool.core.regexp.matcher;

/**
 *  使用正则表达式验证【网页相关字符串】或提取数据
 */
public class HtmlRegexpMatcher extends StringRegexpMatcher{

	public HtmlRegexpMatcher() {
		super("html");
	}

	public HtmlRegexpMatcher(int mask) {
		super("html", mask);
	}

	public String bulidRegexp(String tagName){
		return  "/<"+tagName+".*?</"+tagName+">/";
	}
	
}
