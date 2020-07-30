/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.format.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 字符串格式化注解，用于bean的字段或者参数，格式化字符
 */
@Target({ ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Formater {
  public abstract String value();
}
