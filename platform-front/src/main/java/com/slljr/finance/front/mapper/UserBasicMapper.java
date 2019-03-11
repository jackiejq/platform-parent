package com.slljr.finance.front.mapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.pojo.vo.UserFriendsVO;
import com.slljr.finance.common.pojo.vo.UserReportTotalVO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.slljr.finance.common.pojo.model.UserBasic;

public interface UserBasicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBasic record);

    int insertSelective(UserBasic record);

    UserBasicVO selectByPrimaryKey(Integer id);

    UserBasic selectByPhone(@Param("phone") String phone);

    UserBasic selectByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);

    int updateByPrimaryKeySelective(UserBasic record);

    int updateByPrimaryKey(UserBasic record);

    //查询身份证是否已经存在
    @Select({"select count(*) from user_basic where id_card = #{idCard}"})
	Integer selectByIdCard(String idCard);

    /**
     * 条件查询
     *
     * @author uncle.quentin
     * @date   2018/12/12 16:47
     * @param   userBasic
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserBasic>
     * @version 1.0
     */
   List<UserBasicVO> findByAll(UserBasic userBasic);


    /**
     *
     * 根据ID查询用户基础信息（部分字段）
     * @author uncle.quentin
     * @date   2018/12/20 17:05
     * @param   uid
     * @return com.slljr.finance.common.pojo.vo.UserBasicVO
     * @version 1.0
     */
    UserBasicVO selectPortionById(Integer uid);

    /**
     * 查询用户邀请总人数
     * @param id
     * @return
     */
	Integer totalNumber(Integer id);

	/**
	 * 查询用户好友的手机号/交易时间/交易金额/佣金
	 * @param userFriendsVO
	 * @return
	 */
	List<UserFriendsVO> getUserTaskAndTrad(UserFriendsVO userFriendsVO);

	/**
	    * 通过手机号码查询好友详情
	 * @param id
	 * @param phone
	 * @return
	 */
	List<UserFriendsVO> queryFriendsByPhone(@Param("id")Integer id,@Param("phone") String phone);

	/**
	 * 好友明细
	 * @param userFriends
	 * @return
	 */
	List<UserFriendsVO> queryFriendsDetail(UserFriendsVO userFriends);

	/**
	 * 查询好友电话,注册日期
	 * @param id
	 * @param phone
	 * @return
	 */
	UserBasic  queryInfo(@Param("id") Integer id, @Param("phone") String phone);

	/**
	 * 查询佣金详情
	 * @param userFriends
	 * @return
	 */
	List<UserFriendsVO> queryCommissionDetail(UserFriendsVO userFriends);

    /**
     * 查询代理用户邀请好友总佣金、总人数
     *
     * @param userFriends
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author uncle.quentin
     * @date 2019/1/24 16:53
     * @version 1.0
     */
    Map<String, Object> querySumCommission(UserFriendsVO userFriends);

	/**
	 * 查询佣金详情总人数
	 * @param userFriends
	 * @return
	 */
	Integer selectTotalNumber(UserFriendsVO userFriends);

	/**
	 * 查询本月邀请好友数量
	 * @param id
	 * @return
	 */
	Integer monthTotal(Integer id);

	/**
	 * 查询好友注册/佣金信息
	 * @param userFriendsVO
	 * @return
	 */
	List<UserFriendsVO> queryFriendsInfo(UserFriendsVO userFriendsVO);
//
//	/**
//	   *代理用户或系统普通用户查询好友电话,创建日期
//	 * @param userFriendsVO
//	 * @return
//	 */
//	List<UserFriendsVO> queryFriendsInfoByType(UserFriendsVO userFriendsVO);

	/**
	 * 查询邀请好友统计报表
	 * @param uid
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	UserReportTotalVO selectCommissionReport(@Param("uid")Integer uid, @Param("startTime")Date startTime, @Param("endTime")Date endTime);


}