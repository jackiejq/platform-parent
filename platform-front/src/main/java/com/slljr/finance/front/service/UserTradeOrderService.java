package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.enums.PaymentStatusEnum;
import com.slljr.finance.common.enums.PaymentTypeEnum;
import com.slljr.finance.common.enums.TradeOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import com.slljr.finance.common.pojo.model.*;
import com.slljr.finance.common.pojo.vo.CompensatoryVO;
import com.slljr.finance.common.pojo.vo.UserTradeOrderVO;
import com.slljr.finance.common.utils.DateUtil;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.front.mapper.PaymentChannelMapper;
import com.slljr.finance.front.mapper.UserTradeOrderMapper;
import com.slljr.finance.front.mapper.UserTradePaymentRecordMapper;
import com.slljr.finance.payment.utils.OrderNumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @description: 用户交易订单服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 17:03.
 */
@Service
public class UserTradeOrderService {

	private static final Logger log = LogManager.getLogger();

    @Autowired
    private UserTradeOrderMapper userTradeOrderMapper;

    @Autowired
    private UserTradePaymentRecordMapper userTradePaymentRecordMapper;

    @Resource
    private PaymentChannelMapper paymentChannelMapper;

    @Autowired
    private RepayErrorRecordService repayErrorRecordService;

	@Autowired
	RabbitSender rabbitSender;

	@Autowired
    OrderNumUtils orderNumUtils;

    /**
     * 根据用户和交易类型查询订单
     *
     * @param userTradeOrder 交易信息
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserTradeOrder>
     * @author uncle.quentin
     * @date 2018/12/12 17:26
     * @version 1.0
     */
    public List<UserTradeOrderVO> selectByUserIdAndType(UserTradeOrderDTO userTradeOrder) {
		return userTradeOrderMapper.findByAll(userTradeOrder);
    }


    /**
              * 查询用户本月剩余代偿金额
     * @param uid
     * @return
     */
	public Double findSurplusAmount(int uid) {			
		return userTradePaymentRecordMapper.findSurplusAmount(uid);
	}

    /**
     * 查询用户累计代偿金额
     *
     * @author uncle.quentin
     * @date   2018/12/25 17:13
     * @param   uid
     * @return java.lang.Double
     * @version 1.0
     */
	public Double findTotalCompensationAmount(int uid){
        return userTradePaymentRecordMapper.findTotaltCompensationAmount(uid);
    }

	/**
	   * 查询用户本月代偿笔数
	 * @param uid
	 * @return
	 */
	public Integer findCompensatoryRatiot(int uid) {		
		return userTradeOrderMapper.findCompensatoryRatiot(uid);
	}

	/**
	 * 查询用户本月代偿总额
	 * @param uid
	 * @return
	 */
	public Double findCompensatoryCount(int uid) {
		return userTradeOrderMapper.findCompensatoryCount(uid);
	}

	/**
	 * 查询代偿明细
	 * @param tradeId
	 */
	public List<Map<String, Object>> queryCompensatoryList(Integer tradeId, CompensatoryVO compensatoryVO) {
		//查询代偿列表
		List<UserTradePaymentRecord> userTradePaymentRecord = userTradePaymentRecordMapper.queryCompensatoryList(tradeId);
		List<UserTradePaymentRecord> tempList = new ArrayList<>();	
		List<Map<String, Object>> resList = new ArrayList<>();
		//Double num =0.00;
		double num = 0.00;
		//-1失败,0进行中,1已完成
		Integer flag = 2;

		for (UserTradePaymentRecord record : userTradePaymentRecord) {
			tempList.add(record);

			//计算每期总手续费
			double sDouble = record.getServiceCharge();
			num = MathUtils.add(sDouble, num,2);	

			if (record.getStatus() < flag){
			    flag = record.getStatus();
            }

			if(record.getType()==2) {
                if(flag == -1) {
                    flag = -1;
                }else if(flag == -2){
                    flag = -1;
                }else if(flag == 0 || flag == 1){
                    flag = 0;
                }else if (flag == 2){
                    flag = 1;
                }

				Map<String, Object> rowMap = new HashMap<>();
				rowMap.put("totalAmount", record.getAmount());
				rowMap.put("totalServicecharge", num);
				rowMap.put("status",flag);
				rowMap.put("compensationDate", DateUtil.DateToString(record.getPaymentTime(), "yyyy-MM-dd"));
				rowMap.put("list", tempList);
				resList.add(rowMap);
				num =0.00;
                flag = 2;
				tempList = new ArrayList<>();
			}
		}
		return resList;
				
	}

