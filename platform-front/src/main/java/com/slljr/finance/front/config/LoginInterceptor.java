package com.slljr.finance.front.config;

import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.slljr.finance.common.constants.Constant.AUTHORIZATION;
import static com.slljr.finance.common.constants.Constant.TOKEN_KEY;

@Configuration
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private RedisUtil redisUtil;

   @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (noNeedLogin(handler, request)){
            return true;
        }

       if (!validToken(request, response)){
           return false;
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

        log.debug("请求地址:" + request.getRequestURL().toString());
        return false;
    }

    /**
     * 校验token
     *
     * @param request
     * @param response
     * @return java.lang.Object
     * @author uncle.quentin
     * @date 2019/1/3 10:01
     * @version 1.0
     */
    private boolean validToken(HttpServletRequest request, HttpServletResponse response) {
        //获取Token
        String token = request.getHeader(AUTHORIZATION);
        boolean validResult = true;
        if (StringUtils.isNotEmpty(token)) {
            try {
                Object user = redisUtil.get(TOKEN_KEY + token);
                if (null == user) {
                    validResult = false;
                }
            } catch (Exception e) {
                validResult = false;
            }
        } else {
            validResult = false;
        }

        if (!validResult) {
            ModelMap resultObj = WriteJson.errorWebMsg(MsgEnum.TOKEN_INVALID);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(JsonUtil.getJson(resultObj));
            } catch (IOException e) {
                log.error("网络异常LoginInterceptor.validToken:", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        return validResult;
    }

}
