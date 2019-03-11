package com.slljr.finance.front.mapper;

import com.slljr.finance.common.pojo.model.AgentLevel;

public interface AgentLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgentLevel record);

    int insertSelective(AgentLevel record);

    AgentLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgentLevel record);

    int updateByPrimaryKey(AgentLevel record);
}