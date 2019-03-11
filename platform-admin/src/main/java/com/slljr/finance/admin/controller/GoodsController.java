package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.GoodsService;
import com.slljr.finance.common.pojo.model.Goods;
import com.slljr.finance.common.pojo.vo.GoodsVO;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 1、商品的控制访问层
 *
 * @author XueYi
 * 2018年12月4日 下午4:05:42
 */
@RestController
@Api(tags = {"后台商品信息操作的API"})
@RequestMapping(value = "/goods")
@CrossOrigin
public class GoodsController {

    /**
     * 商品服务接口
     */
    @Autowired
    private GoodsService goodsService;


    /**
     * 添加商品
     *
     * @param goods
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 20:02
     * @version 1.0
     */
    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "添加商品信息")
    public ModelMap addGood(Goods goods) {
        Assert.notNull(goods.getTitle(), "商品标题不能为空");
        Assert.notNull(goods.getPrice(), "商品价格不能为空");
        Assert.notNull(goods.getCount(), "商品剩余数量不能为空");
        Assert.notNull(goods.getDetail(), "商品详情不能为空");

        int successCount = goodsService.insertGoods(goods);
        if (successCount > 0) {
            return WriteJson.successData(successCount);
        } else {
            return WriteJson.errorWebMsg("新增失败");
        }
    }

    /**
     * 分页查询商品信息
     *
     * @param goods
     * @param pageNum
     * @param pageSize
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 20:05
     * @version 1.0
     */
    @RequestMapping(value = "/listGoods", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查看商品列表")
    public ModelMap listGoods(Goods goods, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<GoodsVO> pageBean = goodsService.selectGoodsWithCategoryByPage(goods, pageNum, pageSize);
        return WriteJson.successPage(pageBean);
    }

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
     * 修改商品信息
     *
     * @param goods
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 20:14
     * @version 1.0
     */
    @RequestMapping(value = "/updateGoods", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "修改商品信息")
    public ModelMap updateGoods(Goods goods) {
        Assert.notNull(goods.getId(), "商品id不能为空");

        int successCount = goodsService.updateGoods(goods);
        if (successCount > 0) {
            return WriteJson.successData(1);
        } else {
            return WriteJson.errorWebMsg("商品修改失败");
        }
    }

    /**
     * 根据id删除商品信息
     *
     * @param id
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2018/12/12 20:15
     * @version 1.0
     */
    @RequestMapping(value = "/deleteGoods/{id}", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "根据id删除商品信息")
    public ModelMap deleteGoods(@ApiParam(name = "id", value = "商品编号", required = true) @PathVariable("id") Integer id) {
        Assert.notNull(id, "商品id不能为空");

        int succesCount = goodsService.deleteGoods(id);
        if (succesCount > 0) {
            return WriteJson.successData(succesCount);
        } else {
            return WriteJson.errorWebMsg("商品删除失败");
        }
    }

}
