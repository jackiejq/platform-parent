package com.slljr.finance.front.controller;

import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserProxyAudit;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.UserProxyAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 代理用户审核控制器
 * @author: uncle.quentin.
 * @date: 2019/1/15.
 * @time: 13:54.
 */
@RestController
@Api(tags = "代理用户申请API")
@RequestMapping("/userProxyAudit")
public class UserProxyAuditController extends BaseController {

    /**
     * 代理用户审核服务接口
     */
    @Autowired
    private UserProxyAuditService userProxyAuditService;

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
    @ApiOperation(httpMethod = "POST", value = "添加代理用户待审核数据", notes = "{\"applyDescription\":\"申请描述\",\"spreadProvince\":\"主要推广省份\",\"spreadCity\":\"主要推广城市\"}")
    @ResponseBody
    public ModelMap addProxyUserAudit(@RequestBody UserProxyAudit userProxyAudit, HttpServletRequest request) throws InterfaceException {
        //校验参数有效性
        validParam(userProxyAudit, userProxyAudit.getApplyDescription(), userProxyAudit.getSpreadProvince(), userProxyAudit.getSpreadCity());
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        userProxyAudit.setUid(user.getId());

        //查询当前用户是否存在待审核数据
        List<UserProxyAudit> list = userProxyAuditService.selectCheckingOrPassByStatus(user.getId());
        if (null == list || list.isEmpty()) {
            //新增
            int successCount = userProxyAuditService.applyProxyUser(userProxyAudit);
            if (successCount > 0) {
                return WriteJson.success();
            } else {
                return WriteJson.errorWebMsg(MsgEnum.FAIL);
            }
        } else {
            throw new InterfaceException(MsgEnum.EXIST_CHECKING);
        }

    }

}
