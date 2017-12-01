package wang.igood.db.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/************************************************************
 * <a>数据库字段映射标识</a>
 * @author sunliang
 * @since 2017-11-25
 * @mail 1130437154@qq.com
 * ***********************************************************/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	String value() default "columnName";
}
