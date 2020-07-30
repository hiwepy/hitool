package hitool.core.beanutils.converters.bean;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings("rawtypes")
public class BeanBeanConverter implements Converter {
	
	@SuppressWarnings("unchecked")
	public Object convert(Class type, Object source) {
		if (source == null) {
            throw new ConversionException("No value specified");
        }
		//copy properties from JavaBeans a to JavaBeans b
		try {
			Object bean = type.getConstructor().newInstance();
            BeanUtils.copyProperties(bean, source);
            return bean;
        } catch (Exception e) {
        	throw new ConversionException(e);
        }
	}

}
