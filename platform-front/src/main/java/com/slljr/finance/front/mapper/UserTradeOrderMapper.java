package com.slljr.finance.front.mapper;
import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.vo.UserTradeOrderVO;

public interface UserTradeOrderMapper {
		int deleteByPrimaryKey(Integer id);

	    int insert(UserTradeOrder record);

	    int insertSelective(UserTradeOrder record);

	    UserTradeOrder selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(UserTradeOrder record);

	    int updateByPrimaryKey(UserTradeOrder record);

	    int updateStatusByIdAndUidAndTypeAndStatus(@Param("updatedStatus")Integer updatedStatus,@Param("id")Integer id,@Param("uid")Integer uid,@Param("type")Integer type,@Param("status")Integer status);


	    /**
	     * 条件查询
	     *
	     * @author uncle.quentin
	     * @date   2018/12/12 17:27
	     * @param   userTradeOrder
	     * @return java.util.List<com.slljr.finance.common.pojo.model.UserTradeOrder>
	     * @version 1.0
	     */
	    List<UserTradeOrderVO> findByAll(UserTradeOrderDTO userTradeOrder);

	    List<UserTradeOrder> findByTypeAndStatus(@Param("type")Integer type,@Param("status")Integer status);



		//查询用户历史累计代偿金额
		Double findHistoricalAmount(int uid);

		//查询用户本月代偿比数
		Integer findCompensatoryRatiot(int uid);

		//查询用户本月代偿总额
		Double findCompensatoryCount(int uid);

		Integer countFailOrderByUidAndPaymentCardId(@Param("uid")Integer uid,@Param("paymentCardId")Integer paymentCardId);



}