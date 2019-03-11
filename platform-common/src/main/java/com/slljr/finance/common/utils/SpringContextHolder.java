package com.slljr.finance.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @description: 静态方法方式获取spring容器里面的bean
 * @author: GUOQUN.YANG.
 * @date: 2018/3/20.
 * @time: 10:07.
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据对象类型获取bean
     *
     * @param type
     * @Author: guoqun.yang
     * @Date: 2018/2/7 10:02
     * @version 1.0
     */
    public static <T> T getBean(Class<T> type) {
        assertApplicationContext();
        return applicationContext.getBean(type);
    }

    /**
     * 根据名称获取bean
     *
     * @param beanName
     * @Author: guoqun.yang
     * @Date: 2018/2/7 10:06
     * @version 1.0
     */
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 检查applicationContext内容是否为空
     *
     * @param
     * @Author: guoqun.yang
     * @Date: 2018/2/7 10:07
     * @version 1.0
     */
    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}
