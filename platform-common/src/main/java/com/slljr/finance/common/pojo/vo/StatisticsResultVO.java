package com.slljr.finance.common.pojo.vo;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/1/22.
 * @time: 14:03.
 */
public class StatisticsResultVO {

    /**
     * 统计信息对应key
     */
    private String statisticsKey;

    /**
     * 统计信息对应value
     */
    private String statisticsValue;

    public String getStatisticsKey() {
        return statisticsKey;
    }

    public void setStatisticsKey(String statisticsKey) {
        this.statisticsKey = statisticsKey;
    }

    public String getStatisticsValue() {
        return statisticsValue;
    }

    public void setStatisticsValue(String statisticsValue) {
        this.statisticsValue = statisticsValue;
    }
}
