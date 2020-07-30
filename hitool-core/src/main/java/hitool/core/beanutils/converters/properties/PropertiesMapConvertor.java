package hitool.core.beanutils.converters.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;


@SuppressWarnings({"unchecked","rawtypes"})
public class PropertiesMapConvertor implements Converter {
	
	public Object convert(Class type, Object value) {
		if (value == null) {
            throw new ConversionException("No value specified");
        }
		//Properties to Map
		if(value.getClass().equals(Properties.class)||(value instanceof Properties)){
			Map<String,Object> result = new HashMap<String, Object>();
			Properties resource = (Properties) value;
			Iterator<Object> iterator = resource.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				result.put(key, resource.get(key));
			}
			return result;
		}
		//Map ro Properties
		if(value instanceof Map){
			Properties result = new Properties(); 
			result.putAll((Map) value);
			return result;
		}
		return null;
	}

}
