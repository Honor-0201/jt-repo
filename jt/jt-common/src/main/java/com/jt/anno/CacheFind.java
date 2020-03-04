package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 对方法生效
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface CacheFind {

	/**
	 * key:唯一标识 
	 * 1.此位置key可以动态获取 包名.类名.方法名.参数 
	 * 2.也可以自己指定
	 */
	String key() default "";

	int seconds() default 0;//用户数据不超时

}
