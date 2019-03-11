package com.slljr.finance.common.utils;

import java.util.*;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/21.
 * @time: 10:16.
 */
public class HashMapUtil {

    /**
     * 对集合内的数据按key的字母顺序做排序
     *
     * @param map
     * @return java.util.List<java.util.Map.Entry<java.lang.String,java.lang.Object>>
     * @author uncle.quentin
     * @date 2018/12/21 10:23
     * @version 1.0
     */
    public static List<Map.Entry<String, Object>> sortMap(Map<String, Object> map) {
        final List<Map.Entry<String, Object>> infos = new ArrayList<>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, (o1, o2) -> (o1.getKey().compareTo(o2.getKey())));

        return infos;
    }
}
