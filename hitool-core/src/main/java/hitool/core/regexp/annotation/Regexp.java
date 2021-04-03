package hitool.core.regexp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
 *  正则表达式注解，用于bean的字段或者参数，检查字段是否匹配正则
 */
@Target({ ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Regexp {
	
	public abstract String pattern();
	
	public RegexpType type() default RegexpType.NORMAL;

}
