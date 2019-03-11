package com.slljr.finance.front.service;

import com.google.common.collect.Maps;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.enums.TradeOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import com.slljr.finance.common.pojo.model.*;
import com.slljr.finance.common.pojo.vo.InviteFriendsVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.pojo.vo.UserTradeOrderVO;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.front.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户账户信息服务接口
 * @author: uncle.quentin.
 * @date: 2019/1/6.
 * @time: 14:26.
 */
@Service
public class UserAccountService {

    private static final Logger log = LogManager.getLogger();

    /**
     * 用户账户Mapper
     */
    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 用户账户明细
     */
    @Autowired
    private UserBalanceDetailMapper userBalanceDetailMapper;

    /**
     * 用户等级
     */
    @Autowired
    private AgentLevelMapper agentLevelMapper;

    /**
     * 用户信息Mapper
     */
    @Autowired
    private UserBasicMapper userBasicMapper;

    /**
     * 签到Mapper
     */
    @Autowired
    private UserSignLogMapper userSignLogMapper;

    /**
     * 系统配置服务接口
     */
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AdvConfigMapper advConfigMapper;
    /**
     * 交易订单服务接口
     */
    @Autowired
    private UserTradeOrderMapper userTradeOrderMapper;

    /**
     * 根据用户ID更新用户积分余额
     *
     * @param uid      用户ID
     * @param integral 积分数
     * @return int
     * @author uncle.quentin
     * @date 2019/1/6 16:02
     * @version 1.0
     */
    public Map<String, Object> updateIntegralBalanceByUid(int uid, Double integral, String changedSummary) {
        //查询用户账户余额信息
        UserAccount userAccount = userAccountMapper.findByUid(uid);
        //计算变更后积分余额
        Double changedIntegral = MathUtils.add(userAccount.getIntegralBalance(), integral, 2);
        //更新积分余额信息
        UserAccount record = new UserAccount();
        record.setUid(uid);
        record.setIntegralBalance(changedIntegral);
        log.info(String.format("更新后积分余额：%s；变更积分：%s", changedIntegral, integral));
        int successCount = userAccountMapper.updateByUid(record);
        //插入变更明细
        if (successCount > 0) {
            UserBalanceDetail userBalanceDetail = new UserBalanceDetail();
            userBalanceDetail.setUid(uid);
            userBalanceDetail.setType(UserBalanceDetail.TypeEnum.INTEGRAL.getKey());
            userBalanceDetail.setNumber(integral);
            userBalanceDetail.setSummary(changedSummary);
            userBalanceDetail.setBalance(changedIntegral);
            userBalanceDetail.setStatus(0);
            userBalanceDetailMapper.insertSelective(userBalanceDetail);
            //返回
            Map<String, Object> resultMap = Maps.newHashMap();
            resultMap.put("currentIntegral", integral);
            resultMap.put("totalIntegral", changedIntegral);
            return resultMap;
        }

        return null;
    }


    /**
     * 签到并且今日首次签到更新积分信息
     *
     * @param uid 用户ID
     * @return int
     * @author uncle.quentin
     * @date 2019/1/6 15:48
     * @version 1.0
     */
    private Map<String, Object> updateIntegralByFirstSign(int uid) throws InterfaceException {
        //判断用户是否签到过
        Date now = new Date();
        List<UserSignLog> todaySignInfo = userSignLogMapper.findByUidAndSignTime(uid, now);
        if (null != todaySignInfo && !todaySignInfo.isEmpty()) {
            throw new InterfaceException(MsgEnum.SIGNED.getMsg());
        }
        //签到
        UserSignLog record = new UserSignLog();
        record.setUid(uid);
        int successCount = userSignLogMapper.insertSelective(record);
        //更新用户积分
        if (successCount > 0) {
            SysConfig sysConfig = sysConfigService.selectSysConfigByKey(SysConfigEnum.SIGNED_INTEGRAL);
            if (null != sysConfig) {
                //系统配置默认赠送积分
                Double sysConfigIntegral = (null != sysConfig.getSysValue() ? Double.valueOf(sysConfig.getSysValue()) : 0.0);
                log.info(String.format("系统配置默认赠送积分：%s", sysConfigIntegral));
                //变更原因
                String changedSummary = "普通用户签到";

                return updateIntegralBalanceByUid(uid, sysConfigIntegral, changedSummary);
            }
        }

        return null;
    }

