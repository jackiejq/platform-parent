package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.GoodsMapper;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 商品服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:20.
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 新增商品
     *
     * @param goods
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 19:23
     * @version 1.0
     */
    public int insertGoods(Goods goods) {
        return goodsMapper.insertSelective(goods);
    }

    /**
     * 逻辑删除商品
     *
     * @param goodsId 商品ID
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 19:24
     * @version 1.0
     */
    public int deleteGoods(int goodsId) {
        Goods record = new Goods();
        record.setId(goodsId);
        record.setStatus(DataStatusEnum.INVALID.getKey());
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 修改商品信息
     *
     * @param record
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 19:31
     * @version 1.0
     */
    public int updateGoods(Goods record) {
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询商品信息
     *
     * @param goods    查询参数
     * @param pageNum  开始页
     * @param pageSize 每页数
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.vo.GoodsVO>
     * @author uncle.quentin
     * @date 2018/12/12 19:54
     * @version 1.0
     */
    public PageInfo<GoodsVO> selectGoodsWithCategoryByPage(Goods goods, int pageNum, int pageSize) {
        PageInfo<GoodsVO> goodsPage = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> goodsMapper.findByAllWithCategory(goods));
        return goodsPage;
    }

    /**
     * 条件查询所有商品信息
     *
     * @param goods
     * @return java.util.List<com.slljr.finance.common.pojo.vo.GoodsVO>
     * @author uncle.quentin
     * @date 2018/12/12 19:56
     * @version 1.0
     */
    public List<GoodsVO> selectGoodsWithCategory(Goods goods) {
        return goodsMapper.findByAllWithCategory(goods);
    }


}