	/*
	 * 统计代偿明细
	 */
	public CompensatoryVO totalCompensatory(Integer tradeId) {
		return userTradePaymentRecordMapper.totalCompensatory(tradeId);
	}
	
	public Integer queryTotalNum(Integer tradeId) {
		return userTradePaymentRecordMapper.queryTotalNum(tradeId);
	}

	/**
	 * 根据交易ID查询交易订单信息
	 *
	 * @param tradeId
	 * @return com.slljr.finance.common.pojo.model.UserTradeOrder
	 * @author uncle.quentin
	 * @date 2019/1/6 15:54
	 * @version 1.0
	 */
	public UserTradeOrder selectUserTradeOrderById(Integer tradeId) {
		return userTradeOrderMapper.selectByPrimaryKey(tradeId);
	}

	/**
	 * 根据订单ID查询明细
	 *
	 * @param orderId 订单ID
	 * @return com.slljr.finance.common.pojo.model.UserTradePaymentRecord
	 * @author uncle.quentin
	 * @date 2019/1/10 14:16
	 * @version 1.0
	 */
	public UserTradePaymentRecord selectUserTradePaymentByOrderId(String orderId) {
		return userTradePaymentRecordMapper.findByOrderId(orderId);
	}

	/**
	 * 更新订单明细状态
	 *
	 * @param dataId 数据ID
	 * @param status 状态
	 * @return int
	 * @author uncle.quentin
	 * @date 2019/1/10 16:54
	 * @version 1.0
	 */
	public int updateTradePaymentStatus(int dataId, PaymentStatusEnum status) {
		return updateTradePaymentStatus(dataId, status, null);
	}
	public int updateTradePaymentStatus(int dataId, PaymentStatusEnum status, String errMsg) {
		UserTradePaymentRecord uptRecord = new UserTradePaymentRecord();
		uptRecord.setId(dataId);
		uptRecord.setStatus(status.getKey());
		if(StringUtils.isNotBlank(errMsg)) uptRecord.setErrorMsg(errMsg);
		return userTradePaymentRecordMapper.updateByPrimaryKeySelective(uptRecord);
	}

	/**
	 * 更新用户代偿/收款订单状态
	 *
	 * @param dataId 数据ID
	 * @param status 状态
	 * @return int
	 * @author uncle.quentin
	 * @date 2019/1/10 16:55
	 * @version 1.0
	 */
	public int updateOrderStatus(int dataId, TradeOrderStatusEnum status) {
		UserTradeOrder uptOrder = new UserTradeOrder();
		uptOrder.setId(dataId);
		uptOrder.setStatus(status.getKey());
		return userTradeOrderMapper.updateByPrimaryKeySelective(uptOrder);
	}

