package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.ForumSection;

public interface ForumSectionMapper {
    int insert(ForumSection record);

    int insertSelective(ForumSection record);
}