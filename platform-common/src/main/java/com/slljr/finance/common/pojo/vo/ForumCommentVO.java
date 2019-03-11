package com.slljr.finance.common.pojo.vo;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 15:48.
 */
public class ForumCommentVO {
    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 回复量
     */
    private Integer replyCount;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论楼层索引
     */
    private Integer commentIndexes;

    /**
     * 评论时间
     */
    private String commentCreateTime;
    /**
     * 评论用户昵称
     */
    private String commentUserNickName;
    /**
     * 评论用户头像链接
     */
    private String commentUserHeadImg;
    /**
     * 评论用户论坛等级
     */
    private String commentUserGrade;

    /**
     * 当前用户是否点赞过
     */
    private Boolean isLike;

    /**
     * 是否楼主回复
     */
    private Boolean isLandlord;

    /**
     * 父级评论ID
     */
    private Integer commentParentId;
    /**
     * 父级评论内容
     */
    private String commentParentContent;

    /**
     * 评论楼层索引
     */
    private Integer commentParentIndexes;

    /**
     * 父级评论时间
     */
    private String commentParentCreateTime;
    /**
     * 父级评论用户昵称
     */
    private String commentParentUserNickName;
    /**
     * 父级评论用户头像
     */
    private String commentParentHeadImg;
    /**
     * 父级评论用户论坛等级
     */
    private String commentParentGrade;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getCommentIndexes() {
        return commentIndexes;
    }

    public void setCommentIndexes(Integer commentIndexes) {
        this.commentIndexes = commentIndexes;
    }

    public String getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(String commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    public String getCommentUserNickName() {
        return commentUserNickName;
    }

    public void setCommentUserNickName(String commentUserNickName) {
        this.commentUserNickName = commentUserNickName;
    }

    public String getCommentUserHeadImg() {
        return commentUserHeadImg;
    }

    public void setCommentUserHeadImg(String commentUserHeadImg) {
        this.commentUserHeadImg = commentUserHeadImg;
    }

    public String getCommentUserGrade() {
        return commentUserGrade;
    }

    public void setCommentUserGrade(String commentUserGrade) {
        this.commentUserGrade = commentUserGrade;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public Boolean getLandlord() {
        return isLandlord;
    }

    public void setLandlord(Boolean landlord) {
        isLandlord = landlord;
    }

    public Integer getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(Integer commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getCommentParentContent() {
        return commentParentContent;
    }

    public void setCommentParentContent(String commentParentContent) {
        this.commentParentContent = commentParentContent;
    }

    public Integer getCommentParentIndexes() {
        return commentParentIndexes;
    }

    public void setCommentParentIndexes(Integer commentParentIndexes) {
        this.commentParentIndexes = commentParentIndexes;
    }

    public String getCommentParentCreateTime() {
        return commentParentCreateTime;
    }

    public void setCommentParentCreateTime(String commentParentCreateTime) {
        this.commentParentCreateTime = commentParentCreateTime;
    }

    public String getCommentParentUserNickName() {
        return commentParentUserNickName;
    }

    public void setCommentParentUserNickName(String commentParentUserNickName) {
        this.commentParentUserNickName = commentParentUserNickName;
    }

    public String getCommentParentHeadImg() {
        return commentParentHeadImg;
    }

    public void setCommentParentHeadImg(String commentParentHeadImg) {
        this.commentParentHeadImg = commentParentHeadImg;
    }

    public String getCommentParentGrade() {
        return commentParentGrade;
    }

    public void setCommentParentGrade(String commentParentGrade) {
        this.commentParentGrade = commentParentGrade;
    }
}