    /**
     * 普通用户收款/代偿更新用户积分
     *
     * @param uid     用户ID
     * @param tradeId 订单ID
     * @return int
     * @author uncle.quentin
     * @date 2019/1/6 15:59
     * @version 1.0
     */
    private Map<String, Object> updateIntegralByPayment(int uid, int tradeId) {
        //普通用户收款/代偿,奖励(金额*N%)积分
        UserTradeOrder userTradeOrder = userTradeOrderMapper.selectByPrimaryKey(tradeId);
        log.info(String.format("UserAccountService.updateIntegralByPayment订单状态：%s", null == userTradeOrder.getStatus() ? "" : userTradeOrder.getStatus()));
        //订单已完成，更新用户积分
        if (null != userTradeOrder && TradeOrderStatusEnum.SUCCESS.getKey() == userTradeOrder.getStatus()) {
            //计算（金额*N%）积分
            Double paymentAmount = (null == userTradeOrder.getPaymentAmount() ? 0.0 : userTradeOrder.getPaymentAmount());
            Double integral = 0.0;
            //获取配置N%
            SysConfig sysConfig = sysConfigService.selectSysConfigByKey(SysConfigEnum.PAYMENT_INTEGRAL_RATE);
            if (null != sysConfig) {
                Double rate = (null == sysConfig.getSysValue() ? 0.0 : Double.valueOf(sysConfig.getSysValue()));
                log.info(String.format("UserAccountService.updateIntegralByPayment系统配置比例：%s", rate));
                integral = MathUtils.mul(paymentAmount, rate, 2);
            }
            StringBuffer changedSummary = new StringBuffer("普通用户");
            if (PaymentChannelTypeEnum.DAIHUAN.getKey() == userTradeOrder.getType()) {
                changedSummary.append("代偿");
            } else {
                changedSummary.append("收款");
            }

            log.info(String.format("UserAccountService.updateIntegralByPayment支付金额：%s;计算后奖励：%s;", paymentAmount, integral));

            return updateIntegralBalanceByUid(uid, integral, changedSummary.toString());
        }

        return null;
    }


    /**
     * 更新账户积分余额(收款/代偿、签到)
     *
     * @param operateType 操作类型【1：收款/代偿；2：签到】
     * @param userBasic   用户信息
     * @param tradeId     订单ID
     * @return void
     * @author uncle.quentin
     * @date 2019/1/6 16:08
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateUserAccountIntegralBalance(Integer operateType, UserBasicVO userBasic, Integer tradeId) throws InterfaceException {
        //判断是否普通用户
        log.info(String.format("用户类型：%s", userBasic.getType()));
        if (UserBasic.TypeEnum.NORMAL.getKey() == userBasic.getType()) {
            //普通用户首次签到送积分
            if (Constant.OPERATETYPE_SIGN == operateType) {
                //签到并且今日首次签到更新积分信息
                return updateIntegralByFirstSign(userBasic.getId());
            } else {
                //订单已完成，更新用户积分，奖励(金额*N%)积分
                return updateIntegralByPayment(userBasic.getId(), tradeId);
            }
        } else {
            throw new InterfaceException(MsgEnum.PROXY_NOT_ALLOW_SIGN);
        }

    }

    /**
     * 用户本身为代理，奖励自己分润，(公司利润*N%)金币
     *
     * @param userBasic     用户信息
     * @param order         订单信息
     * @param companyProfit 公司分润
     * @return int
     * @author uncle.quentin
     * @date 2019/1/11 11:00
     * @version 1.0
     */
    public int updateOwnCash(UserBasicVO userBasic, UserTradeOrder order, Double companyProfit) {
        //用户为代理用户
        if (UserBasic.TypeEnum.AGENT.getKey() == userBasic.getType()) {
            UserAccount currentUserAccount = userAccountMapper.findByUid(userBasic.getId());
            if (null != currentUserAccount && null != currentUserAccount.getAgentLevelId()) {
                //获取公司分润信息
                StringBuffer changedSummary = new StringBuffer("代理用户");
                if (1 == order.getType()) {
                    changedSummary.append("代偿");
                } else {
                    changedSummary.append("收款");
                }
                changedSummary.append("奖励当前用户（代理用户）金币");

                return updateUserCashDivided(currentUserAccount.getAgentLevelId(), currentUserAccount.getUid(), order.getId(), companyProfit, changedSummary.toString());
            } else {
                log.error("用户账户代理等级为空,用户ID：{}", userBasic.getId());
            }

        }

        return 0;
    }


