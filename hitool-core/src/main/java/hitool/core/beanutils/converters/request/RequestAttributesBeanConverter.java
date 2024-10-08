package hitool.core.beanutils.converters.request;

import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.BeanPropertyUtils;

@SuppressWarnings({"unchecked","rawtypes"})
public class RequestAttributesBeanConverter implements Converter  {

	public Object convert(Class type, Object value) {
		if (value == null) {
            throw new ConversionException("No value specified");
        }
		//ServletRequest Attribute to JavaBean 
		if (value.getClass().isAssignableFrom(HttpServletRequest.class)) {
			try {
				HttpServletRequest request = (HttpServletRequest)value;
				Enumeration<String> enu = request.getAttributeNames();
				Object bean = type.getConstructor().newInstance();
				while(enu.hasMoreElements()){
					String key = (String) enu.nextElement();
					Object attribute = request.getAttribute(key);
					if( null != attribute){
						attribute = request.getSession().getAttribute(key);
						if(null != attribute){
							attribute = request.getSession().getServletContext().getAttribute(key);
						}
					}
					BeanPropertyUtils.setProperty(bean, key, attribute,true);
				}
				return bean;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
	
}
