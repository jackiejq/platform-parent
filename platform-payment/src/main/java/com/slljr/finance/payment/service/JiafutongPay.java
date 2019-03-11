package com.slljr.finance.payment.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.utils.OKHttpUtils;
import com.slljr.finance.payment.utils.PaymentResult;
import com.slljr.finance.payment.utils.ZMRequestUtils;
import com.slljr.finance.payment.utils.ZMUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 佳付通支付接口
 * 功能: 快捷支付(支付,ps:会自动提款)
 */
public class JiafutongPay {
    static Logger logger = LogManager.getLogger(JiafutongPay.class);
    static OKHttpUtils httpUtils = OKHttpUtils.getInstance();

    /***
     * 用户注册
     * @param userBasic 用户基础信息
     * @param bankCard 用户储蓄卡
     * @return 支付平台用户id
     */
    public static PaymentResult register(UserBasic userBasic, UserBankCardVo bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //商户姓名
        paramMap.put("realName", userBasic.getName());
        //商户地址
        paramMap.put("merAddress", userBasic.getIdcAddress());
        //身份证号码
        paramMap.put("idCard", userBasic.getIdCard());
        //手机号码
        paramMap.put("mobile", bankCard.getPhone());
        //帐户姓名
        paramMap.put("accountName", userBasic.getName());
        //借记卡号
        paramMap.put("accountNo", bankCard.getBankCardNo());
        //银行预留手机号码
        paramMap.put("reservedMobile", bankCard.getPhone());
        //联行号
        paramMap.put("bankCode", ZMUtils.getBankCode(bankCard.getBankName()).split(",")[0]);
                        
        PaymentResult res = ZMRequestUtils.execute("/jft/memberRegister", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"chMerCode=101003336821"}
        //{"key":"111","msg":"手机号已经存在","data":""}
        String cusid = res.get("chMerCode");
        if ("000".equals(res.get("key")) && StringUtils.isNotBlank(cusid)){
            res.setSuccess();
            //注册成功自动开通业务
            res = businessOpen(cusid, channel);
            res.put("chMerCode", cusid);
        }

        return res;
    }



    /**
     * 申请快捷支付
     * @param cusid
     * @param orderid
     * @param amount
     * @param userBasic
     * @param bankCard
     * @return
     */
    public static PaymentResult applyQuickPay(String cusid, String orderid, Double amount, UserBasic userBasic, UserBankCard bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //chMerCode：平台商户编号
        paramMap.put("chMerCode", cusid);
        //orderNo：商户订单号（唯一）
        paramMap.put("orderNo", orderid);
        //orderAmount：金额（元,精确到分）
        paramMap.put("orderAmount", String.valueOf(amount));
        //busCode：业务编码（默认2001）
        paramMap.put("busCode", "2001");
        // realName：用户姓名
        paramMap.put("realName", userBasic.getName());
        //idCard：证件号码
        paramMap.put("idCard", userBasic.getIdCard());
        //accountNo：信用银行卡号
        paramMap.put("accountNo", bankCard.getBankCardNo());
        //mobile：银行预留手机号码
        paramMap.put("mobile", bankCard.getPhone());
        // frontUrl：前端跳转地址
        paramMap.put("frontUrl", ZMUtils.str2HexStr("url"));
        //callBackUrl：回调通知地址
        paramMap.put("callBackUrl", ZMUtils.str2HexStr(channel.getNotifyUrl()));
             
        PaymentResult res = ZMRequestUtils.execute("/jft/quickPay", paramMap, channel);
        if ("000".equals(res.get("key"))) res.setSuccess();
        if (res.isSuccess()){
            String resultType = res.get("resultType");
            String html = null;
            switch (resultType){
                case "url_address":
                    String gotoUrl = res.get("url");
                    try {
                        html = httpUtils.syncGetRequest(gotoUrl);
                    } catch (IOException e){
                        res.setError(PaymentResultCodeEnum. IO_EXCEPTION);
                        logger.error(String.format("佳付通申请快捷支付网络异常!req:%s,res:%s,error:%s", JSON.toJSONString(paramMap), res.toString(), e.getMessage()));
                    }
                    break;
                case "html_text":
                    html = res.get("html");
                    if (StringUtils.isBlank(html)){
                        res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
                        logger.error("佳付通申请快捷支付返回空数据!req:%s,res:%s" + JSON.toJSONString(paramMap), res.toString());
                    }
                    break;
                default:
                    res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
                    logger.error("错误!佳付通申请快捷支付返回结果改版1!req:%s,res:%s" + JSON.toJSONString(paramMap), res.toString());
                    break;
            }
            if (StringUtils.isBlank(html)){
                res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
                return res;
            }
            if (!html.contains("submitForm")){
                res.setError(PaymentResultCodeEnum.RESULT_FORMAT_CHANGE_EXCEPTION);
                logger.error("错误!佳付通申请快捷支付返回结果改版2!req:%s,res:%s" + JSON.toJSONString(paramMap), res.toString());
                return res;
            }

            Document doc = Jsoup.parse(html);
            Element formElement = doc.getElementById("submitForm");
            //获取form隐藏表单数据
            formElement.select("input[type=\"hidden\"]").forEach(element -> {
                paramMap.put(element.attr("name"), element.attr("value"));
            });
            //获取form显示表单数据
            formElement.select("input[type=\"text\"]").forEach(element -> {
                paramMap.put(element.attr("name"), element.attr("value"));
            });

            res.put("formMap", JSON.toJSONString(paramMap));
        }
        //{"key":"000","msg":"交易成功","data":"chMerCode=207717506313&orderNo=1231111213&tranAmount=10&tranTime=20181227184221&chSerialNo=20181227184221542966&tranStatus=3&resultType=url_address&url=https://client-api.verajft.com/fusionPosp/interface/toPayOrder?ID=20181227184221542966&html="}
        //{"key":"108","msg":"订单已存在","data":""}
        if ("108".equals(res.get("key"))){ res.setError(PaymentResultCodeEnum.ORDER_EXISTS);}
        else if("000".equals(res.get("key"))){ res.setSuccess(PaymentResultCodeEnum.SMS_MANUAL_SEND);}              
        else {res.setError(PaymentResultCodeEnum.PAY_FAIL);}
        return res;
    }

