package com.zhulang.basic.utils.api.alert.aop.annotation;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRunTimeLog {
    boolean alertReport() default true;
}
