package hitool.core.beanutils.converters.request;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestAttributesMapConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// ServletRequest Attribute to Map
		if (value.getClass().equals(HttpServletRequest.class) || (value instanceof ServletRequest)) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
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
					map.put(key, attribute);
				}
				return map;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
}
