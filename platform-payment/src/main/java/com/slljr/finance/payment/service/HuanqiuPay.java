package com.slljr.finance.payment.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.slljr.finance.common.enums.BankCardTypeEnum;
import com.slljr.finance.common.enums.HuanqiuResultEnum;
import com.slljr.finance.common.enums.HuanqiuTransTypeEnum;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.utils.HashMapUtil;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.OKHttpUtils;
import com.slljr.finance.payment.utils.*;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 环球汇聚落地还款通道
 * @author: uncle.quentin.
 * @date: 2018/12/21.
 * @time: 9:58.
 */
public class HuanqiuPay {

    private static final Logger log = LogManager.getLogger();

    private static OKHttpUtils okHttpUtil = OKHttpUtils.getInstance();


    /**
     * 获取签名字符串
     *
     * @param param  参数
     * @param appkey 密钥
     * @return java.lang.String
     * @author uncle.quentin
     * @date 2018/12/21 10:27
     * @version 1.0
     */
    public static String getSignStr(Map<String, Object> param, String appkey) {

        //map按字母排序
        List<Map.Entry<String, Object>> mapList = HashMapUtil.sortMap(param);
        if (null != mapList && !mapList.isEmpty()) {
            //处理成空字符串拼接的字符串
            String str = StringUtils.join(mapList, "");
            log.debug("签名串：" + str);
            try {
                //MD5加密
                String encodeStr = ZMUtils.encoderByMd5(str + appkey);
                if (StringUtils.isNotEmpty(encodeStr)) {
                    return encodeStr;
                }
            } catch (Exception e) {
                throw new RuntimeException("加密错误，错误信息：", e);
            }
        }
        return "";
    }

    /**
     * 固定请求报文
     *
     * @param transcode 交易码
     * @param orderNo   流水号 唯一值，每次交易都唯一
     * @param merchno   商户号
     * @param dsorderid 商户订单号 每笔交易唯一，若为查询接口填原交易的商户订单号，如006
     * @param param     业务参数
     * @author uncle.quentin
     * @date 2018/12/21 17:47
     * @version 1.0
     */
    public static Map<String, Object> getParams(String transcode, String orderNo, String merchno, String dsorderid, Map<String, Object> param, String secret) {
        //添加固定参数
        param.put("transcode", transcode);
        //版本号
        param.put("version", "0100");
        //流水号
        param.put("ordersn", orderNo);
        //商户号
        param.put("merchno", merchno);
        //商户订单号
        param.put("dsorderid", dsorderid);
        //加密校验值
        param.put("sign", getSignStr(param, secret));

        return param;
    }

    /**
     * 发送请求
     *
     * @param url
     * @param param
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 11:50
     * @version 1.0
     */
    private static HuanqiuResultVO execute(String url, Map<String, Object> param) throws InterfaceException {
        try {
            //执行请求
            log.info("请求参数：" + JSON.toJSONString(param));
            String response = okHttpUtil.syncPostJSONRequest(url, null, JSON.toJSONString(param), null);
            //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227215941657001","sign":"bbbea66d38ffafaa2a0643e7c089d4dc","ordersn":"20181227215941657000","returncode":"0001",
            // "message":"{\"code\":\"FAIL\",\"message\":\"该身份证号已入驻\",\"subMerNo\":\"71607552\",\"subMerchantNo\":\"71607552\"}"}

//            {"transcode":"404","merchno":"youka2018122001","dsorderid":"20181228193628791001","sign":"615854b0331cbe6b7f1b83beb9d22c4f","ordersn":"20181228193628790000","returncode":"0000",
//                    "message":"{\"code\":\"SUCCESS\",\"message\":\"成功\",\"requestNo\":\"45194INTB239754868061892608\",\"subMerNo\":\"58253288\",\"subMerchantNo\":\"58253288\",\"remark\":\"审核通过,\"}"}

            log.info("接口返回：" + response);
            if (StringUtils.isEmpty(response)) {
                throw new InterfaceException("响应数据为空");
            }
            //解析响应为jsonobject对象
            HuanqiuResultVO result;
            try {
                result = JsonUtil.getObject(response, HuanqiuResultVO.class);
            } catch (Exception e) {
                log.error("解析响应异常:", e);
                throw new InterfaceException("解析响应异常：" + e.getMessage());
            }

            return result;

        } catch (IOException e) {
            throw new InterfaceException("网络异常：" + e.getMessage());
        }
    }

