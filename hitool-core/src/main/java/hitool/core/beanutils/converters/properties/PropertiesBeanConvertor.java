package hitool.core.beanutils.converters.properties;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.reflection.ClassUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PropertiesBeanConvertor implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		try {
			// Properties to JavaBean
			if (value.getClass().equals(Properties.class) || (value instanceof Properties)) {
				Properties resource = (Properties) value;
				Object bean = null;
				if (resource != null && !resource.isEmpty()) {
					bean = type.newInstance();
					Iterator<Entry<Object, Object>> ite = resource.entrySet().iterator();
					while (ite.hasNext()) {
						Entry<Object, Object> entry = ite.next();
						BeanUtils.setProperty(bean, (String) entry.getKey(), entry.getValue());
					}
				}
				return bean;
			}
			// JavaBean to Properties
			if (ClassUtils.isCustomClass(value.getClass())) {
				Properties resource = new Properties();
				Field[] fields = value.getClass().getDeclaredFields();
				for (Field field : fields) {
					String key = field.getName();
					resource.put(key, field.get(value));
				}
				return resource;
			}
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		return null;
	}

}
