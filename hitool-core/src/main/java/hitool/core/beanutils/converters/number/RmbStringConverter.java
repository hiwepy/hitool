package hitool.core.beanutils.converters.number;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class RmbStringConverter implements Converter {
	
	private String toRMB(double money) {
		
    	char[] s1 = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    	char[] s4 = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万'};
    	String str = String.valueOf(Math.round(money * 100 + 0.00001));
    	String result = "";

    	for (int i = 0; i < str.length(); i++) {
    		int n = str.charAt(str.length() - 1 - i) - '0';
    		result = s1[n] + "" + s4[i] + result;
    	}

    	result = result.replaceAll("零仟", "零");
    	result = result.replaceAll("零佰", "零");
    	result = result.replaceAll("零拾", "零");
    	result = result.replaceAll("零亿", "亿");
    	result = result.replaceAll("零万", "万");
    	result = result.replaceAll("零元", "元");
    	result = result.replaceAll("零角", "零");
    	result = result.replaceAll("零分", "零");

    	result = result.replaceAll("零零", "零");
    	result = result.replaceAll("零亿", "亿");
    	result = result.replaceAll("零零", "零");
    	result = result.replaceAll("零万", "万");
    	result = result.replaceAll("零零", "零");
    	result = result.replaceAll("零元", "元");
    	result = result.replaceAll("亿万", "亿");

    	result = result.replaceAll("零$", "");
    	result = result.replaceAll("元$", "元整");

    	return result;
    }

	@SuppressWarnings("rawtypes")
	public Object convert(Class targetType, Object source) {
		if (source == null) {
            throw new ConversionException("No value specified");
        }
		//Number to RMB String
		Class sourceClass = source.getClass();
		if (sourceClass.equals(Number.class)&& source instanceof Number) {
			try {
				return this.toRMB((Double) source);
			} catch (SecurityException e) {
				throw new ConversionException(e);
			} catch (IllegalArgumentException e) {
				throw new ConversionException(e);
			}
		}
		return null;
	}
	
}