    /**
     * 更新邀请人金币信息
     *
     * @param order         订单信息
     * @param userBasic     用户信息
     * @return int
     * @author uncle.quentin
     * @date 2019/1/7 14:22
     * @version 1.0
     */
    private int updateRefererUserCash(UserTradeOrder order, UserBasicVO userBasic) {
        //公司分润
        Double companyProfit = (null == order.getCompanyProfit() ? 0.0 : order.getCompanyProfit());
        log.info("UserAccountService.updateUserCashBalance公司分润：{}", companyProfit);
        //判断本身为代理用户，奖励自己分润 OR 没有邀请人信息，自己本身是代理用户，完成收款/代偿,奖励(公司利润*N%)金币
        int ownSuccessCount = updateOwnCash(userBasic, order, companyProfit);

        //有邀请人
        if (null != userBasic.getRefererUid()) {
            //推荐人信息
            UserBasicVO refererUserBasic = userBasicMapper.selectByPrimaryKey(userBasic.getRefererUid());
            //推荐人账户信息
            UserAccount refererUserAccount = userAccountMapper.findByUid(userBasic.getRefererUid());
            log.info("UserAccountService.updateRefererUserCash推荐人用户类型：{}", refererUserBasic.getType());
            //推荐人为代理用户→奖励推荐人→奖励(公司利润*N%)金币
            if (UserBasic.TypeEnum.AGENT.getKey() == refererUserBasic.getType()) {
                StringBuffer changedSummary = new StringBuffer("用户");
                if (1 == order.getType()) {
                    changedSummary.append("代偿");
                } else {
                    changedSummary.append("收款");
                }
                changedSummary.append("奖励邀请人（代理用户）金币");

                return updateUserCashDivided(refererUserAccount.getAgentLevelId(), refererUserAccount.getUid(), order.getId(), companyProfit, changedSummary.toString());

            } else if (UserBasic.TypeEnum.NORMAL.getKey() == refererUserBasic.getType()) {
                //推荐人为普通用户→判断当前用户是否完成的首次收款→是则奖励推荐人奖励N金币
                UserTradeOrderDTO searchParam = new UserTradeOrderDTO();
                searchParam.setUid(userBasic.getId());
                searchParam.setStatus(TradeOrderStatusEnum.SUCCESS.getKey());
                List<UserTradeOrderVO> currentUserCompleteOrder = userTradeOrderMapper.findByAll(searchParam);
                if (null != currentUserCompleteOrder && currentUserCompleteOrder.size() == 1) {
                    SysConfig sysConfig = sysConfigService.selectSysConfigByKey(SysConfigEnum.FIRST_PAYMENT_CASH);
                    if (null != sysConfig) {
                        StringBuffer changedSummary = new StringBuffer("用户首次");
                        if (1 == order.getType()) {
                            changedSummary.append("代偿");
                        } else {
                            changedSummary.append("收款");
                        }
                        changedSummary.append("奖励邀请人（普通用户）金币");
                        //配置的赠送金额
                        Double sysConfigCash = (null != sysConfig.getSysValue() ? Double.valueOf(sysConfig.getSysValue()) : 0.0);
                        log.info("UserAccountService.updateRefererUserCash普通用户首次收款/代偿赠送金币：{}", sysConfigCash);
                        return updateUserCash(refererUserAccount.getUid(), order.getId(), sysConfigCash, changedSummary.toString());
                    } else {
                        log.info("UserAccountService.updateRefererUserCash没有配置普通用户首次收款/代偿赠送金币");
                    }
                }
            }

        } else {
            log.info(String.format("UserAccountService.updateRefererUserCash无邀请人，本身用户类型：%s", userBasic.getType()));
            return ownSuccessCount;
        }

        return 0;
    }

    /**
     * 奖励(公司利润*N%)金币
     *
     * @param levelId        等级ID
     * @param uid            用户ID
     * @param tradeId        交易ID
     * @param cashBalance    公司利润
     * @param changedSummary 变更描述
     * @return int
     * @author uncle.quentin
     * @date 2019/1/7 11:57
     * @version 1.0
     */
    private int updateUserCashDivided(int levelId, int uid, int tradeId, Double cashBalance, String changedSummary) {
        //获取用户等级
        AgentLevel agentLevel = agentLevelMapper.selectByPrimaryKey(levelId);
        if (null != agentLevel) {
            //获取佣金比例
            Double rate = (null == agentLevel.getCommissionRate() ? 0.0 : agentLevel.getCommissionRate());
            //计算分润金额
            Double cash = MathUtils.mul(cashBalance, rate, 2);

            log.info(String.format("UserAccountService.updateUserCash佣金比例：%s；分润金额：%s", rate, cash));

            return updateUserCash(uid, tradeId, cash, changedSummary);

        }
        return 0;
    }

