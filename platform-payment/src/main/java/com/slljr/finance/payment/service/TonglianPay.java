package com.slljr.finance.payment.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.pojo.model.*;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.payment.utils.PaymentResult;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.payment.utils.ZMRequestUtils;
import com.slljr.finance.payment.utils.ZMUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通联支付接口
 * 功能: 快捷支付(支付+提款)/小额免密(支付+提款)
 */
public class TonglianPay {
    static SimpleDateFormat validdateSdf = new SimpleDateFormat("MMyy");
    /***
     * 用户注册,并绑定储蓄卡(重复注册,返回相同结果)
     * @param userBasic
     * @param bankCard
     * @return 返回支付平台用户id(cusid)
     */
    public static PaymentResult register(UserBasic userBasic, UserBankCardVo bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //商户外部唯一标记
        paramMap.put("outcusid", String.valueOf(userBasic.getId()));
        //帐户名(姓名)
        paramMap.put("acctname", userBasic.getName());
        //身份证号码
        paramMap.put("idno", userBasic.getIdCard());

        String areaCode = ZMUtils.getAreaCode(userBasic.getIdcCity());
        //所在省
        paramMap.put("merprovice", areaCode.split(",")[1]);
        //所在市
        paramMap.put("areacode", areaCode.split(",")[0]);
        //注册地址
        paramMap.put("address", userBasic.getIdcAddress());
        //银行卡预留手机号码
        paramMap.put("phone", bankCard.getPhone());
        //帐户号(储蓄卡号)
        paramMap.put("acctid", bankCard.getBankCardNo());
        //提现手续费(单位元/笔)
        paramMap.put("settfee", String.valueOf(channel.getUserWithdrawCharge()));
        //产品列表(json格式)
        paramMap.put("prodlist", getProducts(channel).toJSONString());

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/addcus", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"cusid=101003336821&outcusid=1000001"}
        if ("000".equals(res.get("key")) && StringUtils.isNotBlank(res.get("cusid"))){ res.setSuccess(); }

        return res;
    }

    /**
     * 绑定信用卡
     * @param cusid
     * @param userBasic
     * @param payCard
     * @return 返回thpinfo, 下次请求需带上
     */
    public static PaymentResult bindCard(String cusid, UserBasic userBasic, UserBankCardVo payCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid",cusid);
        //商户用户号(本公司用户id)
        paramMap.put("meruserid", String.valueOf(userBasic.getId()));
        //帐户名
        paramMap.put("acctname", userBasic.getName());
        //身份证号
        paramMap.put("idno",userBasic.getIdCard());
        //银行卡
        paramMap.put("cardno", payCard.getBankCardNo());
        //卡类型(00：借记卡、02：信用卡)
        paramMap.put("accttype", "02");
        //有效期（MMyy）
        paramMap.put("validdate", validdateSdf.format(payCard.getExpDate()));
        //cvv2
        paramMap.put("cvv2", payCard.getCvvCode());
        //银行预留手机号码
        paramMap.put("tel", payCard.getPhone());

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/bindcard", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"trxstatus=1999&agreeid=&memid=&thpinfo="}
        //{"key":"000","msg":"处理成功","data":"trxstatus=0000&agreeid=201812251135425716&memid=&thpinfo="}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) {res.setSuccess(PaymentResultCodeEnum.SUCCESS); }
        else if ("1999".equals(trxstatus)){res.setSuccess(PaymentResultCodeEnum.SMS_AUTO_SEND); }
        else {res.setError(getCodeEnum(trxstatus));}

        return res;
    }


