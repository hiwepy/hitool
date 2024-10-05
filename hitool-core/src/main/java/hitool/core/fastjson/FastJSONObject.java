/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.fastjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.PropertyFilter;
import hitool.core.fastjson.filter.NullPropertyFilter;

/*
 <p>JSONWriter.Feature属性</p>
 <table style="height:800px; width:744px" cellspacing="1" cellpadding="1" border="1"> 
	 <tbody> 
	  <tr> 
	   <th>名称</th> 
	   <th>含义</th> 
	   <th>支持版本</th> 
	  </tr> 
	 </tbody> 
	 <tbody> 
	  <tr> 
	   <td>QuoteFieldNames</td> 
	   <td>输出key时是否使用双引号,默认为true</td> 
	   <td>&nbsp;</td> 
	  </tr> 
	  <tr> 
	   <td>UseSingleQuotes</td> 
	   <td>使用单引号而不是双引号,默认为false</td> 
	   <td>&nbsp;</td> 
	  </tr> 
	  <tr> 
	   <td>WriteMapNullValue</td> 
	   <td>是否输出值为null的字段,默认为false</td> 
	   <td>&nbsp;</td> 
	  </tr> 
	  <tr> 
	   <td>WriteEnumUsingToString</td> 
	   <td> <p>Enum输出name()或者original,默认为false</p> <p>用枚举toString()输出</p> </td> 
	   <td>&nbsp;</td> 
	  </tr> 
	  <tr> 
	   <td>WriteEnumUsingName</td> 
	   <td>用枚举name()输出</td> 
	  </tr> 
	  <tr> 
	   <td>UseISO8601DateFormat</td> 
	   <td>Date使用ISO8601格式输出，默认为false</td> 
	   <td>&nbsp;</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNullListAsEmpty</td> 
	   <td>List字段如果为null,输出为[],而非null</td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNullStringAsEmpty</td> 
	   <td>字符类型字段如果为null,输出为”“,而非null</td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNullNumberAsZero</td> 
	   <td>数值字段如果为null,输出为0,而非null</td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNullBooleanAsFalse</td> 
	   <td>Boolean字段如果为null,输出为false,而非null</td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>SkipTransientField</td> 
	   <td> <p>如果是true，类中的Get方法对应的Field是transient</p> <p>，序列化时将会被忽略。默认为true</p> </td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>SortField</td> 
	   <td>按字段名称排序后输出。默认为false</td> 
	   <td>1.1</td> 
	  </tr> 
	  <tr> 
	   <td>WriteTabAsSpecial</td> 
	   <td>把\t做转义输出，默认为false</td> 
	   <td>1.1.1 （不推荐）</td> 
	  </tr> 
	  <tr> 
	   <td>PrettyFormat</td> 
	   <td>结果是否格式化,默认为false</td> 
	   <td>1.1.2</td> 
	  </tr> 
	  <tr> 
	   <td>WriteClassName</td> 
	   <td>序列化时写入类型信息，默认为false。反序列化是需用到</td> 
	   <td>1.1.2</td> 
	  </tr> 
	  <tr> 
	   <td>DisableCircularReferenceDetect</td> 
	   <td>消除对同一对象循环引用的问题，默认为false</td> 
	   <td>1.1.6</td> 
	  </tr> 
	  <tr> 
	   <td>WriteSlashAsSpecial</td> 
	   <td>对斜杠’/’进行转义</td> 
	   <td>1.1.9</td> 
	  </tr> 
	  <tr> 
	   <td>BrowserCompatible</td> 
	   <td> <p>将中文都会序列化为\\uXXXX格式，字节数会多一些</p> <p>，但是能兼容IE 6，默认为false</p> </td> 
	   <td>1.1.10</td> 
	  </tr> 
	  <tr> 
	   <td>WriteDateUseDateFormat</td> 
	   <td> <p>全局修改日期格式,默认为false。</p> </td> 
	   <td>1.1.14</td> 
	  </tr> 
	  <tr> 
	   <td>NotWriteRootClassName</td> 
	   <td>&nbsp;</td> 
	   <td>1.1.15</td> 
	  </tr> 
	  <tr> 
	   <td>DisableCheckSpecialChar</td> 
	   <td> <p>一个对象的字符串属性中如果有特殊字符如双引号，</p> <p>将会在转成json时带有反斜杠转移符。如果不需要转义，</p> <p>可以使用这个属性。默认为false</p> </td> 
	   <td>1.1.19</td> 
	  </tr> 
	  <tr> 
	   <td>BeanToArray</td> 
	   <td>将对象转为array输出</td> 
	   <td>1.1.35</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNonStringKeyAsString</td> 
	   <td> <p>Map&lt;Integer,Stirng&gt; 输出 key 默认为 Integer</p> <p>添加此属性，Integer 添加了 “”，变成字符型</p> </td> 
	   <td>1.1.37</td> 
	  </tr> 
	  <tr> 
	   <td>NotWriteDefaultValue</td> 
	   <td>将基础类型的默认值屏蔽</td> 
	   <td>1.1.42</td> 
	  </tr> 
	  <tr> 
	   <td>BrowserSecure</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.6</td> 
	  </tr> 
	  <tr> 
	   <td>IgnoreNonFieldGetter</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.7</td> 
	  </tr> 
	  <tr> 
	   <td>WriteNonStringValueAsString</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.9</td> 
	  </tr> 
	  <tr> 
	   <td>IgnoreErrorGetter</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.11</td> 
	  </tr> 
	  <tr> 
	   <td>WriteBigDecimalAsPlain</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.16</td> 
	  </tr> 
	  <tr> 
	   <td>MapSortField</td> 
	   <td>&nbsp;</td> 
	   <td>1.2.27</td> 
	  </tr> 
	 </tbody> 
	</table>
 */
