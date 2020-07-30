/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.converters.bean.BeanBeanConverter;
import hitool.core.beanutils.converters.bean.BeanMapConverter;
import hitool.core.beanutils.converters.collection.ListArrayConverter;
import hitool.core.beanutils.converters.collection.ListSetConverter;
import hitool.core.beanutils.converters.collection.SetArrayConverter;
import hitool.core.beanutils.converters.date.DateStringConverter;
import hitool.core.beanutils.converters.date.DateTimeStringConverter;
import hitool.core.beanutils.converters.number.EnumIntConverter;

public class BeanConvertUtils extends ConvertUtilsBean {
	
	/** Used to perform conversions between object types when setting properties */
    private static BeanConvertUtils convertUtils = new BeanConvertUtils();
    
    // ------------------------------------------------------- Class Methods
    /** Get singleton instance */
    protected static BeanConvertUtils getInstance() {
        return convertUtils;
    }
    
	public BeanConvertUtils() {
		super.deregister();
		
		this.register(new BeanBeanConverter(),BeanBeanConverter.class);
		this.register(new BeanMapConverter(),BeanMapConverter.class);
		this.register(new ListArrayConverter(),ListArrayConverter.class);
		this.register(new ListSetConverter(),ListSetConverter.class);
		this.register(new SetArrayConverter(),SetArrayConverter.class);
		this.register(new DateStringConverter(),DateStringConverter.class);
		this.register(new DateTimeStringConverter(),DateTimeStringConverter.class);
		this.register(new EnumIntConverter(),EnumIntConverter.class);
		
	}
	
	
	/**
	 * 
	 * 使用指定类型的转换器将对象转换成目标类型对象并返回
	 * @param <R>			转换器泛型
	 * @param <B>			待转换的对象泛型
	 * @param <T>			转换目标类型泛型
	 * @param converterType	转换器的类型class
	 * @param resource		待转换的对象
	 * @param targetType	目标类型
	 * @return  T 			转换后的类型
	 * @throws  
	 */
	public static <R, B ,T> T convert(Class<R> converterType,B resource,Class<T> targetType){
		if(null == resource){
			 throw new ConversionException("resource is not defined !");
		}
		Converter converter = ConvertUtilsBean.getInstance().lookup(converterType);
		if(null == converter){
			 throw new ConversionException("converter is not defined !");
		}
		return (T) converter.convert(targetType, resource);
	}
	
	public static <T> T convert(Object bean, String name,Class<T> targetClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException{
		return BeanConvertUtils.convert(bean, name, false,targetClass);
	}
	
	/**
	 * 从对象bean中取得字段名为name的字段值，并转换成指定类型targetClass
	 * @param bean 取值对象
	 * @param name 取值字段名称
	 * @param deep 是否递归赋值
	 * @param targetClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws IntrospectionException 
	 */
	public static <T> T convert(Object bean, String name, boolean deep,Class<T> targetClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException{
		Object value = BeanPropertyUtils.getProperty(bean, name, deep);
		return BeanConvertUtils.convert(value.getClass(),value,targetClass);
	}
	
}
