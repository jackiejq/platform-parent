package com.slljr.finance.admin.controller;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.GoodsCategoryService;
import com.slljr.finance.common.pojo.model.GoodsCategory;
import com.slljr.finance.common.pojo.vo.GoodsCategoryVo2;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods/category")
@Api(tags={"后台商品分类管理操作的API"})
@CrossOrigin
public class GoodsCategoryController {
    @Autowired
    GoodsCategoryService goodsCategoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询商品所有分类", httpMethod = "GET")
    public ModelMap list(@RequestParam(defaultValue = "1")int pageNum, @RequestParam(defaultValue = "10")int pageSize){
        PageInfo pageInfo = goodsCategoryService.findAll(pageNum, pageSize);
        if (pageInfo != null){
            return WriteJson.successPage(pageInfo);
        }
        return WriteJson.errorWebMsg("查询商品所有分类失败");
    }

    @RequestMapping(value = "/listParentCategory", method = RequestMethod.GET)
    @ApiOperation(value = "查询父商品分类", httpMethod = "GET")
    public ModelMap listParentCategory() {
        List<GoodsCategory> pageInfo = goodsCategoryService.findParentCategory();
        if (null != pageInfo) {
            return WriteJson.successData(pageInfo);
        }
        return WriteJson.errorWebMsg(MsgEnum.NOTEXISTS.getMsg());
    }

    @RequestMapping(value = "/listChildCategory", method = RequestMethod.GET)
    @ApiOperation(value = "查询子商品分类", httpMethod = "GET")
    public ModelMap listChildCategory(Integer parentId) {
        Assert.notNull(parentId, "父分类id不能为空");
        List<GoodsCategoryVo2>  pageInfo = goodsCategoryService.findChildCategory(parentId);
        if (null != pageInfo) {
            return WriteJson.successData(pageInfo);
        }
        return WriteJson.errorWebMsg(MsgEnum.NOTEXISTS.getMsg());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加商品分类", httpMethod = "POST")
    public ModelMap addCategory(String name, Integer parentId){
        Assert.notNull(name, "分类名称不能为空");
        int successCount = goodsCategoryService.addCategory(name, parentId);
        if (successCount > 0) {
            return WriteJson.successData(successCount);
        }
        return WriteJson.errorWebMsg(MsgEnum.FAIL.getMsg());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改商品分类", httpMethod = "POST")
    public ModelMap updateCategory(Integer id, String name, Integer parentId){
        Assert.notNull(id, "分类id不能为空");
        Assert.notNull(name, "分类名称不能为空");

        int successCount = 0;
        //校验是否为一级分类，一级分类不允许修改
        GoodsCategoryVo2 goodsCategory = goodsCategoryService.findCategoryById(id);
        if (null != goodsCategory) {
            if (null != goodsCategory.getParentId()) {
                successCount = goodsCategoryService.updateCategory(id, name, parentId);
            } else {
                return WriteJson.errorWebMsg(MsgEnum.PRIMARY_CLASSIFICATION.getMsg());
            }
        }

        if (successCount > 0) {
            return WriteJson.successData(successCount);
        } else {
            return WriteJson.errorWebMsg(MsgEnum.FAIL.getMsg());
        }
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据id获取分类信息", httpMethod = "GET")
    public ModelMap getCatecory(@PathVariable("id")Integer id){
        GoodsCategoryVo2 category = goodsCategoryService.findCategoryById(id);
        return WriteJson.successData(category);
    }

    @RequestMapping(value = "/listCategory", method = RequestMethod.POST)
    @ApiOperation(value = "获取所有分类信息列表", httpMethod = "POST")
    public ModelMap listCategory() {
        List<GoodsCategoryVo2>  pageInfo = goodsCategoryService.listGoodsCategory();
        return WriteJson.successData(pageInfo);
    }
    
}
