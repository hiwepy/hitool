package hitool.core.beanutils.converters.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListSetConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		// List to Set
		if (value instanceof List) {
			Set<Object> target = null;
			target = new HashSet<Object>();
			Iterator<Object> it = ((Collection) value).iterator();
			while (it.hasNext()) {
				target.add(it.next());
			}
			return target;
		}
		// Set to List
		if (value instanceof Set) {
			List<Object> target = null;
			target = new ArrayList<Object>();
			Iterator<Object> it = ((Collection) value).iterator();
			while (it.hasNext()) {
				target.add(it.next());
			}
			return target;
		}
		return null;
	}

}
