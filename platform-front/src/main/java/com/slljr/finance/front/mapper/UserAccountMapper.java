package com.slljr.finance.front.mapper;
import java.math.BigDecimal;

import com.slljr.finance.common.pojo.vo.InviteFriendsVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.model.UserBalanceDetail;
import com.slljr.finance.common.pojo.vo.UserTaskAccountVO;

public interface UserAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    UserAccount selectByPrimaryKey(Integer id);

    UserAccount findByUid(@Param("uid")Integer uid);

    int updateByPrimaryKeySelective(UserAccount record);

    int updateByPrimaryKey(UserAccount record);


    /**
     * 任务中心用户表头(积分和金币查询)
     * @param id
     * @return
     */
	UserTaskAccountVO getUserTask(Integer id);

	/**
	 * 查询用户本月佣金数量
	 * @param id
	 * @return
	 */
	UserBalanceDetail usercCommission(Integer id);

	/**
	 * 查询用户累计佣金
	 * @param id
	 * @return
	 */
	UserBalanceDetail usercCommissionCount(Integer id);


    /**
     * 根据用户ID修改
     *
     * @author uncle.quentin
     * @date   2019/1/6 15:41
     * @param   record
     * @return int
     * @version 1.0
     */
    int updateByUid(UserAccount record);

	/**
	 * 获取用户邀请好友信息
	 *
	 * @author uncle.quentin
	 * @date   2019/1/14 11:54
	 * @param   uid
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @version 1.0
	 */
	InviteFriendsVO findSumCashBalanceByUid(@Param("uid") Integer uid);

}