package com.slljr.finance.common.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserProxyAudit extends BasePageVO {
    public enum AuditStatusEnum{
        CHECKING(0,"待审核"), PASS(1,"审核通过"), UN_PASS(-1, "驳回"),;

        int key;
        String msg;
        AuditStatusEnum(int key, String msg){
            this.key = key;
            this.msg = msg;
        }

        public static AuditStatusEnum val(Integer operate) {
            //values()方法返回enum实例的数组
            for(AuditStatusEnum s : values()) {
                if(operate.equals(s.getKey())){
                    return s;
                }
            }
            return null;
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
    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    /**
     * 申请描述
     */
    @ApiModelProperty(value = "申请描述")
    private String applyDescription;

    /**
	* 主要推广省份
	*/
    @ApiModelProperty(value = "主要推广省份")
    private String spreadProvince;

    /**
	* 主要推广城市
	*/
    @ApiModelProperty(value = "主要推广城市")
    private String spreadCity;

    /**
     * 审核状态：0、待审核 1、审核通过 -1、驳回
     */
    @ApiModelProperty(value = "审核状态：0、待审核 1、审核通过 -1、驳回")
    private Integer auditStatus;

    /**
     * 审核说明备注
     */
    @ApiModelProperty(value = "审核说明备注")
    private String auditRemark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getApplyDescription() {
        return applyDescription;
    }

    public void setApplyDescription(String applyDescription) {
        this.applyDescription = applyDescription;
    }

    public String getSpreadProvince() {
        return spreadProvince;
    }

    public void setSpreadProvince(String spreadProvince) {
        this.spreadProvince = spreadProvince;
    }

    public String getSpreadCity() {
        return spreadCity;
    }

    public void setSpreadCity(String spreadCity) {
        this.spreadCity = spreadCity;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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
}