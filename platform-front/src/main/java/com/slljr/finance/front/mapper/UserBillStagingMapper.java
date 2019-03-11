package com.slljr.finance.front.mapper;

import org.apache.ibatis.annotations.Select;

import com.slljr.finance.common.pojo.model.BillStaging;

/**
 * 账单分期记录
 * @author 27824
 *
 */
public interface UserBillStagingMapper {

	int deleteByPrimaryKey(Integer id);

    int insert(BillStaging record);

    int insertSelective(BillStaging record);

    BillStaging selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BillStaging record);

    int updateByPrimaryKey(BillStaging record);
	//当前信用卡剩余代偿金额
	@Select({"SELECT sum(ifnull(`num_total` ,0)-ifnull(`amount_pay` ,0)) AS result FROM user_bill_staging where uid = #{uid} and  DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )"})
	Double selectByResidualCompensation(int uid);

	//当前信用卡累计代偿金额
	@Select({"SELECT sum(amount_pay) AS accumulatedCompensation FROM user_bill_staging where uid = #{uid}"})	
	Double selectByAccumulatedCompensation(int uid);

}
