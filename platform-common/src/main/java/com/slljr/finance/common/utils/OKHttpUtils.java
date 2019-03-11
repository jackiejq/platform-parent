package com.slljr.finance.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHTTP工具类
 *
 * @Auth Created by guoqun.yang
 * @Date Created in 11:22 2017/11/13
 * @Version 1.0
 */
public class OKHttpUtils {

    //选择并保持和服务器的一致即可
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");

    /**
     * 请求客户端
     */
    private OkHttpClient okHttpClient;

    /**
     * 回调的集合
     */
    private HashMap<String, Call> callHashMap;

    private OKHttpUtils() {
        initOkHttpFunction();
        callHashMap = new HashMap();
    }

    public static OKHttpUtils getInstance() {
        return OkHttpHelper.okhttpUtils;
    }

    private static class OkHttpHelper {
        private static OKHttpUtils okhttpUtils = new OKHttpUtils();
    }

    /**
     * 初始化Okhttp配制
     *
     * @param
     * @return
     */
    private void initOkHttpFunction() {
        //创建HttpClientBuilder
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //设置超时时间
        okHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS)//
                .writeTimeout(10, TimeUnit.SECONDS)//
                .readTimeout(30, TimeUnit.SECONDS)//
                .build();
    }

    /**
     * 同步GET网络请求
     *
     * @param url 请求网络链接
     */
    public String syncGetRequest(String url) throws IOException {

        return syncGetRequest(url, null, null, null);

    }

    /**
     * 同步GET网络请求
     * 将传入的参数以KEY - VALUE的方式拼接在了请求链接中
     *
     * @param url       请求网络链接
     * @param paramsMap 请求网络参数
     */
    public String syncGetRequest(String url, Map<String, String> paramsMap) throws IOException {

        return syncGetRequest(url, null, paramsMap, null);

    }

    /**
     * 同步GET网络请求
     * 将传入的参数以KEY - VALUE的方式拼接在了请求链接中
     *
     * @param url       请求网络链接
     * @param headerMap 请求网络添加头信息
     * @param paramsMap 请求网络参数
     */
    public String syncGetRequest(String url, Map<String, String> headerMap, Map<String, String> paramsMap) throws IOException {

        return syncGetRequest(url, headerMap, paramsMap, null);

    }

    /**
     * 同步GET网络请求
     * 将传入的参数以KEY - VALUE的方式拼接在了请求链接中
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param paramsMap    请求网络参数
     * @param cacheControl 缓存模式
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     */
    public String syncGetRequest(String url, Map<String, String> headerMap, Map<String, String> paramsMap, CacheControl cacheControl) throws IOException {

        //封装请求参数
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (url != null) {
            if (url.contains("?")) {
                if (url.lastIndexOf("?") != url.length() - 1) {
                    sb.append("?");
                }
            } else {
                sb.append("?");
            }
        }

        if (!isEmpty(paramsMap)) {
            for (String key : paramsMap.keySet()) {
                sb.append(key).append("=").append(paramsMap.get(key)).append("&");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }

        //获取Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //创建请求Request对象
        Request request = requestBuilder.build();
        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    /**
     * get 异步请求
     *
     * @param url      　请求的服务器地址
     * @param callback 　请求响应的回调
     * @param tag      　网络请求的标识
     * @return
     * @time 2017/3/30 9:00
     * @version 0.1
     * @since 0.1
     */
    public void asyncGetRequest(String url, Callback callback, String tag) {

        asyncGetRequest(url, null, null, null, callback, tag);
    }

    /**
     * 异步GET网络请求
     *
     * @param url       请求网络链接
     * @param paramsMap 请求网络参数
     * @param callback  响应回调 * @param tag 网络任务标识
     */
    public void asyncGetRequest(String url, Map<String, String> paramsMap, Callback callback) {

        asyncGetRequest(url, null, paramsMap, null, callback, null);

    }

    /**
     * 异步GET网络请求
     *
     * @param url       请求网络链接
     * @param headerMap 请求网络添加头信息
     * @param paramsMap 请求网络参数
     * @param callback  响应回调 * @param tag 网络任务标识
     */
    public void asyncGetRequest(String url, Map<String, String> headerMap, Map<String, String> paramsMap, Callback callback) {

        asyncGetRequest(url, headerMap, paramsMap, null, callback, null);
    }

    /**
     * 异步GET网络请求
     * 将传入的参数以KEY - VALUE的方式拼接在了请求链接中
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param paramsMap    请求网络参数
     * @param cacheControl 缓存模式
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     * @param callback     响应回调 * @param tag 网络任务标识
     */
    public void asyncGetRequest(String url, Map<String, String> headerMap, Map<String, String> paramsMap, CacheControl cacheControl, Callback callback, String tag) {

        //封装请求参数
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (url != null) {
            if (url.contains("?")) {
                if (url.lastIndexOf("?") != url.length() - 1) {
                    sb.append("?");
                }
            } else {
                sb.append("?");
            }
        }

        if (!isEmpty(paramsMap)) {
            for (String key : paramsMap.keySet()) {
                sb.append(key).append("=").append(paramsMap.get(key)).append("&");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }

        //获取Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //创建请求Request对象
        Request request = requestBuilder.build();
        //设置取消标签
        Call call = okHttpClient.newCall(request);
        //将请求标签添加到请求Key集合中
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        // 发起网络请求
        call.enqueue(callback);

    }


    /**
     * 异步POST网络请求 提交表单数据
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param paramMap     请求网络参数
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     * @param callback     响应回调
     * @param tag          网络任务标识
     */
    public void asyncPostFormRequest(String url, Map<String, String> headerMap, Map<String, String> paramMap, CacheControl cacheControl, Callback callback, String tag) {

        //FORM表单 获取请求参数BODY
        FormBody.Builder formBuilder = new FormBody.Builder();
        // 封装参数
        if (!isEmpty(paramMap)) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        //获取表单BODY
        RequestBody formBody = formBuilder.build();

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //构建请求Request
        Request request = requestBuilder.post(formBody).build();

        //构建请求响应Call
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    public String syncPostFormRequest(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws IOException {
        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //添加请求头信息
        if (!isEmpty(headerMap)) {
            headerMap.keySet().stream().forEach(key->{
                requestBuilder.addHeader(key, headerMap.get(key));
            });
        }
        //添加请求参数
        if (!isEmpty(paramMap)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            paramMap.keySet().stream().forEach(key->{
                formBuilder.add(key, paramMap.get(key)==null?"":paramMap.get(key));
            });
            requestBuilder.post(formBuilder.build());
        }

        //构建请求Request
        Request request = requestBuilder.build();
        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    /**
     * 表单提交并获取响应
     *
     * @param url       请求地址
     * @param headerMap 请求头
     * @param paramMap  参数
     * @return okhttp3.Response
     * @author uncle.quentin
     * @date 2018/12/25 13:01
     * @version 1.0
     */
    public Response syncPostFormRequestWithResponseAll(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws IOException {
        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //添加请求头信息
        if (!isEmpty(headerMap)) {
            headerMap.keySet().stream().forEach(key -> {
                requestBuilder.addHeader(key, headerMap.get(key));
            });
        }
        //添加请求参数
        if (!isEmpty(paramMap)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            paramMap.keySet().stream().forEach(key -> {
                formBuilder.add(key, paramMap.get(key));
            });
            requestBuilder.post(formBuilder.build());
        }

        //构建请求Request
        Request request = requestBuilder.build();
        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response;
    }

    /**
     * 同步POST网络请求 提交JSON数据
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param paramMap     请求网络参数
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     */
    public String syncPostJSONRequest(String url, Map<String, String> headerMap, Map<String, String> paramMap, CacheControl cacheControl) throws IOException {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(paramMap));

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();

    }

    /**
     * 异步POST网络请求 提交JSON数据
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param paramMap     请求网络参数
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     * @param callback     响应回调
     * @param tag          网络任务标识
     */
    public void asyncPostJSONRequest(String url, Map<String, String> headerMap, Map<String, String> paramMap, CacheControl cacheControl, Callback callback, String tag) {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(paramMap));

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //构建请求响应Call
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    /**
     * 异步POST网络请求 提交JSON数据
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param JSONBody     请求网络JSON字符串
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     */
    public String syncPostJSONRequest(String url, Map<String, String> headerMap, String JSONBody, CacheControl cacheControl) throws IOException {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JSONBody);

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    /**
     * 异步POST网络请求 提交JSON数据
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param JSONBody     请求网络JSON字符串
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     * @param callback     响应回调
     * @param tag          网络任务标识
     */
    public void asyncPostJSONRequest(String url, Map<String, String> headerMap, String JSONBody, CacheControl cacheControl, Callback callback, String tag) {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JSONBody);

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //构建请求响应Call
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    /**
     * 异步POST网络请求 提交JSON数据
     * avoid posting large (greater than 1 MiB) documents using this API
     *
     * @param url          请求网络链接
     * @param headerMap    请求网络添加头信息
     * @param string       请求网络JSON字符串
     * @param cacheControl 缓存模式
     *                     对于网络请求模式，默认有两种，一是强制使用网络请求数据 ，一是强制使用缓存，一般使用默认使用网络请求就可以
     *                     public static final CacheControl FORCE_NETWORK = ..;
     *                     public static final CacheControl FORCE_CACHE = ...
     * @param callback     响应回调
     * @param tag          网络任务标识
     */
    public void asyncPostStringRequest(String url, Map<String, String> headerMap, String string, CacheControl cacheControl, Callback callback, String tag) {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //设置缓存
        if (cacheControl != null) {
            requestBuilder.cacheControl(cacheControl);
        }
        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, string);

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //构建请求响应Call
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    /**
     * 异步POST网络请求 提交File
     *
     * @param url       请求网络链接
     * @param headerMap 请求网络添加头信息
     * @param filePath  本地文件路径
     * @param callback  响应回调
     * @param tag       网络任务标识
     */
    public void asyncPostFileRequest(String url, Map<String, String> headerMap, String filePath, Callback callback, String tag) {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //创建File
        File file = new File(filePath);
        //获取RequestBody
        RequestBody requestBody = RequestBody.create(MEDIA_OBJECT_STREAM, file);

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //获取响应体Response
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    /**
     * 异步POST网络请求 提交File
     *
     * @param url       请求网络链接
     * @param headerMap 请求网络添加头信息
     * @param paramsMap 请求网络参数 object 可以是file类型
     * @param callback  响应回调
     * @param tag       网络任务标识
     */
    public void asyncPostMultiFileRequest(String url, Map<String, String> headerMap, Map<String, Object> paramsMap, Callback callback, String tag) {

        //构建请求Request Builder
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //添加请求头信息
        if (!isEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        //构建请求体 MultipartBody Builder
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        //设置类型
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //追加参数

        if (!isEmpty(paramsMap)) {
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    multipartBodyBuilder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    //通过“addFormDataPart”可以添加多个上传的文件。
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_OBJECT_STREAM, file));
                }
            }
        }

        //获取RequestBody
        RequestBody requestBody = multipartBodyBuilder.build();

        //构建请求Request
        Request request = requestBuilder.post(requestBody).build();

        //获取响应体Response
        Call call = okHttpClient.newCall(request);

        //设置取消任务标签
        if (tag != null) {
            callHashMap.put(tag, call);
        }
        //发起请求
        call.enqueue(callback);
    }

    /**
     * 移除 取消网络任务
     *
     * @param tag
     */

    public void removeRequest(String tag) {
        //获取KEY的集合
        Set<Map.Entry<String, Call>> entrySet = callHashMap.entrySet();
        //如果包含KEY
        if (entrySet.contains(tag)) {
            // 获取对应的Call
            Call call = callHashMap.get(tag);
            // 如果没有被取消 执行取消的方法
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
            //移除对应的KEY
            callHashMap.remove(tag);
        }
    }

    private static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

	public String syncGetRequest(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys) throws IOException {
		 //封装请求参数
        StringBuilder sb = new StringBuilder();
        sb.append(host);
        if (host != null) {
            if (host.contains("?")) {
                if (host.lastIndexOf("?") != host.length() - 1) {
                    sb.append("?");
                }
            } else {
                sb.append("?");
            }
        }

        if (!isEmpty(querys)) {
            for (String key : querys.keySet()) {
                sb.append(key).append("=").append(querys.get(key)).append("&");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            host = sb.toString();
        }

        //获取Builder
        Request.Builder requestBuilder = new Request.Builder().url(host);
       
        //添加请求头信息
        if (!isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        //创建请求Request对象
        Request request = requestBuilder.build();
        //获取响应体Response
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
	}

}
