package com.slljr.finance.admin.service;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.vo.GoodsOrderVO;

import java.util.Date;

/**
 * 1、商品订单的service
 * @author XueYi
 * 2018年12月10日 下午8:00:16
 */
public interface IGoodsOrderService {
	/**
	 * 1、添加订单数据
	 * @param goodsOrder
	 */
	int addGoodsOrder(GoodsOrder goodsOrder);
	
	/**
	 * 2、根据订单id查询数据
	 * @return
	 */
	GoodsOrder getGoodsOrder(Integer id);
	
	/**
	 * 3、删除订单信息
	 * @param id
	 */
	int deleteGoodsOrder(Integer id, Integer optUid);
	
	/**
	 * 4、修改订单数据
	 * @param goodsOrder
	 */
	int updateGoodsOrder(GoodsOrder goodsOrder);

	/**
	 * 5、分页查询订单数据
	 * @param goodsOrder
	 * @return
	 */
	PageInfo<GoodsOrderVO> listGoodsOrder(GoodsOrderDTO goodsOrder);

	/**
	 * 6、根据订单id查询详细数据
	 * @return
	 */
	GoodsOrder getGoodsOrderDetailById(Integer id);

	//7.发货后，修改订单状态，添加发货人，快递名称，快递单号，发货时间等
	int updateDeliverInfoById(Integer id, Date postTime, String expressNo, String expressName, Integer optUid, Integer status, Date updateTime);
}
