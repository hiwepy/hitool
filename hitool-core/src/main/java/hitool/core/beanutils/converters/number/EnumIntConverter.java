package hitool.core.beanutils.converters.number;

import org.apache.commons.beanutils.Converter;
/*
 *  枚举类型与Integer的相互转换
 */
@SuppressWarnings("rawtypes")
public class EnumIntConverter implements Converter {

	public Object convert(Class type, Object value) {
		if (value == null){
			return null;
		}
		Object dest = null;
		if (value.getClass().isEnum()){
			dest = Integer.valueOf(((Enum)value).ordinal());
		}else if (value.getClass().equals(Integer.class)){
			Object consts[] = value.getClass().getEnumConstants();
			return consts[((Integer)value).intValue()];
		}
		return dest;
	}
	
}
