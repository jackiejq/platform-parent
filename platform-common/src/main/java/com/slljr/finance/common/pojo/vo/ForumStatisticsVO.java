package com.slljr.finance.common.pojo.vo;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/28.
 * @time: 14:58.
 */
public class ForumStatisticsVO {

    /**
     * 数据ID
     */
    private Integer dataId;

    /**
     * 点赞数量
     */
    private Integer count;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
