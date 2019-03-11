package com.slljr.finance.front.controller;

import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserSignLogDTO;
import com.slljr.finance.common.pojo.model.UserSignLog;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.UserAccountService;
import com.slljr.finance.front.service.UserSignLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/1/7.
 * @time: 16:32.
 */
@RestController
@RequestMapping(value = "/userSign")
@Api(tags = {"用户签到API"})
public class UserSignLogController extends BaseController {

    /**
     * 用户账户信息服务接口
     */
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 用户签到服务接口
     */
    @Autowired
    private UserSignLogService userSignLogService;


    /**
     * 签到
     *
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/7 16:43
     * @version 1.0
     */
    @RequestMapping(value = "/userSign", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户签到")
    @ResponseBody
    @NoLoginAnnotation
    public ModelMap userSign(HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        //校验参数
        validParam(user, user.getId());
        //打卡
        Map<String, Object> result = userAccountService.updateUserAccountIntegralBalance(Constant.OPERATETYPE_SIGN, user, null);
        if (null != result) {
            return WriteJson.successData(result);
        } else {
            return WriteJson.errorWebMsg("签到失败");
        }
    }

    /**
     * 获取用户签到信息
     *
     * @param userSignLogDTO
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/7 17:08
     * @version 1.0
     */
    @RequestMapping(value = "/getSignInfo", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "获取用户签到信息", notes = "{\"startTime\":\"2018-12-31\",\"endTime\":\"2019-01-31\"}")
    @ResponseBody
    @NoLoginAnnotation
    public ModelMap getSignInfo(@RequestBody UserSignLogDTO userSignLogDTO, HttpServletRequest request) throws InterfaceException {
        //校验参数
        validParam(userSignLogDTO, userSignLogDTO.getUid());
        //打卡查询
        List<UserSignLog> userSignLogs = userSignLogService.selectUserSignedLogByCondition(userSignLogDTO);
        //返回数据
        return WriteJson.successData(userSignLogs);
    }

}
