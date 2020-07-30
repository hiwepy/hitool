package hitool.core.regexp.factory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import hitool.core.regexp.PatternMatcher;
import hitool.core.regexp.matcher.DateRegexpMatcher;
import hitool.core.regexp.matcher.HtmlRegexpMatcher;
import hitool.core.regexp.matcher.MathRegexpMatcher;
import hitool.core.regexp.matcher.MobileRegexpMatcher;
import hitool.core.regexp.matcher.NetRegexpMatcher;
import hitool.core.regexp.matcher.SQLRegexpMatcher;
import hitool.core.regexp.matcher.StringRegexpMatcher;

public class PatternMatcherFactory {

	protected static ConcurrentMap<String, PatternMatcher> COMPLIED_MATCHER = new ConcurrentHashMap<String, PatternMatcher>();
	private volatile static PatternMatcherFactory singleton;

	public static PatternMatcherFactory getInstance() {
		if (singleton == null) {
			synchronized (PatternMatcherFactory.class) {
				if (singleton == null) {
					singleton = new PatternMatcherFactory();
				}
			}
		}
		return singleton;
	}
	
	private PatternMatcherFactory() {
		deregister();
	}

	public void deregister() {
		COMPLIED_MATCHER.clear();
		register("normal", new StringRegexpMatcher());
		register("date", new DateRegexpMatcher());
		register("html", new HtmlRegexpMatcher());
		register("math", new MathRegexpMatcher());
		register("mobile", new MobileRegexpMatcher());
		register("net", new NetRegexpMatcher());
		register("sql", new SQLRegexpMatcher());
	}

	public void deregister(String type) {
		COMPLIED_MATCHER.remove(type);
	}

	public PatternMatcher lookup(String type) {
		return ((PatternMatcher) COMPLIED_MATCHER.get(type));
	}

	public void register(String type, PatternMatcher matcher) {
		COMPLIED_MATCHER.put(type, matcher);
	}

	public boolean matches(String input, String regexpName) {
		Iterator<String> ite = COMPLIED_MATCHER.keySet().iterator();
		boolean have = false;
		while (ite.hasNext() && !have) {
			PatternMatcher matcher = lookup(ite.next());
			if (null != matcher) {
				have = matcher.matches(input, regexpName);
				if(have){
					return true;
				}
			}
		}
		return have;
	}

}
