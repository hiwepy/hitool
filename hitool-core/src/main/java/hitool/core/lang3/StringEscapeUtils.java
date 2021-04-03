/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringEscapeUtils extends org.apache.commons.lang3.StringEscapeUtils {

	
	/*
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (StringUtils.isEmpty(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	
	/*
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html){
		if (html == null){
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	/*
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt){
		if (txt == null){
			return "";
		}
		return StringUtils.replace(StringUtils.replace(escapeHtml4(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}
	
}