	/**
	 * 处理订单明细
	 *
	 * @author uncle.quentin
	 * @date   2019/1/10 21:00
	 * @param
	 * @return void
	 * @version 1.0
	 */
	public void dealPayment(){
		//获取代还进行中订单
		List<UserTradeOrder> orders = userTradeOrderMapper.findByTypeAndStatus(PaymentChannelTypeEnum.DAIHUAN.getKey(), TradeOrderStatusEnum.ING.getKey());
		for (UserTradeOrder order : orders) {
			//获取明细
			List<UserTradePaymentRecord> userTradePaymentRecords = userTradePaymentRecordMapper.findByTradeIdOrderByPaymentTimeAsc(order.getId());
			for (int i = 0; i < userTradePaymentRecords.size(); i++) {
				UserTradePaymentRecord userTradePaymentRecord = userTradePaymentRecords.get(i);
				//时间到了,才可执行
                if (userTradePaymentRecord.getPaymentTime().after(new Date())) break;

				//存在单笔明细订单失败，更新整笔交易失败
				if (PaymentStatusEnum.CANCEL.getKey() == userTradePaymentRecord.getStatus()) {
					updateOrderStatus(userTradePaymentRecord.getTradeId(), TradeOrderStatusEnum.CANCEL);
					break;
				} else if (PaymentStatusEnum.FAIL.getKey() == userTradePaymentRecord.getStatus()) {
					updateOrderStatus(userTradePaymentRecord.getTradeId(), TradeOrderStatusEnum.FAIL_SOME);
					break;
				} else if (PaymentStatusEnum.WAITING_PAY.getKey() == userTradePaymentRecord.getStatus()) {
					//待支付订单执行支付→发送消息到支付队列
                    rabbitSender.sendMessage(userTradePaymentRecord);
                    break;
				} else if (PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey() == userTradePaymentRecord.getStatus()) {
					rabbitSender.sendMessage(userTradePaymentRecord);
					break;
				}
			}
		}

	}

    /**
     * 终止代偿订单
     * @param user
     * @param tradeId
     * @return
     */
	public boolean stopOrder(UserBasic user, int tradeId) throws InterfaceException{
		UserTradeOrder order = userTradeOrderMapper.selectByPrimaryKey(tradeId);
		if (order.getStatus() != TradeOrderStatusEnum.ING.getKey()){
			log.info("用户:{},终止代偿订单:{}失败,订单非进行中状态!", user.getPhone(), tradeId);
			throw new InterfaceException("终止代偿失败,订单非进行中状态!");
		}


        /**1.条件检查**/
        List<UserTradePaymentRecord> recordList = userTradePaymentRecordMapper.findByTradeIdOrderByIdAsc(tradeId);
        //第一条待执行的记录下标
        int firstNoStartRecordIndex = -1;
        //能否终止代偿
        boolean canStop = true;
		{
			for (int index = 0;index < recordList.size(); index ++) {
				UserTradePaymentRecord record = recordList.get(index);
				//有支付待确认的记录不能终止
				if (record.getStatus() == PaymentStatusEnum.PAY_WAITING_CONFIRM.getKey()){
					canStop = false;
					break;
				}
				if (record.getStatus() == PaymentStatusEnum.WAITING_PAY.getKey() && firstNoStartRecordIndex == -1){
					firstNoStartRecordIndex = index;
					break;
				}
			}

			if (!canStop){
				log.info("用户:{},终止代偿订单:{}失败,有支付待确认记录,请稍后再试!", user.getPhone(), tradeId);
				throw new InterfaceException("终止代偿失败,有订单正在支付,请稍后再试!");
			}
			if (firstNoStartRecordIndex == -1){
				log.info("用户:{},终止代偿订单:{}失败,订单已完成!", user.getPhone(), tradeId);
				throw new InterfaceException("终止代偿失败,订单已完成!");
			}
			if (firstNoStartRecordIndex == recordList.size() - 1){
				log.info("用户:{},终止代偿订单:{}失败,只剩最后一期还款,请耐心等待!", user.getPhone(), tradeId);
				throw new InterfaceException("终止代偿失败,最后一笔还款即将进行,请耐心等待!");
			}
		}


        /**2.更新订单状态**/
		{
			UserTradePaymentRecord uptRecord = new UserTradePaymentRecord();
			uptRecord.setId(recordList.get(firstNoStartRecordIndex).getId());
			uptRecord.setStatus(PaymentStatusEnum.CANCEL.getKey());
			uptRecord.setUpdateTime(new Date());
			//1.更新支付记录状态
			userTradePaymentRecordMapper.updateByPrimaryKeySelective(uptRecord);
			//2.终止执行订单
			userTradeOrderMapper.updateStatusByIdAndUidAndTypeAndStatus(TradeOrderStatusEnum.CANCEL.getKey(),
					tradeId, user.getId(), PaymentChannelTypeEnum.DAIHUAN.getKey(), TradeOrderStatusEnum.ING.getKey());
		}


        /**3.生成异常还款记录**/
        createRepayErrorRecord(tradeId);

		log.info("用户:{},终止代偿订单:{}成功", user.getPhone(), tradeId);
		return true;
	}

