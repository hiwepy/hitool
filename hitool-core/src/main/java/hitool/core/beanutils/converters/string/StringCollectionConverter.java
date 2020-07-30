package hitool.core.beanutils.converters.string;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({"unchecked","rawtypes"})
public class StringCollectionConverter implements Converter{

	public Object convert(Class type, Object value) {
		if (value == null) {
            throw new ConversionException("No value specified");
        }
		//String to Collection
		if (value instanceof String ) {
			try {
				List<String> result = new ArrayList<String>();
				char[] array = ((String) value).toCharArray();
				for (int i = 0; i < array.length; i++) {
					result.add(String.valueOf(array[i]));
				}
				return result;
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
	
}
