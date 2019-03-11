package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.BankCardTypeEnum;
import com.slljr.finance.common.enums.PaymentChannelTypeEnum;
import com.slljr.finance.common.enums.TradeOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.UserTradeOrderDTO;
import com.slljr.finance.common.pojo.model.UserTradeOrder;
import com.slljr.finance.common.pojo.vo.*;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.PaymentService;
import com.slljr.finance.front.service.UserBankCardSerivce;
import com.slljr.finance.front.service.UserTradeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @description: 用户订单控制器
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 17:29.
 */
@RestController
@RequestMapping(value = "/userTradeOrder")
@Api(tags={"用户交易订单API"})
public class UserTradeOrderController extends BaseController{

    @Autowired
    private UserTradeOrderService userTradeOrderService;
    @Autowired
    private UserBankCardSerivce userBankCardSerivce;

    @Autowired
    PaymentService paymentService;

    /**
     * 查询用户下的代偿或收款记录
     *
     * @author uncle.quentin
     * @date   2018/12/12 17:37
     * @param   userTradeOrder
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value = "/getUserTradeOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "查询用户下的代偿或收款记录" ,notes="{\"type\":\"交易类型[1代偿,2收款]\",\"paymentCardId\":\"支付卡ID\"," +
            "\"startTime\":\"开始时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"endTime\":\"結束时间【格式：yyyy-MM-dd HH:mm:ss:】\"}")
    @ResponseBody
    public ModelMap getUserTradeOrder(@RequestBody UserTradeOrderDTO userTradeOrder,HttpServletRequest request) throws InterfaceException {
        //验证数据有效性
        validParam(userTradeOrder);
        validParam(userTradeOrder.getType());
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        //查询当前用户下
        userTradeOrder.setUid(user.getId());

        //查询
        List<UserTradeOrderVO> userTradeOrderList = userTradeOrderService.selectByUserIdAndType(userTradeOrder);
        double amountTotal = 0 ;
		int compensatoryTotal;
		compensatoryTotal = userTradeOrderList.size();
        //计算总额
		for (int i = 0; i < userTradeOrderList.size(); i++) {
			double paymentAmount = userTradeOrderList.get(i).getPaymentAmount();
            amountTotal = MathUtils.add(amountTotal, paymentAmount, 2);
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("amountTotal", new BigDecimal(amountTotal).setScale(2, BigDecimal.ROUND_UP).toString());
		resultObj.put("compensatoryTotal", compensatoryTotal);
		resultObj.put("data", userTradeOrderList);
				
		return WriteJson.successData(resultObj);
		//return WriteJson.appJsonMsg(true, compensatoryTotal, String.valueOf(amountTotal), userTradeOrderList, null);       
    }


    /**
     * APP首页代偿/收款信息
     * @param request
     * @return
     * @throws InterfaceException
     */
    @RequestMapping(value = "/getIndexBankCardAmountInfo", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "APP首页代偿/收款信息")
	public ModelMap getIndexBankCardAmountInfo(HttpServletRequest request) throws InterfaceException{
        UserBasicVO user = getLoginUser(request);
        JSONObject data = new JSONObject();
        /**首页代偿信息**/
        JSONObject daichang = new JSONObject();
        {
            //本月代偿笔数
            Integer thisMonthCount = userTradeOrderService.findCompensatoryRatiot(user.getId());
            //本月代偿总额
            Double thisMonthTotalAmount = userTradeOrderService.findCompensatoryCount(user.getId());
            //本月剩余代偿总额
            Double thisMonthSurplusAmount = userTradeOrderService.findSurplusAmount(user.getId());
            //历史累计代偿总额
            Double historyTotalAmount = userTradeOrderService.findTotalCompensationAmount(user.getId());

            daichang.put("display", true);
            daichang.put("thisMonthCount", thisMonthCount);
            daichang.put("thisMonthTotalAmount", thisMonthTotalAmount);
            daichang.put("thisMonthSurplusAmount", thisMonthSurplusAmount);
            daichang.put("historyTotalAmount", historyTotalAmount);
        }

        /**首页收款信息**/
        JSONObject shoukuang = new JSONObject();
        {
            //本月收款
            Double thisMonthTotalAmount = userBankCardSerivce.findBankCardReceivable(user.getId());
            //累计收款
            Double historyTotalAmount = userBankCardSerivce.findBankCardAccumulatedReceipts(user.getId());
            //银行卡列表
            List<UserBankCardVo> depositCardList = userBankCardSerivce.findByUidAndType(user.getId(), BankCardTypeEnum.DEPOSIT_CARD.getKey());

            shoukuang.put("display", true);
            shoukuang.put("depositCard", depositCardList.isEmpty()? null : depositCardList.get(0));
            shoukuang.put("thisMonthTotalAmount", thisMonthTotalAmount);
            shoukuang.put("historyTotalAmount", historyTotalAmount);
        }
        data.put("daichang", daichang);
        data.put("shoukuang", shoukuang);
	    return WriteJson.successData(data);
    }

