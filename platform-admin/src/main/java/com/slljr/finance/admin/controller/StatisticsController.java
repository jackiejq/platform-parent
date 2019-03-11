package com.slljr.finance.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.admin.service.HomePageStatisticsService;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.vo.StatisticsResultVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 首页报表控制器
 * @author: uncle.quentin.
 * @date: 2019/1/22.
 * @time: 15:04.
 */
@RestController
@RequestMapping(value = "/homePageStatistics")
@Api(tags = {"首页报表API"})
public class StatisticsController extends BaseController {

    @Autowired
    private HomePageStatisticsService homePageStatisticsService;

    /**
     * 处理echarts返回数据
     *
     * @param statisticsResultList
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/22 15:41
     * @version 1.0
     */
    private ModelMap commonDataDeal(List<StatisticsResultVO> statisticsResultList) {
        if (null != statisticsResultList && !statisticsResultList.isEmpty()) {
            List<String> xAxisData = new ArrayList<>();
            List<String> yAxisData = new ArrayList<>();
            //x坐标和y坐标数据提取处理
            for (StatisticsResultVO statisticsResult : statisticsResultList) {
                xAxisData.add(statisticsResult.getStatisticsKey());
                yAxisData.add(statisticsResult.getStatisticsValue());
            }

            //返回数据
            JSONObject resultObj = new JSONObject();
            resultObj.put("xAxisData", xAxisData);
            resultObj.put("yAxisData", yAxisData);

            return WriteJson.successData(resultObj);
        }
        return null;
    }

    /**
     * 首页报表统计
     *
     * @param statisticType 统计类型【1：交易额；2：公司利润；3：新增会员数】
     * @param dataType      数据类型【1：按日；2：按月；】
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/22 16:09
     * @version 1.0
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "statisticType", value = "统计类型【1：交易额；2：公司利润；3：新增会员数】", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "dataType", value = "数据类型【1：按日；2：按月；】", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getHomePageStatistics", method = RequestMethod.POST)
    @ApiOperation(value = "首页统计", httpMethod = "POST")
    public ModelMap getHomePageStatistics(int statisticType, int dataType) throws InterfaceException {
        //校验非空
        validParamNotNull(statisticType, dataType);

        //返回数据
        List<StatisticsResultVO> statisticsResultList = new ArrayList<>();

        //根据参数查询不同接口
        switch (statisticType) {
            //交易额
            case 1:
                if (dataType == 1) {
                    statisticsResultList = homePageStatisticsService.getNearly30DaysPayAmount();
                } else if (dataType == 2) {
                    statisticsResultList = homePageStatisticsService.getNearly12MonthsPayAmount();
                }
                break;
            //公司利润
            case 2:
                if (dataType == 1) {
                    statisticsResultList = homePageStatisticsService.getNearly30DaysProfit();
                } else if (dataType == 2) {
                    statisticsResultList = homePageStatisticsService.getNearly12MonthsProfit();
                }
                break;
            //新增会员数
            case 3:
                if (dataType == 1) {
                    statisticsResultList = homePageStatisticsService.getNearly30DaysNewUser();
                } else if (dataType == 2) {
                    statisticsResultList = homePageStatisticsService.getNearly12MonthsNewUser();
                }
                break;
            default:
                break;
        }
        //处理封装并返回数据
        return commonDataDeal(statisticsResultList);
    }

}
