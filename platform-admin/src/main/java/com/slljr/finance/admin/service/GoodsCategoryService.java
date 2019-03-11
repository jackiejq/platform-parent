package com.slljr.finance.admin.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.pojo.vo.GoodsCategoryVo2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.slljr.finance.admin.mapper.GoodsCategoryMapper;
import com.slljr.finance.common.pojo.model.GoodsCategory;

@Service
public class GoodsCategoryService {
    @Resource
    GoodsCategoryMapper goodsCategoryMapper;

    public PageInfo<GoodsCategoryVo2> findAll(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsCategoryVo2> list = goodsCategoryMapper.findOrderByParentIdAsc();
        return new PageInfo<>(list);
    }

    public List<GoodsCategory> findParentCategory(){
        return goodsCategoryMapper.findByParentIdIsNull();
    }

    public List<GoodsCategoryVo2> findChildCategory(Integer parentId){
        return goodsCategoryMapper.findByParentId(parentId);
    }

    public int addCategory(String categoryName, Integer parentId){
        GoodsCategory category = new GoodsCategory();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(0);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return goodsCategoryMapper.insertSelective(category);
    }


    public GoodsCategoryVo2 findCategoryById(Integer id){
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    public int updateCategory(Integer id, String categoryName, Integer parentId){
        GoodsCategory category = findCategoryById(id);
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setUpdateTime(new Date());
        return goodsCategoryMapper.updateByPrimaryKeySelective(category);
    }
    
    /**
	 * 查询所有
	 */
	public List<GoodsCategoryVo2> listGoodsCategory() {
        return goodsCategoryMapper.listGoodsCategory();
	}
    
}
