package com.slljr.finance.front.runnable;

import com.slljr.finance.common.enums.PaymentResultCodeEnum;
import com.slljr.finance.common.enums.PaymentStatusEnum;
import com.slljr.finance.common.enums.PaymentTypeEnum;
import com.slljr.finance.common.enums.TradeOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.RepayErrorRecord;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.PaymentResultVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.pojo.vo.UserTradePaymentRecordVO;
import com.slljr.finance.common.utils.DateUtil;
import com.slljr.finance.common.utils.SpringContextHolder;
import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
import com.slljr.finance.front.service.*;
import com.slljr.finance.payment.utils.OrderNumUtils;
import com.slljr.finance.payment.utils.PaymentResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FreepassPayRunnable implements Runnable {
    private static final Logger log = LogManager.getLogger();

    public FreepassPayRunnable(UserTradePaymentRecordVO userTradePaymentRecord){
        this.userTradePaymentRecord = userTradePaymentRecord;
        this.paymentService = SpringContextHolder.getBean(PaymentService.class);
        this.userBasicService = SpringContextHolder.getBean(UserBasicService.class);
        this.userTradeOrderService = SpringContextHolder.getBean(UserTradeOrderService.class);
        this.userTradePaymentRecordMapper = SpringContextHolder.getBean(UserTradePaymentRecordMapper.class);
        this.userAccountService = SpringContextHolder.getBean(UserAccountService.class);
        this.repayErrorRecordService = SpringContextHolder.getBean(RepayErrorRecordService.class);
        this.orderNumUtils = SpringContextHolder.getBean(OrderNumUtils.class);
        this.env = SpringContextHolder.getBean(Environment.class);
    }

    UserTradePaymentRecordVO userTradePaymentRecord;

    PaymentService paymentService;

    UserBasicService userBasicService;

    UserTradeOrderService userTradeOrderService;

    UserTradePaymentRecordMapper userTradePaymentRecordMapper;

    UserAccountService userAccountService;

    RepayErrorRecordService repayErrorRecordService;

    OrderNumUtils orderNumUtils;

    private Environment env;

    @Override
    public void run() {
        //获取用户信息
        UserBasicVO userBasic = userBasicService.selectUserBasicById(userTradePaymentRecord.getUid());
        //获取交易信息
        UserTradeOrder userTradeOrder = userTradeOrderService.selectUserTradeOrderById(userTradePaymentRecord.getTradeId());

        log.info("FreepassPayRunnable→类型{}", userTradePaymentRecord.getType());
        //支付
        if (null != userTradePaymentRecord.getType()) {
            PaymentResultVO paymentResultVO;
            //支付待确认订单，查询并更新订单状态
            if (PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey() == userTradePaymentRecord.getStatus()) {
                queryAndUpdateOrder(userTradePaymentRecord);
            } else {
                if (PaymentTypeEnum.PAYMENT.getKey() == userTradePaymentRecord.getType()) {
                    //执行支付
                    paymentResultVO = paymentService.applyFreepassPay(userBasic, userTradeOrder, userTradePaymentRecord);
                } else {
                    //提现
                    paymentResultVO = paymentService.freepassPayWithdraw(userBasic, userTradeOrder, userTradePaymentRecord);
                }

                if (paymentResultVO.getSuccess()) {
                    if (userTradePaymentRecord.getDataType().equals(1)) {
                        //更新明细状态支付待确认
                        userTradeOrderService.updateTradePaymentStatus(userTradePaymentRecord.getId(), PaymentStatusEnum.PAY_WAITING_CONFIRM);
                    } else {
                        updateRepayError(userTradePaymentRecord, PaymentStatusEnum.PAY_WAITING_CONFIRM);
                    }
                } else {
                    log.warn("支付结果:失败!tradeId:{}, recordId:{}, msg:{}", userTradePaymentRecord.getTradeId(), userTradePaymentRecord.getId(), paymentResultVO.toString());
                    //重试
                    Integer retryCount = Integer.valueOf(env.getProperty("pay.retry.count"));
                    errorRetry(userTradePaymentRecord, paymentResultVO, retryCount);
                }
            }

        }

    }

    /**
     * 查询在通道支付/提现 的订单处理状态，成功或失败更新订单明细和交易状态
     *
     * @author uncle.quentin
     * @date   2019/1/11 15:39
     * @param   userTradePaymentRecord
     * @return void
     * @version 1.0
     */
    private void queryAndUpdateOrder(UserTradePaymentRecordVO userTradePaymentRecord){
        //支付待确认订单→查询订单状态→成功则更新订单明细状态为成功
        PaymentResult paymentQuery = paymentService.orderQuery(userTradePaymentRecord);
        if (paymentQuery.isSuccess()) {
            if (userTradePaymentRecord.getDataType().equals(1)) {
                //更新订单明细状态→成功
                log.info(String.format("订单成功，通道ID：%s；订单明细ID：%s；子商户订单号：%s", userTradePaymentRecord.getChannelId(), userTradePaymentRecord.getId(), userTradePaymentRecord.getOrderId()));
                userTradeOrderService.updateTradePaymentStatus(userTradePaymentRecord.getId(), PaymentStatusEnum.PAY_SUCCESS);
                //如果是最后一笔，更新整笔订单状态为成功
                List<UserTradePaymentRecord> userTradePaymentRecords = userTradePaymentRecordMapper.findByTradeIdOrderByPaymentTimeAsc(userTradePaymentRecord.getTradeId());
                if (userTradePaymentRecord.getId().equals(userTradePaymentRecords.get(userTradePaymentRecords.size() - 1).getId())) {
                    //出现有一笔失败则整笔交易失败
                    userTradeOrderService.updateOrderStatus(userTradePaymentRecord.getTradeId(), TradeOrderStatusEnum.SUCCESS);
                    //更新奖励信息
                    try {
                        userAccountService.updateUserRewardByCompletePay(userTradePaymentRecord.getUid(), userTradePaymentRecord.getTradeId());
                    } catch (InterfaceException e) {
                        log.error("更新任务奖励失败：", e);
                    }
                }
            } else {
                updateRepayError(userTradePaymentRecord, PaymentStatusEnum.PAY_SUCCESS);
            }

        } else if (!paymentQuery.isSuccess() && paymentQuery.getCode() == PaymentResultCodeEnum.PAY_FAIL.getCode()) {
            if (userTradePaymentRecord.getDataType().equals(1)) {
                //更新订单明细状态→失败
                userTradeOrderService.updateTradePaymentStatus(userTradePaymentRecord.getId(), PaymentStatusEnum.FAIL);
                //出现有一笔失败则整笔交易失败
                userTradeOrderService.updateOrderStatus(userTradePaymentRecord.getTradeId(), TradeOrderStatusEnum.FAIL_SOME);
            } else {
                updateRepayError(userTradePaymentRecord, PaymentStatusEnum.FAIL);
            }
        }
    }

    /**
     * 更新异常还款数据
     *
     * @author uncle.quentin
     * @date   2019/2/22 15:51
     * @param   userTradePaymentRecord
     * @return int
     * @version 1.0
     */
    private int updateRepayError(UserTradePaymentRecordVO userTradePaymentRecord,PaymentStatusEnum paymentStatusEnum){
        RepayErrorRecord uptRecord = new RepayErrorRecord();
        uptRecord.setId(userTradePaymentRecord.getId());
        uptRecord.setStatus(paymentStatusEnum.getKey());
        if (StringUtils.isNotBlank(userTradePaymentRecord.getErrorMsg())) {
            uptRecord.setErrorMsg(userTradePaymentRecord.getErrorMsg());
        }
        return repayErrorRecordService.updateRepayErrorRecord(uptRecord);
    }

    /**
     * 失败重置订单状态信息（订单号、状态、执行时间）
     *
     * @param userTradePaymentRecord 订单明细
     * @param paymentResultVO        支付结果
     * @param retryCount             最大重试次数
     * @return void
     * @author uncle.quentin
     * @date 2019/2/25 14:53
     * @version 1.0
     */
    private void errorRetry(UserTradePaymentRecordVO userTradePaymentRecord, PaymentResultVO paymentResultVO, int retryCount) {
        if (null != userTradePaymentRecord.getRetryCount() && userTradePaymentRecord.getRetryCount() + 1 > retryCount) {
            //异常提现记录类型判断[1正常,2异常]
            if (userTradePaymentRecord.getDataType() == 1) {
                //更新明细状态失败
                userTradeOrderService.updateTradePaymentStatus(userTradePaymentRecord.getId(), PaymentStatusEnum.FAIL, paymentResultVO.getMsg());

                //整笔交易失败
                userTradeOrderService.updateOrderStatus(userTradePaymentRecord.getTradeId(), TradeOrderStatusEnum.FAIL_SOME);

                //TODO 如果是异常还款的情况，新增异常还款记录
                /**3.生成异常还款记录**/
                userTradeOrderService.createRepayErrorRecord(userTradePaymentRecord.getTradeId());
            } else {
                updateRepayError(userTradePaymentRecord, PaymentStatusEnum.FAIL);
            }

        } else {
            //重置订单，重新执行（订单状态待支付）
            if (userTradePaymentRecord.getDataType() == 1) {
                UserTradePaymentRecord userTradePaymentRecord1 = new UserTradePaymentRecord();
                userTradePaymentRecord1.setId(userTradePaymentRecord.getId());
                //重置订单ID
                userTradePaymentRecord1.setOrderId(orderNumUtils.generate());
                userTradePaymentRecord1.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
                //执行时间往后推2分钟
                Date newTime = DateUtil.getDateByOffset(5, new Date(), 2);
                userTradePaymentRecord1.setPaymentTime(newTime);
                //重试次数+1
                userTradePaymentRecord1.setRetryCount(userTradePaymentRecord.getRetryCount() + 1);

                userTradePaymentRecordMapper.updateByPrimaryKeySelective(userTradePaymentRecord1);
            } else {
                RepayErrorRecord repayErrorRecord = new RepayErrorRecord();
                repayErrorRecord.setId(userTradePaymentRecord.getId());
                //重置订单ID
                repayErrorRecord.setOrderId(orderNumUtils.generate());
                repayErrorRecord.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
                //执行时间往后推2分钟
                Date newTime = DateUtil.getDateByOffset(5, new Date(), 2);
                repayErrorRecord.setPaymentTime(newTime);
                //重试次数+1
                repayErrorRecord.setRetryCount(userTradePaymentRecord.getRetryCount() + 1);
                repayErrorRecordService.updateRepayErrorRecord(repayErrorRecord);
            }

        }

    }

}
