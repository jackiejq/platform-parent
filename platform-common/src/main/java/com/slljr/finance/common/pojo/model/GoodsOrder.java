package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 1、订单的实体类
 * @author goodni
 * 2019年2月28日 下午1:52:56
 */
public class GoodsOrder extends BasePageVO implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = 3015794654542775340L;

	//主键id
	@ApiModelProperty(value = "订单编号", required = false)
	private Integer id;

	//用户id
	@ApiModelProperty(value = "用户编号", required = false)
    private Integer uid;

    //商品id
	@ApiModelProperty(value = "商品编号", required = false)
    private Integer goodsId;

    //商品标题
//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "商品标题", required = false)
    private String goodsTitle;

    //商品金币价格
//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "商品金币价格（单价）", required = false)
    private Double goodsPrice;

    //商品积分价格
//    @ApiParam(hidden = true)
    @ApiModelProperty(value = "积分价格（单价）", required = false)
    private Double goodsIntegralPrice;

    //商品数量
	@ApiModelProperty(value = "商品数量", required = false)
    private Integer goodsCount;

    //商品图片
//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "商品图片", required = false)
    private String goodsImage;

    //金币总价
	@ApiModelProperty(value = "金币总价（金币价格*数量）", required = false)
    private Double total;

    //积分总价
    @ApiModelProperty(value = "积分总价（积分价格*数量）", required = false)
    private Double totalIntegral;

    //收货人
	@ApiModelProperty(value = "收货人", required = false)
    private String name;

    //收货人手机号码
	@ApiModelProperty(value = "收货人手机号码", required = false)
    private String phone;

    //收货人地址
	@ApiModelProperty(value = "收货人地址", required = false)
    private String address;

    //邮寄时间
//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "邮寄时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    //快递名称
	@ApiModelProperty(value = "快递名称", required = false)
    private String expressName;

    //快递单号
	@ApiModelProperty(value = "快递单号", required = false)
    private String expressNo;

    //状态[-1取消,0待发货,1已发货]
//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "状态[-1取消,0待发货,1已发货]", required = false)
    private Integer status;

//	@ApiParam(hidden = true)
	@ApiModelProperty(value = "状态[-1删除,0正常]", required = false)
	private Integer isStatus;
	
    //操作人用户id
	@ApiModelProperty(value = "操作人用户id", required = false)
    private Integer optUid;
    
    //创建时间
	@ApiParam(hidden = true) 
	@ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //修改时间
	@ApiParam(hidden = true) 
	@ApiModelProperty(value = "修改时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getGoodsIntegralPrice() {
        return goodsIntegralPrice;
    }

    public void setGoodsIntegralPrice(Double goodsIntegralPrice) {
        this.goodsIntegralPrice = goodsIntegralPrice;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Double totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(Integer isStatus) {
        this.isStatus = isStatus;
    }

    public Integer getOptUid() {
        return optUid;
    }

    public void setOptUid(Integer optUid) {
        this.optUid = optUid;
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