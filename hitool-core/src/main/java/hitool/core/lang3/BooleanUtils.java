/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.lang3;

public class BooleanUtils extends org.apache.commons.lang3.BooleanUtils{

	public static boolean parse(String str){
		//如果此字段为必选项,则验证此字段对应的列是否为空
		if(str!=null){
			if("yes".equalsIgnoreCase(str)||"true".equalsIgnoreCase(str)||"1".equalsIgnoreCase(str)){
				return true;
			}else if("no".equalsIgnoreCase(str)||"false".equalsIgnoreCase(str)||"0".equalsIgnoreCase(str)){
				return false;
			}else{
				throw new RuntimeException(str + " can't convert to a boolean value!");
			}
		}else{
			throw new NullPointerException(str + " can't a null value!");
		}
	}
	
	public static void main(String[] args) {
		System.out.println(BooleanUtils.parse("0"));
	}
	
}



