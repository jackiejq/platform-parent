package com.slljr.finance.payment.service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.payment.utils.ZMRequestUtils;
import com.slljr.finance.payment.utils.PaymentResult;
import com.slljr.finance.payment.utils.ZMUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 腾付通支付接口API
 * 功能: 小额免密(支付+提款)
 * @author: uncle.quentin.
 * @date: 2018/12/17.
 * @time: 10:21.
 */
public class TengfutongPay {

    /**
     * 用户注册接口
     *
     * @param userBasic
     * @param recCard   储蓄卡和信用卡都可以, 绑哪个卡就到哪个卡
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/17 10:41
     * @version 1.0
     */
    public static PaymentResult register(UserBasic userBasic, UserBankCardVo recCard, PaymentChannel channel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yy");
        Map<String, String> paramMap = new HashMap<>();
        //对公或私(0为对私,1为对公)
        paramMap.put("is_company", "0");
        //证件类型（01：身份证）
        paramMap.put("is_card_type", "01");
        //证件号码
        paramMap.put("idcard", userBasic.getIdCard());
        //姓名
        paramMap.put("name", userBasic.getName());
        //手机号码
        paramMap.put("phone", recCard.getPhone());
        //银行卡号
        paramMap.put("bank_card", recCard.getBankCardNo());
        //开户行名称
        paramMap.put("bank_name", recCard.getOpeningBank());

        String bankCode = ZMUtils.getBankCode(recCard.getBankName());
        //联行号
        paramMap.put("bank_id", bankCode.split(",")[0]);
        //开户行简称
        paramMap.put("bank_code", bankCode.split(",")[1]);
        //费率 （不小于签约价）（eg：43）=0.0043换算
        paramMap.put("rate", String.valueOf(Double.valueOf(channel.getUserPaymentRate() * 10000).intValue()));
        //单笔手续费（不小于签约价）--单位分
        paramMap.put("charge", String.valueOf(Double.valueOf(channel.getUserWithdrawCharge() * 100).intValue()));
        //cvn2
        paramMap.put("cvn2", StringUtils.isBlank(recCard.getCvvCode())?"111" : recCard.getCvvCode());
        //信用卡有效期（mm-yy）
        paramMap.put("expire_date", recCard.getExpDate()==null?"01-01":sdf.format(recCard.getExpDate()));
        //发送请求
        //{"key":"000","msg":"","data":"sub_mchid=9000646174"}
        PaymentResult res = ZMRequestUtils.execute("/hljc/report", paramMap, channel);
        //返回sub_mchid：子商户号
        if (StringUtils.isNotBlank(res.get("key")) && "000".equals(res.get("key")) && StringUtils.isNotBlank(res.get("sub_mchid"))) {
            res.setSuccess();
        }

        return res;
    }

    /**
     * 支付订单接口
     *
     * @param subMchid  子商户号,注册时返回
     * @param userBasic 用户信息
     * @param payCard   支付卡
     * @param orderId   订单id
     * @param amount    订单金额(元)
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/17 15:21
     * @version 1.0
     */
    public static PaymentResult freepassPay(String subMchid, String orderId, Double amount, UserBasic userBasic, UserBankCardVo payCard, PaymentChannel channel) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yy");
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mchid", subMchid);
        //快捷支付订单号,保证唯一性
        paramMap.put("order_no", orderId);
        //证件号码
        paramMap.put("idcard", userBasic.getIdCard());
        //姓名
        paramMap.put("name", userBasic.getName());
        //手机号码
        paramMap.put("phone", payCard.getPhone());
        //联行号
        paramMap.put("bank_id", ZMUtils.getBankCode(payCard.getBankName()).split(",")[0]);
        //银行卡号
        paramMap.put("bank_card", payCard.getBankCardNo());
        //cvn2
        paramMap.put("cvn2", payCard.getCvvCode());
        //信用卡有效期（mm-yy）
        paramMap.put("expire_date", sdf.format(payCard.getExpDate()));
        //订单金额 单位分
        paramMap.put("amount", String.valueOf(Double.valueOf(amount * 100).intValue()));
        //发送请求
        PaymentResult res = ZMRequestUtils.execute("/hljc/pay", paramMap, channel);
        //{"key":"10017","msg":"非同名卡无法完成支付！","data":"order_no=20181227145809053000&amount=1000&order_date=20181227"}
        //{"key":"000","msg":"下单成功！","data":"order_no=20181227150203098000&amount=1000&order_date=20181227"}
        if ("000".equals(res.get("key"))) {
            res.setSuccess(PaymentResultCodeEnum.PAY_ING);
        } else {
            res.setError(PaymentResultCodeEnum.PAY_FAIL);
        }
        System.out.println("支付订单返回响应：" + res.toString());

