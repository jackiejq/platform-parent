package com.slljr.finance.front.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.UserBalanceDetail;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.*;
import com.slljr.finance.front.mapper.UserAccountMapper;
import com.slljr.finance.front.mapper.UserBalanceDetailMapper;
import com.slljr.finance.front.mapper.UserBasicMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: 用户基础服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 15:04.
 */
@Service
public class UserBasicService {

	@Autowired
	private UserBasicMapper userBasicMapper;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private UserBalanceDetailMapper userBalanceDetail;

	private static final Logger log = LogManager.getLogger();
	/**
	 * 根据推荐人用户id查询用户
	 *
	 * @param refererUid
	 * @return java.util.List<com.slljr.finance.common.pojo.model.UserBasic>
	 * @author uncle.quentin
	 * @date 2018/12/12 16:50
	 * @version 1.0
	 */
	public List<UserBasicVO> selectUserBasicByfindByRefererUid(Integer refererUid) {
		UserBasic userBasic = new UserBasic();
		userBasic.setRefererUid(refererUid);
		return userBasicMapper.findByAll(userBasic);
	}

	/**
	 * 获取用户信息
	 *
	 * @param uid
	 * @return com.slljr.finance.common.pojo.vo.UserBasicVO
	 * @author uncle.quentin
	 * @date 2018/12/18 20:50
	 * @version 1.0
	 */
	public UserBasicVO selectUserBasicById(Integer uid) {
		return userBasicMapper.selectByPrimaryKey(uid);
	}

	public UserBasic selectByPhone(String phone){
		return userBasicMapper.selectByPhone(phone);
	}

	/**
	 * 修改用户名称、身份证信息
	 *
	 * @return int
	 * @author uncle.quentin
	 * @date 2018/12/20 15:39
	 * @version 1.0
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateUserBasic(UserBasic userBasic) {
		return userBasicMapper.updateByPrimaryKeySelective(userBasic);
	}

	/**
	 * 根据ID查询用户基础信息（包含部分字段）
	 *
	 * @author uncle.quentin
	 * @date 2018/12/20 17:09
	 * @param uid
	 * @return com.slljr.finance.common.pojo.model.UserBasic
	 * @version 1.0
	 */
	public UserBasicVO selectPortionUserBasicById(Integer uid) {
		return userBasicMapper.selectPortionById(uid);
	}

	/**
	 * 身份验证通过更新用户信息
	 *
	 * @author uncle.quentin
	 * @date 2018/12/20 22:07
	 * @param userBasic
	 * @param resultVo
	 * @return com.slljr.finance.common.pojo.vo.UserBasicVO
	 * @version 1.0
	 */
	public UserBasicVO updateUserAfterValid(UserBasic userBasic, IdCardValidVO resultVo) {
		userBasic.setId(userBasic.getId());
		userBasic.setName(resultVo.getName());
		userBasic.setIdCard(resultVo.getIdCard());
		userBasic.setIdcAddress(resultVo.getAddress());
		if (StringUtils.isNotEmpty(resultVo.getSex()) && IdCardValidVO.FEMALE.equals(resultVo.getSex())) {
			userBasic.setSex(2);
		} else {
			userBasic.setSex(1);
		}

		int successCount = userBasicMapper.updateByPrimaryKeySelective(userBasic);

		if (successCount > 0) {
			// 获取最新的用户信息返回给客户端
			UserBasicVO userBasic1 = userBasicMapper.selectPortionById(userBasic.getId());
			return userBasic1;
		}
		return null;
	}

	/**
	 * 任务中心用户表头(积分和金币查询)
	 * 
	 * @param id
	 * @return
	 */
	public UserTaskAccountVO getUserTask(Integer id) {
		return userAccountMapper.getUserTask(id);
	}

	/**
	 * 查询用户本月佣金数量
	 * 
	 * @param id
	 * @return
	 */
	public UserBalanceDetail usercCommission(Integer id) {
		return userAccountMapper.usercCommission(id);
	}

	/**
	 * 查询用户累计佣金
	 * 
	 * @param id
	 * @return
	 */
	public UserBalanceDetail usercCommissionCount(Integer id) {
		return userAccountMapper.usercCommissionCount(id);
	}

	public Integer totalNumber(Integer id) {
		return userBasicMapper.totalNumber(id);
	}

