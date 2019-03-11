//package com.slljr.finance.front.scheduled;
//
//import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
//import com.slljr.finance.common.enums.PaymentStatusEnum;
//import com.slljr.finance.common.enums.TradeOrderStatusEnum;
//import com.slljr.finance.common.pojo.model.RepayErrorRecord;
//import com.slljr.finance.common.pojo.model.UserTradeOrder;
//import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
//import com.slljr.finance.common.pojo.vo.UserTradePaymentRecordVO;
//import com.slljr.finance.common.redis.RedisUtil;
//import com.slljr.finance.front.mapper.UserTradeOrderMapper;
//import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
//import com.slljr.finance.front.service.RabbitSender;
//import com.slljr.finance.front.service.RepayErrorRecordService;
//import com.slljr.finance.front.service.UserTradeOrderService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @description: 订单定时任务
// * @author: uncle.quentin.
// * @date: 2019/1/10.
// * @time: 15:19.
// */
//@Service
//@EnableScheduling
//public class OrderTask {
//    private static final Logger log = LogManager.getLogger();
//
//    @Autowired
//    UserTradeOrderService userTradeOrderService;
//    @Autowired
//    UserTradeOrderMapper userTradeOrderMapper;
//    @Autowired
//    UserTradePaymentRecordMapper userTradePaymentRecordMapper;
//    @Autowired
//    RedisUtil redisUtil;
//    @Autowired
//    RabbitSender rabbitSender;
//
//    @Autowired
//    private RepayErrorRecordService repayErrorRecordService;
//
//    /**
//     * 小额免密订单状态查询、并更新订单状态
//     *
//     * @param
//     * @return void
//     * @author uncle.quentin
//     * @date 2019/1/10 15:36
//     * @version 1.0
//     */
//    @Scheduled(cron = "0/30 * * * * ? ")
//    public void freepassPayOrderQuery() {
//        log.info("freepassPayOrderQuery任务开始→→→→→→→→→→→→→→→→→");
//        //处理订单
//        userTradeOrderService.dealPayment();
//        log.info("freepassPayOrderQuery任务结束→→→→→→→→→→→→→→→→→");
//    }
//
//    /**
//     * 异常还款订单
//     *
//     * @author uncle.quentin
//     * @date   2019/2/22 16:24
//     * @param
//     * @return void
//     * @version 1.0
//     */
//    @Scheduled(cron = "0/30 * * * * ? ")
//    public void repayOrderQuery(){
//        log.info("repayOrderQuery任务开始→→→→→→→→→→→→→→→→→");
//        //处理订单
//        List<Integer> status = new ArrayList<>();
//        status.add(PaymentStatusEnum.WAITING_PAY.getKey());
//        status.add(PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey());
//        //查询未处理的异常还款数据
//        List<RepayErrorRecord> repayErrorRecords = repayErrorRecordService.selectRepayErrorByStatus(status);
//        if (null != repayErrorRecords && !repayErrorRecords.isEmpty()) {
//            //单条发送队列
//            for (RepayErrorRecord record : repayErrorRecords) {
//                //转换为可消费对象
//                UserTradePaymentRecordVO userTradePaymentRecordVO = new UserTradePaymentRecordVO();
//                BeanUtils.copyProperties(record, userTradePaymentRecordVO);
//                rabbitSender.sendRepayMessage(userTradePaymentRecordVO);
//            }
//        }
//        log.info("repayOrderQuery任务结束→→→→→→→→→→→→→→→→→");
//    }
//
//
//    /**
//     * 快捷支付支付状态查询
//     * 每20秒查询一次
//     */
//    @Scheduled(cron = "0/20 * * * * ? ")
//    public void quickPayOrderQuery(){
//        log.info("→→→→→→→→→quickPayOrderQuery任务开始→→→→→→→→→");
//        //查询进行中的扣款订单
//        List<UserTradeOrder> orders = userTradeOrderMapper.findByTypeAndStatus(PaymentChannelTypeEnum.SHOUKUAN.getKey(), TradeOrderStatusEnum.ING.getKey());
//        for(UserTradeOrder order : orders){
//            //查询订单支付详情
//            List<UserTradePaymentRecord> recordList = userTradePaymentRecordMapper.findByTradeIdOrderByPaymentTimeAsc(order.getId());
//            for(UserTradePaymentRecord record : recordList){
//                if (record.getStatus() == PaymentStatusEnum.CANCEL.getKey()){
//                    UserTradeOrder uptOrder = new UserTradeOrder();
//                    uptOrder.setId(order.getId());
//                    uptOrder.setStatus(TradeOrderStatusEnum.CANCEL.getKey());
//                    userTradeOrderMapper.updateByPrimaryKeySelective(uptOrder);
//                    break;
//                }else if (record.getStatus() == PaymentStatusEnum.FAIL.getKey()){
//                    UserTradeOrder uptOrder = new UserTradeOrder();
//                    uptOrder.setId(order.getId());
//                    uptOrder.setStatus(TradeOrderStatusEnum.FAIL_SOME.getKey());
//                    userTradeOrderMapper.updateByPrimaryKeySelective(uptOrder);
//                    break;
//                }else if (record.getStatus() == PaymentStatusEnum.WAITING_PAY.getKey()){
////                    UserTradeOrder uptOrder = new UserTradeOrder();
////                    uptOrder.setId(order.getId());
////                    uptOrder.setStatus(TradeOrderStatusEnum.WAITING_CONFIRM.getKey());
////                    userTradeOrderMapper.updateByPrimaryKeySelective(uptOrder);
//                    break;
//                }else if (record.getStatus() == PaymentStatusEnum.PAY_SUCCESS.getKey()){
//                    continue;
//                }else if(record.getStatus() == PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey()){
//                    //只处理支付待确认订单
//                    rabbitSender.sendMessage(record);
//                    break;
//                }
//            }
//        }
//        log.info("→→→→→→→→→quickPayOrderQuery任务结束,进行中订单:{}→→→→→→→→→", orders.size());
//    }
//
//}
