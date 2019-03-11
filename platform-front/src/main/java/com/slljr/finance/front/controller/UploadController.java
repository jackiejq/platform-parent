package com.slljr.finance.front.controller;

import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.common.utils.oss.AliyunOSSUtil;
import com.slljr.finance.common.utils.oss.OSSResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "文件操作类")
@RestController
public class UploadController  extends BaseController{
    @Autowired
    AliyunOSSUtil aliyunOSSUtil;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation(value = "单文件上传", httpMethod = "POST")
    public ModelMap upload(@RequestParam("file") MultipartFile file) {
        Assert.notNull(file, "文件不能为空!");
        OSSResult ossResult = aliyunOSSUtil.fileUpload(file);
        return WriteJson.successData(ossResult);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ApiOperation(value = "多文件上传", httpMethod = "POST")
    public ModelMap uploads(@RequestParam("files") MultipartFile[] files) {
        Assert.notNull(files, "文件不能为空!");
        List<OSSResult> result = new ArrayList<>();
        for(MultipartFile file : files){
            OSSResult ossResult = aliyunOSSUtil.fileUpload(file);
            result.add(ossResult);
        }

        return WriteJson.successData(result);
    }
}
