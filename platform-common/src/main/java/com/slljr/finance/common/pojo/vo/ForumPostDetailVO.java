package com.slljr.finance.common.pojo.vo;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.ForumPost;

/**
 * @description: 帖子明细
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 15:44.
 */
public class ForumPostDetailVO extends ForumPostVO {

    /**
     * 当前用户是否点赞过该帖子
     */
    private boolean isLike;
    /**
     * 评论详情（评论和回复）
     */
    PageInfo<ForumCommentVO> commentList;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public PageInfo<ForumCommentVO> getCommentList() {
        return commentList;
    }

    public void setCommentList(PageInfo<ForumCommentVO> commentList) {
        this.commentList = commentList;
    }
}
