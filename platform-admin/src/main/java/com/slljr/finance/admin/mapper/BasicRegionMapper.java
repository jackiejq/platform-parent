package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.BasicRegion;

public interface BasicRegionMapper {
    int deleteByPrimaryKey(String regionId);

    int insert(BasicRegion record);

    int insertSelective(BasicRegion record);

    BasicRegion selectByPrimaryKey(String regionId);

    int updateByPrimaryKeySelective(BasicRegion record);

    int updateByPrimaryKey(BasicRegion record);
}