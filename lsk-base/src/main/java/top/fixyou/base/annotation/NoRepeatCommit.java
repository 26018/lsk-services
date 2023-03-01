package top.fixyou.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 26018
 * @description 防止重复提交注解
 * @description
 * @date 2023/3/1 11:00
 */

@Target({METHOD})
@Retention(RUNTIME)
public @interface NoRepeatCommit {

    /**
     * 超时时间 在此时间内，不允许提交
     */
    long timeout() default 0;

    /**
     * 时间记录键名
     */
    String key() default "";
}
