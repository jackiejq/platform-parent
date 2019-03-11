package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.AgentLevelService;
import com.slljr.finance.common.pojo.model.AgentLevel;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 代理等级接口控制器
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 11:31.
 */
@RestController
@RequestMapping(value = "/agentLevel")
@Api(tags = {"代理等级接口API"})
public class AgentLevelController {

    /**
     * 代理登记接口服务
     */
    @Autowired
    private AgentLevelService agentLevelService;

    /**
     * 新增代理等级
     *
     * @param agentLevel
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 11:45
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBasic", value = "userBasic", required = false, dataType = "AgentLevel", paramType = "body")
    })
    @ApiOperation(value = "新增或修改代理等级", httpMethod = "POST")
    @RequestMapping(value = "/addOrUpdateUserBasic", method = RequestMethod.POST)
    public ModelMap addAgentLevel(AgentLevel agentLevel) {
        Assert.notNull(agentLevel, "无效参数");
        Assert.notNull(agentLevel.getName(), "等级名称不能为空");
        Assert.notNull(agentLevel.getLevelCode(), "等级编码不能为空");
        Assert.notNull(agentLevel.getMinNewMember(), "最低新增有效人数不能为空");
        Assert.notNull(agentLevel.getMaxNewMember(), "最高新增有效人数不能为空");
        Assert.notNull(agentLevel.getCommissionRate(), "佣金比例不能为空");
        Assert.notNull(agentLevel.getType(), "等级类型不能为空");

        int sucessCount;
        if (null != agentLevel.getId()) {
            sucessCount = agentLevelService.updateAgentLevel(agentLevel);
        } else {
            //新增等级
            agentLevel.setStatus(AgentLevel.AgentLevelStatusEnum.NORMAL.getKey());
            sucessCount = agentLevelService.insertAgentLevel(agentLevel);
        }


        return WriteJson.successData(sucessCount);
    }

    /**
     * 删除代理等级
     *
     * @param id
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 13:33
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "等级ID", required = true, dataType = "int", paramType = "path")
    })
    @ApiOperation(value = "删除代理等级", httpMethod = "POST")
    @RequestMapping(value = "/deleteAgentLevel/{id}", method = RequestMethod.POST)
    public ModelMap deleteAgentLevel(@PathVariable int id) {
        Assert.notNull(id, "无效参数");
        int sucessCount = agentLevelService.deleteAgentLevel(id);
        return WriteJson.successData(sucessCount);
    }

    /**
     * 分页查询代理等级信息
     *
     * @author uncle.quentin
     * @date   2018/12/12 13:37
     * @param   agentLevel
     * @param   pageNum
     * @param   pageSize
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value="/selectAgentLevelByPage",method=RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查询代理等级")
    public ModelMap listAgentLevelByPage(AgentLevel agentLevel, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<AgentLevel> resultPage = agentLevelService.selectAgentLevelByPage(agentLevel, pageNum, pageSize);
        return WriteJson.successPage(resultPage);
    }

    /**
     * 根据ID查询代理等级明细
     *
     * @author uncle.quentin
     * @date   2018/12/12 13:40
     * @param   id
     * @return com.slljr.finance.common.pojo.model.AgentLevel
     * @version 1.0
     */
    @RequestMapping(value="/selectAgentLevelById",method=RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "根据ID查询代理等级明细")
    public ModelMap selectAgentLevelById(int id){
        Assert.notNull(id, "无效参数");
        AgentLevel result  = agentLevelService.selectAgentLevelById(id);
        return WriteJson.successData(result);
    }

}