        return res;
    }

    /**
     * 提现
     *
     * @param subMchid 子商户号
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/17 16:30
     * @version 1.0
     */
    public static PaymentResult freepassPayWithdraw(String subMchid, UserTradePaymentRecord recRecord, PaymentChannel channel) {
        Map<String, String> paramMap = new HashMap<>();
        //sub_mchid：子商户号
        paramMap.put("sub_mchid", subMchid);
        //order_no：代付订单号（代付订单号与快捷支付订单号是不同的订单,保证唯一性）
        paramMap.put("order_no", recRecord.getOrderId());
        //amount：订单金额 单位分  须再扣去代付费用【小数点后二位直接丢掉】
        paramMap.put("amount", String.valueOf(Double.valueOf(recRecord.getAmount() * 100).intValue()));
        //发送请求
        //{"key":"10002","msg":"支付处理中！","data":"order_no=20181227144833954000&amount=400&order_date=20181227"}
        PaymentResult res = ZMRequestUtils.execute("/hljc/mercPay", paramMap, channel);
        if ("10002".equals(res.get("key"))) {
            res.setSuccess(PaymentResultCodeEnum.PAY_ING);
        } else {
            res.setError(PaymentResultCodeEnum.PAY_FAIL);
        }
        return res;
    }

    /**
     * 订单查询接口
     *
     * @param orderNo 订单号
     * @param channel 通道
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/17 14:01
     * @version 1.0
     */
    public static PaymentResult freepassPayQuery(String orderNo, PaymentChannel channel) {
        Map<String, String> paramMap = new HashMap<>();
        //订单号
        paramMap.put("order_no", orderNo);
        //{"key":"000","msg":"","data":"order_no=20181227150422393000&amount=17.98&order_date=20181227150502&real_amount=17.98&pay_status=3&pay_comment=支付处理中"}
        PaymentResult res = ZMRequestUtils.execute("/hljc/query", paramMap, channel);
        //pay_status：状态码（0未支付、1成功、2失败、3处理中）
        if ("000".equals(res.get("key")) && res.getMap().containsKey("pay_status")) {
            Integer pay_status = Integer.valueOf(res.get("pay_status"));
            switch (pay_status){
                case 0:
                    res.setError(PaymentResultCodeEnum.PAY_FAIL);
                    break;
                case 1:
                    res.setSuccess(PaymentResultCodeEnum.SUCCESS);
                    break;
                case 2:
                    res.setError(PaymentResultCodeEnum.PAY_FAIL);
                    break;
                case 3:
                    res.setError(PaymentResultCodeEnum.PAY_ING);
                    break;
            }
        }
        return res;
    }


    /**
     * 修改绑定银行卡信息接口
     *
     * @param bankCard
     * @param subMchid 子商户号
     * @return com.slljr.finance.payment.utils.PaymentResult
     * @author uncle.quentin
     * @date 2018/12/17 15:04
     * @version 1.0
     */
    public static PaymentResult updateBindBank(UserBankCardVo bankCard, String subMchid, PaymentChannel channel) {
        Map<String, String> paramMap = new HashMap<>();
        //子商户号
        paramMap.put("sub_mchid", subMchid);
        //type：N（ N：修改银行卡信息）
        paramMap.put("type", "N");
        //联行号
        String bankCode = ZMUtils.getBankCode(bankCard.getBankName());
        paramMap.put("bank_id", bankCode.split(",")[0]);
        //银行卡号
        paramMap.put("bank_card", bankCard.getBankCardNo());
        //开户行名称
        paramMap.put("bank_name", bankCard.getOpeningBank());
        //开户行简称
        paramMap.put("bank_code", bankCode.split(",")[1]);
        //发送请求
        //{"key":"000","msg":"修改成功！","data":""}
        PaymentResult res = ZMRequestUtils.execute("/hljc/reportUpdate", paramMap, channel);
        if ("000".equals(res.get("key"))) {
            res.setSuccess(PaymentResultCodeEnum.UPDATE_BIND_CARD_SUCCESS);
        } else {
            res.setSuccess(PaymentResultCodeEnum.UPDATE_BIND_CARD_FAIL);
        }
        return res;
    }

