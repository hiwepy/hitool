/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionNumberMask extends VersionNumber {
	
	private static final Pattern _regexp = Pattern.compile("(\\d+)(\\.\\d+)?(\\.\\d+)?([-\\.].+)?");

	public VersionNumberMask(String s) {
		Matcher match = _regexp.matcher(s);
		if (!match.find()) {
			throw new IllegalArgumentException("invalid versionNumber : major.minor(.bugfix)(modifier) :" + s);
		}
		major = Integer.parseInt(match.group(1));
		minor = -1;
		if ((match.group(2) != null) && (match.group(2).length() > 1)) {
			minor = Integer.parseInt(match.group(2).substring(1));
		}
		bugfix = -1;
		if ((match.group(3) != null) && (match.group(3).length() > 1)) {
			bugfix = Integer.parseInt(match.group(3).substring(1));
		}
		modifier = null;
		if ((match.group(4) != null) && (match.group(4).length() > 1)) {
			modifier = match.group(4);
		}
	}

	/**
	 * Doesn't compare modifier
	 */
	@Override
	public int compareTo(VersionNumber o) {
		int back = 0;
		if ((back == 0) && (major > o.major)) {
			back = 1;
		}
		if ((back == 0) && (major < o.major)) {
			back = -1;
		}
		if ((back == 0) && (minor > -1) && (minor > o.minor)) {
			back = 1;
		}
		if ((back == 0) && (minor > -1) && (minor < o.minor)) {
			back = -1;
		}
		if ((back == 0) && (bugfix > -1) && (bugfix > o.bugfix)) {
			back = 1;
		}
		if ((back == 0) && (bugfix > -1) && (bugfix < o.bugfix)) {
			back = -1;
		}
		return back;
	}

}