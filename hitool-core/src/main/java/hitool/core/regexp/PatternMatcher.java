package hitool.core.regexp;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcherInput;
/*
 * 
 *正则匹配封装接口
 *<pre>
 *1.matches 当输入字符串和正则表达式要精确匹配时使用。换句话说，正则表达式必须完整地描述输入字符串。
 *2.matchesPrefix 当正则表达式匹配输入字符串起始部分时使用。
 *3.contains 当正则表达式要匹配输入字符串的一部分时使用（即，它必须是一个子串）。</pre>
 */
public interface PatternMatcher {
	
	/*
	 * 正规表达式匹配
	 * @param source 匹配的源字符串
	 * @param regexName 匹配的正规表达式在配置文件中的键
	 * @return 如果源字符串符合要求返回true,否则返回false
	 */
	public boolean contains(String source, String regexName);
	public boolean contains(PatternMatcherInput input, String regexName);
	
	
	public boolean matches(String source, String regexName);
	public boolean matches(PatternMatcherInput input, String regexName);
	public boolean matchesPrefix(String source, String regexName);
	public boolean matchesPrefix(PatternMatcherInput input, String regexName);
	
	/*
	 * 
	 *返回需要的匹配结果集
	 */
	public MatchResult getMatchResult(String source, String regexName) ;
	public MatchResult getMatchResult(PatternMatcherInput input, String regexName) ;

	public String replaces(String source, String regexName);
	public String replaces(PatternMatcherInput input, String regexName);
	
}
