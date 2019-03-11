package com.slljr.finance.payment.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.slljr.finance.common.enums.PaymentChannelEnum;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.utils.OKHttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/17.
 * @time: 11:17.
 */
public class ZMRequestUtils {
    static Logger log = LogManager.getLogger();
    static OKHttpUtils httpUtils = OKHttpUtils.getInstance();
//    static final String mch = "2018121313412502989664";
//    static final String key = "cee07c0d401d442e900232b06d1634e9";
//    static final String version = "1.0.0";
//    static final String HTTP_URL = "https://api.szjcxd.cn";

    /**
     * 执行封装类,只需传url和正本参数
     * @param url   不需要HOST,如: /tl/cusquery
     * @param paramMap  关键参数
     * @return
     */
    public static PaymentResult execute(String url, Map<String,String> paramMap, PaymentChannel channel){
        String sign = getSign(paramMap, channel);
        String params = getParamStr(paramMap);
        paramMap.put("sign", sign);
        paramMap.put("params", params);
        paramMap.putAll(getBaseParams(channel));

        PaymentResult res = new PaymentResult();
        try {
            //{"key":"590","msg":"服务器繁忙,请稍后重试"}
            log.info("请求参数: " + JSON.toJSON(paramMap).toString());
            url = url.startsWith("http") ? url : channel.getHttpUrl() + url;
            String text = httpUtils.syncPostFormRequest( url, null, paramMap);
            log.info("接口返回: " + text);

            if (StringUtils.isBlank(text)){
                res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
                return res;
            }

            JSON.parseObject(text).entrySet().forEach(entry->res.put(entry.getKey(), entry.getValue()==null?"":entry.getValue().toString()));

            String data = res.get("data");
            if (StringUtils.isNotBlank(data)){
                if (data.trim().startsWith("{") && data.trim().endsWith("}")){
                    JSON.parseObject(data).entrySet().forEach(entry->res.put(entry.getKey(), String.valueOf(entry.getValue())));
                }else if(data.contains("=")){
                    for (String str : data.split("&")){
                        String key = StringUtils.substringBefore(str, "=");
                        String value = StringUtils.substringAfter(str, "=");
                        res.put(key, value);
                    }
                }
            }

            res.setMsg(StringUtils.isNotBlank(res.get("errmsg"))? res.get("errmsg") : res.get("msg"));
        }catch (IOException e){
            res.setError(PaymentResultCodeEnum.IO_EXCEPTION);
        }catch (JSONException e){
            res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
        }catch (Exception e){
            e.printStackTrace();
            res.setError(PaymentResultCodeEnum.UNKNOW_EXCEPTION);
        }

        return res;
    }

    /**
     * 基础参数以外的参数key=value&key=value形式拼接后转Hex
     *
     * @param params
     * @return
     */
    private static String getParamStr(Map<String, String> params) {
        StringBuffer buffer = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            ZMUtils.sortMap(params).forEach(entry -> buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return ZMUtils.str2HexStr(buffer.toString());
    }

    /**
     * 基础参数和正本参数根据key排序后,拼接成key1value1key2value2形式,在最前面插入key,并MD5_32加密后转大写
     *
     * @param params
     * @return
     */
    private static String getSign(Map<String, String> params, PaymentChannel channel) {
        StringBuffer buffer = new StringBuffer();
        Map<String, String> newParams = new HashMap<>();
        //将所有业务请求参数
        newParams.putAll(getBaseParams(channel));
        newParams.putAll(params);
        //参数名称和参数值链接成一个字符串A
        ZMUtils.sortMap(newParams).forEach(entry -> buffer.append(entry.getKey()).append(entry.getValue()));
        //在字符串A的头部加上key组成一个新字符串B
        buffer.insert(0, channel.getEncryptKey());
        //对字符串进行md5再大写得到签名sign
        return ZMUtils.encoderByMd5(buffer.toString()).toUpperCase();
    }

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 基础参数
     *
     * @return java.util.Map<java.lang.String   ,   java.lang.String>
     * @author uncle.quentin
     * @date 2018/12/17 13:40
     * @version 1.0
     */
    private static Map<String, String> getBaseParams(PaymentChannel channel) {
        PaymentChannelEnum channelType = PaymentChannelEnum.get(channel.getCode());
        Map<String, String> map = new HashMap<>();
        map.put("mchno", channel.getMerchantNo());
        map.put("version", JSON.parseObject(channel.getOtherParams()).getString("version"));
        map.put("timestamp", simpleDateFormat.format(new Date()));
        //腾付通需传渠道CODE
        if (PaymentChannelEnum.TENGFUTONG.equals(channelType)) {
            map.put("channel_code", "1001163");
        }
        return map;
    }
}
