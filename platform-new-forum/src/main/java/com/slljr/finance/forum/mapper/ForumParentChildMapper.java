package com.slljr.finance.forum.mapper;

import com.slljr.finance.common.pojo.model.ForumParentChild;

public interface ForumParentChildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumParentChild record);

    int insertSelective(ForumParentChild record);

    ForumParentChild selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumParentChild record);

    int updateByPrimaryKey(ForumParentChild record);
}