package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.SysConfigEnum;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.model.SysConfig;
import com.slljr.finance.common.pojo.vo.GoodsVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.config.NoLoginAnnotation;
import com.slljr.finance.front.service.GoodsService;
import com.slljr.finance.front.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 商品信息控制器
 * @author: uncle.quentin.
 * @date: 2018/12/13.
 * @time: 19:34.
 */
@RestController
@RequestMapping(value = "/goods")
@Api(tags = {"商品信息"})
public class GoodsController extends BaseController{
    /**
     * 商品服务接口
     */
    @Autowired
    private GoodsService goodsService;

    /**
     * 系统配置服务接口
     */
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 根据id进行查询商品详情
     *
     * @param id
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 20:11
     * @version 1.0
     */
    @RequestMapping(value = "/getGoods/{id}", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "根据id查询商品详情")
    @ResponseBody
    @NoLoginAnnotation
    public ModelMap getGoodsAndCategory(@ApiParam(name = "id", value = "商品ID", required = true) @PathVariable("id") Integer id) {
        Assert.notNull(id, "参数为空");
        //根据商品ID查询商品信息，返回带商品分类信息
        Goods searchParam = new Goods();
        searchParam.setId(id);
        List<GoodsVO> goodsVOS = goodsService.selectGoodsWithCategory(searchParam);
        GoodsVO result = new GoodsVO();
        if (null != goodsVOS && !goodsVOS.isEmpty()) {
            result = goodsVOS.get(0);
        }
        return WriteJson.successData(result);
    }

    /**
     * 获取特定的商品信息
     *
     * @author uncle.quentin
     * @date   2018/12/14 9:11
     * @param
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value = "/getSpecificGoods", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "查询指定的商品",notes = "{\"type\":\"1:首页商品，2：任务页商品\"}")
    @ResponseBody
    @NoLoginAnnotation
    public ModelMap getSpecificGoods(@RequestBody String type){
        JSONObject paramObj = JsonUtil.strToJson(type);
        String typeParam = (null == paramObj.get("type") ? "" : paramObj.getString("type"));
        Assert.notNull(type, "类型不能为空");

        List<GoodsVO> goods = new ArrayList<>();
        //获取系统配置的指定商品ID
        SysConfig sysConfigs;
        if (1 == Integer.valueOf(typeParam)) {
            sysConfigs = sysConfigService.selectSysConfigByKey(SysConfigEnum.APP_HOME_GOODS_KEY);
        } else {
            sysConfigs = sysConfigService.selectSysConfigByKey(SysConfigEnum.APP_TASK_PAGE_GOODS_KEY);
        }

        if (null != sysConfigs) {
            String[] strs = sysConfigs.getSysValue().split(",");

            List<Integer> ids = new ArrayList<>();
            for (String str : strs) {
                if (StringUtils.isNotEmpty(str)) {
                    ids.add(Integer.valueOf(str));
                }
            }
            //根据配置的ID集合查询商品详细信息
            goods  = goodsService.selectGoodsByIds(ids);
        }

        return WriteJson.successData(goods);
    }

}
