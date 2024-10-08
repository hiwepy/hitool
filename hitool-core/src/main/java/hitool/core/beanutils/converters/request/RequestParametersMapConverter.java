package hitool.core.beanutils.converters.request;

import jakarta.servlet.ServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestParametersMapConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// ServletRequest Parameters to Map
		if (value.getClass().isAssignableFrom(ServletRequest.class)) {
			try {
				ServletRequest request = (ServletRequest) value;
				return request.getParameterMap();
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
}
