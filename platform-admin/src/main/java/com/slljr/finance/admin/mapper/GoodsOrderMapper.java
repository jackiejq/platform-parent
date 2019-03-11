package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.vo.GoodsOrderVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);
    
    /**
     * 根据id删除数据
     * @param id
     */
    void deleteId(@Param("id") Integer id);
    
    //查看订单列表
    List<GoodsOrderVO> listGoodsOrder(GoodsOrderDTO goodsOrder);

    //根据id查询详情
    GoodsOrder selectOrderDetailById(Integer id);

    //发货后，修改订单状态，添加发货人，快递名称，快递单号，发货时间等
    int updateDeliverInfoById(Integer id, Date postTime, String expressNo, String expressName, Integer optUid, Integer status, Date updateTime);
}