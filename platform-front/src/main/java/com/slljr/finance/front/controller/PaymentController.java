package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserBankCard;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.vo.PaymentResultVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.PaymentService;
import com.slljr.finance.front.service.UserBankCardSerivce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
@Api(tags = "支付相关类")
public class PaymentController extends BaseController{
    private static final Logger log = LogManager.getLogger();

    @Autowired
    PaymentService paymentService;
    @Autowired
    UserBankCardSerivce userBankCardSerivce;

    //最低收款金额
    @Value("${quickpay.amount.min}")
    Double quickpayAmountMin;
    //最低代偿金额
    @Value("${freepasspay.amount.min}")
    Double freepasspayAmountMin;
    //最低代偿卡余额
    @Value("${freepasspay.balance.min}")
    Double freepasspayBalanceMin;


    @RequestMapping(value = "/getSKServiceCharge", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "获取收款预计手续费", notes = "{\"amount\":\"100\",\"payCardId\":\"支付卡id\",\"repayCardId\":\"收款卡id\"}")
    public ModelMap getSKServiceCharge(@RequestBody String param) throws InterfaceException {
        log.debug("请求参数: " + param);
        Assert.notNull(param, "参数不能为空");

        JSONObject json = JSON.parseObject(param);
        Double amount = json.getDouble("amount");
        Integer payCardId = json.getInteger("payCardId");
        Integer repayCardId = json.getInteger("repayCardId");

        Assert.isTrue(amount >= quickpayAmountMin, "收款金额最低" + quickpayAmountMin);
        Assert.notNull(payCardId, "请选择支付信用卡");
        Assert.notNull(repayCardId, "请添加收款储蓄卡");

        PaymentChannel channel = paymentService.getPaymentChannel(amount, payCardId, repayCardId, PaymentChannelTypeEnum.SHOUKUAN);

        if (channel != null) {
            Map<String, Object> map = new HashMap<>();
            Double serviceCharge = MathUtils.mul(amount, channel.getUserPaymentRate(), 2);
            serviceCharge = MathUtils.add(serviceCharge, channel.getUserWithdrawCharge(), 2);
            map.put("channelId", channel.getId());
            map.put("paymentRate", channel.getUserPaymentRate());
            map.put("withdrawCharge", channel.getUserWithdrawCharge());
            map.put("serviceCharge", serviceCharge);
            return WriteJson.successData(map);
        }

        return WriteJson.errorWebMsg("当前信用卡无可用通道,请稍后再试");
    }

    @RequestMapping(value = "/applyFreepassPay", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "申请代偿", notes = "{\"paymentAmount\": 1000, \"cardBalance\": 500, \"lastPaymentDate\": 26, \"payCardId\": 1001, \"recCardId\": 1001, \"city\": \"长沙\"}")
    public ModelMap applyFreepassPay(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);
        Assert.notNull(param, "参数不能为空");

        JSONObject json = JSON.parseObject(param);
        Double paymentAmount = json.getDouble("paymentAmount");
        Double cardBalance = json.getDouble("cardBalance");
        Integer lastPaymentDate = json.getInteger("lastPaymentDate");
        Integer payCardId = json.getInteger("payCardId");
        Integer recCardId = json.getInteger("recCardId");
        String city = json.getString("city");

        Assert.isTrue(paymentAmount >= freepasspayAmountMin, "账单金额最低" + freepasspayAmountMin);
        Assert.isTrue(cardBalance >= freepasspayBalanceMin, "剩余额度最低" + freepasspayBalanceMin);
        Assert.isTrue(lastPaymentDate != null && lastPaymentDate > 0 && lastPaymentDate <= 31, "最后还款日错误");
        Assert.isTrue(payCardId != null && recCardId != null, "请选择代偿信用卡");


        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        //更新信用卡的最后还款日
        userBankCardSerivce.updateLastPaymentDate(payCardId, lastPaymentDate);

