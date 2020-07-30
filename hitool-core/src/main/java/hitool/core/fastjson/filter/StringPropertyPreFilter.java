/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.fastjson.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;

public class StringPropertyPreFilter implements PropertyPreFilter {

    private final Class<?>    clazz;
    private final List<String> includes = new ArrayList<String>();
    private final List<String> excludes = new ArrayList<String>();
    private int               maxLevel = 0;

    public StringPropertyPreFilter(String... includeProperties){
        this(null, includeProperties);
    }

    public StringPropertyPreFilter(Class<?> clazz, String... includeProperties){
        this(clazz, null, Arrays.asList(includeProperties));
    }
    
    public StringPropertyPreFilter(Class<?> clazz, 
    		List<String>  excludeProperties,
    		List<String> includeProperties){
        super();
        this.clazz = clazz;
        
        if( null != excludeProperties ){
        	for (String item : excludeProperties) {
                if (item != null) {
                    this.excludes.add(item);
                }
            }
        }
        
        if( null != includeProperties ){
        	for (String item : includeProperties) {
                if (item != null) {
                    this.includes.add(item);
                }
            }
        }
        
    }
    
    /**
     * @since 1.2.9
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /**
     * @since 1.2.9
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }

        if (this.excludes.contains(name)) {
            return false;
        }
        
        if (maxLevel > 0) {
            int level = 0;
            SerialContext context = serializer.getContext();
            while (context != null) {
                level++;
                if (level > maxLevel) {
                    return false;
                }
                context = context.parent;
            }
        }

        if (includes.size() == 0 || includes.contains(name)) {
            return true;
        }
        
        return false;
    }

}