//    /**
//     * 协议申请接口
//     *
//     * @param userBasic 用户信息
//     * @param bankCard  用户银行卡信息
//     * @param orderNo   订单号
//     * @return com.slljr.finance.payment.utils.PaymentResult
//     * @author uncle.quentin
//     * @date 2018/12/17 17:47
//     * @version 1.0
//     */
//    public static PaymentResult applyQuickpay(UserBasic userBasic, UserBankCard bankCard, String orderNo) {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm-yy");
//        Map<String, String> paramMap = new HashMap<>();
//        //请求订单号
//        paramMap.put("order_no", orderNo);
//        //身份证号码
//        paramMap.put("idcard", userBasic.getIdCard());
//        //姓名
//        paramMap.put("name", userBasic.getName());
//        //手机号码
//        paramMap.put("phone", userBasic.getPhone());
//        //银行卡号
//        paramMap.put("bank_card", bankCard.getBankCardNo());
//        //开户行名称
//        paramMap.put("bank_name",  bankCard.getOpeningBank());
//        //cvn2
//        paramMap.put("cvn2", bankCard.getCvvCode());
//        //信用卡有效期（mm-yy）
//        paramMap.put("expire_date", sdf.format(bankCard.getExpDate()));
//
//        PaymentResult res = ZMRequestUtils.execute("/hljc/treatyApply", paramMap, PaymentChannelEnum.TENGFUTONG);
//        System.out.println("协议申请返回响应：" + res.toString());
//        return res;
//    }
//
//    /**
//     * 协议确认
//     *
//     * @param orderNo 申请协议订单号
//     * @param cardNo  银行卡号
//     * @param smsCode 手机验证码
//     * @return com.slljr.finance.payment.utils.PaymentResult
//     * @author uncle.quentin
//     * @date 2018/12/17 17:54
//     * @version 1.0
//     */
//    public static PaymentResult confirmQuickpay(String orderNo, String cardNo, String smsCode) {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm-yy");
//        Map<String, String> paramMap = new HashMap<>();
//        //order_no：申请协议订单号
//        paramMap.put("order_no", orderNo);
//        //bank_card：银行卡号
//        paramMap.put("bank_card", cardNo);
//        //sms_code：手机验证码
//        paramMap.put("sms_code", smsCode);
//
//        PaymentResult res = ZMRequestUtils.execute("/hljc/treatyApply", paramMap, PaymentChannelEnum.TENGFUTONG);
//        return res;
//    }
//
//    /**
//     * 快捷支付
//     *
//     * @param subMchId 子商户号
//     * @param orderNo  订单号
//     * @param treatyId 协议号
//     * @param amount   金额（单位分）
//     * @return com.slljr.finance.payment.utils.PaymentResult
//     * @author uncle.quentin
//     * @date 2018/12/17 17:58
//     * @version 1.0
//     */
//    public static PaymentResult quickpay(String subMchId, String orderNo, String treatyId, String amount) {
//        Map<String, String> paramMap = new HashMap<>();
//        //sub_mchid：子商户号
//        paramMap.put("sub_mchid", subMchId);
//        //order_no：订单号
//        paramMap.put("order_no", orderNo);
//        //treaty_id：协议号
//        paramMap.put("treaty_id", treatyId);
//        //amount：金额（单位分）
//        paramMap.put("amount", amount);
//
//        PaymentResult res = ZMRequestUtils.execute("/hljc/treatyConfirm", paramMap, PaymentChannelEnum.TENGFUTONG);
//        return res;
//    }

}
