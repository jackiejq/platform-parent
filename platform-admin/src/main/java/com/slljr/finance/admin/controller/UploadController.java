package com.slljr.finance.admin.controller;

import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.common.utils.oss.AliyunOSSUtil;
import com.slljr.finance.common.utils.oss.OSSForumResult;
import com.slljr.finance.common.utils.oss.OSSResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件操作类")
@RestController
public class UploadController {
    @Autowired
    AliyunOSSUtil aliyunOSSUtil;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation(value = "上传", httpMethod = "POST")
    public ModelMap upload(@RequestParam("file") MultipartFile file) {
        Assert.notNull(file, "文件不能为空!");
        OSSResult ossResult = aliyunOSSUtil.fileUpload(file);
        return WriteJson.successData(ossResult);
    }
    
    /**
            * 后台论坛管理发帖图片上传接口
     */
    
    @RequestMapping(value = "/uploadForum", method = RequestMethod.POST)
    @ApiOperation(value = "后台论坛管理发帖图片上传", httpMethod = "POST")
    @ResponseBody
    public ModelMap uploadForum(@RequestParam("file") MultipartFile file) {
        Assert.notNull(file, "文件不能为空!");
        OSSForumResult ossResult;
		try {
			ossResult = aliyunOSSUtil.uploadForum(file);
			return WriteJson.jsonMsg(true, 0, "操作成功",  ossResult, 1, null);
		} catch (Exception e) {
			return WriteJson.jsonMsg(false, 1, "失败",  null, 0, null);
		}              
        
    }
    
}
