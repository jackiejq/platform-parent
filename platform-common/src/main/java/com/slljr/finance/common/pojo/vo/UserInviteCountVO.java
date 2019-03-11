package com.slljr.finance.common.pojo.vo;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/13.
 * @time: 17:52.
 */
public class UserInviteCountVO {

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 邀请人数
     */
    private Integer inviteCount;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }
}
