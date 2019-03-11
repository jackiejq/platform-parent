package com.slljr.finance.front.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.HttpUtil;
import com.slljr.finance.common.enums.*;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.*;
import com.slljr.finance.common.pojo.vo.PaymentResultVO;
import com.slljr.finance.common.pojo.vo.UserBankCardVo;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.OKHttpUtils;
import com.slljr.finance.front.mapper.*;
import com.slljr.finance.front.utils.BillParams;
import com.slljr.finance.front.utils.BillUtils;
import com.slljr.finance.payment.service.HuanqiuPay;
import com.slljr.finance.payment.service.JiafutongPay;
import com.slljr.finance.payment.service.TengfutongPay;
import com.slljr.finance.payment.service.TonglianPay;
import com.slljr.finance.payment.utils.OrderNumUtils;
import com.slljr.finance.payment.utils.PaymentResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PaymentService {
    Logger log = LogManager.getLogger();

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OrderNumUtils orderNumUtils;
    @Resource
    UserBankCardMapper userBankCardMapper;
    @Autowired
    PaymentChannelMapper paymentChannelMapper;
    @Autowired
    PaymentChannelUserService paymentChannelUserService;
    @Resource
    PaymentChannelUserMapper paymentChannelUserMapper;
    @Resource
    PaymentChannelBankMapper paymentChannelBankMapper;
    @Resource
    PaymentChannelBankCardMapper paymentChannelBankCardMapper;
    @Resource
    UserTradePaymentRecordMapper userTradePaymentRecordMapper;
    @Resource
    UserTradeOrderMapper userTradeOrderMapper;
    @Autowired
    RsaService rsaService;

    //redis存储参数有效时间(秒)
    final static int expireTime = 15 * 60;
    final static String channelKey = "channel";
    final static String channelUserKey = "channelUser";
    final static String payCardKey = "payCard";
    final static String recCardKey = "recCard";
    final static String tradeOrderKey = "tradeOrder";

    //返回参数map,下次请求可能需要带上上次返回的参数
    final static String paramMapKey = "paramMap";

    /**
     * 申请绑卡
     * @param userBasic
     * @return
     */
    public PaymentResultVO bindCard(UserBasic userBasic, UserTradeOrder tradeOrder, PaymentChannelTypeEnum channelTypeEnum) throws InterfaceException{
        //本次业务办理Id
        String businessId = UUID.randomUUID().toString();
        Double paymentAmount = tradeOrder.getPaymentAmount();
        Integer payCardId = tradeOrder.getPaymentCardId();
        Integer repayCardId = tradeOrder.getReceiveCardId();

        //选择通道
        PaymentChannel channel = getPaymentChannel(BillUtils.getSingleAmountMin(tradeOrder), payCardId, repayCardId, channelTypeEnum);
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());

        //查询银行卡信息
        UserBankCardVo payCard = userBankCardMapper.selectByPrimaryKey(payCardId);
        UserBankCardVo recCard = payCardId.equals(repayCardId) ? payCard : userBankCardMapper.selectByPrimaryKey(repayCardId);

        rsaService.bankCardDecode(payCard);
        rsaService.bankCardDecode(recCard);

        if (StringUtils.isBlank(tradeOrder.getCity())) tradeOrder.setCity(userBasic.getIdcCity());

        //数据存入redis
        redisUtil.set(getRedisKey(businessId, channelKey), channel, expireTime);
        redisUtil.set(getRedisKey(businessId, payCardKey), payCard, expireTime);
        redisUtil.set(getRedisKey(businessId, recCardKey), recCard, expireTime);
        //生成计划
        Integer tradeOrderId = orderCreate(businessId, tradeOrder, channel);

        //1.查询用户在该通道是否已注册,未注册执行注册****注册必须使用储蓄卡(腾付通除外)
        PaymentChannelUser channelUser = paymentChannelUserService.queryChannelUser(userBasic.getId(), recCard.getId(), channelEnum);
        if (channelUser == null) {
            PaymentResult registRes = new PaymentResult();
            String cusid = null;

            UserBankCard queryBankCard = new UserBankCard();
            queryBankCard.setUid(userBasic.getId());
            queryBankCard.setBankCardType(BankCardTypeEnum.DEPOSIT_CARD.getKey());
            List<UserBankCardVo> queryBankCards =  userBankCardMapper.findByUidAndType(queryBankCard);

            if (queryBankCards.isEmpty()) throw new InterfaceException("请先绑定储蓄卡");

            rsaService.bankCardDecode(queryBankCards.get(0));

            if (StringUtils.isBlank(userBasic.getIdcCity())) userBasic.setIdcCity(tradeOrder.getCity());
            switch (channelEnum) {
                case TENGFUTONG:
                    //腾付通支持注册多账户
                    registRes = TengfutongPay.register(userBasic, recCard, channel);
                    cusid = registRes.get("sub_mchid");
                    break;
                case TONGLIAN:
                    registRes = TonglianPay.register(userBasic, queryBankCards.get(0), channel);
                    cusid = registRes.get("cusid");
                    break;
                case HUANQIUHUIJU:
                    registRes = HuanqiuPay.register(userBasic, queryBankCards.get(0), channel);
                    cusid = registRes.get("subMerNo");
                    break;
                case JIAFUTONG:
                    registRes = JiafutongPay.register(userBasic, queryBankCards.get(0), channel);
                    cusid = registRes.get("chMerCode");
                    break;
            }

            //注册失败
            if (registRes == null || !registRes.isSuccess()){
                registRes.setMsg("注册失败");
                return getPaymentResultVO(businessId, registRes);
            }
            if (StringUtils.isBlank(cusid)){
                registRes.setError("用户注册返回id为空");
                return getPaymentResultVO(businessId, registRes);
            }

            channelUser = new PaymentChannelUser();
            channelUser.setUid(userBasic.getId());
            channelUser.setChannelUid(cusid);
            channelUser.setBankCardId(channelEnum == PaymentChannelEnum.TENGFUTONG ? recCard.getId() : queryBankCards.get(0).getId());
            channelUser.setStatus(0);
            channelUser.setChannelCode(channel.getCode());
            paymentChannelUserMapper.insertSelective(channelUser);
            redisUtil.set(getRedisKey(businessId, paramMapKey), registRes.getMap(), expireTime);
        }

        redisUtil.set(getRedisKey(businessId, channelUserKey), channelUser, expireTime);

        //2.查询该支付卡是否已绑定,未绑定就绑定
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), payCardId, channel.getCode());
        if (channelBankCard == null){
            PaymentResult bindCardRes = new PaymentResult();
            switch (channelEnum) {
                case TENGFUTONG:
                    //腾付通不用绑卡
                    bindCardRes.setSuccess();
                    channelBankCard = new PaymentChannelBankCard();
                    break;
                case TONGLIAN:
                    //通联要绑卡,且要短信验证码,有回带参数,如已注册,则返回成功,不需要短信验证码
                    bindCardRes = TonglianPay.bindCard(channelUser.getChannelUid(), userBasic, payCard, channel);
                    redisUtil.set(getRedisKey(businessId, paramMapKey), bindCardRes.getMap(), expireTime);

                    //如果返回成功(已绑卡)则往下走,失败或者需要短信验证码都直接返回
                    if (bindCardRes.getCode() != PaymentResultCodeEnum.SUCCESS.getCode()){
                        return getPaymentResultVO(businessId, bindCardRes);
                    }

                    //历史已绑卡但未入库,这里补录
                    String agreeid = bindCardRes.get("agreeid");
                    channelBankCard = new PaymentChannelBankCard();
                    channelBankCard.setProtocolCode(agreeid);
                    break;
                case HUANQIUHUIJU:
                    //环球要绑卡,且要短信验证码,有回带参数
                    bindCardRes = HuanqiuPay.openCard(userBasic, payCard, channelUser.getChannelUid(), channel);
                    //代还绑定提现卡
                    HuanqiuPay.bindWithdrawCard(channelUser.getChannelUid(), payCard, channel);

                    redisUtil.set(getRedisKey(businessId, paramMapKey), bindCardRes.getMap(), expireTime);
                    return getPaymentResultVO(businessId, bindCardRes);

                case JIAFUTONG:
                    //佳付通不用绑卡
                    channelBankCard = new PaymentChannelBankCard();
                    break;
            }

            //通道不用绑卡/历史已绑卡但未入库
            channelBankCard.setUid(userBasic.getId());
            channelBankCard.setBankCardId(payCard.getId());
            channelBankCard.setChannelCode(channel.getCode());
            channelBankCard.setStatus(0);
            channelBankCard.setCreateTime(new Date());
            channelBankCard.setUpdateTime(new Date());

            paymentChannelBankCardMapper.insertSelective(channelBankCard);
        }


        //代还绑卡成功就更新订单状态
        if (channelTypeEnum.getKey() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
            //1.更新订单状态
            orderConfirm(tradeOrder);
        }

        PaymentResult res = new PaymentResult();
        res.setSuccess(PaymentResultCodeEnum.BIND_CARD_SUCCESS);

        return getPaymentResultVO(businessId, tradeOrderId, res);
    }


    /**
     * 重发绑卡验证码
     * @param userBasic
     * @param businessId
     * @return
     */
    public PaymentResultVO resendBindCardSMS(UserBasic userBasic, String businessId){
        //业务办理有效期判断
        if (businessIsTimeOut(businessId)){
            return getBusinessTimeOutVO(businessId);
        }

        PaymentResult res = new PaymentResult();
        //获取redis数据
        PaymentChannel channel = (PaymentChannel)redisUtil.get(getRedisKey(businessId, channelKey));
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        UserBankCardVo payCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, payCardKey));
        PaymentChannelUser channelUser = (PaymentChannelUser)redisUtil.get(getRedisKey(businessId, channelUserKey));
        Map<String,String> resMap = (Map) redisUtil.get(getRedisKey(businessId, paramMapKey));

        switch (channelEnum) {
            case TONGLIAN:
                res = TonglianPay.resendBindSms(channelUser.getChannelUid(), resMap, userBasic, payCard, channel);
                break;
            case HUANQIUHUIJU:
                res = HuanqiuPay.sendMessageCode(payCard, resMap);
                break;
        }
        if (res.isSuccess()) res.setMsg("验证码发送成功!");
        return getPaymentResultVO(businessId, res);
    }

    /**
     * 绑卡确认
     * @param userBasic
     * @param smscode
     * @param businessId
     * @return
     */
    public PaymentResultVO bindCardConfirm(UserBasic userBasic, String smscode, String businessId){
        //业务办理有效期判断
        if (businessIsTimeOut(businessId)){
            return getBusinessTimeOutVO(businessId);
        }

        PaymentChannel channel = (PaymentChannel)redisUtil.get(getRedisKey(businessId, channelKey));
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        UserBankCardVo payCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, payCardKey));
        PaymentChannelUser channelUser = (PaymentChannelUser)redisUtil.get(getRedisKey(businessId, channelUserKey));

        //判断是否已绑卡,未绑卡则确认绑卡
        PaymentResult bindCardRes = new PaymentResult();
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), payCard.getId(), channel.getCode());
        if (channelBankCard == null){
            String protocolCode = null;
            Map<String,String> resMap = (Map) redisUtil.get(getRedisKey(businessId, paramMapKey));
            switch (channelEnum) {
                case TONGLIAN:
                    bindCardRes = TonglianPay.bindCardConfirm(channelUser.getChannelUid(), smscode, resMap, userBasic, payCard, channel);
                    if (!bindCardRes.isSuccess()){
                        return getPaymentResultVO(businessId, bindCardRes);
                    }

                    protocolCode = bindCardRes.get("agreeid");
                    break;
                case HUANQIUHUIJU:
                    bindCardRes = HuanqiuPay.openCardConfirm(payCard, smscode, resMap);
                    if (!bindCardRes.isSuccess()){
                        return getPaymentResultVO(businessId, bindCardRes);
                    }

                    bindCardRes = HuanqiuPay.bindWithdrawCard(channelUser.getChannelUid(), payCard, channel);
                    if (!bindCardRes.isSuccess()){
                        return getPaymentResultVO(businessId, bindCardRes);
                    }
                    break;
            }

            channelBankCard = new PaymentChannelBankCard();
            channelBankCard.setUid(userBasic.getId());
            channelBankCard.setBankCardId(payCard.getId());
            channelBankCard.setChannelCode(channel.getCode());
            channelBankCard.setProtocolCode(protocolCode);
            channelBankCard.setStatus(0);
            channelBankCard.setCreateTime(new Date());
            channelBankCard.setUpdateTime(new Date());
            paymentChannelBankCardMapper.insertSelective(channelBankCard);
        }
        //获取交易订单
        UserTradeOrder tradeOrder = (UserTradeOrder)redisUtil.get(getRedisKey(businessId, tradeOrderKey));
        PaymentResultVO resultVO = getPaymentResultVO(businessId, tradeOrder.getId(), bindCardRes);
        //代还绑卡成功就更新订单状态
        if (bindCardRes.isSuccess() && channel.getType() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
            //1.更新订单状态
            orderConfirm(tradeOrder);
        }
        if (resultVO.getSuccess()) resultVO.setMsg(PaymentResultCodeEnum.SUCCESS.getMsg());
        return resultVO;
    }


    /**
     * 代偿支付
     * @param userBasic
     * @param tradeOrder
     * @param payRecord
     * @return
     */
    public PaymentResultVO applyFreepassPay(UserBasic userBasic, UserTradeOrder tradeOrder, UserTradePaymentRecord payRecord){
        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(tradeOrder.getChannelId());
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = paymentChannelUserService.queryChannelUser(userBasic.getId(), tradeOrder.getReceiveCardId(), channelEnum);
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), tradeOrder.getPaymentCardId(), channel.getCode());
        UserBankCardVo payCard = userBankCardMapper.selectByPrimaryKey(tradeOrder.getPaymentCardId());
        rsaService.bankCardDecode(payCard);

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TENGFUTONG:
                res = TengfutongPay.freepassPay(channelUser.getChannelUid(), payRecord.getOrderId(), payRecord.getAmount(),userBasic, payCard, channel);
                break;
            case TONGLIAN:
                res = TonglianPay.freepassPay(channelUser.getChannelUid(), channelBankCard.getProtocolCode(), tradeOrder.getCity(), "消费", channel, payRecord);
                break;
            case HUANQIUHUIJU:
                res = HuanqiuPay.pay(payCard, channelUser.getChannelUid(), payRecord, channel);
                break;
        }
        return getPaymentResultVO(res);
    }

    /**
     * 代偿提现
     * @param userBasic
     * @param tradeOrder
     * @param recRecord
     * @return
     */
    public PaymentResultVO freepassPayWithdraw(UserBasic userBasic, UserTradeOrder tradeOrder, UserTradePaymentRecord recRecord){
        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(tradeOrder.getChannelId());
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = paymentChannelUserService.queryChannelUser(userBasic.getId(), tradeOrder.getReceiveCardId(), channelEnum);
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), tradeOrder.getPaymentCardId(), channel.getCode());
        UserBankCardVo payCard = userBankCardMapper.selectByPrimaryKey(tradeOrder.getPaymentCardId());
        UserBankCardVo recCard = userBankCardMapper.selectByPrimaryKey(tradeOrder.getReceiveCardId());
        rsaService.bankCardDecode(payCard);
        rsaService.bankCardDecode(recCard);

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TENGFUTONG:
                res = TengfutongPay.freepassPayWithdraw(channelUser.getChannelUid(), recRecord, channel);
                break;
            case TONGLIAN:
                res = TonglianPay.freepassPayWithdraw(channelUser.getChannelUid(), recRecord.getOrderId(), channelBankCard.getProtocolCode(), recRecord.getAmount(), channel);
                break;
            case HUANQIUHUIJU:
                res = HuanqiuPay.withdraw(channelUser.getChannelUid(), recCard, recRecord.getAmount(), channel);
                break;
        }
        return getPaymentResultVO( res);
    }

    /**
     * 申请收款
     * @param businessId
     * @param userBasic
     * @return
     */
    public PaymentResultVO applyQuickPay(String businessId, UserBasic userBasic){
        //业务办理有效期判断
        if (businessIsTimeOut(businessId)){
            return getBusinessTimeOutVO(businessId);
        }

        UserTradeOrder tradeOrder = (UserTradeOrder)redisUtil.get(getRedisKey(businessId, tradeOrderKey));
        UserTradePaymentRecord payRecord = userTradePaymentRecordMapper.findByTradeIdAndType(tradeOrder.getId(), PaymentTypeEnum.PAYMENT.getKey()).get(0);

        PaymentChannel channel = (PaymentChannel)redisUtil.get(getRedisKey(businessId, channelKey));
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = (PaymentChannelUser)redisUtil.get(getRedisKey(businessId, channelUserKey));
        UserBankCardVo payCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, payCardKey));
        UserBankCardVo recCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, recCardKey));
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), tradeOrder.getPaymentCardId(), channel.getCode());

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TONGLIAN:
                res = TonglianPay.applyQuickPay(channelUser.getChannelUid(), payRecord.getOrderId(), channelBankCard.getProtocolCode(), tradeOrder.getPaymentAmount(), tradeOrder.getSummary(), channel);
                break;
            case JIAFUTONG:
                res = JiafutongPay.applyQuickPay(channelUser.getChannelUid(), payRecord.getOrderId(), tradeOrder.getPaymentAmount(), userBasic, payCard, channel);
                if (res.isSuccess()){
                    PaymentResult sendSMSRes = JiafutongPay.resendQuickPaySms(payCard, res.getMap());
                    if (sendSMSRes.isSuccess())res.setSuccess(PaymentResultCodeEnum.SMS_SEND_SUCCESS);
                }
                break;
            case TENGFUTONG:
                res = TengfutongPay.freepassPay(channelUser.getChannelUid(), payRecord.getOrderId(), tradeOrder.getPaymentAmount(), userBasic, payCard, channel);
                break;
        }

        redisUtil.set(getRedisKey(businessId, paramMapKey), res.getMap(), expireTime);
        return getPaymentResultVO(businessId, res);
    }

    /**
     * 重新发送收款验证码
     * @param businessId
     * @param userBasic
     * @return
     */
    public PaymentResultVO resendQuickPaySMS(String businessId, UserBasic userBasic){
        //业务办理有效期判断
        if (businessIsTimeOut(businessId)){
            return getBusinessTimeOutVO(businessId);
        }

        UserTradeOrder tradeOrder = (UserTradeOrder)redisUtil.get(getRedisKey(businessId, tradeOrderKey));
        PaymentChannel channel = (PaymentChannel)redisUtil.get(getRedisKey(businessId, channelKey));
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = (PaymentChannelUser)redisUtil.get(getRedisKey(businessId, channelUserKey));
        UserBankCardVo payCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, payCardKey));
        UserBankCardVo recCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, recCardKey));

        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), tradeOrder.getPaymentCardId(), channel.getCode());
        Map<String,String> resMap = (Map) redisUtil.get(getRedisKey(businessId, paramMapKey));

        PaymentResult res = new PaymentResult();

        switch (channelEnum) {
            case TONGLIAN:
                res = TonglianPay.resendQuickpaySms(channelUser.getChannelUid(), channelBankCard.getProtocolCode(), resMap, channel);
                break;
            case JIAFUTONG:
                res = JiafutongPay.resendQuickPaySms(payCard, resMap);
                break;
            case TENGFUTONG:
                break;
        }

        return getPaymentResultVO(businessId, res);
    }

    /**
     * 确认收款
     * @param businessId
     * @param smscode
     * @param userBasic
     * @return
     */
    public PaymentResultVO confirmQuickPay(String businessId, String smscode, UserBasic userBasic){
        //业务办理有效期判断
        if (businessIsTimeOut(businessId)){
            return getBusinessTimeOutVO(businessId);
        }

        UserTradeOrder tradeOrder = (UserTradeOrder)redisUtil.get(getRedisKey(businessId, tradeOrderKey));
        PaymentChannel channel = (PaymentChannel)redisUtil.get(getRedisKey(businessId, channelKey));
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = (PaymentChannelUser)redisUtil.get(getRedisKey(businessId, channelUserKey));
        UserBankCardVo payCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, payCardKey));
        UserBankCardVo recCard = (UserBankCardVo)redisUtil.get(getRedisKey(businessId, recCardKey));
        PaymentChannelBankCard channelBankCard = paymentChannelBankCardMapper.findByUidAndBankCardIdAndChannelCode(userBasic.getId(), tradeOrder.getPaymentCardId(), channel.getCode());
        UserTradePaymentRecord recRecord = userTradePaymentRecordMapper.findByTradeIdAndType(tradeOrder.getId(), PaymentTypeEnum.REPAYMENT.getKey()).get(0);
        Map<String,String> resMap = (Map) redisUtil.get(getRedisKey(businessId, paramMapKey));

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TONGLIAN:
                res = TonglianPay.confirmQuickPay(channelUser.getChannelUid(), channelBankCard.getProtocolCode(), smscode, resMap, channel);
                JSONObject json = new JSONObject();
                json.put("trxid", res.get("trxid"));
                recRecord.setOtherParams(json.toJSONString());
                break;
            case JIAFUTONG:
                res = JiafutongPay.confirmQuickPay(payCard, smscode, resMap);
                break;
            case TENGFUTONG:
                break;
        }

        if (res.isSuccess()){
            //1.更新订单状态
            orderConfirm(tradeOrder);

            //2.更新收款记录otherParams
            UserTradePaymentRecord uptRecord = new UserTradePaymentRecord();
            uptRecord.setId(recRecord.getId());
            uptRecord.setOtherParams(recRecord.getOtherParams());
            userTradePaymentRecordMapper.updateByPrimaryKeySelective(uptRecord);
        }
        return getPaymentResultVO(businessId, res);
    }

    /**
     * 快捷支付提现
     * @param recRecord
     * @return
     */
    public PaymentResult quickpayWithdraw(UserTradePaymentRecord recRecord){
        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(recRecord.getChannelId());
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());
        PaymentChannelUser channelUser = paymentChannelUserService.queryChannelUser(recRecord.getUid(), recRecord.getCardId(), channelEnum);

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TONGLIAN:
                res = TonglianPay.quickPayWithdraw(channelUser.getChannelUid(), recRecord, channel);
                break;
            case JIAFUTONG:
                //佳付通自动提现
                res.setSuccess();
                break;
            case TENGFUTONG:
                res = TengfutongPay.freepassPayWithdraw(channelUser.getChannelUid(), recRecord, channel);
                break;
        }
        return res;
    }

    /**
     * 订单查询(支付/提现状态)
     * @param recode
     * @return
     */
    public PaymentResult orderQuery(UserTradePaymentRecord recode){
        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(recode.getChannelId());
        PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());

        PaymentResult res = new PaymentResult();
        switch (channelEnum) {
            case TONGLIAN:
                if (channel.getType() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
                    res = TonglianPay.freepassPayQuery(recode.getOrderId(), channel);
                }else if (channel.getType() == PaymentChannelTypeEnum.SHOUKUAN.getKey()){
                    res = TonglianPay.quickPayQuery(recode.getOrderId(), channel);
                }
                break;
            case JIAFUTONG:
                if (recode.getType() == PaymentTypeEnum.PAYMENT.getKey()){
                    res = JiafutongPay.orderQuery(recode.getOrderId(), channel);
                }else{
                    //佳付通付款成功是自动提现的.
                    res.setSuccess();
                }
                break;
            case TENGFUTONG:
                res = TengfutongPay.freepassPayQuery(recode.getOrderId(), channel);
                break;
            case HUANQIUHUIJU:
                if (PaymentTypeEnum.PAYMENT.getKey() == recode.getType()) {
                    res = HuanqiuPay.queryOrder(HuanqiuTransTypeEnum.TRANSACTION, recode.getOrderId(), channel);
                } else {
                    res = HuanqiuPay.queryOrder(HuanqiuTransTypeEnum.WITHDRAW, recode.getOrderId(), channel);
                }
                break;
        }
        return res;
    }

    /**
     * 费率更新
     */
    public void ratioUpdate(){
        List<PaymentChannel> channelList = paymentChannelMapper.findByStatus(1);
        for(PaymentChannel channel : channelList){
            PaymentChannelEnum channelEnum = PaymentChannelEnum.get(channel.getCode());

            List<PaymentChannelUser> channelUserList = paymentChannelUserMapper.findByChannelCode(channel.getCode());
            for (PaymentChannelUser channelUser : channelUserList){
                PaymentResult res = null;
                switch (channelEnum){
                    case TONGLIAN:
                        //储蓄卡
                        UserBankCard qrBankCard = new UserBankCard();
                        qrBankCard.setUid(channelUser.getUid());
                        qrBankCard.setBankCardType(BankCardTypeEnum.DEPOSIT_CARD.getKey());
                        List<UserBankCardVo> bkList = userBankCardMapper.findByUidAndType(qrBankCard);
                        if (!bkList.isEmpty()){
                            UserBankCardVo userBankCard1 = bkList.get(0);
                            rsaService.bankCardDecode(userBankCard1);
                            res = TonglianPay.rateUpdate(channelUser.getChannelUid(), userBankCard1.getBankCardNo(), channel);
                        }
                        break;
                    case JIAFUTONG:
                        res = JiafutongPay.rateUpdate(channelUser.getChannelUid(), channel);
                        break;
                    case TENGFUTONG:
                        //腾付通没有更新费率接口,但是可以重复进件,所以更新费率机制就是把用户在通道注册的信息从DB删掉,下次自动再开户
                        paymentChannelUserMapper.deleteByUidAndChannelCode(channelUser.getUid(), channelUser.getChannelCode());
                        paymentChannelBankCardMapper.deleteByUidAndChannelCode(channelUser.getUid(), channelUser.getChannelCode());
                        break;
                    case HUANQIUHUIJU:
                        //每次交易把手续费传过去,不用在通道设置
                        res = new PaymentResult();
                        res.setSuccess();
                        break;
                }

                if(res!=null){
                    log.info(res.toString());
                }
            }
        }
    }

    /**
     * 更新订单状态: 待确认->进行中
     * @param tradeOrder
     */
    private void orderConfirm(UserTradeOrder tradeOrder){
        tradeOrder.setStatus(TradeOrderStatusEnum.ING.getKey());
        //1.更新订单状态为进行中
        UserTradeOrder uptTradeOrder = new UserTradeOrder();
        uptTradeOrder.setId(tradeOrder.getId());
        uptTradeOrder.setStatus(TradeOrderStatusEnum.ING.getKey());
        userTradeOrderMapper.updateByPrimaryKeySelective(uptTradeOrder);
        if (tradeOrder.getType() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
            //代偿把所有记录更新为待支付
            userTradePaymentRecordMapper.updateStatusByTradeId(PaymentStatusEnum.WAITING_PAY.getKey(), tradeOrder.getId());
        }else if(tradeOrder.getType() == PaymentChannelTypeEnum.SHOUKUAN.getKey()){
            //收款只把支付记录更新为支付待确认
            userTradePaymentRecordMapper.updateStatusByTradeIdAndType(PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey(), tradeOrder.getId(), PaymentTypeEnum.PAYMENT.getKey());
        }
    }


    /**
     * 生成订单和支付计划
     * @param tradeOrder
     * @param channel
     * @return
     */
    private Integer orderCreate(String businessId, UserTradeOrder tradeOrder, PaymentChannel channel) throws InterfaceException{
        List<UserTradePaymentRecord> records = new ArrayList<>();

        tradeOrder.setChannelId(channel.getId());
        tradeOrder.setSummary("消费");
        tradeOrder.setReceiveAmount(0D);
        tradeOrder.setServiceCharge(0D);
        tradeOrder.setStatus(TradeOrderStatusEnum.WAITING_CONFIRM.getKey());
        userTradeOrderMapper.insertSelective(tradeOrder);
        redisUtil.set(getRedisKey(businessId, tradeOrderKey), tradeOrder, expireTime);

        if (channel.getType() ==  PaymentChannelTypeEnum.DAIHUAN.getKey()){
            records.addAll(orderToRecord(tradeOrder, channel));
            //还款金额
            Double repaymentAmount = 0D;
            //手续费
            Double serviceCharge = 0D;
            for(UserTradePaymentRecord temp : records){
                //设置订单编号
                temp.setOrderId(orderNumUtils.generate());
                //计算手续费
                serviceCharge = MathUtils.add(serviceCharge, temp.getServiceCharge(), 2);
                //计算还款金额
                if (temp.getType() == PaymentTypeEnum.REPAYMENT.getKey()) repaymentAmount = MathUtils.add(repaymentAmount, temp.getAmount(), 2);
            }

            tradeOrder.setServiceCharge(serviceCharge);
            tradeOrder.setReceiveAmount(repaymentAmount);
        }else{
            UserTradePaymentRecord payRecord = new UserTradePaymentRecord();
            UserTradePaymentRecord recRecord = new UserTradePaymentRecord();

            //支付手续费
            double payServiceCharge = MathUtils.round(tradeOrder.getPaymentAmount() * channel.getUserPaymentRate(), BigDecimal.ROUND_UP, 2);
            //总手续费
            double totalServiceCharge = MathUtils.add(payServiceCharge, channel.getUserWithdrawCharge(), 2);

            //支付记录
            payRecord.setUid(tradeOrder.getUid());
            payRecord.setTradeId(tradeOrder.getId());
            payRecord.setOrderId(orderNumUtils.generate());
            payRecord.setType(PaymentTypeEnum.PAYMENT.getKey());
            payRecord.setAmount(tradeOrder.getPaymentAmount());
            payRecord.setCardId(tradeOrder.getPaymentCardId());
            payRecord.setServiceCharge(payServiceCharge);
            payRecord.setChannelId(tradeOrder.getChannelId());
            payRecord.setPaymentTime(new Date());
            payRecord.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
            payRecord.setCreateTime(new Date());
            payRecord.setUpdateTime(new Date());

            //提现记录
            recRecord.setUid(tradeOrder.getUid());
            recRecord.setTradeId(tradeOrder.getId());
            recRecord.setOrderId(orderNumUtils.generate());
            recRecord.setType(PaymentTypeEnum.REPAYMENT.getKey());
            recRecord.setCardId(tradeOrder.getReceiveCardId());
            recRecord.setAmount(MathUtils.sub(tradeOrder.getPaymentAmount(), totalServiceCharge, 2));
            recRecord.setServiceCharge(channel.getUserWithdrawCharge());
            recRecord.setChannelId(tradeOrder.getChannelId());
            recRecord.setPaymentTime(new Date());
            recRecord.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
            recRecord.setCreateTime(new Date());
            recRecord.setUpdateTime(new Date());

            records.add(payRecord);
            records.add(recRecord);

            tradeOrder.setReceiveAmount(MathUtils.sub(tradeOrder.getPaymentAmount(), payServiceCharge, 2));
            tradeOrder.setServiceCharge(totalServiceCharge);
        }
        userTradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
        userTradePaymentRecordMapper.insertList(records);
        return tradeOrder.getId();
    }





    private String getRedisKey(String businessId, String paramName){
        return new StringBuffer(businessId).append("_").append(paramName).toString();
    }
    private PaymentResultVO getPaymentResultVO(PaymentResult res){
        return getPaymentResultVO(null, null, res);
    }
    private PaymentResultVO getPaymentResultVO(String businessId, PaymentResult res){
        return getPaymentResultVO(businessId, null, res);
    }
    private PaymentResultVO getPaymentResultVO(String businessId, Integer tradeOrderId, PaymentResult res){
        PaymentResultVO resVO = new PaymentResultVO();
        BeanUtils.copyProperties(res, resVO);
        resVO.setBusinessId(businessId);
        resVO.setTradeOrderId(tradeOrderId);
        resVO.setSuccess(res.isSuccess());
        return resVO;
    }

    /**
     * 检查当前业务办理是否已超时
     * @param businessId
     * @return
     */
    private boolean businessIsTimeOut(String businessId){
        return redisUtil.hasKey(getRedisKey(businessId, channelKey))? false : true;
    }

    /**
     * 获取业务办理超时返回结果
     * @param businessId
     * @return
     */
    private PaymentResultVO getBusinessTimeOutVO(String businessId){
        PaymentResultVO resultVO = new PaymentResultVO();
        resultVO.setMsg("业务办理已超时,请返回首页重新申请!");
        return resultVO;
    }

    /**
     * 自动选择通道
     * @param amount 单笔金额
     * @param payCardId 支付卡id
     * @param repayCardId 收款卡id
     * @param channelTypeEnum
     * @return
     */
    public PaymentChannel getPaymentChannel(double amount, int payCardId, int repayCardId, PaymentChannelTypeEnum channelTypeEnum) throws InterfaceException {
        UserBankCardVo payCard = userBankCardMapper.selectByPrimaryKey(payCardId);
        UserBankCardVo repayCard = userBankCardMapper.selectByPrimaryKey(repayCardId);
        if (payCard == null) throw new InterfaceException("支付卡不存在");
        if (repayCard == null) throw new InterfaceException("收款卡不存在");

        //在运行的通道列表
        List<PaymentChannel> runingChannels = paymentChannelMapper.findByTypeAndStatus(channelTypeEnum.getKey(), 1);
        //对该银行支持的通道列表
        List<PaymentChannel> supportChannels = new ArrayList<>();
        //不考虑限额,支持该银行通道数
        int supportNotLimitCount = 0;
        if (channelTypeEnum.getKey() == PaymentChannelTypeEnum.SHOUKUAN.getKey()){
            int normalSingleLimit = 5 * 10000;
            for(PaymentChannel channel : runingChannels){
                //查询通道是否支持该银行,根据具体通道觉得是否使用
                PaymentChannelBank channelPayBank = paymentChannelBankMapper.findByChannelIdAndBankId(channel.getId(), payCard.getBankId());
                PaymentChannelBank channelRepayBank = paymentChannelBankMapper.findByChannelIdAndBankId(channel.getId(), repayCard.getBankId());
                /**
                 * 佳付通-收款
                 * 费率第一低,大额
                 * 支持所有银行(长沙银行除外)
                 */
                if ( !payCard.getBankName().contains("长沙银行")
                        && channel.getCode().equals(PaymentChannelEnum.JIAFUTONG.getCode())){
                    if (channelRepayBank == null && !repayCard.getBankName().contains("招商银行")){
                        log.warn("佳付通不支持该收款银行{}", repayCard.getBankName());
                        continue;
                    }
                    supportNotLimitCount += 1;
                    if( (channelPayBank != null && amount <= channelPayBank.getSingleLimit())
                            || (channelPayBank == null && amount <= normalSingleLimit) ){
                        supportChannels.add(channel);
                    }
                    continue;
                }


                /**
                 * 通联(线下)-收款
                 * 费率第二低,大额落地(保险)
                 * 支持所有银行(不支持交通银行,招商银行容易被电话拦截)
                 */
                if ( !payCard.getBankName().contains("交通银行")
//                        && !bankCard.getBankName().contains("招商银行")
                        && channel.getCode().equals(PaymentChannelEnum.TONGLIAN.getCode())
                        && channel.getOtherParams().contains("QUICKPAY_OF_NP") ){
                    supportNotLimitCount += 1;
                    if( (channelPayBank != null && amount <= channelPayBank.getSingleLimit())
                            || (channelPayBank == null && amount <= normalSingleLimit) ){
                        supportChannels.add(channel);
                    }
                    continue;
                }


                /**
                 * 通联(线上)-收款
                 * 费率第三低,大额线上(商旅)
                 * 支持所有银行(不支持交通银行,招商银行容易被电话拦截)
                 */
                if ( !payCard.getBankName().contains("交通银行")
//                        && !bankCard.getBankName().contains("招商银行")
                        && channel.getCode().equals(PaymentChannelEnum.TONGLIAN.getCode())
                        && channel.getOtherParams().contains("QUICKPAY_OL_HP") ){
                    supportNotLimitCount += 1;
                    if( (channelPayBank != null && amount <= channelPayBank.getSingleLimit())
                            || (channelPayBank == null && amount <= normalSingleLimit) ){
                        supportChannels.add(channel);
                    }
                    continue;
                }
            }
        }else if (channelTypeEnum.getKey() == PaymentChannelTypeEnum.DAIHUAN.getKey()){
            //普通单笔限额
            int normalSingleLimit = 1 * 1000;
            for(PaymentChannel channel : runingChannels){
                //查询通道是否支持该银行,根据具体通道觉得是否使用
                PaymentChannelBank channelBank = paymentChannelBankMapper.findByChannelIdAndBankId(channel.getId(), payCard.getBankId());
                /**
                 * 通联-代偿
                 * 支持所有银行(交通银行除外)
                 * 费率第一低,小额落地
                 */
                if ( !payCard.getBankName().contains("交通银行")
                        && channel.getCode().equals(PaymentChannelEnum.TONGLIAN.getCode()) ){
                    supportNotLimitCount += 1;
                    if( (channelBank != null && amount <= channelBank.getSingleLimit())
                            || (channelBank == null && amount <= normalSingleLimit) ){
                        supportChannels.add(channel);
                    }
                    continue;
                }

                /**
                 * 环球-代偿
                 * 支持所有银行(交通银行/花旗银行除外)
                 * 费率同通联
                 * 光大/中行单笔500,单日1000,其他单笔1000
                 */
                if ( !payCard.getBankName().contains("交通银行")
                        && !payCard.getBankName().contains("花旗银行")
                        && channel.getCode().equals(PaymentChannelEnum.HUANQIUHUIJU.getCode())){
                    supportNotLimitCount += 1;
                    if( (channelBank != null && amount <= channelBank.getSingleLimit())
                            || (channelBank == null && amount <= normalSingleLimit) ){
                        supportChannels.add(channel);
                    }
                    continue;
                }

                /**
                 * 腾付通-代偿
                 * 支持15家主流银行
                 * 费率最高,大额落地
                 * 中国银行,邮政银行,招商银行,光大银行,中信银行,华夏银行,浦发银行,民生银行,广发银行,兴业银行,上海银行,北京银行,平安银行,建设银行,交通银行,
                 */
                if ( channelBank != null && channel.getCode().equals(PaymentChannelEnum.TENGFUTONG.getCode()) ){
                    supportNotLimitCount += 1;
                    if( amount <= channelBank.getSingleLimit() ){
                        supportChannels.add(channel);
                    }
                    continue;
                }
            }
        }

        if (supportNotLimitCount == 0){
            log.error("当前卡无可用通道,请稍后再试,支付卡:{},收款卡:{}", payCard.getBankName(),repayCard.getBankName());
            throw new InterfaceException("当前卡无可用通道,请稍后再试");
        }else if(supportChannels.isEmpty()){
            log.warn("{},金额:{},支付卡:{},收款卡:{},金额超限,请减少账单金额或推迟最后还款日", channelTypeEnum.getCode(), amount, payCard.getBankName(),repayCard.getBankName());
            throw new InterfaceException("金额超限,请减少账单金额或推迟最后还款日");
        }
        PaymentChannel channel = supportChannels.get(0);
        log.info("{},{},单笔金额:{},支持通道数:{},推荐通道:{}", channelTypeEnum.getCode(),payCard.getBankName(),amount,supportChannels.size(),channel.getName());
        return channel;
    }

    private List<UserTradePaymentRecord> orderToRecord(UserTradeOrder tradeOrder, PaymentChannel channel) throws InterfaceException{
        UserBankCardVo payCard = userBankCardMapper.selectByPrimaryKey(tradeOrder.getPaymentCardId());
        PaymentChannelBank channelBank = paymentChannelBankMapper.findByChannelIdAndBankId(channel.getId(), payCard.getBankId());

        //初始化拆分账单所需参数
        BillParams billParams = BillUtils.initBillParams(tradeOrder, channel, channelBank);
        log.info("账单拆分信息:\n{}", billParams.toString());

        /**********1.限额校验**********/
        //卡内余额校验
        if (billParams.getCardBalance() < billParams.getCardBalanceMin()){
            throw new InterfaceException("卡剩余额度不足,最低要求" + billParams.getCardBalanceMin());
        }
        //单笔超限校验
        if (billParams.getSingleAmountMin() > billParams.getSingleAmountMax()){
            log.warn("用户:{},单笔额度超限,单笔限额{}", tradeOrder.getUid(), billParams.getSingleAmountMax());
            throw new InterfaceException("金额超限,请减少账单金额或推迟最后还款日");
        }
        //单期超限校验
        if (billParams.getSingleAmountMin() * 3 > billParams.getPeriodAmountMax()){
            log.warn("用户:{},单期额度超限,单期限额{}", tradeOrder.getUid(), billParams.getPeriodAmountMax());
            throw new InterfaceException("金额超限,请减少账单金额或推迟最后还款日");
        }
        //单日超限校验
        if (billParams.getSingleAmountMin() * 9 > billParams.getDayAmountMax()){
            log.warn("用户:{},单日额度超限,单日限额{}", tradeOrder.getUid(), billParams.getDayAmountMax());
            throw new InterfaceException("金额超限,请减少账单金额或推迟最后还款日");
        }

        /**********2.随机每期金额放到集合**********/
        List<Double> periodAmounts = BillUtils.getPeriodAmounts(billParams);


        /**********3.期数金额随机分配到还款日期(每日最多3笔)**********/
        Map<String, List<Double>> payDayPeriodMap = BillUtils.getDatePayPeriodAmount(billParams, periodAmounts);


        /****4.把每期金额拆分成交易记录****/
        List<UserTradePaymentRecord> allRecord = new ArrayList<>();
        for(String ymdKey : payDayPeriodMap.keySet()){
            //当天期数金额集合
            List<Double> dayPeriodAmounts = payDayPeriodMap.get(ymdKey);
            //当天支付记录集合
            List<UserTradePaymentRecord> records = BillUtils.getDatePayRecord(ymdKey, dayPeriodAmounts, billParams);
            //添加到总记录
            allRecord.addAll(records);
        }
        Collections.sort(allRecord, Comparator.comparing(UserTradePaymentRecord::getPaymentTime));

        return allRecord;
    }

}