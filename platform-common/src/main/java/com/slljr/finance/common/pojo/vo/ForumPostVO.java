package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.ForumPost;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 14:45.
 */
public class ForumPostVO extends ForumPost {

    /**
     * 用户头像链接
     */
    private String headImg;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户等级
     */
    private Integer grade;

    /**
     * 回复数量
     */
    private Integer commentCount;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 内容首张图片URL
     */
    private String contentImg;

    /**
     * 发布年份
     */
    private Integer publishYear;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }
}
