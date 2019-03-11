package com.slljr.finance.front.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.slljr.finance.common.pojo.model.UserQuickConsume;

public interface UserQuickConsumeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserQuickConsume record);

    int insertSelective(UserQuickConsume record);

    UserQuickConsume selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserQuickConsume record);

    int updateByPrimaryKey(UserQuickConsume record);
    /**
     * 查询用户当前银行卡当月已收款金额
     * @param uid
     * @return
     */
    @Select({"select sum(get_amount) as getAmount from user_quick_consume where uid = #{uid} and  DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )"})
	Double findByGetAmount(@Param("uid")int uid);

    /**
     * 查询用户当前银行卡累计收款金额
     * @param uid
     * @return
     */    
    @Select({"select sum(get_amount) as getAmount from user_quick_consume where uid = #{uid}"})
	Double findBygetAmountCount(@Param("uid")int uid);
}