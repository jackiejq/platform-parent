package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.PaymentChannelService;
import com.slljr.finance.common.pojo.model.PaymentChannel;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "支付通道接口")
public class PaymentChannelController {

	
	
	@Autowired
	private PaymentChannelService paymentChannelService;
	/**
	 * 分页查看支付通道列表
	 * @return
	 */
	@RequestMapping(value="/findPaymentChannelList",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "分页查看支付通道列表")		
	public ModelMap findPaymentChannelList(PaymentChannel paymentChannel,@RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){		
		 PageInfo<PaymentChannel> pageInfo = paymentChannelService.findPaymentChannelList(paymentChannel,pageNum, pageSize);
		 return WriteJson.successPage(pageInfo);		 		
	}

	@RequestMapping(value="/findByType",method=RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "根据类型查看支付通道列表")
	public ModelMap findByType(Integer channelType){
		Assert.notNull(channelType, "请选择通道类型");
		List<PaymentChannel> list = paymentChannelService.findByType(channelType);
		return WriteJson.successData(list);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/deletePaymentChannel", method = RequestMethod.POST)
	@ApiOperation(value = "删除支付通道数据", httpMethod = "POST")
	public ModelMap deletePaymentChannel(Integer id) {
		Assert.notNull(id, "删除项的id为空");		
		try {
			paymentChannelService.deletePaymentChannel(id);
		} catch (Exception e) {
			return WriteJson.errorWebMsg("删除失败");
		}
		return WriteJson.success();
	}
	
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping(value ="/updatePaymentChannel", method = RequestMethod.POST)
	@ApiOperation(value = "修改支付通道数据", httpMethod = "POST")
	public ModelMap updatePaymentChannel(PaymentChannel paymentChannel) {
		Assert.notNull(paymentChannel.getId(), "修改项的id为空");		
		try {
			Integer num = paymentChannelService.updatePaymentChannel(paymentChannel);
			return WriteJson.success();
		} catch (Exception e) {
			return WriteJson.errorWebMsg("修改支付通道信息失败");
		}
		
	}
	
	/**
	 * 增加
	 * @return
	 */
	@RequestMapping(value ="/addPaymentChannel", method = RequestMethod.POST)
	@ApiOperation(value = "增加支付通道数据", httpMethod = "POST")
	public ModelMap addPaymentChannel(PaymentChannel paymentChannel) {
		Assert.notNull(paymentChannel.getCode(), "通道代码不能为空");
		Assert.notNull(paymentChannel.getName(), "通道名称不能为空");
		Assert.notNull(paymentChannel.getPaymentRate(), "刷卡费率/原价不能为空");
		Assert.notNull(paymentChannel.getUserPaymentRate(), "刷卡费率/售价不能为空");
		Assert.notNull(paymentChannel.getWithdrawCharge(), "提现每笔手续费/原价不能为空");
		Assert.notNull(paymentChannel.getUserWithdrawCharge(), "提现每笔手续费/售价不能为空");
		Assert.notNull(paymentChannel.getMerchantNo(), "商户号不能为空");
		Assert.notNull(paymentChannel.getEncryptKey(), "加密key不能为空");
		Assert.notNull(paymentChannel.getHttpUrl(), "接口主机地址不能为空");
		Assert.notNull(paymentChannel.getOtherParams(), "其他非公共参数不能为空");
		Assert.notNull(paymentChannel.getRunTimeStart(), "营业开始时间不能为空");
		Assert.notNull(paymentChannel.getRunTimeEnd(), "营业结束时间不能为空");
		Assert.notNull(paymentChannel.getType(), "类型不能为空");
		try {			
			paymentChannelService.addPaymentChannel(paymentChannel);
			return WriteJson.success();
		} catch (Exception e) {
			e.printStackTrace();
			return WriteJson.errorWebMsg("添加失败");
		}					
	}
	
	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/findPaymentChannelDetails",method=RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "查找单条记录详情")
	public ModelMap findPaymentChannelDetails(Integer id) {
		Assert.notNull(id, "id不能为空");		
		PaymentChannel paymentChannel = paymentChannelService.findPaymentChannelDetails(id);
		if(paymentChannel == null) {
			return WriteJson.errorWebMsg("查询无记录");
		}
		return WriteJson.successData(paymentChannel);	
	}	
	
}
