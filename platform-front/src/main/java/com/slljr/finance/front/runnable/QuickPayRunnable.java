package com.slljr.finance.front.runnable;


import com.slljr.finance.common.enums.*;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.SpringContextHolder;
import com.slljr.finance.front.mapper.PaymentChannelMapper;
import com.slljr.finance.front.mapper.UserTradeOrderMapper;
import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
import com.slljr.finance.front.service.PaymentService;
import com.slljr.finance.front.service.UserAccountService;
import com.slljr.finance.front.service.UserTradeOrderService;
import com.slljr.finance.payment.utils.PaymentResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickPayRunnable implements Runnable {
    Logger log = LogManager.getLogger();
    public QuickPayRunnable(UserTradePaymentRecord record){
        this.record = record;
        this.redisUtil = SpringContextHolder.getBean(RedisUtil.class);
        this.paymentService = SpringContextHolder.getBean(PaymentService.class);
        this.userAccountService = SpringContextHolder.getBean(UserAccountService.class);
        this.userTradeOrderService = SpringContextHolder.getBean(UserTradeOrderService.class);
        this.userTradeOrderMapper = SpringContextHolder.getBean(UserTradeOrderMapper.class);
        this.paymentChannelMapper = SpringContextHolder.getBean(PaymentChannelMapper.class);
        this.userTradePaymentRecordMapper = SpringContextHolder.getBean(UserTradePaymentRecordMapper.class);
    }
    UserTradePaymentRecord record;

    RedisUtil redisUtil;
    PaymentService paymentService;
    UserAccountService userAccountService;
    UserTradeOrderService userTradeOrderService;
    UserTradeOrderMapper userTradeOrderMapper;
    PaymentChannelMapper paymentChannelMapper;
    UserTradePaymentRecordMapper userTradePaymentRecordMapper;

    @Override
    public void run() {
        log.info("收款{},orderId:{},recordId:{},amount:{}",record.getType()==PaymentTypeEnum.PAYMENT.getKey()?"支付":"提现",record.getTradeId(), record.getId(),record.getAmount());
        try {
            UserTradeOrder order = userTradeOrderMapper.selectByPrimaryKey(record.getTradeId());
            if(order.getType() == PaymentChannelTypeEnum.SHOUKUAN.getKey()){
                if (record.getType() == PaymentTypeEnum.PAYMENT.getKey()){
                    /**快捷支付支付状态查询,并更新DB/发起提现**/
                    //查询支付状态
                    PaymentResult payRes = paymentService.orderQuery(record);
                    log.info("支付结果:{},code:{},msg:{}", payRes.isSuccess(), payRes.getCode(), payRes.getMsg());
                    if (payRes.isSuccess() && payRes.getCode() == PaymentResultCodeEnum.SUCCESS.getCode()){
                        //支付成功,更新记录状态
                        userTradeOrderService.updateTradePaymentStatus(record.getId(), PaymentStatusEnum.PAY_SUCCESS);
                        //查询该笔订单提现记录
                        UserTradePaymentRecord repayRecord = userTradePaymentRecordMapper.findByTradeIdAndType(order.getId(), PaymentTypeEnum.REPAYMENT.getKey()).get(0);
                        //发起提现
                        PaymentResult repayRes = paymentService.quickpayWithdraw(repayRecord);
                        log.info("申请提现:{},code:{},msg:{}", repayRes.isSuccess(), repayRes.getCode(), repayRes.getMsg());
                        if (repayRes.isSuccess()){
                            //提现提交成功,更新记录状态
                            userTradeOrderService.updateTradePaymentStatus(repayRecord.getId(), PaymentStatusEnum.PAY_WAITING_CONFIRM);
                        }
                    }else if (payRes.getCode() == PaymentResultCodeEnum.FAIL.getCode() || payRes.getCode() != PaymentResultCodeEnum.PAY_ING.getCode()){
                        //非支付中认为失败,更新记录状态
                        userTradeOrderService.updateTradePaymentStatus(record.getId(), PaymentStatusEnum.FAIL, payRes.getMsg());
                        userTradeOrderService.updateOrderStatus(order.getId(), TradeOrderStatusEnum.FAIL_ALL);
                    }
                }else if (record.getType() == PaymentTypeEnum.REPAYMENT.getKey()){
                    /**快捷支付提现状态查询,并更新DB/佣金结算**/
                    //查询提现状态
                    PaymentResult repayRes = paymentService.orderQuery(record);
                    log.info("提现结果:{},code:{},msg:{}", repayRes.isSuccess(), repayRes.getCode(), repayRes.getMsg());
                    if (repayRes.isSuccess() && repayRes.getCode() == PaymentResultCodeEnum.SUCCESS.getCode()){
                        //提现成功,更新提现记录状态
                        userTradeOrderService.updateTradePaymentStatus(record.getId(), PaymentStatusEnum.PAY_SUCCESS);
                        /**计算公司利润**/
                        //更新推荐人佣金
                        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(order.getChannelId());
                        //支付费率差
                        Double payRateProfit = MathUtils.sub(channel.getUserPaymentRate(), channel.getPaymentRate(), 6);
                        //支付利润
                        Double payProfit = MathUtils.mul(order.getPaymentAmount(), payRateProfit, 4);
                        //还款利润
                        Double repayProfit = MathUtils.sub(channel.getUserWithdrawCharge(), channel.getWithdrawCharge(), 4);
                        //公司利润
                        Double companyProfit = MathUtils.add(payProfit, repayProfit, 2);

                        //更新订单状态
                        UserTradeOrder uptOrder = new UserTradeOrder();
                        uptOrder.setId(order.getId());
                        uptOrder.setCompanyProfit(companyProfit);
                        uptOrder.setStatus(TradeOrderStatusEnum.SUCCESS.getKey());
                        userTradeOrderMapper.updateByPrimaryKeySelective(uptOrder);

                        //更新佣金/积分信息
                        userAccountService.updateUserRewardByCompletePay(order.getUid(), order.getId());
                    }else if(repayRes.getCode() == PaymentResultCodeEnum.FAIL.getCode() || repayRes.getCode() != PaymentResultCodeEnum.PAY_ING.getCode()){
                        //提现失败,更新记录和订单状态
                        userTradeOrderService.updateTradePaymentStatus(record.getId(), PaymentStatusEnum.FAIL, repayRes.getMsg());
                        userTradeOrderService.updateOrderStatus(order.getId(), TradeOrderStatusEnum.FAIL_SOME);
                    }

                }
            }
        }catch (Exception e){
            log.error("快捷支付线程运行异常! \nrecord: {}, \nerrorMessage: {}", record.toString(), e);
            e.printStackTrace();
        }

        String recordKey = "UserTradePaymentRecord_" + record.getId();
        redisUtil.del(recordKey);

    }
}
