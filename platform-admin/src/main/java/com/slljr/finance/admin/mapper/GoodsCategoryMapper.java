package com.slljr.finance.admin.mapper;
import java.util.List;

import com.slljr.finance.common.pojo.vo.GoodsCategoryVo2;
import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.GoodsCategory;
import com.slljr.finance.common.pojo.vo.CategoryVo;

public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategoryVo2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    List<GoodsCategoryVo2> findOrderByParentIdAsc();


    /**
     * 查询二级分类
     * @param parentId
     * @return
     */
    List<GoodsCategoryVo2> findByParentId(@Param("parentId")Integer parentId);

    /**
     * 查询一级分类
     * @return
     */
    List<GoodsCategory> findByParentIdIsNull();

    /**
     * 根据parentId查询一级分类的id和名称
     * @param parentId
     * @return
     */
    CategoryVo selectParentId(@Param("parentId")Integer parentId);
    
    /**
     * 查询所有数据
     * @return
     */
    List<GoodsCategoryVo2> listGoodsCategory();

}