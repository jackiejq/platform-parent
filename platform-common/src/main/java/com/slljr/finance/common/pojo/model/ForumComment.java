package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ForumComment {

    /**
     * 评论类型
     */
    public enum targetTypeEnum{
        POST(1,"帖子"), COMMENT(2,"评论");

        int key;
        String msg;
        targetTypeEnum(int key, String msg){
            this.key = key;
            this.msg = msg;
        }

        public int getKey() {
            return key;
        }
        public void setKey(int key) {
            this.key = key;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    /**
	* 主键
	*/
    private Integer id;

    /**
     * 帖子ID
     */
    private Integer postId;

    /**
	* 目标ID
	*/
    private Integer targetId;

    /**
	* 目标类型（1：帖子、2：评论）
	*/
    private Integer targetType;

    /**
	* 回帖内容
	*/
    private String content;

    /**
	* 评论用户ID
	*/
    private Integer uid;

    /**
     * 楼层索引
     */
    private Integer indexes;

    /**
	* 创建时间
	*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
	* 修改时间
	*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
	* 状态(0：正常、-1：失效)
	*/
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getIndexes() {
        return indexes;
    }

    public void setIndexes(Integer indexes) {
        this.indexes = indexes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}