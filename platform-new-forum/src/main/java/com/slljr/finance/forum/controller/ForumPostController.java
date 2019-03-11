package com.slljr.finance.forum.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.ForumPost;
import com.slljr.finance.common.pojo.vo.BasePageVO;
import com.slljr.finance.common.pojo.vo.ForumPostDetailVO;
import com.slljr.finance.common.pojo.vo.ForumPostVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.utils.JsonUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;
import com.slljr.finance.forum.service.ForumCommentService;
import com.slljr.finance.forum.service.ForumPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 帖子操作API
 * @author: uncle.quentin.
 * @date: 2019/2/12.
 * @time: 11:22.
 */
@RestController
@RequestMapping(value = "/forumPost")
@Api(tags = {"帖子操作API"})
public class ForumPostController extends BaseController {

    private static final Logger log = LogManager.getLogger();

    /**
     * 帖子服务接口
     */
    @Autowired
    private ForumPostService forumPostService;

    /**
     * 评论回复服务接口
     */
    @Autowired
    private ForumCommentService forumCommentService;

    /**
     * 用户发帖
     *
     * @param forumPost
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/12 11:29
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/posting", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户发帖操作", notes = "{\"title\":\"帖子标题\",\"content\":\"帖子内容\"}")
    public ModelMap posting(@RequestBody ForumPost forumPost, HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        forumPost.setUid(user.getId());

        //校验用户论坛状态
        validUserForumOperateStatus(user.getId());

        int successCount = forumPostService.posting(forumPost);
        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("发帖失败");
        }
    }

    /**
     * 删除帖子
     *
     * @param param
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/12 11:46
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/deletePost", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户删除帖子操作", notes = "{\"id\":\"帖子id\"}")
    public ModelMap deletePost(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        //获取帖子ID
        JSONObject obj = JsonUtil.strToJson(param);
        Integer id = obj.getInteger("id");
        //校验参数
        validParam(id);
        //判断是否当前用户的帖子
        {
            ForumPostVO forumPost = forumPostService.selectPostDetail(id);
            //获取登录用户信息
            UserBasicVO user = getLoginUser(request);
            if (null != forumPost) {
                if (!forumPost.getUid().equals(user.getId())) {
                    throw new InterfaceException(MsgEnum.NOT_ALLOW_DELETE_POST);
                }
            } else {
                throw new InterfaceException(MsgEnum.DATA_NULL);
            }
        }

        //删除操作
        int successCount = forumPostService.deletePost(id);
        if (successCount > 0) {
            return WriteJson.success();
        } else {
            return WriteJson.errorWebMsg("删除帖子失败");
        }
    }

    /**
     * 分页查询
     *
     * @param forumPost
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/12 14:24
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/listPost", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查询帖子", notes = "{\"startTime\":\"开始时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"endTime\":\"结束时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"pageNum\":\"当前页码\",\"pageSize\":\"每页数\"}")
    public ModelMap listPost(@RequestBody ForumPost forumPost) {
        PageInfo<ForumPostVO> pageInfo = forumPostService.selectPostByPage(forumPost);
        return WriteJson.successData(pageInfo);
    }

    /**
     * 查看帖子明细展开评论
     *
     * @param request
     * @return org.springframework.ui.ModelMap
     * @author uncle.quentin
     * @date 2019/2/12 14:35
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/getPostDetailInfo", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "用户查询帖子明细", notes = "{\"id\":\"帖子id\",\"pageNum\":\"页码\",\"pageSize\":\"每页数\"}")
    public ModelMap getPostDetailInfo(@RequestBody String param, HttpServletRequest request) throws InterfaceException {
        //获取帖子ID
        JSONObject obj = JsonUtil.strToJson(param);
        Integer id = obj.getInteger("id");
        Integer pageNum = (null == obj.get("pageNum") ? 1 : obj.getInteger("pageNum"));
        Integer pageSize = (null == obj.get("pageSize") ? 10 : obj.getInteger("pageSize"));
        //校验参数
        validParam(id);

        //查看量+1
        {
            ForumPost forumPost = new ForumPost();
            forumPost.setId(id);
            forumPost.setViewCount(1);
            int successCount = forumPostService.updatePostViewCount(forumPost);
            if (successCount > 0) {
                //刷新浏览量缓存
                forumPostService.refreshPostViewCountCache(forumPost);
            }
        }

        //获取登录用户信息
        UserBasicVO user = null;
        try {
            user = getLoginUser(request);
        } catch (InterfaceException e) {
            log.error("游客访问");
        }
        //评论分页
        BasePageVO basePage = new BasePageVO();
        basePage.setPageNum(pageNum);
        basePage.setPageSize(pageSize);
        //为空则未登录访问
        Integer uid = (null == user ? null : user.getId());

        ForumPostDetailVO forumPostDetail = forumCommentService.listPostDetail(id, uid, basePage);

        return WriteJson.successData(forumPostDetail);
    }

    /**
     * 分页查询当前用户帖子
     *
     * @author uncle.quentin
     * @date   2019/3/1 13:54
     * @param   forumPost
     * @param   request
     * @return org.springframework.ui.ModelMap
     * @version 1.0
     */
    @ResponseBody
    @RequestMapping(value = "/myPosts", method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "分页查询帖子", notes = "{\"startTime\":\"开始时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"endTime\":\"结束时间【格式：yyyy-MM-dd HH:mm:ss:】\",\"pageNum\":\"当前页码\",\"pageSize\":\"每页数\"}")
    public ModelMap myPosts(@RequestBody ForumPost forumPost, HttpServletRequest request) throws InterfaceException {
        //获取登录用户信息
        UserBasicVO user = getLoginUser(request);
        forumPost.setUid(user.getId());

        //分页查询
        PageInfo<ForumPostVO> pageInfo = forumPostService.selectPostByPageOrderByCreateTime(forumPost);

        //返回
        JSONObject result = new JSONObject();
        result.put("userDetail", user);
        result.put("pageInfo",pageInfo);

        return WriteJson.successData(result);
    }
}