    /**
     * 绑卡确认(输入短信验证码)
     * @param cusid
     * @param smscode
     * @param userBasic
     * @param bankCard
     * @return 返回该银行卡绑定的协议编号(agreei),支付时需要
     */
    public static PaymentResult bindCardConfirm(String cusid, String smscode, Map<String,String> resMap, UserBasic userBasic, UserBankCardVo bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid",cusid);
        //商户用户号(本公司用户id)
        paramMap.put("meruserid", String.valueOf(userBasic.getId()));
        //帐户名
        paramMap.put("acctname", userBasic.getName());
        //身份证号
        paramMap.put("idno",userBasic.getIdCard());
        //银行卡
        paramMap.put("cardno", bankCard.getBankCardNo());
        //卡类型(00：借记卡、02：信用卡)
        paramMap.put("accttype", "02");
        //有效期（MMyy）
        paramMap.put("validdate", validdateSdf.format(bankCard.getExpDate()));
        //cvv2
        paramMap.put("cvv2", bankCard.getCvvCode());
        //银行预留手机号码
        paramMap.put("tel", bankCard.getPhone());
        //短信验证码
        paramMap.put("smscode", smscode);
        //交易透传信息
        paramMap.put("thpinfo", resMap.get("thpinfo"));

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/bindcardconfirm", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"trxstatus=0000&agreeid=201812251135425716&memid="}
        //{"key":"112","msg":"您输入的身份验证信息有误，请确认后重试[1020305]","data":""}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus) && StringUtils.isNotBlank(res.get("agreeid"))){res.setSuccess(PaymentResultCodeEnum.BIND_CARD_SUCCESS);}
        else if("112".equals(res.get("key"))) res .setError("身份证信息错误");
        else {res.setError(getCodeEnum(trxstatus));}

        return res;
    }

