package com.slljr.finance.front.mapper;
import java.util.Date;

import com.slljr.finance.common.pojo.model.UserTradePaymentRecord;
import com.slljr.finance.common.pojo.vo.CompensatoryVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserTradePaymentRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserTradePaymentRecord record);

    int insertSelective(UserTradePaymentRecord record);

    int insertList(@Param("list")List<UserTradePaymentRecord> list);

    UserTradePaymentRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTradePaymentRecord record);

	/**
	 * 根据订单ID更新明细信息
	 *
	 * @author uncle.quentin
	 * @date   2019/1/3 15:40
	 * @param   record
	 * @return int
	 * @version 1.0
	 */
    int updateByTradeId(UserTradePaymentRecord record);

    int updateByPrimaryKey(UserTradePaymentRecord record);

    int updateStatusByTradeId(@Param("updatedStatus")Integer updatedStatus,@Param("tradeId")Integer tradeId);

	int updateStatusByTradeIdAndType(@Param("updatedStatus")Integer updatedStatus,@Param("tradeId")Integer tradeId,@Param("type")Integer type);

	int updateStatusByTradeIdAndUidAndStatus(@Param("updatedStatus")Integer updatedStatus,@Param("tradeId")Integer tradeId,@Param("uid")Integer uid,@Param("status")Integer status);




    /**
     * 查询用户当前银行卡当月已收款金额
     * @param uid
     * @return
     */

    @Select({"SELECT IFNULL(ROUND(sum(payment_amount), 2), 0) AS amount FROM user_trade_order WHERE uid =  #{uid} AND DATE_FORMAT(create_time, '%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') AND status = 2 AND type = 2"})
	Double findByGetAmount(@Param("uid") int uid);

    /**
     * 查询用户当前银行卡累计收款金额
     * @param uid
     * @return
     */

    @Select("SELECT IFNULL(ROUND(sum(payment_amount), 2), 0) AS amount FROM user_trade_order WHERE uid =  #{uid} AND status = 2 AND type = 2")
	Double findBygetAmountCount(@Param("uid")int uid);

    //查询用户本月剩余代偿
	Double findSurplusAmount(@Param("uid")int uid);

    /**
     * 查询用户累计代偿金额
     *
     * @author uncle.quentin
     * @date   2018/12/25 17:12
     * @param   uid
     * @return java.lang.Double
     * @version 1.0
     */
	Double findTotaltCompensationAmount(@Param("uid") int uid);

	/**
	 * 查询代偿列表
	 * @param id
	 * @return
	 */
	List<UserTradePaymentRecord> queryCompensatoryList(Integer tradeId);

	/**
	 * 统计代偿明细
	 * @param id
	 * @return
	 */
	CompensatoryVO totalCompensatory(Integer tradeId);

	List<UserTradePaymentRecord> findByTradeIdAndType(@Param("tradeId")Integer tradeId,@Param("type")Integer type);

	List<UserTradePaymentRecord> findByTradeIdAndStatusOrderByIdAsc(@Param("tradeId")Integer tradeId,@Param("status")Integer status);

	List<UserTradePaymentRecord> findByTradeIdOrderByPaymentTimeAsc(@Param("tradeId")Integer tradeId);

	List<UserTradePaymentRecord> findByTradeIdOrderByIdAsc(@Param("tradeId")Integer tradeId);







	Integer queryTotalNum(Integer tradeId);

	/**
	 * 根据orderId查询明细
	 *
	 * @author uncle.quentin
	 * @date   2019/1/10 14:14
	 * @param   orderId
	 * @return java.util.List<com.slljr.finance.common.pojo.model.UserTradePaymentRecord>
	 * @version 1.0
	 */
	UserTradePaymentRecord findByOrderId(@Param("orderId")String orderId);

	/**
	 * 根据订单状态以及支付时间小于当前时间的订单明细
	 *
	 * @param status
	 * @return java.util.List<com.slljr.finance.common.pojo.model.UserTradePaymentRecord>
	 * @author uncle.quentin
	 * @date 2019/1/10 15:50
	 * @version 1.0
	 */
	List<UserTradePaymentRecord> findbyStatusAndType(@Param("status") Integer status, @Param("type") Integer type);

	/**
	 * 更新交易未成功条数
	 *
	 * @author uncle.quentin
	 * @date   2019/1/10 17:34
	 * @param   tradeId
	 * @return java.lang.Integer
	 * @version 1.0
	 */
	Integer findNotSuccessTradePaymentCount(@Param("tradeId")Integer tradeId);

	/**
	 * 根据交易ID查询可以重新设置代偿数据
	 *
	 * @author uncle.quentin
	 * @date   2019/2/21 13:50
	 * @param   tradeId
	 * @return java.util.List<com.slljr.finance.common.pojo.model.UserTradePaymentRecord>
	 * @version 1.0
	 */
	List<UserTradePaymentRecord> selectCanResetData(@Param("tradeId")Integer tradeId);

}