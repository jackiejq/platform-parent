package com.slljr.finance.admin.mapper;

import java.util.List;

import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import com.slljr.finance.common.pojo.vo.StatisticsResultVO;
import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.model.UserTradeOrderExample;
import com.slljr.finance.common.pojo.vo.UserTradeDetailsVo;

public interface UserTradeOrderMapper {
    long countByExample(UserTradeOrderExample example);

    int deleteByExample(UserTradeOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserTradeOrder record);

    int insertSelective(UserTradeOrder record);

    List<UserTradeOrder> selectByExample(UserTradeOrderExample example);

    UserTradeOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserTradeOrder record, @Param("example") UserTradeOrderExample example);

    int updateByExample(@Param("record") UserTradeOrder record, @Param("example") UserTradeOrderExample example);

    int updateByPrimaryKeySelective(UserTradeOrder record);

    int updateByPrimaryKey(UserTradeOrder record);
    //查找用户交易记录
	//List<UserTradeOrder> findUserTrade(Integer uid);
	//分页查询用户交易记录
	List<UserTradeOrder> findUserTradeList(UserTradeOrderDTO userTradeOrder);

    /**
     * 近30天按天统计交易额
     *
     * @param days
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsPaymentAmountByDay(@Param("days") List<Integer> days);

    /**
     * 近12个月按天统计交易额
     *
     * @param months
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsPaymentAmountByMonth(@Param("months") List<Integer> months);

    /**
     * 近30天按天统计公司利润
     *
     * @param days
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsCompanyProfitByDay(@Param("days") List<Integer> days);

    /**
     * 近12个月按天统计公司利润
     *
     * @param months
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsCompanyProfitByMonth(@Param("months") List<Integer> months);
}