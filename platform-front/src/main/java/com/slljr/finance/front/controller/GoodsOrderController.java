package com.slljr.finance.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.enums.GoodsOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.front.service.GoodsOrderService;
import com.slljr.finance.front.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/2/27.
 * @time: 14:32.
 */
@RestController
@RequestMapping(value = "/goodsOrder")
@Api(tags = {"商品订单信息"})
public class GoodsOrderController extends BaseController {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 创建订单
     *
     * @param goods
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/27 18:29
     * @version 1.0
     */
    @RequestMapping(value = "/exchangeGoods", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "兑换商品创建订单", notes = "{\"goodsId\":\"商品ID\",\"goodsCount\":\"商品数量\"}")
    @ResponseBody
    public ModelMap exchangeGoods(@RequestBody GoodsOrderDTO goods, HttpServletRequest request) throws InterfaceException {
        //必填参数
        validParam(goods.getGoodsId(), goods.getGoodsCount());
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        log.info("创建订单，商品ID:{},兑换用户:{}", goods.getGoodsId(), goods.getUid());

        //兑换商品
        int successCount = goodsOrderService.exchangeGoods(user, goods);
        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("兑换失败");
        }
    }

    /**
     * 完善订单
     *
     * @param goods 订单收货地址信息
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/27 18:28
     * @version 1.0
     */
    @RequestMapping(value = "/updateGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "完善商品订单", notes = "{\"id\":\"订单ID\",\"name\":\"收货人\",\"phone\":\"收货人手机号码\",\"address\":\"收货人地址\"}")
    @ResponseBody
    public ModelMap updateGoodsOrder(@RequestBody GoodsOrderDTO goods) throws InterfaceException {
        //必填参数
        validParam(goods.getId(), goods.getName(), goods.getPhone(), goods.getAddress());

        log.info("完善订单，订单ID:{},收货人:{},收货手机:{},收货地址:{}", goods.getId(), goods.getName(), goods.getPhone(), goods.getAddress());

        //修改订单信息
        int successCount = goodsOrderService.updateGoodsOrder(goods);
        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("完善订单失败");
        }
    }

    /**
     * 取消商品订单
     *
     * @param param
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/27 19:02
     * @version 1.0
     */
    @RequestMapping(value = "/cancelGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "取消商品订单", notes = "{\"id\":\"订单ID\"}")
    @ResponseBody
    public ModelMap cancelGoodsOrder(@RequestBody String param) throws InterfaceException {
        JSONObject object = JsonUtil.strToJson(param);
        Integer orderId = object.getInteger("id");
        //必填参数
        validParam(orderId);

        log.info("取消订单ID:{}", orderId);

        GoodsOrderDTO goodsOrder = new GoodsOrderDTO();
        goodsOrder.setId(orderId);
        goodsOrder.setStatus(GoodsOrderStatusEnum.CANCEL.getKey());
        //取消订单
        int successCount = goodsOrderService.updateGoodsOrder(goodsOrder);
        if (successCount > 0) {
            //查询订单信息、还原库存
            GoodsOrder goodsOrder1 = goodsOrderService.selectGoodsOrderById(orderId);
            //修改库存
            goodsService.udpateGoodsStock(1, goodsOrder1.getGoodsId(), goodsOrder1.getGoodsCount());

            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("取消订单失败");
        }
    }


    /**
     * 删除订单
     *
     * @author uncle.quentin
     * @date   2019/2/27 19:41
     * @param   param
     * @param   request
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @RequestMapping(value = "/deleteGoodsOrder", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "删除商品订单", notes = "{\"id\":\"订单ID\"}")
    @ResponseBody
    public ModelMap deleteGoodsOrder(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);

        JSONObject object = JsonUtil.strToJson(param);
        Integer orderId = object.getInteger("id");
        //必填参数
        validParam(orderId);

        //查询订单信息
        GoodsOrder goodsOrder = goodsOrderService.selectGoodsOrderById(orderId);
        //非当前用户创建订单不能删除
        if (null == goodsOrder) {
            throw new InterfaceException(MsgEnum.NOTEXISTS);
        } else {
            if (!goodsOrder.getUid().equals(user.getId())) {
                throw new InterfaceException(MsgEnum.NOT_ALLOW_DELETE);
            }
        }
        //删除订单
        GoodsOrderDTO updateOrder = new GoodsOrderDTO();
        updateOrder.setId(orderId);
        updateOrder.setIsStatus(DataStatusEnum.INVALID.getKey());
        int successCount = goodsOrderService.updateGoodsOrder(updateOrder);

        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("删除订单失败");
        }
    }
}
