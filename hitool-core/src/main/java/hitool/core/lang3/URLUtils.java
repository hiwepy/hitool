/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtils {

	protected static final String DEFAULT_URL_ENCODING = "UTF-8";
	//验证规则
    protected static String regEx = ".*%[0-9A-F]{2}.*";
    //编译正则表达式
    protected static Pattern pattern = Pattern.compile(regEx);
    
	/*
	 * 判断字串是否经过URLEncoder编码(正则判断字串是否匹配"%xy"，其中xy是两位16进制的数值)
	 */
	public static boolean isURLEncoder(String str){
		if(str == null || str.length() == 0){
			return false;
		}else{
		    Matcher matcher = pattern.matcher(str);
		    // 字符串是否与正则表达式相匹配
		    return matcher.matches();		    
		}
	}
	
    public static String escape(String name) {
        String ret = "";

        try {
            ret = URLEncoder.encode(name, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String unescape(String name) {
        String ret = "";

        try {
            ret = URLDecoder.decode(name, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