    /**
     * 代偿明细【1、列表明细 2、汇总：代偿总额，笔数，是否显示终止代偿】
     *
     * @param param
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/1/24 11:27
     * @version 1.0
     */
    @RequestMapping(value = "/listCompensation", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "查询代偿明细", notes = " {\"tradeId\":\"交易订单id\"}")
    @ResponseBody
    public ModelMap listCompensation(@RequestBody String param) throws InterfaceException {
        //解析参数
        JSONObject json = JSON.parseObject(param);
        Integer tradeId = json.getInteger("tradeId");
        //参数校验
        validParam(tradeId);
        //汇总
        CompensatoryVO compensatoryVO = userTradeOrderService.totalCompensatory(tradeId);
        //代偿明细
        List<Map<String, Object>> totalList = userTradeOrderService.queryCompensatoryList(tradeId, compensatoryVO);

        //返回
        JSONObject resultObj = new JSONObject();
        resultObj.put("detail", totalList);
        resultObj.put("summary", compensatoryVO);

        return WriteJson.successData(resultObj);
    }

    /**
     * 终止代偿(只有全部未执行才能终止)
     * @param param
     * @return
     */
    @RequestMapping(value = "/stopOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "终止代偿", notes = "{\"tradeId\":\"交易订单id\"}")
    public ModelMap stopOrder(@RequestBody String param, HttpServletRequest request) throws InterfaceException{
        JSONObject json = JSON.parseObject(param);
        Integer tradeId = json.getInteger("tradeId");
        validParam(tradeId);

        UserBasicVO user = getLoginUser(request);
        if (userTradeOrderService.stopOrder(user, tradeId)){
            return WriteJson.success();
        }
        return WriteJson.errorWebMsg("终止代偿失败，计划未执行才可终止！");
    }

    @RequestMapping(value = "/restartOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "重新设置代偿", notes = "{\"tradeId\":\"交易订单id\"}")
    public ModelMap restartOrder(@RequestBody String param, HttpServletRequest request) throws InterfaceException{
        JSONObject json = JSON.parseObject(param);
        Integer tradeId = json.getInteger("tradeId");
        validParam(tradeId);

        UserBasicVO user = getLoginUser(request);

        //查询旧订单信息
        UserTradeOrder userTradeOrder = userTradeOrderService.selectUserTradeOrderById(tradeId);
        //校验并更新旧订单状态
        updateTradeOrderStatus(userTradeOrder);
        //剩余代偿金额
        Double surplusAmount = userTradeOrderService.getSurplusOrderAmount(tradeId);

        userTradeOrder.setId(null);
        userTradeOrder.setPaymentAmount(surplusAmount);
        userTradeOrder.setBillAmount(surplusAmount);
        userTradeOrder.setReceiveAmount(0D);
        userTradeOrder.setServiceCharge(0D);
        userTradeOrder.setStatus(TradeOrderStatusEnum.RESET.getKey());

        PaymentResultVO resVO = paymentService.bindCard(user, userTradeOrder, PaymentChannelTypeEnum.DAIHUAN);

        if (null != resVO){
            JSONObject object = new JSONObject();
            object.put("tradeId", resVO.getTradeOrderId());
            return WriteJson.successData(object);
        }

        return WriteJson.errorWebMsg("重新设置代偿失败！");
    }

    /**
     * 校验并更新旧订单状态
     *
     * @author uncle.quentin
     * @date   2019/2/22 14:07
     * @param   userTradeOrder
     * @return int
     * @version 1.0
     */
    private int updateTradeOrderStatus(UserTradeOrder userTradeOrder) throws InterfaceException {
        if (null == userTradeOrder) {
            throw new InterfaceException(MsgEnum.NOTEXISTS);
        }
        //判断该订单是否已重置过
        if (null != userTradeOrder && TradeOrderStatusEnum.RESET.getKey() == userTradeOrder.getStatus()) {
            throw new InterfaceException(MsgEnum.CAN_NOT_RESET);
        }

        //更新状态为已重置
        return userTradeOrderService.updateOrderStatus(userTradeOrder.getId(), TradeOrderStatusEnum.RESET);
    }

}
