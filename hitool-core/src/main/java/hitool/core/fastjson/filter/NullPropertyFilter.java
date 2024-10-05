/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.fastjson.filter;


import com.alibaba.fastjson2.filter.PropertyFilter;

import java.util.Objects;

public class NullPropertyFilter implements PropertyFilter {

	/*
	 * 过滤不需要被序列化的属性
	 * @param source 属性所在的对象
	 * @param name 属性名
	 * @param value 属性值
	 * @return 返回false属性将被忽略，ture属性将被保留
	 */
	@Override
	public boolean apply(Object source, String name, Object value) {
		//所属对象和属性值均不能为空
		Objects.isNull(source);

		return (source != null && value != null  && !"null".equalsIgnoreCase(String.valueOf(value))) ?  true : false;
		
	}

}
