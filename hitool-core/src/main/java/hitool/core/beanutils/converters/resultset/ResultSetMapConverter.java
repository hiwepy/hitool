package hitool.core.beanutils.converters.resultset;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.ResultSetUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ResultSetMapConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		try {
			// ResultSet to List<Map>
			Class sourceClass = value.getClass();
			if (sourceClass.equals(ResultSet.class) && value instanceof ResultSet) {
				// 获取类属性
				ResultSet rest = (ResultSet) value;
				List<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();
				String[] colNames = ResultSetUtils.getColNames(rest);
				while (rest.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < colNames.length; i++) {
						map.put(colNames[i].toLowerCase(), rest.getObject(colNames[i]));
					}
					collection.add(map);
				}
				return collection;
			}
			return null;
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

}