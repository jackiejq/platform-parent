package com.slljr.finance.admin.controller;

import static com.slljr.finance.common.constants.Constant.AUTHORIZATION;
import static com.slljr.finance.common.constants.Constant.TOKEN_KEY;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.service.ForumTopicService;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.ForumTopic;
import com.slljr.finance.common.pojo.vo.ForumTopicListVO;
import com.slljr.finance.common.pojo.vo.FroumUserTopicListVO;
import com.slljr.finance.common.pojo.vo.UserBasicVO;
import com.slljr.finance.common.redis.RedisUtil;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.WriteJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 论坛发帖控制层
 * @author LXK
 *
 */
@RestController
@RequestMapping(value="/adminTopic")
@Api(tags= {"管理后台论坛发帖操作的API"})
@CrossOrigin
public class ForumTopicController {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ForumTopicService forumTopicService;
	
	/**
	 * 查询所有帖子列表
	 */
	@RequestMapping(value="/queryForumTopiclist",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询所有帖子列表",notes="{'pageNum':'pageNum','pageSize':'pageSize','id':'帖子id','uid':'用户uid','title':'帖子标题','creatTime':'帖子创建时间'}")		
	public ModelMap queryForumTopiclist(ForumTopic forumTopic, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
	//public ModelMap queryForumTopiclist(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
		//UserBasicVO user = getLoginUser(request);
		/*JSONObject paramObj = JsonUtil.strToJson(param);
		pageNum = (Integer) paramObj.get("pageNum");
		pageSize = (Integer) paramObj.get("pageSize");*/  
		PageInfo<ForumTopicListVO> pageInfo = forumTopicService.queryForumTopiclist(forumTopic,pageNum, pageSize);
		return WriteJson.successPage(pageInfo);
	}
	
	/**
	 *查询指定帖子详情
	 */
	
	@RequestMapping(value="/queryForumTopicDetails",method=RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "查询指定帖子详情",notes="{'uid':'看帖用户uid','id':'帖子id'}")		
	public ModelMap queryForumTopicDetails(Integer id,Integer uid ){
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		if(uid ==null) {
			ForumTopicListVO forumTopicListVO = forumTopicService.queryForumTopicDetails(id);
			if(forumTopicListVO !=null) {
				return WriteJson.successData(forumTopicListVO);
			}
			return WriteJson.errorWebMsg("无相关帖子的信息");
		}else {
			ForumTopicListVO forumTopicListVO = forumTopicService.queryTopicDetails(id,uid);
			if(forumTopicListVO !=null) {
				return WriteJson.successData(forumTopicListVO);
			}
			return WriteJson.errorWebMsg("无相关帖子的信息");
		}				
	}
	
	/**
	 * 查询当前用户帖子列表
	 */
	
	@RequestMapping(value="/queryUserTopicList",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "查询当前用户帖子列表",notes="{'uid':'用户uid'}")		
	public ModelMap queryUserTopicList(Integer uid,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){	
		if(uid == null ) {
			return WriteJson.errorWebMsg("uid为空");
		}
		PageInfo<FroumUserTopicListVO> pageInfo = forumTopicService.queryUserTopicList(uid,pageNum, pageSize);
		return WriteJson.successPage(pageInfo);
	}
	
	/*
	 * 发帖点赞操作
	 */
	@RequestMapping(value="/topicGreat",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "发帖点赞操作",notes="{'id':'帖子id','uid':'发帖人uid','parent_reply_uid':'点赞用户uid'}")		
	public ModelMap topicGreat(Integer id,Integer uid){
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		if(uid == null ) {
			return WriteJson.errorWebMsg("点赞用户uid为空");
		}		
		try {
			forumTopicService.topicGreat(id, uid);
			return WriteJson.success();
		} catch (Exception e) {
			return WriteJson.errorWebMsg("操作失败");
		}
		
	}
	
	/**
	 * 管理员发帖
	 * @param request
	 * @return
	 * @throws InterfaceException
	 */
	
	@RequestMapping(value="/topicInfo",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "管理员发帖操作",notes="")		
	public ModelMap topicInfo(ForumTopic forumTopic){
		Assert.notNull(forumTopic.getUid(), "发帖用户uid为空");
		Assert.notNull(forumTopic.getTitle(), "发帖标题为空");
		Assert.notNull(forumTopic.getContent(), "发帖内容为空");			
		try {
			forumTopicService.insert(forumTopic);
			return WriteJson.success();
		} catch (Exception e) {
			return WriteJson.errorWebMsg("操作失败");
		}		
	}
	
	/**
	  * 帖子浏览量增加
	  */
	@RequestMapping(value="/addBrowseVolume",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "帖子浏览量增加",notes="'id':'帖子id'")		
	public ModelMap addBrowseVolume(Integer id){
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}
		try {
			int num = forumTopicService.addBrowseVolume(id);
			if(num != 1 ) {
				return WriteJson.success();	
			}else {
				return WriteJson.errorWebMsg("此帖子不存在");
			}			
		} catch (Exception e) {
			return WriteJson.errorWebMsg("操作失败");
		}
		
	}
	
	
	/**
	 * 管理员操作用户帖子(删除)
	 */
	@RequestMapping(value="/deleteTopic",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "管理员操作用户帖子(删除)",notes="")		
	public ModelMap deleteTopic(Integer id){
		if(id == null ) {
			return WriteJson.errorWebMsg("帖子id为空");
		}				
		forumTopicService.deleteTopic(id);
		return WriteJson.success();
			
	}
	
	/**
	 * 管理员操作用户状态(-1:禁言;0:正常)
	 */
	@RequestMapping(value="/operatingUserStatus",method=RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "管理员操作用户状态(-1:禁言;0:正常)",notes="'uid':'被操作用户uid','status':'禁言和取消禁言[-1:禁言;0:正常]'")		
	public ModelMap operatingUserStatus(Integer uid,Integer status){
		if(uid == null ) {
			return WriteJson.errorWebMsg("被操作uid为空");
		}
		if(status == null ) {
			return WriteJson.errorWebMsg("操作状态有误");
		}
		forumTopicService.operatingUserStatus(uid,status);
		return WriteJson.success();
			
	}
	
	

	/*
	 public UserBasicVO getLoginUser(HttpServletRequest request) throws InterfaceException {
	        //获取Token
	        String token = request.getHeader(AUTHORIZATION);

	        if (StringUtils.isNotEmpty(token)) {
	            Object user = redisUtil.hget(TOKEN_KEY, token);
	            if (null != user) {
	                UserBasicVO userBasic = (UserBasicVO) user;
	                if (null != userBasic) {
	                    return userBasic;
	                }
	            }
	        }

	        throw new InterfaceException(MsgEnum.UNREGISTERED.getMsg());
	    }
	 */
	 
}