	/**
	 * 查询用户好友电话,名字,创建日期,增加佣金
	 * @param userFriendsVO
	 * @return
	 */
	public PageInfo<UserFriendsVO> queryFriendsInfo(UserFriendsVO userFriendsVO) {	
        PageInfo<UserFriendsVO> pageInfo = PageHelper.startPage(userFriendsVO.getPageNum(), userFriendsVO.getPageSize())
                .doSelectPageInfo(()->userBasicMapper.queryFriendsInfo(userFriendsVO));
		return pageInfo;
	}

	/**
	 * 查询用户好友的手机号/交易时间/交易金额/佣金
	 * 
	 * @param userFriendsVO
	 * @return
	 */
	public List<UserFriendsVO> getUserTaskAndTrad(UserFriendsVO userFriendsVO) {
		return userBasicMapper.getUserTaskAndTrad(userFriendsVO);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param userBasic
	 * @return
	 */
	public Integer updateUserInfo(UserBasic userBasic) {
		return userBasicMapper.updateByPrimaryKeySelective(userBasic);
	}

	/**
	 * 通过电话号码查询好友
	 * 
	 * @param userFriends
	 * @return
	 */
	public List<UserFriendsVO> queryFriendsByPhone(UserFriendsVO userFriends) {
		List<UserFriendsVO> list = userBasicMapper.queryFriendsByPhone(userFriends.getId(), userFriends.getPhone());		
		if(list.isEmpty() || list == null){
			return null;
		}
		return list;
	}

	/**
	 * 好友明细
	 * 
	 * @param userFriends
	 * @return
	 */
	public List<UserFriendsVO> queryFriendsDetail(UserFriendsVO userFriends) {
		return userBasicMapper.queryFriendsDetail(userFriends);
	}

	public UserBasic queryInfo(Integer id, String phone) {
		return userBasicMapper.queryInfo(id, phone);
	}

	/**
	 * 查询佣金详情
	 * 
	 * @param userFriends
	 * @return
	 */
	public PageInfo<UserFriendsVO> queryCommissionDetail(UserFriendsVO userFriends) {
		PageInfo<UserFriendsVO> userFriendsVOPageInfo = PageHelper.startPage(userFriends.getPageNum(), userFriends.getPageSize()).doSelectPageInfo(() -> userBasicMapper.queryCommissionDetail(userFriends));
		return userFriendsVOPageInfo;
	}

	/**
	 * 获取代理用户邀请好友总佣金、总人数
	 *
	 * @author uncle.quentin
	 * @date   2019/1/24 16:56
	 * @param   userFriends
	 * @return java.util.Map<java.lang.String   ,   java.lang.Object>
	 * @version 1.0
	 */
	public Map<String, Object> querySumCommission(UserFriendsVO userFriends){
		return userBasicMapper.querySumCommission(userFriends);
	}

	/**
	 * 查询佣金详情总人数
	 * 
	 * @param userFriends
	 * @return
	 */
	public Integer selectTotalNumber(UserFriendsVO userFriends) {
		return userBasicMapper.selectTotalNumber(userFriends);
	}

	/**
	 * 查询用户本月邀请好友
	 * 
	 * @param id
	 * @return
	 */
	public Integer monthTotal(Integer id) {
		return userBasicMapper.monthTotal(id);
	}

	/**
	 * 好友佣金报表统计    
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public List<UserReportTotalVO> queryCommissionReport(Integer id) {
		List<UserReportTotalVO> list = new ArrayList<>();
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("今日");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.DAY_OF_MONTH, -1);
			endCld.add(Calendar.DAY_OF_MONTH, -1);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("昨日");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.DAY_OF_MONTH, -7);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("近一周");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.MONTH, -1);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("近一月");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.MONTH, -3);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("近三月");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.MONTH, -6);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("近六月");
			list.add(report);
		}
		{
			Calendar startCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			Calendar endCld = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			startCld.set(Calendar.HOUR_OF_DAY, 0);startCld.set(Calendar.MINUTE, 0);startCld.set(Calendar.SECOND, 0);
			endCld.set(Calendar.HOUR_OF_DAY, 23);endCld.set(Calendar.MINUTE, 59);endCld.set(Calendar.SECOND, 59);
			startCld.add(Calendar.MONTH, -12);

			UserReportTotalVO report = userBasicMapper.selectCommissionReport(id, startCld.getTime(), endCld.getTime());
			report.setDate("近一年");
			list.add(report);
		}

		return list;
	}

}