    /**
     * 重新发送绑卡验证码
     */
    public static PaymentResult resendBindSms(String cusid, Map<String,String> resMap, UserBasic userBasic, UserBankCard bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid",cusid);
        //商户用户号(本公司用户id)
        paramMap.put("meruserid", String.valueOf(userBasic.getId()));
        //帐户名
        paramMap.put("acctname", userBasic.getName());
        //身份证号
        paramMap.put("idno",userBasic.getIdCard());
        //银行卡
        paramMap.put("cardno", bankCard.getBankCardNo());
        //卡类型(00：借记卡、02：信用卡)
        paramMap.put("accttype", "02");
        //有效期（MMyy）
        paramMap.put("validdate", validdateSdf.format(bankCard.getExpDate()));
        //cvv2
        paramMap.put("cvv2", bankCard.getCvvCode());
        //银行预留手机号码
        paramMap.put("tel", bankCard.getPhone());
        //交易透传信息
        paramMap.put("thpinfo", resMap.get("thpinfo"));

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/resendbindsms", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":""}
        if ("000".equals(res.get("key"))){
            res.setSuccess();
        }
        return res;
    }

    /**
     * 申请快捷支付
     * @param cusid
     * @param orderid
     * @param agreeid 已签约银行卡协议id
     * @param amount, 单位:元
     * @param subject
     * @return 返回thpinfo/trxid, 确认支付需带上
     */
    public static PaymentResult applyQuickPay(String cusid, String orderid, String agreeid, Double amount, String subject, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //订单号（唯一性）
        paramMap.put("orderid", orderid);
        //签约协议编号
        paramMap.put("agreeid", agreeid);
        //交易类型（QUICKPAY_OF_NP：线下商户、QUICKPAY_OL_HP：线上全渠道）
        paramMap.put("trxcode", JSON.parseObject(channel.getOtherParams()).getString("trxcode"));
        //订单金额（单位分）
        paramMap.put("amount", String.valueOf(Double.valueOf(amount * 100).intValue()));
        //订单内容
        paramMap.put("subject", subject);
        //回调通知地址（16进制单独编码,支持地址后面带参数）
        paramMap.put("notify_url", ZMUtils.str2HexStr(channel.getNotifyUrl()));

        PaymentResult res = ZMRequestUtils.execute("/tl/applypay", paramMap, channel);
        //{"key":"113","msg":"代理商户产品日交易总额超限","data":""}
        //{"key":"000","msg":"处理成功","data":"orderid=20181225120030974000&trxstatus=1999&errmsg=请输入短信验证码&trxid=18120004003929&fintime=&thpinfo={\"sign\":\"\",\"tphtrxcrtime\":\"\",\"tphtrxid\":0,\"trxflag\":\"trx\",\"trxsn\":\"\"}"}
        String trxstatus = res.get("trxstatus");

        if ("1999".equals(res.get("trxstatus"))){res.setSuccess(PaymentResultCodeEnum.SMS_AUTO_SEND);}
        else {res.setError(getCodeEnum(trxstatus));}

        return res;
    }

    /**
     * 确认快捷支付
     */
    public static PaymentResult confirmQuickPay(String cusid, String agreeid, String smscode, Map<String,String> resMap, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //平台的交易流水号
        paramMap.put("trxid", resMap.get("trxid"));
        //签约协议编号
        paramMap.put("agreeid", agreeid);
        //短信验证码
        paramMap.put("smscode", smscode);
        //交易透传信息
        paramMap.put("thpinfo", resMap.get("thpinfo"));

        PaymentResult res = ZMRequestUtils.execute("/tl/confirmpay", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"trxstatus=2000&errmsg=支付请求成功&trxid=18120004003929&fintime=&thpinfo="}
        //{"key":"000","msg":"处理成功","data":"trxstatus=3057&errmsg=请重新获取验证码&trxid=18120004060138&fintime=&thpinfo="}
        String trxstatus = res.get("trxstatus");
        if ("2000".equals(trxstatus)) res.setSuccess(PaymentResultCodeEnum.PAY_ING);
        else {res.setError(getCodeEnum(trxstatus));}

        return res;
    }

    /**
     * 重新发送快捷支付短信验证码
     */
    public static PaymentResult resendQuickpaySms(String cusid, String agreeid, Map<String,String> resMap, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //平台的交易流水号
        paramMap.put("trxid", resMap.get("trxid"));
        //签约协议编号
        paramMap.put("agreeid", agreeid);
        //交易透传信息
        paramMap.put("thpinfo", resMap.get("thpinfo"));


        PaymentResult res = ZMRequestUtils.execute("/tl/paysms", paramMap, channel);
        if ("000".equals(res.get("key"))) res.setSuccess(PaymentResultCodeEnum.SMS_SEND_SUCCESS);
        return res;
    }

    /**
     * 快捷支付提现
     */
    public static PaymentResult quickPayWithdraw(String cusid, UserTradePaymentRecord recRecord, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //平台的交易流水号 trxid
        paramMap.put("trxid", JSON.parseObject(recRecord.getOtherParams()).getString("trxid"));
        //订单号（唯一性、与快捷扣款订单号不同）
        paramMap.put("orderid", recRecord.getOrderId());
        //回调通知地址（16进制单独编码,支持地址后面带参数）
        paramMap.put("notify_url", ZMUtils.str2HexStr(channel.getNotifyUrl()));

        PaymentResult res = ZMRequestUtils.execute("/tl/withdraw", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"orderid=20181225152140122000&trxstatus=0000&errmsg=处理成功&trxid=18120004062477&fintime=20181225152141&acctno=622588****6195&amount=994&actualamount=894&fee=100"}\
        //{"key":"000","msg":"处理成功","data":"orderid=&trxstatus=3008&errmsg=账户余额不足&trxid=&fintime=&acctno=&amount=&actualamount=&fee="}

        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) res.setSuccess(PaymentResultCodeEnum.WITHDRAW_SUCCESS);
        else {res.setError(getCodeEnum(trxstatus));}
        return res;
    }



    /**
     * 小额免密支付
     */
    public static PaymentResult freepassPay(String cusid,String agreeid, String city, String subject, PaymentChannel channel, UserTradePaymentRecord payRecord){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号(注意,是进件时,通联返回的用户id)
        paramMap.put("cusid", cusid);
        //订单号（唯一性）
        paramMap.put("orderid", payRecord.getOrderId());
        //签约协议编号
        paramMap.put("agreeid", agreeid);
        //订单金额（单位分）
        paramMap.put("amount", String.valueOf(Double.valueOf(payRecord.getAmount() * 100).intValue()));
        //订单内容
        paramMap.put("subject", subject);
        //市别(代码)
        paramMap.put("city", ZMUtils.getAreaCode(city).split(",")[0]);
        //回调通知地址（16进制单独编码,支持地址后面带参数）
        paramMap.put("notify_url", ZMUtils.str2HexStr(channel.getNotifyUrl()));

        PaymentResult res = ZMRequestUtils.execute("/tl/quickpass", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"orderid=20181225153515699000&trxstatus=0000&errmsg=&trxid=18120004064734&fintime=20181225153516"}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) res.setSuccess(PaymentResultCodeEnum.PAY_SUCCESS);
        else {res.setError(getCodeEnum(trxstatus));}
        return res;
    }

    /**
     * 小额免密提现
     * @param cusid
     * @param orderid
     * @param agreeid
     * @param amount
     * @return
     */
    public static PaymentResult freepassPayWithdraw(String cusid, String orderid, String agreeid, Double amount, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //订单号（唯一性）
        paramMap.put("orderid", orderid);
        //签约协议编号
        paramMap.put("agreeid", agreeid);
        //订单金额（单位分）,-----代还申请提现时加上用户提现手续费,因为平台会自动扣掉,否则会导致多扣一元
        paramMap.put("amount", String.valueOf(Double.valueOf(MathUtils.add(amount, channel.getUserWithdrawCharge(),2)*100).intValue()));
        //        //回调通知地址（16进制单独编码,支持地址后面带参数）
        paramMap.put("notify_url", ZMUtils.str2HexStr(channel.getNotifyUrl()));

        PaymentResult res = ZMRequestUtils.execute("/tl/delegatepay", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"orderid=20181225153813510000&trxstatus=0000&errmsg=处理成功&trxid=18120004065331&fintime=20181225153814&acctno=518718****5535&amount=1000&actualamount=940&fee=60"}
        //{"key":"000","msg":"处理成功","data":"orderid=&trxstatus=3008&errmsg=账户余额不足&trxid=&fintime=&acctno=&amount=&actualamount=&fee=null"}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) {res.setSuccess(PaymentResultCodeEnum.WITHDRAW_SUCCESS);}
        else {res.setError(getCodeEnum(trxstatus));}

        return res;
    }

    /**
     * 小额免密订单查询
     * 只有支付/提现成功才会返回SUCCESS
     * @param orderid
     * @param channel
     * @return
     */
    public static PaymentResult freepassPayQuery(String orderid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //订单号
        paramMap.put("orderid", orderid);
        PaymentResult res = ZMRequestUtils.execute("/tl/querypay", paramMap, channel);
        //{"key":"000","msg":"","data":"orderid=20181225161443902000&trxamt=10.00&trxstatus=0000&errmsg=处理成功"}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) {res.setSuccess(PaymentResultCodeEnum.SUCCESS);}
        else {res.setError(getCodeEnum(trxstatus));}
        return res;
    }

    /**
     * 快捷支付订单查询
     * 只有支付/提现成功才会返回SUCCESS
     * @param orderid
     * @param channel
     * @return
     */
    public static PaymentResult quickPayQuery(String orderid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //订单号（唯一性、与快捷扣款订单号不同）
        paramMap.put("orderid", orderid);

        PaymentResult res = ZMRequestUtils.execute("/tl/query", paramMap, channel);
        //{"key":"000","msg":"","data":"orderid=20181225151905056000&trxamt=10.00&trxstatus=0000&errmsg=success"}
        String trxstatus = res.get("trxstatus");
        if ("0000".equals(trxstatus)) {res.setSuccess(PaymentResultCodeEnum.SUCCESS);}
        else {res.setError(getCodeEnum(trxstatus));}
        return res;
    }

    /**
     * 余额查询
     */
    public static PaymentResult balanceQuery(String cusid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/balance", paramMap, channel);
        //{"key":"000","msg":"处理成功","data":"balance=1087"}
        if ("000".equals(res.get("key"))){ res.setSuccess(); }
        return res;
    }


    /**
     * 查询是否已注册
     * @param outcusid 用户id(我们平台)
     */
    public static PaymentResult registerQuery(String outcusid, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("outcusid", outcusid);

        PaymentResult res = ZMRequestUtils.execute("/tl/cusquery", paramMap,channel);
        //{"key":"000","msg":"处理成功","data":"cusid=101003336821&outcusid=1000001&state=1"}
        if ("000".equals(res.get("key"))){ res.setSuccess(); }
        return res;
    }


    /**
     * 签约/费率列表, 费率为百分比, eg: 0.6=0.6%
     * @param channel
     * @return
     */
    private static JSONArray getProducts(PaymentChannel channel){
        JSONArray array = new JSONArray();
        JSONObject object1 = new JSONObject();
        object1.put("trxcode", "QUICKPAY_OL_HP");
        object1.put("feerate", channel.getUserPaymentRate() * 100);

        JSONObject object2 = new JSONObject();
        object2.put("trxcode", "QUICKPAY_OF_NP");
        object2.put("feerate", channel.getUserPaymentRate() * 100);

        JSONObject object3 = new JSONObject();
        object3.put("trxcode", "TRX_PAY");
        object3.put("feerate", channel.getUserWithdrawCharge());

        JSONObject object4 = new JSONObject();
        object4.put("trxcode", "QUICKPAY_NOSMS");
        object4.put("feerate", channel.getUserPaymentRate() * 100);

        array.add(object1);
        array.add(object2);
        array.add(object3);
        array.add(object4);
        return array;
    }

    /**
     * 更新用户费率
     */
    public static PaymentResult rateUpdate(String cusid, String bankcardNo, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //帐户号(卡号)
        paramMap.put("acctid", bankcardNo);
        //提现手续费(单位元/笔)
        paramMap.put("settfee", String.valueOf(channel.getUserWithdrawCharge()));
        //产品列表(json格式)
        paramMap.put("prodlist", getProducts(channel).toJSONString());

        PaymentResult res = ZMRequestUtils.execute("https://tx.szjcxd.cn/tl/updatesettinfo", paramMap, channel);
        return res;
    }


    /**
     * 解绑银行卡/信用卡
     */
    private static void unbindCard(String cusid, UserBankCard bankCard, PaymentChannel channel){
        Map<String,String> paramMap = new HashMap<>();
        //平台生成商户号
        paramMap.put("cusid", cusid);
        //银行卡
        paramMap.put("cardno", bankCard.getBankCardNo());

        PaymentResult res = ZMRequestUtils.execute("/tl/unbindcard", paramMap, channel);
    }

    private static PaymentResultCodeEnum getCodeEnum(String trxstatus){
        if (trxstatus == null) return null;

        switch (trxstatus){
            case "1999":
                return PaymentResultCodeEnum.SMS_AUTO_SEND;
            case "2000":
                return PaymentResultCodeEnum.PAY_ING;
            case "3051":
                return PaymentResultCodeEnum.BIND_CARD_EXIST;
            case "3057":
                return PaymentResultCodeEnum.SMS_VERIFY_EXPIRE;
            case "3058":
                //trxstatus=3058&errmsg=短信验证码错误
                return PaymentResultCodeEnum.SMS_VERIFY_FAIL;
            case "3008":
                return PaymentResultCodeEnum.WITHDRAW_AMOUNT_LOW;
        }

        return  null;
    }

}
