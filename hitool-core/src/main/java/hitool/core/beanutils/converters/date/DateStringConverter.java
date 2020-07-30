package hitool.core.beanutils.converters.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
@SuppressWarnings({"unchecked","rawtypes"})
public class DateStringConverter implements Converter {
	
	private SimpleDateFormat sdf = new SimpleDateFormat();
	private DateConverter converter = new DateConverter();
	
	public Object convert(Class type, Object source) {
		if (source == null) {
            throw new ConversionException("No value specified");
        }
		//String to Date
		if (source instanceof String) {
			try {
				return sdf.parse((String) source);
			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}
		//Date to String
		if (source instanceof Date) {
			converter.setUseLocaleFormat(true);
			converter.setPatterns(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			return converter.convert(String.class, source);
		}
		return null;
	}
}