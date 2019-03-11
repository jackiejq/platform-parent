package com.slljr.finance.common.pojo.model;

import com.slljr.finance.common.pojo.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value="com.slljr.finance.common.pojo.model.Goods")
public class Goods extends BasePageVO implements Serializable {
    /**
	* 
	*/
    @ApiModelProperty(value="")
    private Integer id;

    /**
	* 商品标题
	*/
    @ApiModelProperty(value="商品标题")
    private String title;

    /**
	* 积分价格
	*/
    @ApiModelProperty(value="积分价格")
    private Double price;


    /**
     * 金币价格
     */
    @ApiModelProperty(value="金币价格")
    private Double priceCash;

    /**
     * 类型[1积分, 2金币, 3积分+金币]
     */
    @ApiModelProperty(value="类型[1积分, 2金币, 3积分+金币]")
    private Integer type;

    /**
	* 图片url
	*/
    @ApiModelProperty(value="图片url")
    private String imgUrl;

    /**
	* 商品剩余数量
	*/
    @ApiModelProperty(value="商品剩余数量")
    private Integer count;

    /**
	* 分类id
	*/
    @ApiModelProperty(value="分类id")
    private Integer categoryId;

    /**
	* 状态[-1删除,0正常]
	*/
    @ApiModelProperty(value="状态[-1删除,0正常]")
    private Integer status;

    /**
	* 创建时间
	*/
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
	* 修改时间
	*/
    @ApiModelProperty(value="修改时间")
    private Date updateTime;

    /**
	* 商品详情
	*/
    @ApiModelProperty(value="商品详情")
    private String detail;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceCash() {
        return priceCash;
    }

    public void setPriceCash(Double priceCash) {
        this.priceCash = priceCash;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}