@SuppressWarnings("serial")
public class FastJSONObject extends JSONObject {

	/*private static final SerializeConfig   config;
	static {
	    config = new SerializeConfig();
	    config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	    config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	}*/

	/*
	 * 空字段过滤器
	 */
	public static PropertyFilter NULL_PROPERTY_FILTER = new NullPropertyFilter();
	
	protected static JSONWriter.Feature[] PARSER_FEATURES = {

			JSONWriter.Feature.IgnoreNonFieldGetter,
			JSONWriter.Feature.IgnoreErrorGetter
        
	};
	
	/*
	 * 默认json格式化方式
	 */
	protected static  JSONWriter.Feature[] GENERATE_FEATURES = { 
		
		JSONWriter.Feature.UnquoteFieldName,
		JSONWriter.Feature.SortMapEntriesByKeys,
		JSONWriter.Feature.PrettyFormat,
		
		// 输出空置字段
		JSONWriter.Feature.WriteMapNullValue,

		//用枚举name()输出
		JSONWriter.Feature.WriteEnumsUsingName,
	    
		//list字段如果为null，输出为[]，而不是null
        JSONWriter.Feature.WriteNullListAsEmpty,

        //字符类型字段如果为null，输出为""，而不是null
        JSONWriter.Feature.WriteNullStringAsEmpty,
        
        //数值字段如果为null，输出为0，而不是null
        JSONWriter.Feature.WriteNullNumberAsZero,

        //Boolean字段如果为null，输出为false，而不是null
        JSONWriter.Feature.WriteNullBooleanAsFalse,

        JSONWriter.Feature.WriteNonStringKeyAsString,
        JSONWriter.Feature.WriteNonStringValueAsString,
        
        JSONWriter.Feature.WriteBigDecimalAsPlain,
        
        
        //如果是true，类中的Get方法对应的Field是transient，序列化时将会被忽略。默认为true
		//JSONWriter.Feature..SkipTransientField,
        
        //消除对同一对象循环引用的问题，默认为false
		// JSONWriter.Feature.ReferenceDetection,

		//使用指定的格式格式化日期类型对象
	    // JSONWriter.Feature.WriteDateUseDateFormat
		// JSONWriter.Feature.DisableCircularReferenceDetect,
        
        //使用指定的格式格式化日期类型对象
       	// JSONWriter.Feature.WriteDateUseDateFormat,
		JSONWriter.Feature.IgnoreNonFieldGetter,
		JSONWriter.Feature.IgnoreErrorGetter
        
        
	};
	
	static {
		JSON..DEFFAULT_DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";
	}
	
	public static <T> String defaults(String key,String value) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("key", key);
		map.put("text", value);
		return JSONObject.toJSONString(map);
	}
	
	public static <T> String toCleanJSONString(T element) {
		return FastJSONObject.toCleanJSONString(element,true);
	}
	
	public static <T> String toCleanJSONString(T element,boolean notEmpty) {
		return notEmpty?JSONObject.toJSONString(element, GENERATE_FEATURES): JSONObject.toJSONString(element);
	}
	
	public static <T> String toCleanJSONString(List<T> list) {
		return FastJSONObject.toCleanJSONString(list,true);
	}
	
	public static <T> String toCleanJSONString(List<T> list,boolean notEmpty) {
		if(list==null){	return null;}
		return notEmpty?JSONArray.toJSONString(list, GENERATE_FEATURES): JSONArray.toJSONString(list);
	}
	
	public static <T> String toPageJSONString(List<T> list) {
		return FastJSONObject.toPageJSONString(list,true);
	}
	
	public static <T> String toPageJSONString(List<T> list,boolean notEmpty) {
		if(list==null){	return null;}
		JSONObject pageObj = new JSONObject();
		pageObj.put("totalCount", list.size());
		pageObj.put("result", notEmpty?JSONArray.toJSONString(list, GENERATE_FEATURES): JSONArray.toJSONString(list));
		return pageObj.toString();
	}
	
	public static <T> String toPageJSONString(List<T> list, int count) {
		return FastJSONObject.toPageJSONString(list,count,true);
	}
	
	public static <T> String toPageJSONString(List<T> list, int count,boolean notEmpty) {
		if(list==null){return null;}
		JSONObject pageObj = new JSONObject();
		pageObj.put("totalCount", count);
		pageObj.put("result", notEmpty?JSONArray.toJSONString(list, GENERATE_FEATURES): JSONArray.toJSONString(list));
		return pageObj.toString();
	}
	
	public static <T> T toBean(String jsonStr, Class<T> clazz){
		return JSONObject.toJavaObject((JSONObject)JSONObject.parse(jsonStr), clazz);
	}

	
	public static <T> List<T> toBeans(String jsonStr, String key, Class<T> clazz){
		JSONObject parseObject = JSONObject.parseObject(jsonStr);
		JSONArray jsonArray = parseObject.getJSONArray(key);
		List<T> list = new ArrayList<T>();
		for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext();) {
			JSONObject object = (JSONObject) iterator.next();
			T javaObject = JSONObject.toJavaObject(object, clazz);
			list.add(javaObject);
		}
		return list;
	}
	
}