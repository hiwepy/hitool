/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.fastjson.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson2.serializer.PropertyFilter;

public class ExcludePropertyFilter implements PropertyFilter {

	 private Map<Class<?>, Set<String>> excludeMap = new HashMap<Class<?>, Set<String>>();  
	  
     public ExcludePropertyFilter(Map<Class<?>, Set<String>> excludeMap){  
         this.excludeMap = excludeMap;  
     }
     
     /*
 	 * 过滤不需要被序列化的属性
 	 * @param source 属性所在的对象
 	 * @param name 属性名
 	 * @param value 属性值
 	 * @return 返回false属性将被忽略，ture属性将被保留
 	 */
     public boolean apply(Object source, String name, Object value) {  
         for(Entry<Class<?>, Set<String>> entry : excludeMap.entrySet()) {  
             Class<?> class1 = entry.getKey();  
             if(source.getClass() == class1){  
                 Set<String> fields = entry.getValue();  
                 for(String field : fields) {  
                     if(field.equals(name)){  
                         return false;  
                     }
                 }  
             }  
         }
         return true;  
     }  

}
