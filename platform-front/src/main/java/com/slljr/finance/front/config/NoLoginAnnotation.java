package com.slljr.finance.front.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 不需要登录注解
 * 用法: 在不需要登录方法上方 @NoLoginAnnotation
 */
public @interface NoLoginAnnotation {
}
