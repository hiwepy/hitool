package hitool.core.beanutils.converters.request;

import java.util.Enumeration;
import java.util.Properties;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestAttributesPropertiesConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// ServletRequest Attribute to Properties
		if (value.getClass().isAssignableFrom(HttpServletRequest.class)) {
			try {
				Properties result = new Properties();
				HttpServletRequest request = (HttpServletRequest) value;
				Enumeration<String> enu = request.getAttributeNames();
				while (enu.hasMoreElements()) {
					String key = (String) enu.nextElement();
					Object attribute = request.getAttribute(key);
					if (null != attribute) {
						attribute = request.getSession().getAttribute(key);
						if (null != attribute) {
							attribute = request.getSession().getServletContext().getAttribute(key);
						}
					}
					result.put(key, attribute);
				}
				return result;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
}
