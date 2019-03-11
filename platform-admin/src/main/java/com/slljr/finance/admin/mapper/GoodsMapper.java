package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.Goods;import com.slljr.finance.common.pojo.vo.GoodsVO;import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    /**
     * 条件查询商品信息，带分类信息
     *
     * @param goods
     * @return java.util.List<com.slljr.finance.common.pojo.model.Goods>
     * @author uncle.quentin
     * @date 2018/12/12 19:48
     * @version 1.0
     */
    List<GoodsVO> findByAllWithCategory(Goods goods);
}