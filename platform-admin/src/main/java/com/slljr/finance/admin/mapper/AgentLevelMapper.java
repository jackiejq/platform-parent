package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.AgentLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgentLevel record);

    int insertSelective(AgentLevel record);

    AgentLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgentLevel record);

    int updateByPrimaryKey(AgentLevel record);

    /**
     * 条件查询用户等级
     *
     * @param record
     * @return java.util.List<com.slljr.finance.common.pojo.model.AgentLevel>
     * @author uncle.quentin
     * @date 2018/12/12 11:21
     * @version 1.0
     */
    List<AgentLevel> selectByCondition(AgentLevel record);

    List<AgentLevel> selectByType(@Param("type") Integer type);
}