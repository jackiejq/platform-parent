package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.GoodsOrder;

/**
 * @description: 商品订单VO业务实体类
 * @author: uncle.quentin.
 * @date: 2018/12/13.
 * @time: 10:44.
 */
public class GoodsOrderVO extends GoodsOrder {

    /**
     * 商品详情
     */
    private String goodsDetail;

    /**
     * 操作人姓名
     */
    private String optUname;

    /**
     * 用户姓名
     */
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
