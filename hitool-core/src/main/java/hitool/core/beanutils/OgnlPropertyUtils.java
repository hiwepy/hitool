/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hitool.core.beanutils.exception.PropertyGettingException;
import hitool.core.beanutils.exception.PropertySettingException;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;


@SuppressWarnings({ "unchecked", "unused" })
@Slf4j
public abstract class OgnlPropertyUtils {

	/*
	 * Logging for this instance
	 */
	public static Object getProperty(Object bean, String expression){
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (expression == null)) {
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug("OgnlBeanUtils.getProperty(" + bean.getClass() + ", " + expression + ")");
		}
		try { 
			if(bean instanceof OgnlContext){
				//调用OgnlRuntime的方法判断是否包含该属性
				return Ognl.getValue(Ognl.parseExpression(expression), (OgnlContext) bean, new Object());
			}else{
				OgnlContext context = (OgnlContext) Ognl.createDefaultContext(bean);
				//调用OgnlRuntime的方法判断是否包含该属性
				return Ognl.getValue(Ognl.parseExpression(expression),context,bean);
			}
		} catch (OgnlException e) {
			if(e.getReason() != null){
				String msg = "Caught an Ognl exception while getting property {0} !";
				throw new PropertyGettingException(msg, e.getCause(), new String[]{expression});
			}
		}
		return null;
	}
	
	public static Object getProperty(Object bean, String expression,Class reusltType){
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (expression == null)) {
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug("OgnlBeanUtils.getProperty(" + bean + ", " + expression +", " + reusltType + ")");
		}
		try { 
			if(bean instanceof OgnlContext){
				//调用OgnlRuntime的方法判断是否包含该属性
				return Ognl.getValue(Ognl.parseExpression(expression), (OgnlContext)bean,new Object(),reusltType);
			}else{
				OgnlContext context = Ognl.createDefaultContext(bean);
				//调用OgnlRuntime的方法判断是否包含该属性
				return Ognl.getValue(Ognl.parseExpression(expression),context,bean,reusltType);
			}
		} catch (OgnlException e) {
			if(e.getReason() != null){
				String msg = "Caught an Ognl exception while getting property {0} !";
				throw new PropertyGettingException(msg, e.getCause(), new String[]{expression});
			}
		}
		return null;
	}
	
	public static void setProperty(Object bean, String expression, Object value) {
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (expression == null)) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("OgnlBeanUtils.setProperty(" + bean + ", " + expression +", " + value + ")");
		}
		try {
			if(bean instanceof OgnlContext){
				//调用OgnlRuntime的方法判断是否包含该属性
				Ognl.setValue(Ognl.parseExpression(expression),(OgnlContext) bean,new Object(),value);
			}else{
				//调用OgnlRuntime的方法判断是否包含该属性
				Ognl.setValue(Ognl.parseExpression(expression),Ognl.createDefaultContext(bean),bean,value);
			}
		} catch (OgnlException e) {
			e.printStackTrace();
			if(e.getReason() != null){
				String msg = "Caught an Ognl exception while getting property {0} !";
				throw new PropertySettingException(msg, e.getCause(), new String[]{expression});
			}
			
		}
	}

	/*
	 * <b>获取对象bean的name属性的数组的结果表示</b>
	 * 	1.Array类型则转换成 Object[]
	 * 	2.Collection类型则逐个取值后放到Object[]
	 * 	3.普通的类型放到Object[]
	 * @param bean
	 * @param expression
	 * @return
	 */
	public static Object[] getArrayProperty(Object bean, String expression){
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (expression == null)) {
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug("JavaBeanUtils.getArrayProperty(" + bean + ", " + expression + ")");
		}
		Object value = OgnlPropertyUtils.getProperty(bean, expression);
		if (value == null) {
			return (null);
		} else if (value instanceof Collection) {
			ArrayList<Object> values = new ArrayList<Object>();
			Iterator<Object> items = ((Collection<Object>) value).iterator();
			while (items.hasNext()) {
				Object item = items.next();
				if (item == null) {
					values.add(null);
				} else {
					values.add(item);
				}
			}
			return values.toArray(new Object[values.size()]);
		} else if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			Object results[] = new Object[length];
			for (int i = 0; i < length; i++) {
				Object item = Array.get(value, i);
				if (item == null) {
					results[i] = null;
				} else {
					results[i] = item;
				}
			}
			return (results);
		}  else {
			return (new Object[]{value});
		}
	}

	public static void populate(Object bean, Map<String,Object> properties) {
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (properties == null)) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("OgnlBeanUtils.populate(" + bean + ", " + properties + ")");
		}
		// Loop through the property name/value pairs to be set
		Iterator<String> names = properties.keySet().iterator();
		while (names.hasNext()) {

			// Identify the property name and value(s) to be assigned
			String expression = names.next();
			if (expression == null) {
				continue;
			}
			
			Object value = properties.get(expression);
			// Perform the assignment for this property
			OgnlPropertyUtils.setProperty(bean, expression, value);
		}
	}
}
