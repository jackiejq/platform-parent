package com.slljr.finance.common.pojo.vo;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/28.
 * @time: 9:56.
 */
public class ForumCacheVO {

    /**
     * 数据ID
     */
    private Integer dataId;

    /**
     * 点赞类型【1：帖子、2：评论】
     */
    private Integer likeType;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 回复类型【1：帖子、2：评论】
     */
    private Integer commentType;

    /**
     * 回复数量
     */
    private Integer commentCount;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getLikeType() {
        return likeType;
    }

    public void setLikeType(Integer likeType) {
        this.likeType = likeType;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}