        UserTradeOrder tradeOrder = new UserTradeOrder();
        tradeOrder.setUid(user.getId());
        tradeOrder.setPaymentCardId(payCardId);
        tradeOrder.setReceiveCardId(recCardId);
        tradeOrder.setType(PaymentChannelTypeEnum.DAIHUAN.getKey());
        tradeOrder.setPaymentAmount(paymentAmount);
        tradeOrder.setBillAmount(paymentAmount);
        tradeOrder.setReceiveAmount(0D);
        tradeOrder.setServiceCharge(0D);
        tradeOrder.setCardBalance(cardBalance);
        tradeOrder.setCity(city);


        //计算最后还款日
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Calendar lastPayCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        //设置还款开始日期(从计划第二天开始)
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1);
        //设置还款日
        lastPayCalendar.set(Calendar.DAY_OF_MONTH, lastPaymentDate);
        //如果今天日期大于最后还款日,则最后还款日为下月
        if (todayCalendar.getTimeInMillis() > lastPayCalendar.getTimeInMillis()) lastPayCalendar.add(Calendar.MONTH, 1);
        tradeOrder.setCardLastPaymentDate(lastPayCalendar.getTime());

        PaymentResultVO resVO = paymentService.bindCard(user, tradeOrder, PaymentChannelTypeEnum.DAIHUAN);

        log.debug("返回结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }

    @RequestMapping(value = "/resendApplyFreepassPaySMS", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "发送申请代偿短信验证码", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\"}")
    public ModelMap resendApplyFreepassPaySMS(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);

        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");
        //业务id
        String businessId = json.getString("businessId");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        PaymentResultVO resVO = paymentService.resendBindCardSMS(user, businessId);
        log.debug("返回结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }

    @RequestMapping(value = "/confirmFreepassPay", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "确认代偿", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\", \"smscode\": \"432126\"}")
    public ModelMap confirmFreepassPay(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);

        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");
        Assert.notNull(json.get("smscode"), "短信验证码不能为空");

        //业务id
        String businessId = json.getString("businessId");
        //短信验证码
        String smscode = json.getString("smscode");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        PaymentResultVO resVO = paymentService.bindCardConfirm(user, smscode, businessId);
        log.debug("返回结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }




    @RequestMapping(value = "/quickPayBindCard", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "申请收款绑卡", notes = "{\"paymentAmount\": \"200\", \"payCardId\": 121, \"recCardId\": 343}")
    public ModelMap quickPayBindCard(@RequestBody String param, HttpServletRequest request) throws InterfaceException{
        log.debug("请求参数: " + param);

        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Double paymentAmount = json.getDouble("paymentAmount");
        Integer payCardId = json.getInteger("payCardId");
        Integer recCardId = json.getInteger("recCardId");

        Assert.isTrue(paymentAmount >= quickpayAmountMin, "收款金额最低" + quickpayAmountMin);
        Assert.notNull(payCardId, "请选择支付信用卡");
        Assert.notNull(recCardId, "请添加收款储蓄卡");

        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        UserTradeOrder tradeOrder = new UserTradeOrder();
        tradeOrder.setUid(user.getId());
        tradeOrder.setPaymentAmount(paymentAmount);
        tradeOrder.setPaymentCardId(payCardId);
        tradeOrder.setReceiveCardId(recCardId);
        tradeOrder.setType(PaymentChannelTypeEnum.SHOUKUAN.getKey());

        PaymentResultVO resVO = paymentService.bindCard(user, tradeOrder, PaymentChannelTypeEnum.SHOUKUAN);
        //如果绑卡不需要短信验证码,则自动调用申请快捷支付接口
        if (resVO.getSuccess() && resVO.getCode() != PaymentResultCodeEnum.SMS_AUTO_SEND.getCode()
                && resVO.getCode() != PaymentResultCodeEnum.SMS_MANUAL_SEND.getCode() ){
            PaymentResultVO applyQuickPayResVO = paymentService.applyQuickPay(resVO.getBusinessId(), user);
            if (applyQuickPayResVO.getSuccess()){
                resVO.setSuccess(true);
                resVO.setCode(PaymentResultCodeEnum.SUCCESS.getCode());
                resVO.setMsg(PaymentResultCodeEnum.SUCCESS.getMsg());
            }else{
                resVO.setSuccess(false);
                resVO.setCode(PaymentResultCodeEnum.FAIL.getCode());
                resVO.setMsg(PaymentResultCodeEnum.FAIL.getMsg());
            }
        }
        log.debug("快捷申请绑卡结果: " + JSON.toJSONString(resVO));

        return WriteJson.successData(resVO);
    }


    @RequestMapping(value = "/resendQuickPayBindCardSMS", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "发送收款绑卡短信验证码", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\"}")
    public ModelMap resendQuickPayBindCardSMS(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);

        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");
        //业务id
        String businessId = json.getString("businessId");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        PaymentResultVO resVO = paymentService.resendBindCardSMS(user, businessId);
        log.debug("重新发送绑卡验证码结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }


    @RequestMapping(value = "/confirmQuickPayBindCard", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "确认收款绑卡验证码", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\", \"smscode\": \"432126\"}")
    public ModelMap confirmQuickPayBindCard(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);
        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");
        Assert.notNull(json.get("smscode"), "短信验证码不能为空");

        //业务id
        String businessId = json.getString("businessId");
        //短信验证码
        String smscode = json.getString("smscode");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        PaymentResultVO resVO = paymentService.bindCardConfirm(user, smscode, businessId);

        log.debug("确认绑卡结果: " + JSON.toJSONString(resVO));
        //如果绑卡验证成功,则自动调用申请快捷支付接口
        if (resVO.getSuccess()){
            PaymentResultVO applyQuickPayResVO = paymentService.applyQuickPay(resVO.getBusinessId(), user);
            if (applyQuickPayResVO.getSuccess()){
                resVO.setSuccess(true);
                resVO.setCode(PaymentResultCodeEnum.SUCCESS.getCode());
                resVO.setMsg(PaymentResultCodeEnum.SUCCESS.getMsg());
            }else{
                resVO.setSuccess(false);
            }

            log.debug("申请收款结果: " + JSON.toJSONString(applyQuickPayResVO));

        }

        return WriteJson.successData(resVO);
    }

//    @RequestMapping(value = "/applyQuickPay", method = RequestMethod.POST)
//    @ApiOperation(httpMethod = "POST", value = "申请收款", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\"}")
//    public ModelMap applyQuickPay(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
//        log.debug("请求参数: " + param);
//        Assert.notNull(param, "参数不能为空");
//        JSONObject json = JSON.parseObject(param);
//        Assert.notNull(json.get("businessId"), "businessId不能为空");
//
//        //业务id
//        String businessId = json.getString("businessId");
//        //获取登录用户信息
//        UserBasicVO user = getLoginUser(request);
//
//        PaymentResultVO applyQuickPayResVO = paymentService.applyQuickPay(businessId, user);
//        log.debug("返回结果: " + JSON.toJSONString(applyQuickPayResVO));
//        return WriteJson.successData(applyQuickPayResVO);
//    }

    @RequestMapping(value = "/resendQuickPaySMS", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "重新发送收款验证码", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\"}")
    public ModelMap resendQuickPaySMS(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);
        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");

        //业务id
        String businessId = json.getString("businessId");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        PaymentResultVO resVO = paymentService.resendQuickPaySMS(businessId, user);
        log.debug("重新发送快捷验证码结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }

    @RequestMapping(value = "/confirmQuickPay", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "确认收款", notes = "{\"businessId\": \"dfdssd-fefdsfsd-3432dsfds-df\", \"smscode\": \"432126\"}")
    public ModelMap confirmQuickPay(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        log.debug("请求参数: " + param);
        Assert.notNull(param, "参数不能为空");
        JSONObject json = JSON.parseObject(param);
        Assert.notNull(json.get("businessId"), "businessId不能为空");
        Assert.notNull(json.get("smscode"), "短信验证码不能为空");

        //业务id
        String businessId = json.getString("businessId");
        //短信验证码
        String smscode = json.getString("smscode");
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        PaymentResultVO resVO = paymentService.confirmQuickPay(businessId, smscode, user);
        log.debug("确认收款结果: " + JSON.toJSONString(resVO));
        return WriteJson.successData(resVO);
    }



}
