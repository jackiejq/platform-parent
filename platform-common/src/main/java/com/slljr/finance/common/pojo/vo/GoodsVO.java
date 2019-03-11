package com.slljr.finance.common.pojo.vo;

import com.slljr.finance.common.pojo.model.Goods;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:15.
 */
public class GoodsVO extends Goods {

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 商品分类父级ID
     */
    private Integer categoryParentId;

    /**
     * 商品分类父级名称
     */
    private String categoryParentName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Integer categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

    public String getCategoryParentName() {
        return categoryParentName;
    }

    public void setCategoryParentName(String categoryParentName) {
        this.categoryParentName = categoryParentName;
    }
}