	public void createRepayErrorRecord(int tradeId){
        /**
         * 调用该方法场景:
         * 1.手动终止代偿
         * 2.支付失败
         */
		UserTradeOrder order = userTradeOrderMapper.selectByPrimaryKey(tradeId);
		if (order.getStatus() != TradeOrderStatusEnum.FAIL_SOME.getKey() && order.getStatus() != TradeOrderStatusEnum.CANCEL.getKey()){
		    log.warn("创建异常提现记录失败,订单状态非部分失败或取消!tradeId:{},status:{}", tradeId, order.getStatus());
		    return;
        }

        PaymentChannel channel = paymentChannelMapper.selectByPrimaryKey(order.getChannelId());
        List<UserTradePaymentRecord> recordList = userTradePaymentRecordMapper.findByTradeIdOrderByIdAsc(tradeId);

        //找到最后一个失败的记录
        int lastErrorPayIndex = -1;
        for (int index = 0; index < recordList.size(); index ++){
            UserTradePaymentRecord record = recordList.get(index);
            if (record.getStatus() == PaymentStatusEnum.FAIL.getKey() || record.getStatus() == PaymentStatusEnum.CANCEL.getKey()){
                lastErrorPayIndex = index;
                break;
            }
        }

        if (lastErrorPayIndex == -1 || lastErrorPayIndex == 0){
            log.info("用户:{},创建异常提现订单:{}失败,没有支付失败记录!", order.getUid(), tradeId);
            return;
        }

        if (recordList.get(lastErrorPayIndex-1).getType() == PaymentTypeEnum.REPAYMENT.getKey()){
            log.info("用户:{},创建异常提现订单:{}失败,上一条是提现记录,故无已支付未提现记录!", order.getUid(), tradeId);
            return;
        }


        double waitRepaymentAmount = 0;
        double serviceCharge = 0;
        for (int index = lastErrorPayIndex - 1; index >= 0; index --){
            UserTradePaymentRecord record = recordList.get(index);
            if (record.getType() == PaymentTypeEnum.REPAYMENT.getKey()) break;
            //累计支付成功金额
            waitRepaymentAmount = MathUtils.round(waitRepaymentAmount + record.getAmount(), BigDecimal.ROUND_DOWN, 2);
            //累计刷卡手续费
            serviceCharge = MathUtils.round(serviceCharge + record.getAmount() * channel.getUserPaymentRate(), BigDecimal.ROUND_UP, 2);
        }


        if (waitRepaymentAmount > 0){
            //加上提现手续费
            serviceCharge = MathUtils.round(serviceCharge + channel.getUserWithdrawCharge(), BigDecimal.ROUND_UP, 2);
            //实际提现金额
            waitRepaymentAmount = MathUtils.round(waitRepaymentAmount - serviceCharge, BigDecimal.ROUND_DOWN, 1);

            Date currDate = new Date();
            UserTradePaymentRecord lastErrorRecord = recordList.get(lastErrorPayIndex);
            RepayErrorRecord errorRepay = new RepayErrorRecord();
            errorRepay.setUid(lastErrorRecord.getUid());
            errorRepay.setTradeId(lastErrorRecord.getTradeId());
            errorRepay.setOrderId(orderNumUtils.generate());
            errorRepay.setType(PaymentTypeEnum.REPAYMENT.getKey());
            errorRepay.setAmount(waitRepaymentAmount);
            errorRepay.setCardId(lastErrorRecord.getCardId());
            errorRepay.setServiceCharge(channel.getUserWithdrawCharge());
            errorRepay.setErrorMsg("总手续费:" + serviceCharge);
            errorRepay.setChannelId(lastErrorRecord.getChannelId());
            errorRepay.setPaymentTime(currDate);
            errorRepay.setStatus(PaymentStatusEnum.WAITING_PAY.getKey());
            errorRepay.setRetryCount(0);
            errorRepay.setRefRecordId(lastErrorRecord.getId());
            errorRepay.setCreateTime(currDate);
            errorRepay.setUpdateTime(currDate);

            repayErrorRecordService.addRepayErrorRecord(errorRepay);
			log.info("用户:{},创建异常提现订单:{}成功,提现金额:{}", order.getUid(), tradeId, waitRepaymentAmount);
        }
	}

