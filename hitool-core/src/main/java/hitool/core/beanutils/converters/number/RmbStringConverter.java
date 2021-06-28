package hitool.core.beanutils.converters.number;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.lang3.RmbString;

public class RmbStringConverter implements Converter {
	
	@SuppressWarnings("rawtypes")
	public Object convert(Class targetType, Object source) {
		if (source == null) {
            throw new ConversionException("No value specified");
        }
		//Number to RMB String
		Class sourceClass = source.getClass();
		if (sourceClass.equals(Number.class)&& source instanceof Number) {
			try {
				return RmbString.toString((Double) source);
			} catch (SecurityException e) {
				throw new ConversionException(e);
			} catch (IllegalArgumentException e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
	
}