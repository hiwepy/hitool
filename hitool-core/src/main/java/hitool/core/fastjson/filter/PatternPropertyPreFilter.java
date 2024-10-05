/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.fastjson.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson2.serializer.JSONSerializer;
import com.alibaba.fastjson2.serializer.PropertyPreFilter;
import com.alibaba.fastjson2.serializer.SerialContext;

public class PatternPropertyPreFilter implements PropertyPreFilter {

    private final Class<?>    clazz;
    private final List<Pattern> includes = new ArrayList<Pattern>();
    private final List<Pattern> excludes = new ArrayList<Pattern>();
    private int               maxLevel = 0;

    public PatternPropertyPreFilter(Pattern... includePatterns){
        this(null, includePatterns);
    }

    public PatternPropertyPreFilter(Class<?> clazz, Pattern... includePatterns){
        this(clazz, null, Arrays.asList(includePatterns));
    }
    
    public PatternPropertyPreFilter(Class<?> clazz, 
    		List<Pattern> excludePatterns,
    		List<Pattern> includePatterns){
        super();
        this.clazz = clazz;
        
        if( null != excludePatterns ){
        	for (Pattern item : excludePatterns) {
                if (item != null) {
                    this.excludes.add(item);
                }
            }
        }
        
        if( null != includePatterns ){
        	for (Pattern item : includePatterns) {
                if (item != null) {
                    this.includes.add(item);
                }
            }
        }
        
    }
    
    /*
     * @since 1.2.9
     */
    public int getMaxLevel() {
        return maxLevel;
    }
    
    /*
     * @since 1.2.9
     */
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public List<Pattern> getIncludes() {
        return includes;
    }

    public List<Pattern> getExcludes() {
        return excludes;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return true;
        }
        
        for(Iterator<Pattern> it = this.excludes.iterator(); it.hasNext(); ) {
        	Matcher m = it.next().matcher(name);
        	if(m.matches()){
        		return false;
        	}
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

        if (includes.size() == 0 ) {
            return true;
        }
        
        for(Iterator<Pattern> it = this.includes.iterator(); it.hasNext(); ) {
        	Matcher m = it.next().matcher(name);
        	if(m.matches()){
        		return true;
        	}
        }
        
        return false;
    }

}
