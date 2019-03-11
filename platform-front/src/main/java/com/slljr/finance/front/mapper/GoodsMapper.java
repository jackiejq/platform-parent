package com.slljr.finance.front.mapper;

import java.util.List;

import com.slljr.finance.common.pojo.dto.PageGoodsDto;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.vo.GoodsVO;

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

    /**
     * 根据ID集合查询商品
     *
     * @author uncle.quentin
     * @date   2018/12/14 9:06
     * @param   ids
     * @return java.util.List<com.slljr.finance.common.pojo.vo.GoodsVO>
     * @version 1.0
     */
    List<GoodsVO> selectByIds(List<Integer> ids);
    
}