    /**
     * 更新金币
     *
     * @param uid            用户ID
     * @param tradeId        交易ID
     * @param cash           变更金额
     * @param changedSummary 变更描述
     * @return int
     * @author uncle.quentin
     * @date 2019/1/11 10:28
     * @version 1.0
     */
    public int updateUserCash(int uid, int tradeId, double cash, String changedSummary) {
        //查询用户账户余额信息
        UserAccount userAccount = userAccountMapper.findByUid(uid);
        //计算变更后金币余额
        Double balance = (null == userAccount.getCashBalance() ? 0.0 : userAccount.getCashBalance());
        Double changedCash = MathUtils.add(balance, cash, 2);

        log.info(String.format("UserAccountService.updateUserCash佣金余额：%s；变更后余额：%s", balance, changedCash));

        //更新用户账户金币余额信息
        UserAccount record = new UserAccount();
        record.setUid(uid);
        record.setCashBalance(changedCash);
        int successCount = userAccountMapper.updateByUid(record);
        //更新订单代理用户分润、插入变更明细
        if (successCount > 0) {
            UserTradeOrder userTradeOrder = new UserTradeOrder();
            userTradeOrder.setId(tradeId);
            userTradeOrder.setAgencyProfit(cash);
            userTradeOrderMapper.updateByPrimaryKeySelective(userTradeOrder);

            UserBalanceDetail userBalanceDetail = new UserBalanceDetail();
            userBalanceDetail.setUid(uid);
            userBalanceDetail.setType(UserBalanceDetail.TypeEnum.CASH.getKey());
            userBalanceDetail.setNumber(cash);
            userBalanceDetail.setSummary(changedSummary);
            userBalanceDetail.setBalance(changedCash);
            userBalanceDetail.setStatus(0);
            userBalanceDetailMapper.insertSelective(userBalanceDetail);
            return successCount;
        }

        return successCount;
    }

    /**
     * 更新用户佣金（金币）信息
     *
     * @param userBasic 用户信息
     * @param tradeId   交易订单ID
     * @return int
     * @author uncle.quentin
     * @date 2019/1/6 16:13
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateUserCashBalance(UserBasicVO userBasic, int tradeId) {
        //获取公司分润信息
        UserTradeOrder order = userTradeOrderMapper.selectByPrimaryKey(tradeId);
        if (null == order) {
            log.error("UserAccountService.updateUserCashBalance订单为空,交易ID：{}", tradeId);
            return 0;
        } else {
            //获取邀请人信息
            return updateRefererUserCash(order, userBasic);
        }
    }


    /**
     * 用户完成收款，更新用户以及代理用户奖励信息
     *
     * @param uid     用户ID
     * @param tradeId 交易订单ID
     * @return int
     * @author uncle.quentin
     * @date 2019/1/7 16:25
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRewardByCompletePay(int uid, int tradeId) throws InterfaceException {
        log.info(String.format("用户ID：%s;订单ID：%s", uid, tradeId));
        //获取用户信息 必要参数→{"id":1000103,"type":1,"refererUid":1000104}
        UserBasicVO userBasic = userBasicMapper.selectByPrimaryKey(uid);
        if (null != userBasic) {
            //更新用户积分信息
            updateUserAccountIntegralBalance(Constant.OPERATETYPE_PAYMENT, userBasic, tradeId);
            //更新用户金币信息
            updateUserCashBalance(userBasic, tradeId);
        }
    }

    /**
     * 获取用户邀请好友信息
     *
     * @author uncle.quentin
     * @date   2019/1/14 11:57
     * @param   uid
     * @return com.slljr.finance.common.pojo.vo.InviteFriendsVO
     * @version 1.0
     */
    public InviteFriendsVO selectInviteFriendsInfo(Integer uid){
        InviteFriendsVO bean = new InviteFriendsVO();
        //累计佣金
        bean.setAccumulativeCash(0D);
        //累计人数
        bean.setAccumulativeCount(0);

        return userAccountMapper.findSumCashBalanceByUid(uid);
    }

    /**
     * 根据用户ID查询用户账户信息
     *
     * @author uncle.quentin
     * @date   2019/2/27 11:47
     * @param   uid
     * @return com.slljr.finance.common.pojo.model.UserAccount
     * @version 1.0
     */
    public UserAccount selectAccountByUid(Integer uid){
        return userAccountMapper.findByUid(uid);
    }

    /**
     * 更新账户金币、积分
     *
     * @param userAccount
     * @return int
     * @author uncle.quentin
     * @date 2019/2/27 17:02
     * @version 1.0
     */
    public int updateAccountCBOrIB(UserAccount userAccount){
       return userAccountMapper.updateByUid(userAccount);
    }

}
