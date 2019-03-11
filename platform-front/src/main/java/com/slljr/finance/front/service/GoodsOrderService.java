package com.slljr.finance.front.service;

import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.enums.GoodsOrderStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.dto.GoodsOrderDTO;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.model.GoodsOrder;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.front.mapper.GoodsMapper;
import com.slljr.finance.front.mapper.GoodsOrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 商品订单服务接口
 * @author: uncle.quentin.
 * @date: 2019/2/27.
 * @time: 14:28.
 */
@Service
public class GoodsOrderService {

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserAccountService userAccountService;


    /**
     * 兑换商品
     *
     * @author uncle.quentin
     * @date   2019/2/27 14:36
     * @param   user  用户信息
     * @param   goods 商品订单信息
     * @return int
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public int exchangeGoods(UserBasicVO user, GoodsOrderDTO goods) throws InterfaceException {
        //返回结果
        int result;

        //验证库存信息
        Goods goods1 = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (null != goods1) {
            if (null == goods1.getCount() || goods1.getCount() - goods.getGoodsCount() < 0) {
                throw new InterfaceException(MsgEnum.STOCK_NOT_ENOUGH);
            }
        } else {
            throw new InterfaceException(MsgEnum.NOTEXISTS);
        }

        //获取账户余额信息
        UserAccount userAccount = userAccountService.selectAccountByUid(user.getId());
        if (null != userAccount) {
            //账户金币余额
            Double cashBalance = userAccount.getCashBalance();
            //账户积分余额
            Double integralBalance = userAccount.getIntegralBalance();
            //更新后的账户余额信息对象
            UserAccount updateAccount = new UserAccount();
            updateAccount.setUid(userAccount.getUid());
            //商品兑换类型（积分、金币、积分+金币）
            switch (goods1.getType()) {
                //积分
                case 1:
                    //价格
                    Double integralPrice = MathUtils.mul(goods1.getPrice(), goods.getGoodsCount().doubleValue(), 2);
                    //积分余额
                    Double accountIntegralPrice = MathUtils.sub(integralBalance, integralPrice, 2);
                    if (accountIntegralPrice < 0) {
                        throw new InterfaceException(MsgEnum.INTEGRAL_NOT_ENOUGH);
                    }
                    //订单价格信息
                    goods.setGoodsIntegralPrice(goods1.getPrice());
                    goods.setTotalIntegral(integralPrice);
                    //扣除积分
                    updateAccount.setIntegralBalance(accountIntegralPrice);
                    break;
                //金币
                case 2:
                    //价格
                    Double cashPrice = MathUtils.mul(goods1.getPriceCash(), goods.getGoodsCount().doubleValue(), 2);
                    //金币余额
                    Double accountCashPrice = MathUtils.sub(cashBalance, cashPrice, 2);
                    if (accountCashPrice < 0) {
                        throw new InterfaceException(MsgEnum.CASH_NOT_ENOUGH);
                    }
                    //订单价格信息
                    goods.setGoodsPrice(goods1.getPriceCash());
                    goods.setTotal(cashPrice);
                    //扣除金币
                    updateAccount.setCashBalance(accountCashPrice);
                    break;
                //积分+金币
                case 3:
                    //价格
                    {
                        Double integralPrice1 = MathUtils.mul(goods1.getPrice(), goods.getGoodsCount().doubleValue(), 2);
                        //积分余额
                        Double accountIntegralPrice1 = MathUtils.sub(integralBalance, integralPrice1, 2);
                        if (accountIntegralPrice1 < 0) {
                            throw new InterfaceException(MsgEnum.INTEGRAL_NOT_ENOUGH);
                        }
                        //订单价格信息
                        goods.setGoodsIntegralPrice(goods1.getPrice());
                        goods.setTotalIntegral(accountIntegralPrice1);
                        //扣除积分
                        updateAccount.setIntegralBalance(accountIntegralPrice1);
                    }

                    //价格
                    {
                        Double cashPrice1 = MathUtils.mul(goods1.getPriceCash(), goods.getGoodsCount().doubleValue(), 2);
                        //金币余额
                        Double accountCashPrice1 = MathUtils.sub(cashBalance, cashPrice1, 2);
                        if (accountCashPrice1 < 0) {
                            throw new InterfaceException(MsgEnum.CASH_NOT_ENOUGH);
                        }
                        //订单价格信息
                        goods.setGoodsPrice(goods1.getPriceCash());
                        goods.setTotal(cashPrice1);
                        //扣除金币
                        updateAccount.setCashBalance(accountCashPrice1);
                    }

                    break;
                default:
                    break;
            }

            //更新
            userAccountService.updateAccountCBOrIB(updateAccount);
            //新增订单
            goods.setUid(user.getId());
            result = addGoodsOrder(goods1, goods);
        } else {
            throw new InterfaceException(MsgEnum.INSUFFICIENT_ACCOUNT_BALANCE);
        }

        return result;
    }

    /**
     * 新增商品订单
     *
     * @param goodsOrderDTO
     * @return int
     * @author uncle.quentin
     * @date 2019/2/27 16:16
     * @version 1.0
     */
    private int addGoodsOrder(Goods goods, GoodsOrderDTO goodsOrderDTO) {
        int result;
        //新增订单
        {
            GoodsOrder goodsOrder = new GoodsOrder();
            BeanUtils.copyProperties(goodsOrderDTO, goodsOrder);
            //商品标题
            goodsOrder.setGoodsTitle(goods.getTitle());
            //商品图片
            goodsOrder.setGoodsImage(goods.getImgUrl());
            //数据状态
            goodsOrder.setIsStatus(DataStatusEnum.NORMAL.getKey());
            //订单状态
            goodsOrder.setStatus(GoodsOrderStatusEnum.PACKED.getKey());
            result = goodsOrderMapper.insertSelective(goodsOrder);
        }

        //商品库存减
        {
            Goods updateStock = new Goods();
            updateStock.setId(goods.getId());
            updateStock.setCount(goods.getCount() - goodsOrderDTO.getGoodsCount());
            goodsMapper.updateByPrimaryKeySelective(updateStock);
        }

        return result;
    }

    /**
     * 修改订单信息
     *
     * @author uncle.quentin
     * @date   2019/2/27 18:31
     * @param   goodsOrder
     * @return int
     * @version 1.0
     */
    public int updateGoodsOrder(GoodsOrderDTO goodsOrder){
        return goodsOrderMapper.updateByPrimaryKeySelective(goodsOrder);
    }

    /**
     * 根据ID查询商品订单信息
     *
     * @author uncle.quentin
     * @date   2019/2/27 19:10
     * @param   goodsOrderId
     * @return com.slljr.finance.common.pojo.model.GoodsOrder
     * @version 1.0
     */
    public GoodsOrder selectGoodsOrderById(Integer goodsOrderId){
        return goodsOrderMapper.selectByPrimaryKey(goodsOrderId);
    }

}
