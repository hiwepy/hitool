package hitool.core.beanutils.converters.bean;

import java.util.Map;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.BeanPropertyUtils;
import hitool.core.beanutils.BeanUtils;

@SuppressWarnings({"unchecked","rawtypes"})
public class BeanMapConverter implements Converter {
	
	public Object convert(Class type, Object source) {
		if (source == null) {
            throw new ConversionException("No value specified");
        }
		//Map to JavaBean
		if (source instanceof Map){
			try {
	            Object bean = type.newInstance();
	            BeanUtils.populate(bean, (Map)source);
	            return bean;
	        } catch (Exception e) {
	        	throw new ConversionException(e);
	        }
		}
		//JavaBean to Map 
		if (source instanceof Map){
			try {
	            Object bean = type.newInstance();
	            BeanPropertyUtils.populate(bean,(Map)source,true);
	            return bean;
	        } catch (Exception e) {
	        	throw new ConversionException(e);
	        }
		}
		return null;
	}

}
