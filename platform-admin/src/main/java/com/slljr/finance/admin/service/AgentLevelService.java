package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.AgentLevelMapper;
import com.slljr.finance.common.pojo.model.AgentLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 代理等级服务接口
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 11:14.
 */
@Service
public class AgentLevelService {

    @Autowired
    private AgentLevelMapper agentLevelMapper;

    /**
     * 新增代理等级
     *
     * @param agentLevel
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 11:16
     * @version 1.0
     */
    public int insertAgentLevel(AgentLevel agentLevel) {
        return agentLevelMapper.insertSelective(agentLevel);
    }

    /**
     * 修改代理等级
     *
     * @param agentLevel
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 11:29
     * @version 1.0
     */
    public int updateAgentLevel(AgentLevel agentLevel) {
        return agentLevelMapper.updateByPrimaryKeySelective(agentLevel);
    }

    /**
     * 删除代理等级
     *
     * @param id
     * @return int
     * @author uncle.quentin
     * @date 2018/12/12 11:30
     * @version 1.0
     */
    public int deleteAgentLevel(int id) {
        return agentLevelMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID查询代理等级
     *
     * @param id
     * @return com.slljr.finance.common.pojo.model.AgentLevel
     * @author uncle.quentin
     * @date 2018/12/12 11:17
     * @version 1.0
     */
    public AgentLevel selectAgentLevelById(int id) {
        return agentLevelMapper.selectByPrimaryKey(id);
    }

    /**
     * 条件查询代理等级
     *
     * @param agentLevel
     * @return java.util.List<com.slljr.finance.common.pojo.model.AgentLevel>
     * @author uncle.quentin
     * @date 2018/12/12 11:22
     * @version 1.0
     */
    public List<AgentLevel> selectAgentLevelByCondition(AgentLevel agentLevel) {
        return agentLevelMapper.selectByCondition(agentLevel);
    }

    /**
     * 查询全部根据ID排序
     *
     * @author uncle.quentin
     * @date   2019/2/13 16:56
     * @param
     * @return java.util.List<com.slljr.finance.common.pojo.model.AgentLevel>
     * @version 1.0
     */
    public List<AgentLevel> selectAgentLevelByType(Integer type){
        return agentLevelMapper.selectByType(type);
    }

    /**
     * 分页查询代理等级
     *
     * @param agentLevel 查询参数对象
     * @param pageNum    开始页
     * @param pageSize   每页数
     * @return com.github.pagehelper.PageInfo<com.slljr.finance.common.pojo.model.AgentLevel>
     * @author uncle.quentin
     * @date 2018/12/12 11:27
     * @version 1.0
     */
    public PageInfo<AgentLevel> selectAgentLevelByPage(AgentLevel agentLevel, int pageNum, int pageSize) {
        PageInfo<AgentLevel> result = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> agentLevelMapper.selectByCondition(agentLevel));
        return result;
    }


}
