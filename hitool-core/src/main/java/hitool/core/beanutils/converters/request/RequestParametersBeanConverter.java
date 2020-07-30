package hitool.core.beanutils.converters.request;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.BeanPropertyUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RequestParametersBeanConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// ServletRequest Parameters to JavaBean
		if (value.getClass().equals(HttpServletRequest.class) || (value instanceof ServletRequest)) {
			try {
				ServletRequest request = (ServletRequest) value;
				Object bean = type.getConstructor().newInstance();
				Enumeration<String> parameterNames = request.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String name = parameterNames.nextElement();
					String val = request.getParameter(name);
					BeanPropertyUtils.setProperty(bean, name, val, true);
				}
				return bean;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}

}
