package hitool.core.beanutils.converters.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DateTimeStringConverter implements Converter {

	private SimpleDateFormat sdf = new SimpleDateFormat();

	public Object convert(Class targetType, Object source) {
		if (source == null) {
			throw new ConversionException("No value specified");
		}
		try {
			// String to Timestamp
			if (source instanceof String) {
				Date utilDate = sdf.parse(source.toString());
				// return new Timestamp(utilDate.getTime());
				return targetType.getConstructor(Long.class).newInstance(utilDate.getTime());
			}
			// Timestamp to String
			if (source instanceof Timestamp) {
				Timestamp temp = (Timestamp) source;
				return sdf.format(new Date(temp.getTime()));
			}
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		return null;

	}
}
