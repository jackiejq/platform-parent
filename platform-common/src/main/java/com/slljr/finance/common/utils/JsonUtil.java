package com.slljr.finance.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @description: List集合和JSON互转工具类
 * @author: GUOQUN.YANG.
 * @date: 2018/3/20.
 * @time: 10:07.
 */
public class JsonUtil {

    private static final Logger log = LogManager.getLogger();

    /**
     * JSON转对象
     *
     * @param pojo
     * @param tclass
     * @Author: guoqun.yang
     * @Date: 2018/3/20 10:13
     * @version 1.0
     */
    public static <T> T getObject(String pojo, Class<T> tclass) {
        try {
            return JSONObject.parseObject(pojo, tclass);
        } catch (Exception e) {
            log.error("转 JSON 失败", e);
        }
        return null;
    }

    /**
     * 对象转JSON
     *
     * @param tResponse
     * @Author: guoqun.yang
     * @Date: 2018/3/20 10:13
     * @version 1.0
     */
    public static <T> String getJson(T tResponse) {
        return JSONObject.toJSONString(tResponse);
    }

    /**
     * List转JSON
     *
     * @param ts
     * @Author: guoqun.yang
     * @Date: 2018/3/20 10:08
     * @version 1.0
     */
    public static <T> String listToJson(List<T> ts) {
        return JSON.toJSONString(ts);
    }

    /**
     * JSON转List
     *
     * @param jsonString
     * @param clazz
     * @Author: guoqun.yang
     * @Date: 2018/3/20 10:09
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return JSONArray.parseArray(jsonString, clazz);
    }

    /**
     * String转JsonObject
     *
     * @param jsonString
     * @return com.alibaba.fastjson.JSONObject
     * @author uncle.quentin
     * @date 2018/12/19 12:46
     * @version 1.0
     */
    public static JSONObject strToJson(String jsonString) {
        return JSONObject.parseObject(jsonString);
    }
}