	/**
	 * 剩余代偿金额
	 *
	 * @author uncle.quentin
	 * @date   2019/2/21 17:44
	 * @param   tradeId
	 * @return java.lang.Double
	 * @version 1.0
	 */
	public Double getSurplusOrderAmount(Integer tradeId) throws InterfaceException {
		//获取是否可以重新设置代偿数据
 		List<UserTradePaymentRecord> userTradePaymentRecords = userTradePaymentRecordMapper.selectCanResetData(tradeId);

		if (null != userTradePaymentRecords && !userTradePaymentRecords.isEmpty()) {
			//已还金额 = sum(单笔支付金额-支付手续费)
			Double successAmount = 0.0;

			Double withdrawServiceCharge = 0.0;

			//是否继续计算成功扣款金额
			boolean flag = true;
			//是否当期第一笔扣款失败
			boolean failIndexFlag = false;

			for (int i = 0; i < userTradePaymentRecords.size(); i++) {
				UserTradePaymentRecord userTradePaymentRecord = userTradePaymentRecords.get(i);
				if (flag) {
					//成功扣除金额
					if (userTradePaymentRecord.getStatus() == 2 && userTradePaymentRecord.getType() == 1) {
						Double tempAmount = MathUtils.sub(userTradePaymentRecord.getAmount(), userTradePaymentRecord.getServiceCharge(), 2);
						successAmount = MathUtils.add(successAmount, tempAmount, 2);
					}

					//遇到失败，flag == false，则碰到还款，则拿到手续费并退出循环
					if (userTradePaymentRecord.getStatus() == -1 || userTradePaymentRecord.getStatus() == -2) {
						flag = false;
						//非每期还款第一笔扣款失败，累计还款手续费
						if (i > 0 && userTradePaymentRecords.get(i - 1).getType() != 2) {
							failIndexFlag = true;
						}
					}
				}

				if (userTradePaymentRecord.getType() == 2) {
					//失败数据是第一笔扣款，则不用减掉当期还款手续费
					if (failIndexFlag) {
						withdrawServiceCharge = MathUtils.add(withdrawServiceCharge, userTradePaymentRecord.getServiceCharge(), 2);
					}
					if (!flag) {
						//大于0表示前面有成功的支付记录,遇到flag==false表示上面出现失败数据，type==2还款计算最终扣款成功金额，跳出循环
						if (successAmount > 0) {
							successAmount = MathUtils.sub(successAmount, withdrawServiceCharge, 2);
						}
						break;
					}
				}

			}

			//计算剩余代偿
			return calcSurplus(userTradePaymentRecords, successAmount);
		} else {
			throw new InterfaceException(MsgEnum.RENEW_SET_COMPENSATION);
		}

	}

	/**
	 * 计算剩余代偿
	 * 剩余代偿 = 【订单所有还款总额】 - 【已支付扣款总额】 - 【失败当期还款手续费】
	 *
	 * @param userTradePaymentRecords 订单所有记录
	 * @param successAmount           已成功支付金额
	 * @return java.lang.Double
	 * @author uncle.quentin
	 * @date 2019/2/21 16:10
	 * @version 1.0
	 */
	private Double calcSurplus(List<UserTradePaymentRecord> userTradePaymentRecords, Double successAmount) {
		//订单所有还款金额
		Double totalReturnAmount = 0.0;
		for (UserTradePaymentRecord userTradePaymentRecord : userTradePaymentRecords) {
			if (userTradePaymentRecord.getType() == 2) {
				totalReturnAmount = MathUtils.add(totalReturnAmount, userTradePaymentRecord.getAmount(), 2);
			}
		}

		//剩余代偿金额
		return MathUtils.round(MathUtils.sub(totalReturnAmount, successAmount, 2), BigDecimal.ROUND_UP, 0);
	}

}
