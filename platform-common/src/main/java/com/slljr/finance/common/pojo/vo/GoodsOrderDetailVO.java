package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.GoodsOrder;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description:商品订单详情
 * @author: goodni.
 * @date: 2019/3/1
 * @time: 10:41
 */
public class GoodsOrderDetailVO extends GoodsOrder implements Serializable {

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名", required = false)
    private String userName;
    /**
     * 商品详情
     */
    private String goodsDetail;
    /**
     * 操作人姓名
     */
    private String optUname;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getOptUname() {
        return optUname;
    }

    public void setOptUname(String optUname) {
        this.optUname = optUname;
    }
}
