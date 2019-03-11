package com.slljr.finance.admin.mapper;

import java.util.List;

import com.slljr.finance.common.pojo.model.AdvConfig;

import io.lettuce.core.dynamic.annotation.Param;
/**
 * 1、轮播图的数据访问层接口
 * @author XueYi
 * 2018年12月7日 下午3:47:54
 */
public interface AdvConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdvConfig record);

    int insertSelective(AdvConfig record);

    AdvConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdvConfig record);

    int updateByPrimaryKey(AdvConfig record);
    
    //分页查轮播图数据
  /*  List<AdvConfig> listAdvConfig(PageAdvConfigDto advConfigDto);*/
    
    //打开轮播图显示
    void openDisplay(@Param("id") Integer id);
    
    //关闭轮播图显示
    void offDisplay(@Param("id") Integer id);
    
    //分页查轮播图数据
	List<AdvConfig> listAdvConfig(AdvConfig advConfig);
}