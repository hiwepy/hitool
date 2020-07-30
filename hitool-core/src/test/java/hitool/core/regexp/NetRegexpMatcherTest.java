package hitool.core.regexp;

import junit.framework.TestCase;

import hitool.core.regexp.matcher.NetRegexpMatcher;

public class NetRegexpMatcherTest extends TestCase{

	public void testNetRegexpMatcher() {
		NetRegexpMatcher regex = new NetRegexpMatcher();
		String REST_URL = "/dev/action/method/#/#/#/#/#/#.action";
		System.out.println(regex.matches(REST_URL, "REST_URL"));
	}
	
}
