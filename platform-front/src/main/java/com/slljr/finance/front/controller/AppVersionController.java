package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.AppVersion;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * @description: APP更新控制器
 * @author: uncle.quentin.
 * @date: 2019/1/28
 * @time: 16:55
 */
@RestController
@RequestMapping(value="/appVersion")
@Api(tags= {"APP更新API"})
public class AppVersionController extends BaseController{

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping(value = "/getNewVersion", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "查询最新APP版本信息", notes = "{\"type\":\"1:Android，2：IOS\"}")
    @NoLoginAnnotation
    public ModelMap getNewVersion(@RequestBody String type) throws InterfaceException {
        //校验非空
        validParam(type);
        JSONObject paramObj = JsonUtil.strToJson(type);
        int typeParam = (null == paramObj.get("type") ? null : paramObj.getInteger("type"));

        //最新版本信息
        AppVersion appVersion = appVersionService.getNewVersion(typeParam);

        return WriteJson.successData(appVersion);
    }
}
