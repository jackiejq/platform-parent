package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.enums.WithdrewAuditStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.WithdrawDetail;
import com.slljr.finance.common.pojo.vo.UserBalanceDetailVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.pojo.vo.WithdrawDetailVO;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.UserBalanceDetailService;
import com.slljr.finance.front.service.WithdrawDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/balance/detail")
@Api(tags = "用户积分/现金详情查询")
public class UserBalanceDetailController extends BaseController{
    @Autowired
    UserBalanceDetailService userBalanceDetailService;

    @Autowired
    private WithdrawDetailService withdrawDetailService;


    /**
     * 查询TOP20近一月奖励金数据
     *
     * @author uncle.quentin
     * @date   2019/1/15 19:34
     * @param
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value = "/listTopBalanceDetail", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "查询TOP20近一月奖励金数据")
    public ModelMap listTopBalanceDetail(){
        List<UserBalanceDetailVO> resultList = userBalanceDetailService.selectBalanceTopNearlyOneMonth();
        return WriteJson.successData(resultList);
    }


    @RequestMapping(value = "/applyWithdraw", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "申请提现", notes = "{\"amount\": 100}")
    public ModelMap applyWithdraw(@RequestBody String param, HttpServletRequest request, HttpServletResponse response) throws InterfaceException{
        JSONObject json = JSON.parseObject(param);
        Double amount = json.getDouble("amount");
        Integer cardId = json.getInteger("cardId");
        validParam(amount, cardId);

        if (amount <= 0) {
            return WriteJson.success("提现金额必须大于0");
        }

        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        WithdrawDetail withdrawDetail = new WithdrawDetail();
        withdrawDetail.setAmount(amount);
        withdrawDetail.setCardId(cardId);
        withdrawDetail.setUid(user.getId());
        withdrawDetail.setStatus(WithdrewAuditStatusEnum.WAITING.getKey());

        int successCount = withdrawDetailService.addWithdrawDetail(withdrawDetail);

        if (successCount > 0){
            return WriteJson.success("提现处理中,请耐心等待!");
        }

        return WriteJson.errorWebMsg("提现失败,请咨询客服!");
    }

    @RequestMapping(value = "/listWithdrawDetail", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(httpMethod = "POST", value = "提现记录",notes="{\"pageNum\":\"当前页\",\"pageSize\":\"每页显示的条数\",startTime\":\"开始时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"endTime\":\"結束时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"status\":\"0:进行中；1：成功；2：失败\"}")
    public ModelMap applyWithdraw(@RequestBody WithdrawDetail withdrawDetail, HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        if (null == withdrawDetail) {
            withdrawDetail = new WithdrawDetail();
        }
        withdrawDetail.setUid(user.getId());
        PageInfo<WithdrawDetailVO> pageInfo = withdrawDetailService.getWithdrawDetailByUid(withdrawDetail);

        return WriteJson.successData(pageInfo);
    }
}
