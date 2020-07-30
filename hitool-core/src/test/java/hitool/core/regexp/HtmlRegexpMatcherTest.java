package hitool.core.regexp;

import junit.framework.TestCase;

import hitool.core.regexp.matcher.HtmlRegexpMatcher;

public class HtmlRegexpMatcherTest extends TestCase{

	public void testHtmlRegexpMatcher() {
		HtmlRegexpMatcher r = new HtmlRegexpMatcher();
		String str = "<body>sdasds</body>";
		System.out.println(str.matches(r.bulidRegexp("body")));
	}
	
}
