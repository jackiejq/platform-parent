package com.slljr.finance.front.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.vo.GoodsVO;
import com.slljr.finance.front.mapper.GoodsMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: 商品服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/13.
 * @time: 19:35.
 */
@Service
public class GoodsService {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 分页查询商品信息
     *
     * @param goods    查询参数
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.vo.GoodsVO>
     * @author uncle.quentin
     * @date 2018/12/12 19:54
     * @version 1.0
     */
    public PageInfo<GoodsVO> selectGoodsWithCategoryByPage(Goods goods) {
        PageInfo<GoodsVO> goodsPage = PageHelper.startPage(goods.getPageNum(), goods.getPageSize()).doSelectPageInfo(() -> goodsMapper.findByAllWithCategory(goods));
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

    /**
     * 根据ID集合查询商品
     *
     * @param ids
     * @return java.util.List<com.slljr.finance.common.pojo.vo.GoodsVO>
     * @author uncle.quentin
     * @date 2018/12/14 9:07
     * @version 1.0
     */
    public List<GoodsVO> selectGoodsByIds(List<Integer> ids) {
        return goodsMapper.selectByIds(ids);
    }

    /**
     * 修改商品库存
     *
     * @param operateType 操作类型【1：增加 2：减少】
     * @param goodsId     商品ID
     * @param changeStock 　变更数量
     * @return int
     * @author uncle.quentin
     * @date 2019/2/27 19:15
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public int udpateGoodsStock(Integer operateType, Integer goodsId, Integer changeStock) {
        log.info("修改商品库存，操作类型:{},商品ID:{}变更数量:{}", operateType, goodsId, changeStock);

        Goods updateStock = new Goods();
        updateStock.setId(goodsId);
        //查询商品现有库存
        Goods oldGoods = goodsMapper.selectByPrimaryKey(goodsId);
        if (operateType == 1) {
            updateStock.setCount(oldGoods.getCount() + changeStock);
        } else {
            updateStock.setCount(oldGoods.getCount() - changeStock);
        }
        return goodsMapper.updateByPrimaryKeySelective(updateStock);
    }

}