    /**
     * 发送请求
     *
     * @param transcode 交易码
     * @param paramObj  业务参数
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 18:13
     * @version 1.0
     */
    private static HuanqiuResultVO sendRequest(String transcode, Map<String, Object> paramObj, PaymentChannel channel) {
        //请求参数（包含业务参数和基本参数）
        Map<String, Object> requestParams;
        //设置固定请求参数
        {
            String orderNo = MakeOrderNum.generate();
            String osubNo = MakeOrderNum.generate();
            requestParams = getParams(transcode, orderNo, channel.getMerchantNo(), osubNo, paramObj, channel.getEncryptKey());
        }

        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = new HuanqiuResultVO();
            try {
                //获取响应
                huanqiuResult = execute(channel.getHttpUrl(), requestParams);
            } catch (InterfaceException e) {
                huanqiuResult.setSuccess(false);
                log.error("请求异常：", e);
            }
            return huanqiuResult;
        }
    }

    /**
     * 发送请求
     *
     * @param transcode 交易码
     * @param paramObj  业务参数
     * @param channel   通道信息
     * @param osubNo    商户订单号
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 18:13
     * @version 1.0
     */
    private static HuanqiuResultVO sendPayRequest(String transcode, Map<String, Object> paramObj, PaymentChannel channel, String osubNo) {
        //请求参数（包含业务参数和基本参数）
        Map<String, Object> requestParams;
        //设置固定请求参数
        {
            String orderNo = MakeOrderNum.generate();
            requestParams = getParams(transcode, orderNo, channel.getMerchantNo(), osubNo, paramObj, channel.getEncryptKey());
        }

        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = new HuanqiuResultVO();
            try {
                //获取响应
                huanqiuResult = execute(channel.getHttpUrl(), requestParams);
            } catch (InterfaceException e) {
                huanqiuResult.setSuccess(false);
                log.error("请求异常：", e);
            }
            return huanqiuResult;
        }
    }

    /**
     * 发送请求
     *
     * @param transcode 交易码
     * @param paramObj  业务参数
     * @param osubNo    商户订单号
     * @param channel   通道信息（异步通知地址、请求地址）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 18:13
     * @version 1.0
     */
    private static HuanqiuResultVO sendRequest(String transcode, Map<String, Object> paramObj, String osubNo, PaymentChannel channel) {
        //请求参数（包含业务参数和基本参数）
        Map<String, Object> requestParams;
        //设置固定请求参数
        {
            String orderNo = MakeOrderNum.generate();
            requestParams = getParams(transcode, orderNo, channel.getMerchantNo(), osubNo, paramObj, channel.getEncryptKey());
        }

        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = new HuanqiuResultVO();
            try {
                huanqiuResult = execute("http://pay.huanqiuhuiju.com/authsys/api/auth/execute.do", requestParams);
            } catch (InterfaceException e) {
                huanqiuResult.setSuccess(false);
                log.error("请求异常：", e);
            }
            return huanqiuResult;
        }
    }


    /**
     * 获取响应结果
     *
     * @param result
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/27 17:09
     * @version 1.0
     */
    private static HuanqiuResultVO getResult(HuanqiuResultVO result, String methodName) {
        //返回响应结果
        if (null == result) {
            result = new HuanqiuResultVO();
            result.setSuccess(false);
        }

        if (StringUtils.isEmpty(result.getMessage())) {
            //errtext不为空、没有message信息
            if (StringUtils.isNotEmpty(result.getErrtext())) {
                result.setMessage(result.getErrtext());
            } else {
                result.setMessage(PaymentResultCodeEnum.FAIL.getMsg());
            }
        } else {
            //解析message明细信息
            JSONObject resultObj = JsonUtil.strToJson(result.getMessage());
            String resultDetailCode = resultObj.getString("code");
            String resultDetailMessage = resultObj.getString("message");
            String subMerchantNo = resultObj.getString("subMerNo");
            String amount = resultObj.getString("amount");

            if (StringUtils.isNotEmpty(resultDetailCode)) {
                if (StringUtils.isNotEmpty(resultDetailCode)) {
                    if (HuanqiuResultVO.DATASUCCESS.equals(resultDetailCode) && HuanqiuResultEnum.HUANQIU_RESUL_SUCCESS.getKey().equals(result.getReturncode())) {
                        result.setSuccess(true);
                    } else {
                        result.setSuccess(false);
                        result.setMessage(resultDetailMessage);
                    }
                } else {
                    //打印具体响应信息
                    log.warn("返回信息：" + result.toString());
                    result.setSuccess(false);
                    result.setMessage(PaymentResultCodeEnum.UNKNOW_EXCEPTION.getMsg());
                }
            } else {
                //打印具体响应信息
                log.warn("返回信息：" + result.toString());
                result.setSuccess(false);
                result.setMessage(PaymentResultCodeEnum.UNKNOW_EXCEPTION.getMsg());
            }

            //商户入网返回子商户号
            if (StringUtils.isNotEmpty(methodName)) {
                if ("network".equals(methodName) && StringUtils.isNotEmpty(subMerchantNo)) {
                    result.setSubMerNo(subMerchantNo);
                }else if ("queryBalance".equals(methodName) && StringUtils.isNotEmpty(amount)){
                    result.setBanlanceAmount(amount);
                }
            }

        }

        return result;
    }

    /**
     * 支付返回信息
     *
     * @author uncle.quentin
     * @date   2019/1/2 14:26
     * @param   result
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @version 1.0
     */
    private static PaymentResult getPayResult(HuanqiuResultVO result){
        PaymentResult paymentResult = new PaymentResult();
        //返回响应结果
        if (null == result) {
            result = new HuanqiuResultVO();
            paymentResult.setError(PaymentResultCodeEnum.FAIL);
        }

        //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227184011567001","sign":"35e1b27118fdc0aeffd881cdb145a6fd","ordersn":"20181227184011567000","returncode":"0003",
        // "message":"{\"merchantOrderNo\":\"hc2018122718364900100101354\",\"subMerchantNo\":\"24656886\",\"amount\":\"1.0\",\"userFee\":\"0.06\",\"cardNo\":\"6221550859466827\",\"code\":\"SUCCESS\",\"message\":\"支付成功\"}"}
        if (StringUtils.isEmpty(result.getMessage())) {
            //errtext不为空、没有message信息
            if (StringUtils.isNotEmpty(result.getErrtext())) {
                paymentResult.setError(result.getErrtext());
            } else {
                paymentResult.setError(PaymentResultCodeEnum.PAY_FAIL);
            }
        } else {
            //解析message明细信息
            JSONObject resultObj = JsonUtil.strToJson(result.getMessage());
            String resultDetailCode = resultObj.getString("code");
            String resultDetailMessage = resultObj.getString("message");

            if (StringUtils.isNotEmpty(resultDetailCode)) {
                //returnCode等于0003（支付处理中）并且message下面的code等于SUCCESS为下单成功
                if (HuanqiuResultVO.DATASUCCESS.equals(resultDetailCode)) {
                    if ((HuanqiuResultEnum.HUANQIU_RESUL_SUCCESS.getKey().equals(result.getReturncode()))) {
                        paymentResult.setSuccess(PaymentResultCodeEnum.PAY_SUCCESS);
                    } else if (HuanqiuResultEnum.HUANQIU_RESUL_PROCESSING.getKey().equals(result.getReturncode())) {
                        paymentResult.setSuccess(PaymentResultCodeEnum.PAY_ING);
                    } else {
                        paymentResult.setError(PaymentResultCodeEnum.PAY_FAIL);
                    }
                } else {
                    if (StringUtils.isEmpty(resultDetailMessage)) {
                        paymentResult.setError(PaymentResultCodeEnum.PAY_FAIL);
                    } else {
                        paymentResult.setError(resultDetailMessage);
                    }
                }
            } else {
                paymentResult.setError(PaymentResultCodeEnum.PAY_FAIL);
            }

        }

        //返回通道订单号
        if (StringUtils.isNotEmpty(result.getDsorderid())) {
            paymentResult.put("dsorderid", result.getDsorderid());
        }

        return paymentResult;
    }

    /**
     * 商户入网
     *
     * @param userBasic 用户信息
     * @param bankCard  用户银行卡信息
     * @param channel   通道信息（异步通知地址、请求地址）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @return dsorderid（商户入网通道返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 11:15
     * @version 1.0
     */
    public static PaymentResult register(UserBasic userBasic, UserBankCard bankCard, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //接口名称
            paramObj.put("methodname", "network");
            //异步通知地址
            paramObj.put("notifyUrl", channel.getNotifyUrl());
            //子商户信息(以下参数顺序固定)
            Map<String, String> subObj = Maps.newLinkedHashMap();
            {
                //子商户类型 3，目前固定为3
                subObj.put("subMerType", "3");
                //姓名
                subObj.put("legalName", userBasic.getName());
                //手机号
                subObj.put("legalMobile", bankCard.getPhone());
                //身份证号 注意是L和i
                subObj.put("legalIdNo", userBasic.getIdCard());
                //证件号是否为长期 传1或2，1代表是，2代表否
                subObj.put("legalValidityFlag", "1");
                //银行卡号
                subObj.put("legalBankCard", bankCard.getBankCardNo());
                //银行名称 例如：建设银行
                subObj.put("legalBankName", bankCard.getOpeningBank());
                //银行卡类型 debit(借记卡)，credit(贷记卡) 传对应的英文
                if (BankCardTypeEnum.DEPOSIT_CARD.getKey() == bankCard.getBankCardType()) {
                    subObj.put("legalBankCardType", "debit");
                } else {
                    subObj.put("legalBankCardType", "credit");
                }
                //银行卡开卡姓名
                subObj.put("merOpenName", userBasic.getName());
                //起结金额 例如10
                subObj.put("minSettleAmout", "10");
                //个人住宅地址
                subObj.put("companyAddress", userBasic.getIdcAddress());

            }
            paramObj.put("subMer", subObj);
        }

        PaymentResult paymentResult = new PaymentResult();
        //发送请求、获取响应、返回
        {
            //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227162132420001","sign":"44f845461317f6a5b5891f3163277929","ordersn":"20181227162132420000","returncode":"0000",
            // "message":"{\"code\":\"SUCCESS\",\"message\":\"成功\",\"requestNo\":\"45194INTB239343418247999488\",\"subMerNo\":\"12034544\",\"subMerchantNo\":\"12034544\",\"remark\":\"审核通过,\"}"}
            HuanqiuResultVO huanqiuResult = sendRequest("404", paramObj, channel);
            //解析响应结果
            HuanqiuResultVO newHuanqiuResult = getResult(huanqiuResult,"network");

            if (huanqiuResult.isSuccess()) {
                paymentResult.setSuccess();
            } else {
                paymentResult.setError(newHuanqiuResult.getMessage());
            }

            if (StringUtils.isNotBlank(huanqiuResult.getSubMerNo())){
                paymentResult.setSuccess();
                paymentResult.put("subMerNo", huanqiuResult.getSubMerNo());
            }
            if (StringUtils.isNotBlank(newHuanqiuResult.getSubMerNo())){
                paymentResult.setSuccess();
                paymentResult.put("subMerNo", newHuanqiuResult.getSubMerNo());
            }

            //返回通道订单号
            if (StringUtils.isNotEmpty(newHuanqiuResult.getDsorderid())) {
                paymentResult.put("dsorderid", newHuanqiuResult.getDsorderid());
            }

        }
        return paymentResult;

    }

    /**
     * 查询子商户余额
     *
     * @param
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 17:38
     * @version 1.0
     */
    public static PaymentResult queryBalance(String subMerchantNo, PaymentChannel channel) {
        Map<String, Object> paramObj = Maps.newHashMap();
        //业务参数
        {
            //调用方法
            paramObj.put("methodname", "queryBalance");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
        }

        PaymentResult paymentResult = new PaymentResult();
        //发送请求、获取响应、返回
        {
            //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227164010000001","sign":"331a1057db826ec10c8b0becfc638021","ordersn":"20181227164010000000","returncode":"0000",
            // "message":"{\"amount\":\"0.00\",\"code\":\"SUCCESS\",\"message\":\"请求成功\"}"}
            HuanqiuResultVO huanqiuResult = sendRequest("404", paramObj, channel);

            //解析响应结果
            HuanqiuResultVO newHuanqiuResult = getResult(huanqiuResult, "queryBalance");

            if (newHuanqiuResult.isSuccess()) {
                paymentResult.setSuccess();
            } else {
                paymentResult.setError(newHuanqiuResult.getMessage());
            }

            if (StringUtils.isNotBlank(newHuanqiuResult.getBanlanceAmount())){
                paymentResult.put("banlance", newHuanqiuResult.getBanlanceAmount());
            }

        }
        return paymentResult;
    }


    /**
     * 同名卡开卡
     * 返回暂存信息：confirmMap、cookie
     *
     * @param userBasic     用户信息
     * @param bankCard      银行信息
     * @param subMerchantNo 子商户号
     * @param channel       通道信息（异步通知地址、请求地址、绑卡以后跳转到的页面）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @return dsorderid（开卡商户返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 18:06
     * @version 1.0
     */
    public static PaymentResult openCard(UserBasic userBasic, UserBankCard bankCard, String subMerchantNo, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //调用方法
            paramObj.put("methodname", "openCard");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
            //银行卡号
            paramObj.put("bankCardNo", bankCard.getBankCardNo());
            //手机号
            paramObj.put("mobile", bankCard.getPhone());
            //名字
            paramObj.put("name", userBasic.getName());
            //身份证
            paramObj.put("idCardNo", userBasic.getIdCard());
            //银行卡类型 debit(借记卡)，credit(贷记卡) 传对应的英文
            if (BankCardTypeEnum.DEPOSIT_CARD.getKey() == bankCard.getBankCardType()) {
                paramObj.put("cardType", "debit");
            } else {
                paramObj.put("cardType", "credit");
            }
            //异步通知地址
            paramObj.put("notifyUrl", channel.getNotifyUrl());
            //绑卡以后跳转到的页面
            paramObj.put("returnUrl", JSONObject.parseObject(channel.getOtherParams()).get("returnUrl"));
        }

        PaymentResult paymentResult = new PaymentResult();
        //发送请求、获取响应、返回
        //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227165934806001","sign":"bde0b1b4d92fe96f75f3054f0651370e","ordersn":"20181227165934806000",
        // "returncode":"0000","message":"http://pay.huanqiuhuiju.com/authsys/api/html/gethc.do?orderid=hc2018122716561300100108209&sign=8e121e0d0cc2c02253c4e235122d043b"}
        HuanqiuResultVO huanqiuResultVO = sendRequest("404", paramObj, channel);


        if (HuanqiuResultEnum.HUANQIU_RESUL_SUCCESS.getKey().equals(huanqiuResultVO.getReturncode()) && StringUtils.isNotEmpty(huanqiuResultVO.getMessage())) {
            //自动跳转用户界面
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Mobile Safari/537.36");
            headMap.put("Referer", huanqiuResultVO.getMessage());

            paymentResult = autoCommit(huanqiuResultVO.getMessage(), headMap, paymentResult);

            //环球只手动发短信,但我们帮他触发
            PaymentResult sendSmsRes = sendMessageCode(bankCard, paymentResult.getMap());
            if (sendSmsRes.isSuccess()){
                paymentResult.setSuccess(PaymentResultCodeEnum.SMS_AUTO_SEND);
            }else {
                paymentResult.setSuccess(PaymentResultCodeEnum.SMS_MANUAL_SEND);
            }
        } else {
            paymentResult.setError(huanqiuResultVO.getMessage());
        }

        //返回通道订单号
        if (StringUtils.isNotEmpty(huanqiuResultVO.getDsorderid())) {
            paymentResult.put("dsorderid", huanqiuResultVO.getDsorderid());
        }

        return paymentResult;
    }


    /**
     * 自动跳转用户界面
     *
     * @param messageUrl
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/25 10:42
     * @version 1.0
     */
    private static PaymentResult autoCommit(String messageUrl, Map<String, String> headMap, PaymentResult paymentResult) {
        //解析URL
        String html = "";
        if (StringUtils.isNotEmpty(messageUrl)) {
            try {
                //模拟提交步骤1
                html = okHttpUtil.syncGetRequest(messageUrl);
            } catch (IOException e) {
                paymentResult.setError(PaymentResultCodeEnum.IO_EXCEPTION);
                log.error("网络异常：", e);
            }
        } else {
            paymentResult.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
            log.error("返回URL为空");
        }

        //跳转用户填写资料页面
        Map<String, String> defaultCommitMap = Maps.newHashMap();
        if (StringUtils.isNotEmpty(html) && html.contains("pay_form")) {
            Document doc = Jsoup.parse(html);
            Element formElement = doc.getElementById("pay_form");
            //获取form隐藏表单数据
            formElement.select("input[type=\"hidden\"]").forEach(element -> defaultCommitMap.put(element.attr("name"), element.attr("value")));
            //获取form显示表单数据
            formElement.select("input[type=\"text\"]").forEach(element -> defaultCommitMap.put(element.attr("name"), element.attr("value")));

            try {
                //模拟提交步骤2
                Response forwardPageResponse = okHttpUtil.syncPostFormRequestWithResponseAll("https://fzapi.yemadai.com/snc/opencard", headMap, defaultCommitMap);
                List<String> cookies = forwardPageResponse.headers("Set-Cookie");
                List<String> tempCookies = new ArrayList<>();
                for (String str : cookies) {
                    str = str.substring(0, str.indexOf(";"));
                    tempCookies.add(str);
                }
                String joinStr = StringUtils.join(tempCookies, ";");

                paymentResult.put("cookie", joinStr);

                String forwardPage = forwardPageResponse.body().string();
                Map<String, String> confirmMap = Maps.newHashMap();
                if (StringUtils.isNotEmpty(forwardPage)) {
                    Document doc1 = Jsoup.parse(forwardPage);
                    Element formElement1 = doc1.getElementById("openAndPay");
                    if (null != formElement1) {
                        //获取form隐藏表单数据
                        formElement1.select("input[type=\"hidden\"]").forEach(element -> confirmMap.put(element.attr("name"), element.attr("value")));
                        //获取form显示表单数据
                        formElement1.select("input[type=\"text\"]").forEach(element -> confirmMap.put(element.attr("name"), element.attr("value")));
                    }

                }

                paymentResult.put("confirmMap", JSONObject.toJSONString(confirmMap));
            } catch (IOException e) {
                paymentResult.setError(PaymentResultCodeEnum.IO_EXCEPTION);
                log.error("网络异常：", e);
            }
        }

        return paymentResult;
    }

    /**
     * 开卡确认，模拟用户自动提交
     *
     * @param bankCard            银行信息【有效期、安全码】
     * @param messageCode         短信验证码
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/25 9:27
     * @version 1.0
     */
    public static PaymentResult openCardConfirm(UserBankCard bankCard, String messageCode, Map<String, String> resMap) {
        PaymentResult paymentResult = new PaymentResult();

        String defaultCommitMapStr = resMap.get("confirmMap");
        String cookie = resMap.get("cookie");

        //获取上一个请求表单
        Map<String, String> defaultCommitMap1 = null;
        try {
            defaultCommitMap1 = (Map<String, String>) JSON.parse(defaultCommitMapStr);
        } catch (Exception e) {
            paymentResult.setError("参数异常");
            log.error("参数异常：", e);
        }
        //信用卡有效期
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        defaultCommitMap1.put("expired", sdf.format(bankCard.getExpDate()));
        //信用卡背面三位安全码
        defaultCommitMap1.put("cvn2", bankCard.getCvvCode());
        //验证码
        defaultCommitMap1.put("messageCode", messageCode);
        defaultCommitMap1.put("mobile", bankCard.getPhone());
        //模拟提交步骤3
        String toopencardPage = null;
        try {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Mobile Safari/537.36");
            headMap.put("Referer", "https://fzapi.yemadai.com/snc/opencard");
            headMap.put("Cookie", cookie);

            toopencardPage = okHttpUtil.syncPostFormRequest("https://fzapi.yemadai.com/snc/toopencard", headMap, defaultCommitMap1);
        } catch (IOException e) {
            paymentResult.setError(PaymentResultCodeEnum.IO_EXCEPTION);
            log.error("网络异常：", e);
        }
        //模拟提交步骤4
        paymentResult = forwardStep3(toopencardPage, paymentResult);
        return paymentResult;
    }

    /**
     * 发送短信
     *
     * @param bankCard
     * @param
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/24 21:01
     * @version 1.0
     */
    public static PaymentResult sendMessageCode(UserBankCard bankCard, Map<String,String> resMap) {
        PaymentResult paymentResult = new PaymentResult();
        Map<String, String> defaultCommitMap = Maps.newHashMap();
        Map<String, String> reqMap = JSON.parseObject(resMap.get("confirmMap"), Map.class);

        defaultCommitMap.put("xhrFields[withCredentials]", "true");
        defaultCommitMap.put("crossDomain", "true");
        defaultCommitMap.put("cvn", bankCard.getCvvCode());
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        defaultCommitMap.put("expired", sdf.format(bankCard.getExpDate()));
        defaultCommitMap.put("requestNo", reqMap.get("requestNo"));
        try {
            Map<String, String> headMap = Maps.newHashMap();
            headMap.put("Referer", "https://fzapi.yemadai.com/snc/opencard");
            headMap.put("Cookie", resMap.get("cookie"));
            headMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Mobile Safari/537.36");
            String forwardPage = okHttpUtil.syncPostFormRequest("https://fzapi.yemadai.com/snc/sendbindcardmessage", headMap, defaultCommitMap);
            log.info("发送验证码结果: " + forwardPage);
            if (StringUtils.isNotEmpty(forwardPage) && "success".equals(forwardPage)) {
                paymentResult.setSuccess(PaymentResultCodeEnum.SMS_SEND_SUCCESS);
            } else {
                paymentResult.setError(PaymentResultCodeEnum.SMS_SEND_FAIL);
            }
        } catch (IOException e) {
            paymentResult.setError(PaymentResultCodeEnum.IO_EXCEPTION);
            log.error("网络异常：", e);
        }

        return paymentResult;
    }

    /**
     * 跳转step3
     *
     * @param forwardPage
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/25 16:34
     * @version 1.0
     */
    private static PaymentResult forwardStep3(String forwardPage, PaymentResult paymentResult) {
        if (StringUtils.isNotEmpty(forwardPage) && forwardPage.contains("pay_form")) {
            Map<String, String> defaultCommitMap = Maps.newHashMap();
            Document doc = Jsoup.parse(forwardPage);
            Element formElement = doc.getElementById("form1");
            //获取form隐藏表单数据
            formElement.select("input[type=\"hidden\"]").forEach(element -> defaultCommitMap.put(element.attr("name"), element.attr("value")));
            //获取form显示表单数据
            formElement.select("input[type=\"text\"]").forEach(element -> defaultCommitMap.put(element.attr("name"), element.attr("value")));
            try {
                String openCardResult = okHttpUtil.syncPostFormRequest("http://pay.huanqiuhuiju.com/authsys/api/hc/notify/opencardreturn.do", null, defaultCommitMap);
                if (StringUtils.isNotEmpty(openCardResult) && openCardResult.contains("redirect.do?code=SUCCESS&message=开卡成功")) {
                    paymentResult.setSuccess();
                }
            } catch (IOException e) {
                paymentResult.setError(PaymentResultCodeEnum.IO_EXCEPTION);
            }
        } else {
            paymentResult.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
        }

        return paymentResult;
    }


    /**
     * 同名卡交易
     *
     * @param bankCard      银行信息
     * @param subMerchantNo 子商户号
     * @param channel       通道信息（异步通知地址、请求地址）
     * @param payRecord     订单信息→交易金额(元)、商户订单号
     * @param channel       手续费
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @return dsorderid（交易支付通道返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 18:50
     * @version 1.0
     */
    public static PaymentResult pay(UserBankCard bankCard, String subMerchantNo, UserTradePaymentRecord payRecord, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //调用方法
            paramObj.put("methodname", "trade");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
            //银行卡号
            paramObj.put("bankCardNo", bankCard.getBankCardNo());
            //金额 单位(元)
            paramObj.put("amount", amountFormat(payRecord.getAmount()));
            //异步通知地址
            paramObj.put("notifyUrl", channel.getNotifyUrl());
            //手续费
            Double userPayRate = channel.getUserPaymentRate();
            if (null != userPayRate) {
                //计算手续费,单位元
                paramObj.put("userFee", amountFormat(userPayRate * payRecord.getAmount()));
            }
            //行业编码 不传则随机跳，可以通过接口获得支持的行业，请求地址如下
            //paramObj.put("mcc", "");
            //营业执照号 可以指定消费商户
            //paramObj.put("companycode", "");

        }

        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = sendPayRequest("404", paramObj, channel, payRecord.getOrderId());
            //解析响应结果
            return getPayResult(huanqiuResult);

        }
    }


    /**
     * 绑定提现卡
     *
     * @param subMerchantNo 子商户号
     * @param withdrawCard  银行卡号、银行名称
     * @param channel       通道信息（异步通知地址、请求地址）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @return dsorderid（绑定结果通道返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 19:10
     * @version 1.0
     */
    public static PaymentResult bindWithdrawCard(String subMerchantNo, UserBankCardVo withdrawCard, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //调用方法
            paramObj.put("methodname", "bindCard");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
            //银行卡号
            paramObj.put("bankCardNo", withdrawCard.getBankCardNo());
            //银行名称
            //银行名称
            String bankName = withdrawCard.getBankName();
            String openingBank = withdrawCard.getOpeningBank();
            paramObj.put("bankName", StringUtils.isEmpty(bankName) ? openingBank : bankName);
        }
        PaymentResult paymentResult = new PaymentResult();
        //发送请求、获取响应、返回
        HuanqiuResultVO huanqiuResult = sendRequest("404", paramObj, channel);

        //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20190102162145690001","sign":"f4013332ebe0746d2912b1af9207f453","ordersn":"20190102162145690000","returncode":"0000",
        // "message":"{\"code\":\"SUCCESS\",\"message\":\"成功\"}"}
        //解析响应结果
        HuanqiuResultVO newHuanqiuResult = getResult(huanqiuResult, null);
        if (newHuanqiuResult.isSuccess()) {
            paymentResult.setSuccess();
            //返回通道订单号
            paymentResult.put("dsorderid", newHuanqiuResult.getDsorderid());
        } else {
            paymentResult.setError(newHuanqiuResult.getMessage());
        }

        return paymentResult;
    }

    /**
     * 解绑提现卡
     *
     * @param subMerchantNo 子商户号
     * @param withdrawCard  银行卡号、银行名称
     * @param channel       通道信息（异步通知地址、请求地址）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @return dsorderid（解绑结果通道返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 19:10
     * @version 1.0
     */
    public static PaymentResult unbindWithdrawCard(String subMerchantNo, UserBankCardVo withdrawCard, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //调用方法
            paramObj.put("methodname", "unbindCard");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
            //银行卡号
            paramObj.put("bankCardNo", withdrawCard.getBankCardNo());
            //银行名称
            paramObj.put("bankName", withdrawCard.getBankName());
        }

        PaymentResult paymentResult = new PaymentResult();
        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = sendRequest("404", paramObj, channel);

            //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20190102162258513001","sign":"a32ad418cbceed0e51ce84da5776ec5d","ordersn":"20190102162258513000","returncode":"0000",
            // "message":"{\"code\":\"SUCCESS\",\"message\":\"成功\"}"}
            //解析响应结果
            HuanqiuResultVO newHuanqiuResult = getResult(huanqiuResult, null);
            if (newHuanqiuResult.isSuccess()) {
                //返回通道订单号
                paymentResult.put("dsorderid", newHuanqiuResult.getDsorderid());
                paymentResult.setSuccess();
            } else {
                paymentResult.setError(newHuanqiuResult.getMessage());
            }
        }

        return paymentResult;
    }

    /**
     * 提现结果
     *
     * @author uncle.quentin
     * @date   2019/1/3 16:42
     * @param   result
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @version 1.0
     */
    private static PaymentResult getWithdrawResult(HuanqiuResultVO result){
        PaymentResult paymentResult = new PaymentResult();
        //返回响应结果
        if (null == result) {
            result = new HuanqiuResultVO();
            paymentResult.setError(PaymentResultCodeEnum.FAIL);
        }

        //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20181227184011567001","sign":"35e1b27118fdc0aeffd881cdb145a6fd","ordersn":"20181227184011567000","returncode":"0003",
        // "message":"{\"merchantOrderNo\":\"hc2018122718364900100101354\",\"subMerchantNo\":\"24656886\",\"amount\":\"1.0\",\"userFee\":\"0.06\",\"cardNo\":\"6221550859466827\",\"code\":\"SUCCESS\",\"message\":\"支付成功\"}"}
        if (StringUtils.isEmpty(result.getMessage())) {
            //errtext不为空、没有message信息
            if (StringUtils.isNotEmpty(result.getErrtext())) {
                paymentResult.setError(result.getErrtext());
            } else {
                paymentResult.setError(PaymentResultCodeEnum.WITHDRAW_FAIL);
            }
        } else {
            //解析message明细信息
            JSONObject resultObj = JsonUtil.strToJson(result.getMessage());
            String resultDetailCode = resultObj.getString("code");
            String resultDetailMessage = resultObj.getString("message");

            if (StringUtils.isNotEmpty(resultDetailCode)) {
                //returnCode等于0003（支付处理中）并且message下面的code等于SUCCESS为下单成功
                if (HuanqiuResultVO.DATASUCCESS.equals(resultDetailCode)) {
                    if ((HuanqiuResultEnum.HUANQIU_RESUL_SUCCESS.getKey().equals(result.getReturncode()))) {
                        paymentResult.setSuccess(PaymentResultCodeEnum.WITHDRAW_SUCCESS);
                    } else if (HuanqiuResultEnum.HUANQIU_RESUL_PROCESSING.getKey().equals(result.getReturncode())) {
                        paymentResult.setSuccess(PaymentResultCodeEnum.WITHDRAW_ING);
                    } else {
                        paymentResult.setError(PaymentResultCodeEnum.WITHDRAW_FAIL);
                    }
                } else {
                    if (StringUtils.isEmpty(resultDetailMessage)) {
                        paymentResult.setError(PaymentResultCodeEnum.WITHDRAW_FAIL);
                    } else {
                        paymentResult.setError(resultDetailMessage);
                    }
                }
            } else {
                paymentResult.setError(PaymentResultCodeEnum.WITHDRAW_FAIL);
            }

        }

        //返回通道订单号
        if (StringUtils.isNotEmpty(result.getDsorderid())) {
            paymentResult.put("dsorderid", result.getDsorderid());
        }

        return paymentResult;
    }

    /**
     * 子商户提现
     *
     * @param subMerchantNo    子商户号
     * @param withdrawCard     银行卡号、银行名称
     * @param channel          通道信息（异步通知地址、请求地址）
     * @param amount           提现金额（已经扣除手续费，即userFee不用传手续费给通道，否则到账金额会减去userFee的手续费）
     * @param channel（userFee） 商户收取用户手续费
     * @return dsorderid（提现通道返回订单号，可用于查询接口）
     * @author uncle.quentin
     * @date 2018/12/21 19:14
     * @version 1.0
     */
    public static PaymentResult withdraw(String subMerchantNo, UserBankCardVo withdrawCard, Double amount, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        {
            //调用方法
            paramObj.put("methodname", "withdraw");
            //子商户号
            paramObj.put("subMerchantNo", subMerchantNo);
            //银行卡号
            paramObj.put("bankCardNo", withdrawCard.getBankCardNo());
            //银行名称
            String bankName = withdrawCard.getBankName();
            String openingBank = withdrawCard.getOpeningBank();
            paramObj.put("bankName", StringUtils.isEmpty(bankName) ? openingBank : bankName);
            //异步通知地址
            paramObj.put("notifyUrl", channel.getNotifyUrl());
            //提现金额
            paramObj.put("amount", amountFormat(amount));
            //代付手续费 商户收取用户的代付手续费
            paramObj.put("userFee", 0);
        }

        PaymentResult paymentResult = new PaymentResult();

        //发送请求、获取响应、返回
        {
            HuanqiuResultVO huanqiuResult = sendRequest("404", paramObj, channel);

            //{"transcode":"404","merchno":"youka2018122001","dsorderid":"20190102162451557001","sign":"6afd309a8953b1d2c982591a3f82ac05","ordersn":"20190102162451557000","returncode":"0003",
            // "message":"{\"merchantFee\":\"0.50\",\"code\":\"SUCCESS\",\"message\":\"成功\"}"}
            //解析响应结果
            return getWithdrawResult(huanqiuResult);
        }

    }

    /**
     * 代收付查询接口
     *
     * @param transtype 交易类型[55:交易、56:提现、57:开卡、58:绑卡、59:入驻]
     * @param osubNo    订单号（支付通道订单号）
     * @param channel   通道信息（异步通知地址、请求地址）
     * @return com.slljr.finance.payment.utils.HuanqiuResultVO
     * @author uncle.quentin
     * @date 2018/12/21 19:17
     * @version 1.0
     */
    public static PaymentResult queryOrder(HuanqiuTransTypeEnum transtype, String osubNo, PaymentChannel channel) {
        //业务参数
        Map<String, Object> paramObj = Maps.newHashMap();
        //交易类型
        paramObj.put("transtype", transtype.getKey());

        PaymentResult paymentResult = new PaymentResult();


        //发送请求、获取响应、返回
        {
            //发送请求、获取响应、返回
            HuanqiuResultVO huanqiuResult = sendRequest("902", paramObj, osubNo, channel);

            //{"transcode":"902","merchno":"youka2018122001","dsorderid":"20190102162451557001","sign":"dadb0bd46cba9b285792cb8bfad88402","ordersn":"20190102162750296000",
            // "returncode":"0000","errtext":"成功","orderid":"hc2019010216211800100118925","amount":"1.88","status":"00","transtype":"56","currency":"CNY",
            // "paytime":"20190102162131","signType":"MD5","message":"SUCCESSnull"}
            //解析响应结果
            if (null != huanqiuResult) {
                if (HuanqiuResultEnum.HUANQIU_RESUL_SUCCESS.getKey().equals(huanqiuResult.getReturncode())) {
                    PaymentResultCodeEnum paymentResultCodeEnum = getCodeEnum(huanqiuResult.getStatus(), transtype);
                    if (null != paymentResultCodeEnum) {
                        if (PaymentResultCodeEnum.SUCCESS == paymentResultCodeEnum) {
                            switch (transtype) {
                                case TRANSACTION:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.PAY_SUCCESS);
                                    break;
                                case WITHDRAW:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.WITHDRAW_SUCCESS);
                                    break;
                                case OPEN_CARD:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.BIND_CARD_SUCCESS);
                                    break;
                                case BINDCARD:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.OPEN_CARD_SUCCESS);
                                    break;
                                case ENTERING:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.REGISTER_SUCCESS);
                                    break;
                                default:
                                    paymentResult.setSuccess(PaymentResultCodeEnum.SUCCESS);
                                    break;
                            }

                        } else {
                            paymentResult.setError(paymentResultCodeEnum.getMsg());
                        }
                    }
                } else {
                    paymentResult.setError(huanqiuResult.getMessage());
                }
            } else {
                paymentResult.setError(PaymentResultCodeEnum.FAIL);
            }

        }

        return paymentResult;
    }

    /**
     * 金额格式化,向上取整,保留两位小数
     * @param amount
     * @return
     */
    private static Double amountFormat(Double amount){
        return new BigDecimal(amount).setScale(2, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 转换订单状态
     *
     * @param trxstatus
     * @return com.slljr.finance.common.enums.PaymentResultCodeEnum
     * @author uncle.quentin
     * @date 2019/1/2 16:50
     * @version 1.0
     */
    private static PaymentResultCodeEnum getCodeEnum(String trxstatus, HuanqiuTransTypeEnum transtype) {
        if (trxstatus == null) {
            return null;
        }

        switch (trxstatus) {
            case "00":
                return PaymentResultCodeEnum.SUCCESS;
            case "01":
                return PaymentResultCodeEnum.PROCESSING;
            case "02":
                return PaymentResultCodeEnum.FAIL;
            case "99":
                return PaymentResultCodeEnum.ORDER_NOT_EXIST;
            default:
                break;

        }

        return null;
    }
}
