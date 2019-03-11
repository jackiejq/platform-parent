package com.slljr.finance.common.utils.oss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.PutObjectResult;
import com.slljr.finance.common.encrypt.MD5Helper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Component
public class AliyunOSSUtil {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    @Value("${aliyun.oss.bucketName}")
    private String bucketName = "yoocard";
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId = "LTAIMtzDqwPJcypB";
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret = "BFc28X7J0YxkMk3YVzPIBf2mxPVPxN";

    public static void main(String[] args) throws Exception{
        AliyunOSSUtil ossUtil = new AliyunOSSUtil();

        File pdfFile = new File("E:\\company\\cdream\\workspace\\yoocard-0125.apk");
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);


        OSSResult result = ossUtil.fileUpload(multipartFile);
        System.out.println(JSON.toJSONString(result));
    }
    /**
     * 文件上传并返回文件路径
     *
     * @param file
     * @return
     * @throws IOException
     */
    public OSSResult fileUpload(MultipartFile file) {
        OSSResult ossResult = null;

        //初始化
        OSSClient ossClient = ossClientInitialization();
        creatBucket(ossClient);
        //根据文件名称生成MD5 KEY
        String fileType = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        String fileKey = MD5Helper.encrypt16(System.currentTimeMillis() + file.getOriginalFilename()) + "." + fileType;
        //上传文件
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //{"clientCRC":1142037394773739400,"eTag":"38513AE6203E6B8B0ED83B1D3473305D","requestId":"5C18E1E345618193F31251C8","serverCRC":1142037394773739400}
            PutObjectResult res = ossClient.putObject(bucketName, fileKey, inputStream);
            //拼接URL
            StringBuffer buffer = new StringBuffer();
            buffer.append("https://").append(bucketName).append(".").append(endpoint).append("/").append(fileKey);

            ossResult = new OSSResult(fileKey, fileType, file.getSize(), buffer.toString());
        } catch (IOException e) {
            ossResult = new OSSResult("网络异常!" + e.getMessage());
        } finally {
            //关闭
            ossClientRelease(ossClient);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ossResult;
    }


    /**
             * 后台论坛图片上传操作
     */
	public OSSForumResult uploadForum(MultipartFile file) {
		OSSForumResult ossResult = null;

	        //初始化
	        OSSClient ossClient = ossClientInitialization();
	        creatBucket(ossClient);
	        //根据文件名称生成MD5 KEY
	        String fileType = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
	        String title = MD5Helper.encrypt16(System.currentTimeMillis() + file.getOriginalFilename()) + "." + fileType;
	        //上传文件
	        InputStream inputStream = null;
	        try {
	            inputStream = file.getInputStream();
	            //{"clientCRC":1142037394773739400,"eTag":"38513AE6203E6B8B0ED83B1D3473305D","requestId":"5C18E1E345618193F31251C8","serverCRC":1142037394773739400}
	            PutObjectResult res = ossClient.putObject(bucketName, title, inputStream);
	            //拼接URL
	            StringBuffer buffer = new StringBuffer();
	            buffer.append("https://").append(bucketName).append(".").append(endpoint).append("/").append(title);
	            //ossResult = new OSSResult(fileKey, fileType, file.getSize(), buffer.toString());
	            ossResult = new OSSForumResult(title, fileType, file.getSize(), buffer.toString());
	        } catch (IOException e) {
	            ossResult = new OSSForumResult("网络异常!" + e.getMessage());
	        } finally {
	            //关闭
	            ossClientRelease(ossClient);
	            if (inputStream != null) {
	                try {
	                    inputStream.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	        return ossResult;
	}
    
    
//    /**
//     * 获得url链接
//     *
//     * @param fileName
//     * @return
//     */
//    public String getUrl(OSSClient ossClient,String fileName) {
//        // 设置URL过期时间为10年 3600l* 1000*24*365*10
//        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
//        // 生成URL
//        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
//        if (url != null) {
//            return url.toString();
//        }
//        return null;
//    }

    /**
     * 获取ossClient
     *
     * @return
     */
    private OSSClient ossClientInitialization() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 判断是否存在bucketName
     *
     * @return
     */
    private boolean hasBucket(OSSClient ossClient) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 创建bucket实例
     */
    private void creatBucket(OSSClient ossClient) {
        if (!hasBucket(ossClient))
            ossClient.createBucket(bucketName);
    }

    /**
     * 释放ossClient资源
     */
    private void ossClientRelease(OSSClient ossClient) {
        ossClient.shutdown();
    }

    /**
     * 获取bucket信息
     */
    private BucketInfo getBucketInfo(OSSClient ossClient) {
        return ossClient.getBucketInfo(bucketName);
    }

    /**
     * 获取附件上传保存到服务器的名称
     *
     * @param fileName 文件原始名称
     * @return
     */
    private String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        if (fileName.lastIndexOf(".") > -1) {
            fileName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        } else {
            fileName = "." + fileName.toLowerCase();
        }

        return String.valueOf(new Date().getTime()) + fileName;
    }

}