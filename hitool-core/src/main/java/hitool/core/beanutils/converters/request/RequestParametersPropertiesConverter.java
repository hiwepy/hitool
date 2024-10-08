package hitool.core.beanutils.converters.request;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.ServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestParametersPropertiesConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// ServletRequest Parameters to Properties
		if (value.getClass().isAssignableFrom(ServletRequest.class)) {
			try {
				ServletRequest request = (ServletRequest) value;
				Properties result = new Properties();
				Map resource = request.getParameterMap();
				Iterator<Object> iterator = resource.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					result.put(key, resource.get(key));
				}
				return result;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
}
