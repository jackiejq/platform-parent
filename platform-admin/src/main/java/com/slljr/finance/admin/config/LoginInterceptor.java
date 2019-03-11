package com.slljr.finance.admin.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class LoginInterceptor extends HandlerInterceptorAdapter {
     private static final Logger logger = LogManager.getLogger();


   @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (noNeedLogin(handler, request)){
            return true;
        }

        //登录session判断

        return super.preHandle(request, response, handler);
    }


    /**
     * 判断是否有不需要登录注解
     * @param handler
     * @return
     */
    public boolean noNeedLogin(Object handler, HttpServletRequest request){
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            NoLoginAnnotation methodAnnotation = handlerMethod.getMethodAnnotation(NoLoginAnnotation.class);
            if (methodAnnotation != null){
                return true;
            }
        }

        logger.debug("请求地址:" + request.getRequestURL().toString());
        return false;
    }

}
