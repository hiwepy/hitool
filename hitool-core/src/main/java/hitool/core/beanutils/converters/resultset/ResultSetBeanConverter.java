package hitool.core.beanutils.converters.resultset;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import hitool.core.beanutils.ResultSetUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ResultSetBeanConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null) {
			throw new ConversionException("No value specified");
		}
		try {
			// ResultSet to List<JavaBean>
			Class sourceClass = value.getClass();
			if (sourceClass.equals(ResultSet.class) && value instanceof ResultSet) {
				List<Object> collection = new ArrayList<Object>();
				ResultSet rest = (ResultSet) value;
				String[] colNames = ResultSetUtils.getColNames(rest);
				while (rest.next()) {
					Object object = type.newInstance();// 创建JavaBean对象
					for (int i = 0; i < colNames.length; i++) {
						BeanUtils.copyProperty(object, colNames[i].toLowerCase(), rest.getObject(colNames[i]));
					}
					collection.add(object);
				}
				return collection;
			}
			return null;
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

}