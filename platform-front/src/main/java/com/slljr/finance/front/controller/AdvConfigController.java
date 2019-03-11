package com.slljr.finance.front.controller;
/**
 * 1、查询轮播的controller
 * @author XueYi
 * 2018年12月7日 下午6:17:17
 */

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.AdvConfig;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.IAdvConfigService;
import com.slljr.finance.front.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/advConfig")
@Api(tags= {"广告位API"})
public class AdvConfigController  extends BaseController{

    private static final Logger log = LogManager.getLogger();

	@Autowired
	private IAdvConfigService advConfigService;

	/**
	 * 系统配置服务接口
	 */
	@Autowired
	private SysConfigService sysConfigService;

	@RequestMapping(value="/listAdvConfig", method=RequestMethod.POST)
	@ApiOperation(value="查询所有广告位", httpMethod = "POST")
	@ResponseBody
    @NoLoginAnnotation
	public ModelMap listAdvConfig() {
        List<AdvConfig> listAdvConfig = advConfigService.listAdvConfig();
        return WriteJson.successData(listAdvConfig);
	}

    /**
     * 查询指定广告位信息
     *
     * @param type
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/14 10:31
     * @version 1.0
     */
    @RequestMapping(value = "/selectSpecialAdv", method = RequestMethod.POST)
    @ApiOperation(value = "查询指定广告位信息", httpMethod = "POST",notes = "{\"type:\"广告位类型[1:额度测算,2:保险合作,3:APP启动图]\", \"device\": \"(非必传)[1安卓,2苹果]\"}")
    @ResponseBody
    @NoLoginAnnotation
    public ModelMap selectSpecialAdv(@RequestBody String type) throws InterfaceException {
        //校验非空
        validParam(type);
        JSONObject paramObj = JsonUtil.strToJson(type);
        String typeParam = (null == paramObj.get("type") ? "" : paramObj.getString("type"));
        SysConfigEnum sysConfigEnum;
        switch (Integer.valueOf(typeParam)) {
            //额度测算
            case 1:
                sysConfigEnum = SysConfigEnum.APP_ADV_KEY_1;
                break;
            //保险合作
            case 2:
                sysConfigEnum = SysConfigEnum.APP_ADV_KEY_2;
                break;
            //启动图
            case 3:
                Integer device = paramObj.getInteger("device");
                if (device != null && device == 1){
                    sysConfigEnum = SysConfigEnum.APP_START_IMAGE_ANDROID;
                }else {
                    sysConfigEnum = SysConfigEnum.APP_START_IMAGE_IOS;
                }
                break;
            default:
                log.warn("无效类型：" + type);
                return WriteJson.errorWebMsg("无效类型参数:" + type);
        }

        SysConfig sysConfig = sysConfigService.selectByKey(sysConfigEnum);

        //获取数据并返回信息
        if (null != sysConfig) {
            List<AdvConfig> result = advConfigService.selectAdvByIds(sysConfig.getSysValue());
            return WriteJson.successData(result);
        } else {
            return WriteJson.errorWebMsg("数据为空");
        }
    }
}
