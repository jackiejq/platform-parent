package com.slljr.finance.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.GoodsMapper;
import com.slljr.finance.admin.mapper.GoodsOrderMapper;
import com.slljr.finance.admin.service.IGoodsOrderService;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.vo.GoodsOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 1、商品订单的业务逻辑层实现类
 *
 * @author XueYi
 * 2018年12月10日 下午8:02:49
 */
@Service
public class GoodsOrderServiceImpl implements IGoodsOrderService {

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 1、添加订单数据
     */
    @Override
    public int addGoodsOrder(GoodsOrder goodsOrder) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsOrder.getGoodsId());
        goodsOrder.setPostTime(new Date());
        goodsOrder.setCreateTime(new Date());

        if (goodsOrder.getStatus() == null) {
            goodsOrder.setStatus(0);
        }
        if (goodsOrder.getIsStatus() == null) {
            goodsOrder.setIsStatus(0);
        }
        goodsOrder.setCreateTime(new Date());
        goodsOrder.setUpdateTime(new Date());

        Double price = goods.getPriceCash();
        Double integralPrice = goods.getPrice();
        int count = goodsOrder.getGoodsCount();
        if (price != null) {
            Double total = price.doubleValue() * count;
            goodsOrder.setGoodsPrice(price);
            goodsOrder.setTotal(total);
        }
        if (integralPrice != null) {
            Double totalIntegral = integralPrice.doubleValue() * count;
            goodsOrder.setGoodsIntegralPrice(integralPrice);
            goodsOrder.setTotalIntegral(totalIntegral);
        }
        goodsOrder.setGoodsId(goods.getId());
        goodsOrder.setGoodsTitle(goods.getTitle());
        goodsOrder.setGoodsImage(goods.getImgUrl());
        return goodsOrderMapper.insert(goodsOrder);
    }

    /**
     * 2、根据订单id查询订单详情
     */
    @Override
    public GoodsOrder getGoodsOrder(Integer id) {
        GoodsOrder goodsOrder = goodsOrderMapper.selectByPrimaryKey(id);
        Assert.notNull(goodsOrder, "订单数据不存在");
        return goodsOrder;
    }

    /**
     * 3、删除订单数据
     */
    @Override
    public int deleteGoodsOrder(Integer id, Integer optUid) {
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setId(id);
        goodsOrder.setOptUid(optUid);
        goodsOrder.setIsStatus(DataStatusEnum.INVALID.getKey());
        return goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
    }

    /**
     * 4、修改订单数据
     */
    @Override
    public int updateGoodsOrder(GoodsOrder goodsOrder) {
        return goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
    }

    /**
     * 5、分页查询订单数据
     */
    @Override
    public PageInfo<GoodsOrderVO> listGoodsOrder(GoodsOrderDTO goodsOrder) {
        //1、设置分页信息
        PageInfo<GoodsOrderVO> pageInfo = PageHelper.startPage(goodsOrder.getPageNum(), goodsOrder.getPageSize()).doSelectPageInfo(() -> goodsOrderMapper.listGoodsOrder(goodsOrder));
        return pageInfo;
    }

    /**
     * @Author goodni
     * @Description 依据订单id查询详情
     * @Date 2019/3/1 11:04
     */
    @Override
    public GoodsOrder getGoodsOrderDetailById(Integer id) {
        return goodsOrderMapper.selectOrderDetailById(id);
    }

    /**
     * @Author goodni
     * @Description 发货后修改状态
     * @Date 2019/3/1 11:04
     */
    @Override
    public int updateDeliverInfoById(Integer id, Date postTime, String expressNo, String expressName, Integer optUid, Integer status, Date updateTime) {
        return goodsOrderMapper.updateDeliverInfoById(id, postTime, expressNo, expressName, optUid, status, updateTime);
    }
}
