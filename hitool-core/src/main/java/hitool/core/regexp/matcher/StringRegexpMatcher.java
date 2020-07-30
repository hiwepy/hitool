package hitool.core.regexp.matcher;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import hitool.core.lang3.StringUtils;
import hitool.core.regexp.JakartaOROUtils;
import hitool.core.regexp.RegexpPatternUtils;

/**
 * 使用正则表达式验证【普通字符串】或提取数据
 */
public class StringRegexpMatcher implements hitool.core.regexp.PatternMatcher {

	protected String type = "normal";
	/* 
     * CASE_INSENSITIVE_MASK : 区分大小写 
     * DEFAULT_MASK : 默认(不区分大小写) 
     * EXTENDED_MASK : 支持Perl5 扩展正则表达式 
     * MULTILINE_MASK : 多行匹配，^$匹配每行内容． 
     * SINGLELINE_MASK　：单行匹配  ^$匹配全部内容. 
     * READ_ONLY_MASK : Perl5Pattern 是只读的，提高性能且线程安全． 
     */  
	protected int mask = Perl5Compiler.CASE_INSENSITIVE_MASK;
	
	// 正规表达式比较匹配对象
	protected PatternMatcher matcher = new Perl5Matcher();
	
	public StringRegexpMatcher(){
		this.type = "normal";
	}
	
	protected StringRegexpMatcher(String type){
		this.type = type;
	}
	
	protected StringRegexpMatcher(String type,int mask){
		this.type = type;
		this.mask = mask;
	}

	@Override
	public boolean contains(String input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.contains(regexp, mask, input);
	}
	
	@Override
	public boolean contains(PatternMatcherInput input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.contains(regexp, mask, input);
	}

	@Override
	public boolean matches(String input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.matches(regexp, mask, input);
	}
	
	@Override
	public boolean matches(PatternMatcherInput input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.matches(regexp, mask, input);
	}
	
	@Override
	public boolean matchesPrefix(String input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.matchesPrefix(regexp, mask, input);
	}

	@Override
	public boolean matchesPrefix(PatternMatcherInput input, String regexName) {
		String regexp = RegexpPatternUtils.getRegexp(type, regexName);
		if(!StringUtils.hasText(regexp)){
			return false;
		}
		// 返回匹配结果
		return JakartaOROUtils.matchesPrefix(regexp, mask, input);
	}
	
	@Override
	public MatchResult getMatchResult(String input, String regexName) {
		MatchResult matchResult = null;
		String regexp = RegexpPatternUtils.getRegexp(type,regexName);
		if(StringUtils.hasText(regexp)){
			// 返回匹配结果
			matchResult = JakartaOROUtils.getMatchResult(regexp, mask, input);
		}
		return matchResult;
	}
	
	@Override
	public MatchResult getMatchResult(PatternMatcherInput input,String regexName) {
		MatchResult matchResult = null;
		String regexp = RegexpPatternUtils.getRegexp(type,regexName);
		if(StringUtils.hasText(regexp)){
			// 返回匹配结果
			matchResult = JakartaOROUtils.getMatchResult(regexp, mask, input);
		}
		return matchResult;
	}
	
	public String replaces(String input,String regexName){
		String output = input;
		String regexp = RegexpPatternUtils.getRegexp(type,regexName);
		if(StringUtils.hasText(regexp)){
			// 返回替换后的结果
			output = JakartaOROUtils.replaces(regexp, mask, input);
		}
		return output;
	}
	
	public String replaces(PatternMatcherInput input, String regexName){
		String output = input.toString();
		String regexp = RegexpPatternUtils.getRegexp(type,regexName);
		if(StringUtils.hasText(regexp)){
			// 返回替换后的结果
			output = JakartaOROUtils.replaces(regexp, mask, input);
		}
		return output;
	}
	
}
