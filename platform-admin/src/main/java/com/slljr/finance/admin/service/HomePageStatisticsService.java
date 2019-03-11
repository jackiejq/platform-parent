package com.slljr.finance.admin.service;

import com.slljr.finance.admin.mapper.UserBasicMapper;
import com.slljr.finance.admin.mapper.UserTradeOrderMapper;
import com.slljr.finance.common.pojo.vo.StatisticsResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 首页统计服务接口
 * @author: uncle.quentin.
 * @date: 2019/1/22.
 * @time: 15:20.
 */
@Service
public class HomePageStatisticsService {

    @Autowired
    private UserBasicMapper userBasicMapper;

    @Autowired
    private UserTradeOrderMapper userTradeOrderMapper;


    /**
     * 近30天所有用户【交易额】按天分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly30DaysPayAmount() {
        //近N天数据（目前30天）
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            days.add(i);
        }
        //查询统计数据
        return userTradeOrderMapper.statisticsPaymentAmountByDay(days);
    }

    /**
     * 近12个月所有用户下【交易额】按月分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly12MonthsPayAmount() {
        //近N个月数据（目前12个月）
        List<Integer> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(i);
        }
        //查询统计数据
        return userTradeOrderMapper.statisticsPaymentAmountByMonth(months);
    }

    /**
     * 近30天所有用户【公司利润】按天分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly30DaysProfit() {
        //近N天数据（目前30天）
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            days.add(i);
        }
        //查询统计数据
        return userTradeOrderMapper.statisticsCompanyProfitByDay(days);
    }

    /**
     * 近12个月所有用户【公司利润】按月分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly12MonthsProfit() {
        //近N个月数据（目前12个月）
        List<Integer> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(i);
        }
        //查询统计数据
        return userTradeOrderMapper.statisticsCompanyProfitByMonth(months);
    }

    /**
     * 近30天所有用户【用户增长数】按天分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly30DaysNewUser() {
        //近N天数据（目前30天）
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            days.add(i);
        }
        //查询统计数据
        return userBasicMapper.statisticsNewUserByDay(days);
    }

    /**
     * 近12个月所有用户【用户增长数】按月分组统计
     *
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 15:24
     * @version 1.0
     */
    public List<StatisticsResultVO> getNearly12MonthsNewUser() {
        //近N个月数据（目前12个月）
        List<Integer> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(i);
        }
        //查询统计数据
        return userBasicMapper.statisticsNewUserByMonth(months);
    }

}