    //formMap申请支付获取页面数据
    public static PaymentResult resendQuickPaySms(UserBankCard bankCard, Map<String, String> resMap){
        PaymentResult res = new PaymentResult();

        String url = "https://client-api.verajft.com/fusionPosp/interface/getCheckCode";
        Map<String, String> formMap = JSON.parseObject(resMap.get("formMap"), Map.class);
        //填充值
        formMap.put("cvv", bankCard.getCvvCode());
        formMap.put("validityDate", new SimpleDateFormat("MMyy").format(bankCard.getExpDate()));
        formMap.put("checkCode", "");
        try {
            String text = httpUtils.syncPostFormRequest(url, null, formMap);
            //{"resCode":"0000","resMsg":{"sign":"","tphtrxcrtime":"","tphtrxid":0,"trxflag":"trx","trxsn":""}}
            JSONObject json = JSONObject.parseObject(text);
            if ("0000".equals(json.getString("resCode"))){
                // if(data.resCode=="0000"){//请求成功
                //$("input[name='sys_trade_no']").val(data.resMsg);
                formMap.put("sys_trade_no", json.getString("resMsg"));
                res.setSuccess(PaymentResultCodeEnum.SMS_SEND_SUCCESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static PaymentResult confirmQuickPay(UserBankCard bankCard, String smscode, Map<String, String> resMap){
        PaymentResult res = new PaymentResult();

        String url = "https://client-api.verajft.com/fusionPosp/interface/submitOrder";
        Map<String, String> formMap = JSON.parseObject(resMap.get("formMap"), Map.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://client-api.verajft.com");
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36");

        //填充值
        formMap.put("cvv", bankCard.getCvvCode());
        formMap.put("validityDate", new SimpleDateFormat("MMyy").format(bankCard.getExpDate()));
        formMap.put("checkCode", smscode);



        try {
            String text = httpUtils.syncPostFormRequest(url, headers, formMap);
            /**注意,有些银行卡未开通快捷支付,还需开通快捷支付**/
            if (text.contains("id=\"submitUrl\"") && text.contains("id=\"AutoJump\"")){
                Document doc = Jsoup.parse(text);
                //同意开通快捷支付
                String jumpUrl1 = doc.getElementById("submitUrl").val();
                Map<String,String> jumpParams = new HashMap<>();
                Element formElement = doc.getElementById("AutoJump");
                //获取form隐藏表单数据
                formElement.select("input[type=\"hidden\"]").forEach(element -> {
                    jumpParams.put(element.attr("name"), element.attr("value"));
                });
                //获取form显示表单数据
                formElement.select("input[type=\"text\"]").forEach(element -> {
                    jumpParams.put(element.attr("name"), element.attr("value"));
                });
                //初始页https://finance.chinapnr.com/npay/merchantRequest?cmd_id=217&version=10&mer_cust_id=6666000005204072&check_value=xx
                text = httpUtils.syncPostFormRequest(jumpUrl1, headers, jumpParams);

                //开通快捷支付http
                String baseHttp = "https://finance.chinapnr.com";
                //同意开通快捷支付
                if (text.contains("integration/agreementConfirm")){
                    jumpParams.clear();
                    doc = Jsoup.parse(text);
                    formElement = doc.getElementById("AutoJump");

                    String jumpUrl2 = baseHttp + doc.getElementsByTag("form").get(0).attr("action");
                    //获取form隐藏表单数据
                    formElement.select("input[type=\"hidden\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });
                    //获取form显示表单数据
                    formElement.select("input[type=\"text\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });

                    //同意开通快捷支付https://finance.chinapnr.com/npay/merchantRequest/integration/agreementConfirm
                    text = httpUtils.syncPostFormRequest(jumpUrl2, headers, jumpParams);
                }


                //验证银行卡信息
                if (text.contains("integration/qpCardBindConfirm")){
                    jumpParams.clear();
                    doc = Jsoup.parse(text);
                    formElement = doc.getElementsByTag("form").get(0);

                    String jumpUrl3 = baseHttp + StringUtils.substringBetween(text, "var url='", "';");
                    //获取form隐藏表单数据
                    formElement.select("input[type=\"hidden\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });
                    //获取form显示表单数据
                    formElement.select("input[type=\"text\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });

                    //填充信用卡有效期和安全码
                    SimpleDateFormat mmyySdf = new SimpleDateFormat("MMyy");
                    jumpParams.put("creditValiDate", mmyySdf.format(bankCard.getExpDate()));
                    jumpParams.put("cvv2", bankCard.getCvvCode());

                    //验证银行卡信息https://finance.chinapnr.com/npay/merchantRequest/integration/qpCardBindConfirm
                    text = httpUtils.syncPostFormRequest(jumpUrl3, headers, jumpParams);
                }

                //获取支付结果
                if (text.contains("huifu/frontCallBack")){
                    jumpParams.clear();
                    doc = Jsoup.parse(text);
                    formElement = doc.getElementById("autoForm");

                    String jumpUrl4 = formElement.attr("action");
                    //获取form隐藏表单数据
                    formElement.select("input[type=\"hidden\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });
                    //获取form显示表单数据
                    formElement.select("input[type=\"text\"]").forEach(element -> {
                        jumpParams.put(element.attr("name"), element.attr("value"));
                    });

                    //https://client-api.verajft.com/fusionPosp/huifu/frontCallBack
                    text = httpUtils.syncPostFormRequest(jumpUrl4, headers, jumpParams);
                }
            }

            if (text.contains("<h2 class=\"msg__title\">交易已受理</h2>")){
                res.setSuccess(PaymentResultCodeEnum.PAY_ING);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 订单查询接口
     * @param orderid
     * @return
     */
    public static PaymentResult orderQuery(String orderid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //orderNo：订单号
        paramMap.put("orderNo", orderid);  
        //{"key":"000","msg":"","data":"orderno=20181225151905056000&amount=10.00&status=1&msg=关联订单不存在,请先申请支付"}
        //{"key":"000","msg":"","data":"orderno=201901141630571550952&amount=93.00&status=3&msg=交易成功"}
        //status：状态码（0：处理中、1：失败、2：成功、3：待支付）
        PaymentResult res = ZMRequestUtils.execute("/jft/tradeQuery", paramMap, channel);
        String trxstatus = res.get("status");
        if ("000".equals(res.get("key")) && "0".equals(trxstatus)){ res.setError(PaymentResultCodeEnum.PAY_ING);}
        else if("000".equals(res.get("key")) && "2".equals(trxstatus)){ res.setSuccess(PaymentResultCodeEnum.SUCCESS);}
        else if("000".equals(res.get("key")) && "1".equals(trxstatus)){ res.setError(PaymentResultCodeEnum.PAY_FAIL);}
        else if("000".equals(res.get("key")) && "3".equals(trxstatus) && ("交易成功".equals(res.get("msg")) || res.get("msg").contains("成功") )){ res.setError(PaymentResultCodeEnum.PAY_ING);}
        else {res.setError(PaymentResultCodeEnum.PAY_WAIT);}
        return res;
    }


    /**
     * 商户结算卡(储蓄卡)修改
     * @param cusid 支付平台用户id
     * @param userBasic
     * @param bankCard
     * @return
     */
    public static PaymentResult updateDepositCard(String cusid, UserBasic userBasic, UserBankCard bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //chMerCode：平台商户编号
        paramMap.put("chMerCode", cusid);
        //accountName：帐户姓名
        paramMap.put("accountName", userBasic.getName());
        // accountNo：借记卡号
        paramMap.put("accountNo",bankCard.getBankCardNo() );
        //reservedMobile：银行预留手机号码
        paramMap.put("reservedMobile", bankCard.getPhone());
        // bankCode：联行号
        paramMap.put("bankCode", "308584000013");
        PaymentResult res = ZMRequestUtils.execute("/jft/memberBankCardModify", paramMap, channel);
       //返回结果: {"key":"115","msg":"商户修改结算卡失败","data":""}UPDATE_BANK_CARD_FAIL
        if ("000".equals(res.get("key"))){ res.setError(PaymentResultCodeEnum.SUCCESS);}       
        else{res.setSuccess(PaymentResultCodeEnum.PAY_FAIL);}
        return res;
    }



    /**
     * 商户开通业务
     */
    private static PaymentResult businessOpen(String cusid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //chMerCode：平台商户编号
        paramMap.put("chMerCode", cusid);
        //busCode：业务编码（默认2001）
        paramMap.put("busCode", "2001");
        //drawFee：单笔手续费（单位元）
        paramMap.put("drawFee", String.valueOf(channel.getUserWithdrawCharge()));
        //tradeRate：交易费率（eg：0.4%=0.004）
        paramMap.put("tradeRate", String.valueOf(channel.getUserPaymentRate()));
        PaymentResult res = ZMRequestUtils.execute("/jft/memberBusinessOpen", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"cusid=101003336821&outcusid=1000001"}
        //"key":"111","msg":"手机号已经存在","data":""
        if ("000".equals(res.get("key"))) {res.setSuccess();}
        else if ("111".equals(res.get("key"))){res.setSuccess(PaymentResultCodeEnum.PHONE_EXISTS); }
        else {res.setError(PaymentResultCodeEnum.REGISTER_FAIL);}
        return res;
    }

    /**
     * 更新用户费率
     */
    public static PaymentResult rateUpdate(String cusid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //chMerCode：平台商户编号
        paramMap.put("chMerCode", cusid);
        //busCode：业务编码（默认2001）
        paramMap.put("busCode", "2001");
        //drawFee：单笔手续费（单位元）
        paramMap.put("drawFee", String.valueOf(channel.getUserWithdrawCharge()));
        //tradeRate：交易费率（eg：0.4%=0.004）
        paramMap.put("tradeRate", String.valueOf(channel.getUserPaymentRate()));
        PaymentResult res = ZMRequestUtils.execute("/jft/memberRateModify", paramMap, channel);
        if ("000".equals(res.get("key"))) res.setSuccess();
        return res;
    }


    private static PaymentResultCodeEnum getCodeEnum(String trxstatus){
        if (trxstatus == null) return null;

        switch (trxstatus){
	        case "5001":
	            return PaymentResultCodeEnum.PAY_WAIT;//待支付
	        case "5002":
	            return PaymentResultCodeEnum.PAY_ING;//支付中
	        case "5003":
	            return PaymentResultCodeEnum.PAY_SUCCESS;//支付成功
	        case "5004":
	            return PaymentResultCodeEnum.PAY_FAIL;//支付失败                  
        }

        return  null;
    }


    
}
