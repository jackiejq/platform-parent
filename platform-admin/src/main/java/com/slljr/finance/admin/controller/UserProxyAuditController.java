package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.UserProxyAuditService;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserProxyAuditDTO;
import com.slljr.finance.common.pojo.model.UserProxyAudit;
import com.slljr.finance.common.pojo.vo.UserProxyAuditVO;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @description: 代理用户审核控制器
 * @author: uncle.quentin.
 * @date: 2019/1/15.
 * @time: 13:54.
 */
@RestController
@Api(tags = "代理用户审核API")
@RequestMapping("/userProxyAudit")
public class UserProxyAuditController extends BaseController {

    /**
     * 代理用户审核服务接口
     */
    @Autowired
    private UserProxyAuditService userProxyAuditService;

    /**
     * 查询数据
     *
     * @param userProxyAudit
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/15 14:05
     * @version 1.0
     */
    @RequestMapping(value = "/listData", method = RequestMethod.POST)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态【0、待审核 1、审核通过 -1、驳回】", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户姓名", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userPhone", value = "用户手机号", required = false, dataType = "string", paramType = "query")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "{\"pageNum\": \"页码\",\"pageSize\": \"每页数\",\"id\": 1,\"uid\": \"用户ID\",\"applyDescription\": \"申请描述\",\"auditStatus\": \"审核状态【0、待审核 1、审核通过 -1、驳回】\",\"auditRemark\": \"审核备注\",\"createTime\": \"创建时间\",\"updateTime\": \"修改时间\",\n" +
            "    \"userName\":\"用户信息\",\"userPhone\":\"用户手机号\"}")})
    public ModelMap listProxyUserAudit(@ApiIgnore UserProxyAuditDTO userProxyAudit) {
        PageInfo<UserProxyAuditVO> userProxyAuditList = userProxyAuditService.listProxyUserAudit(userProxyAudit);
        return WriteJson.successPage(userProxyAuditList);
    }

    /**
     * 添加代理用户待审核数据
     *
     * @param userProxyAudit
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/15 14:36
     * @version 1.0
     */
    @RequestMapping(value = "/addData", method = RequestMethod.POST)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "applyDescription", value = "申请描述", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "spreadProvince", value = "主要推广省份", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "spreadCity", value = "主要推广城市", required = true, dataType = "string", paramType = "query")
    })
    public ModelMap addProxyUserAudit(@ApiIgnore UserProxyAudit userProxyAudit) throws InterfaceException {
        //校验参数有效性
        validParamNotNull(userProxyAudit, userProxyAudit.getApplyDescription(), userProxyAudit.getSpreadProvince(), userProxyAudit.getSpreadCity());
        //新增
        int successCount = userProxyAuditService.applyProxyUser(userProxyAudit);
        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg(MsgEnum.FAIL);
        }
    }

    /**
     * 审核
     *
     * @param id          数据ID
     * @param auditStatus 审核状态
     * @param auditRemark 审核说明备注
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/15 15:18
     * @version 1.0
     */
    @RequestMapping(value = "/dealData", method = RequestMethod.POST)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "auditStatus", value = "审核状态：0、待审核 1、审核通过 -1、驳回", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "auditRemark", value = "审核说明备注", required = true, dataType = "string", paramType = "query")
    })
    public ModelMap dealProxyUserAudit(int id, int auditStatus, String auditRemark) throws InterfaceException {
        //校验参数有效性
        validParamNotNull(id, auditStatus);
        //转换状态
        UserProxyAudit.AuditStatusEnum auditStatusEnum = UserProxyAudit.AuditStatusEnum.val(auditStatus);

        if (null == auditStatusEnum) {
            return WriteJson.errorWebMsg(MsgEnum.AUDIT_STATUS_ERROR);
        }
        //审核
        int successCount = userProxyAuditService.dealAudit(id, auditStatusEnum, auditRemark);

        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg(MsgEnum.FAIL);
        }
    }

}
