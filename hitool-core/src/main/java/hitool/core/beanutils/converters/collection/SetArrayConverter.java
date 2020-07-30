package hitool.core.beanutils.converters.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SetArrayConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		if (value instanceof List) {
			return ((Collection) value).toArray();
		}
		if (value.getClass().isArray()) {
			Set<Object> target = null;
			Object[] arr = (Object[]) value;
			target = new HashSet<Object>();
			for (int i = 0; i < arr.length; i++) {
				target.add(arr[i]);
			}
			return target;
		}
		return null;
	}

}
