package com.slljr.finance.common.pojo.vo;

/**
 * @description: 用户邀请好友记录信息
 * @author: uncle.quentin.
 * @date: 2019/1/14.
 * @time: 11:49.
 */
public class InviteFriendsVO {
    /**
     * 累计佣金
     */
    private Double accumulativeCash;
    /**
     * 累计人数
     */
    private Integer accumulativeCount;

	public Double getAccumulativeCash() {
        return accumulativeCash;
    }

    public void setAccumulativeCash(Double accumulativeCash) {
        this.accumulativeCash = accumulativeCash;
    }

    public Integer getAccumulativeCount() {
        return accumulativeCount;
    }

    public void setAccumulativeCount(Integer accumulativeCount) {
        this.accumulativeCount = accumulativeCount;
    }

}
