package hitool.core.format;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import hitool.core.format.annotations.Formater;

public class FormatHandler {

	private static Format  format = null;
	public static <T> void format(T entity) throws Exception{
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation annotation = field.getAnnotation(Formater.class);
			if (annotation!=null) {
				boolean accessible = false;
				if(!field.isAccessible()){
					accessible = true;
					field.setAccessible(true);
				}
				Formater formater = (Formater) annotation;
				if(field.getType().equals(String.class)){
					field.set(entity, PatternFormatUtils.format(formater.value(),new String[]{field.get(entity).toString()}));
				}else if(field.getType().equals(Date.class)){
					SimpleDateFormat fmt = new SimpleDateFormat(formater.value());
					//field.set(entity,fmt.parseObject(fmt.format(field.get(entity))) );
				}
				
				if(accessible){
					field.setAccessible(false);
				}
			} else {
				continue;
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		TestSS entity = new  TestSS("万大龙", new Date());
		FormatHandler.format(entity);
		System.out.println(entity.getFiled_1());
		System.out.println(entity.getFiled_2());
	}
}


