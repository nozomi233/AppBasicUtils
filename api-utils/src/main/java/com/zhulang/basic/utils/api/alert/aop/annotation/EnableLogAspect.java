package com.zhulang.basic.utils.api.alert.aop.annotation;

import com.zhulang.basic.utils.api.alert.ExceptionAlertService;
import com.zhulang.basic.utils.api.alert.aop.ApiRunTimeLogAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动类注解，注入日志切面
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ExceptionAlertService.class, ApiRunTimeLogAspect.class})
public @interface EnableLogAspect {
}
