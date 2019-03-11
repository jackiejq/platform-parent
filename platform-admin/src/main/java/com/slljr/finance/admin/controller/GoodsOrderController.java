package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.IGoodsOrderService;
import com.slljr.finance.admin.service.UserAccountService;
import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.vo.GoodsOrderVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 1、商品订单操作的controller
 *
 * @author XueYi
 * 2018年12月10日 下午8:06:45
 */
@RestController
@Api(tags = {"后台商品订单信息操作的API"})
@RequestMapping(value = "/goodsOrder")
@CrossOrigin
public class GoodsOrderController {

    private static final Logger log = LogManager.getLogger(GoodsOrderController.class);

    @Autowired
    private IGoodsOrderService goodsOrderService;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * 添加商品订单数据
     *
     * @param goodsOrder
     * @return
     */
    @RequestMapping(value = "/addGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "添加商品订单信息")
    public ModelMap addGoodsOrder(GoodsOrder goodsOrder) {
        Assert.notNull(goodsOrder.getUid(), "用户id不能为空");
        Assert.notNull(goodsOrder.getGoodsId(), "商品id不能为空");
        Assert.notNull(goodsOrder.getGoodsCount(), "商品数量不能为空");
        Assert.notNull(goodsOrder.getName(), "收货人不能为空");
        Assert.notNull(goodsOrder.getAddress(), "收货地址不能为空");
        Assert.notNull(goodsOrder.getPhone(), "收货电话不能为空");

        int index = goodsOrderService.addGoodsOrder(goodsOrder);
        if (index > 0) {
            return WriteJson.success("添加订单成功");
        }
        return WriteJson.errorWebMsg("添加订单失败");
    }

    /**
     * 根据id查询订单详细数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getGoodsOrderDetailById/{id}", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "根据id查询商品订单详情")
    public ModelMap getGoodsOrderDetailById(@ApiParam(name = "id", value = "商品订单id", required = true) @PathVariable("id") Integer id) {
        GoodsOrder goodsOrder = goodsOrderService.getGoodsOrderDetailById(id);
        if (goodsOrder == null) {
            return WriteJson.errorWebMsg("无此商品订单");
        }
        if (goodsOrder.getIsStatus() == -1) {
            return WriteJson.errorWebMsg("商品订单已被删除，查询失败");
        }
        return WriteJson.successData(goodsOrder);
    }

    /**
     * 删除订单，如果商品状态【待发货0】则将金币积分返还到用户账号
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteGoodsOrder/{id}/{optUid}", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "根据id删除商品订单信息")
    @Transactional(rollbackFor = Exception.class)
    public ModelMap deleteGoodsOrder(@ApiParam(name = "id", value = "订单编号", required = true) @PathVariable("id") Integer id,
                                     @ApiParam(name = "optUid", value = "操作员编号", required = true) @PathVariable("optUid") Integer optUid) {
        GoodsOrder order = goodsOrderService.getGoodsOrder(id);
        if (order.getIsStatus() == -1) {
            return WriteJson.errorWebMsg("删除失败,订单已被删除");
        }
        //如果订单为0待发货，删除该订单则将总金币及总积分返还到用户账户
        if (order.getStatus() == 0) {
            returnBalanceToUser(order);
        }
        int index = goodsOrderService.deleteGoodsOrder(id, optUid);
        if (index > 0) {
            return WriteJson.success("删除订单成功");
        }
        return WriteJson.errorWebMsg("删除订单失败");
    }

    /**
     * 发货后修改商品数据
     *
     * @param goodsOrder
     * @return
     */
    @RequestMapping(value = "/updateDeliverInfoById/{id}/{optUid}", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "发货后修改商品订单信息")
    public ModelMap updateDeliverInfoById(@PathVariable("id") Integer id, String expressNo, String expressName, @PathVariable("optUid") Integer optUid) {
        Assert.notNull(id, "订单编号不能为空");
        Assert.notNull(optUid, "操作人员id不能为空");
        Assert.notNull(expressName, "快递名称不能为空");
        Assert.notNull(expressNo, "快递单号不能为空");
        int index = goodsOrderService.updateDeliverInfoById(id, new Date(), expressNo, expressName, optUid, 1, new Date());
        if (index > 0) {
            return WriteJson.success("修改商品订单成功");
        }
        return WriteJson.errorWebMsg("修改商品订单失败");
    }

    /**
     * 修改商品数据
     *
     * @param goodsOrder
     * @return
     */
    @RequestMapping(value = "/updateGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "修改商品订单信息")
    public ModelMap updateGoodsOrder(GoodsOrder goodsOrder) {
        Assert.notNull(goodsOrder.getId(), "订单id不能为空");
        Assert.notNull(goodsOrder.getName(), "收货人不能为空");
        Assert.notNull(goodsOrder.getAddress(), "收货地址不能为空");
        Assert.notNull(goodsOrder.getPhone(), "收货电话不能为空");
        GoodsOrder order = goodsOrderService.getGoodsOrder(goodsOrder.getId());
        order.setName(goodsOrder.getName());
        order.setAddress(goodsOrder.getAddress());
        order.setPhone(goodsOrder.getPhone());
        order.setExpressNo(goodsOrder.getExpressNo());
        order.setExpressName(goodsOrder.getExpressName());

        int index = goodsOrderService.updateGoodsOrder(goodsOrder);
        if (index > 0) {
            return WriteJson.success("修改商品订单成功");
        }
        return WriteJson.errorWebMsg("修改商品订单失败");
    }

    /**
     * @return org.springframework.ui.ModelMap
     * @Author goodni
     * @Description 分页查询商品订单列表
     * @Date 2019/3/1 9:45
     * @Param [goodsOrder]
     */
    @RequestMapping(value = "/listGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查询商品订单信息")
    public ModelMap listGoodsOrder(GoodsOrderDTO goodsOrder) {
        PageInfo<GoodsOrderVO> listGoodsOrder = goodsOrderService.listGoodsOrder(goodsOrder);
        return WriteJson.successPage(listGoodsOrder);
    }

    @RequestMapping(value = "/deliveryCancel/{id}/{optUid}", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "取消发货")
    public ModelMap deliveryCancel(@ApiParam(name = "id", value = "订单编号", required = true) @PathVariable("id") Integer id,
                                   @ApiParam(name = "optUid", value = "操作员编号", required = true) @PathVariable("optUid") Integer optUid) {
        GoodsOrder order = goodsOrderService.getGoodsOrder(id);
        if (order.getStatus() == -1 || order.getIsStatus() == -1) {
            return WriteJson.errorWebMsg("取消失败,订单已经是取消状态或已被删除");
        }
        //修改订单状态为0待发货，将总金币及总积分返还到用户账户
        returnBalanceToUser(order);
        order.setStatus(-1);//修改状态为取消
        order.setOptUid(optUid);
        int index = goodsOrderService.updateGoodsOrder(order);
        if (index > 0) {
            return WriteJson.success("取消订单成功");
        }
        return WriteJson.errorWebMsg("取消订单失败");
    }

    /**
     * @return void
     * @Author goodni
     * @Description 退回用户余额
     * @Date 2019/3/4 11:49
     * @Param [order]
     */
    private void returnBalanceToUser(GoodsOrder order) {
        UserAccount userAccount = userAccountService.getByUid(order.getUid());
        userAccount.setUid(order.getUid());
        userAccount.setCashBalance(userAccount.getCashBalance() + order.getTotal());
        userAccount.setIntegralBalance(userAccount.getIntegralBalance() + order.getTotalIntegral());
        userAccount.setUpdateTime(new Date());
        userAccountService.updateByUid(userAccount);
    }
}
