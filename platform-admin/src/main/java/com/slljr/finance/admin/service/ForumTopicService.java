package com.slljr.finance.admin.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.ForumGreatMapper;
import com.slljr.finance.admin.mapper.ForumTopicMapper;
import com.slljr.finance.common.pojo.model.ForumGreat;
import com.slljr.finance.common.pojo.model.ForumTopic;
import com.slljr.finance.common.pojo.model.ForumUser;
import com.slljr.finance.common.pojo.model.UserPicture;
import com.slljr.finance.common.pojo.vo.ForumTopicListVO;
import com.slljr.finance.common.pojo.vo.FroumUserTopicListVO;
import com.slljr.finance.admin.mapper.ForumUserMapper;
import com.slljr.finance.admin.mapper.UserPictureMapper;

@Service
public class ForumTopicService {
	
	
	@Autowired
	private ForumTopicMapper forumTopicMapper;
	
	@Autowired
	private ForumGreatMapper forumGreatMapper;
	
	@Autowired
	private ForumUserMapper forumUserMapper;
	
	@Autowired
	UserPictureMapper userPictureMapper;

	public PageInfo<ForumTopicListVO> queryForumTopiclist(ForumTopic forumTopic,Integer pageNum, Integer pageSize) {		
		  PageHelper.startPage(pageNum, pageSize);
		  	if(forumTopic != null && forumTopic.getId() !=null ) {
		  		forumTopic.setId(forumTopic.getId());
		  	}
		  	if(forumTopic != null && forumTopic.getUid() !=null ) {
		  		forumTopic.setUid(forumTopic.getUid());
		  	}
		  	if(forumTopic != null && forumTopic.getTitle() !=null ) {
		  		forumTopic.setTitle(forumTopic.getTitle());
		  	}
		  	if(forumTopic != null && forumTopic.getCreateTime() !=null ) {
		  		forumTopic.setCreateTime(forumTopic.getCreateTime());
		  	}
	        List<ForumTopicListVO> list = forumTopicMapper.queryForumTopiclist(forumTopic);
	        return new PageInfo<>(list);
	}

	/**
	 * 通过帖子id查询帖子详情
	 * @param id
	 * @return
	 */
	@Transactional
	public ForumTopicListVO queryForumTopicDetails(Integer id) {
		ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(id);
		if(forumTopic != null && forumTopic.getViewCount() != null) {
			int num = forumTopic.getViewCount();
			forumTopic.setViewCount(num+1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);			
		}else if(forumTopic != null && forumTopic.getViewCount() == null){
			forumTopic.setViewCount(1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);			
		}
		ForumTopicListVO forumTopicListVO = forumTopicMapper.queryForumTopicDetails(id);
		forumTopicListVO.setFlag("false");
		return forumTopicListVO;
	}

	/**
	 * 登陆用户查询详情
	 * @param id
	 * @param uid
	 * @return
	 */
	@Transactional
	public ForumTopicListVO queryTopicDetails(Integer id, Integer uid) {
		ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(id);
		if(forumTopic != null && forumTopic.getViewCount() != null) {
			int num = forumTopic.getViewCount();
			forumTopic.setViewCount(num+1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);			
		}else if(forumTopic != null && forumTopic.getViewCount() == null){
			forumTopic.setViewCount(1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);			
		}
		ForumTopicListVO forumTopicListVO = forumTopicMapper.queryForumTopicDetails(id);	
		try {
			
			ForumGreat forumGreat = new ForumGreat();
			forumGreat = forumGreatMapper.selectByGreatInfo(id, uid);
			if (forumGreat != null) {
				forumTopicListVO.setFlag("true");
				
			} else {
				forumTopicListVO.setFlag("false");
			} 
			return forumTopicListVO;
		} catch (Exception e) {
			forumTopicListVO.setFlag("false");
			return forumTopicListVO;
		}
		
	}
	
	
	/**
	 * 查询用户发帖列表
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<FroumUserTopicListVO> queryUserTopicList(Integer uid, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
        List<FroumUserTopicListVO> list = forumTopicMapper.queryUserTopicList(uid);
        return new PageInfo<>(list);
	}

	/**
	 * 点赞操作
	 * @param id
	 * @param uid
	 * @param parent_reply_uid
	 * @return
	 */
	public void topicGreat(Integer id, Integer uid) {
		try {
			//查询是否有该用户对该文章的点赞记录
			Integer parentReplyUid = uid;
			List<ForumGreat> list = forumGreatMapper.selectByForumInfo(id, parentReplyUid);
			if (list != null && list.size() > 0) {
				//如果找到了这条记录，则删除该记录，同时文章的点赞数减1
				ForumGreat great = list.get(0);
				//删除记录
				forumGreatMapper.deleteByPrimaryKey(great.getId());
				ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(id);
				//文章点赞数减1
				int num = (int)forumTopic.getLikeCount();				
				forumTopic.setLikeCount(num - 1);
				forumTopic.setUpdateTime(new Date());
				forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
			} else {
				ForumGreat great = new ForumGreat();
				great.setTopicId(id);
				great.setParentReplyUid(parentReplyUid);
				great.setType(1);
				great.setLikeCount(1);
				great.setCreateTime(new Date());
				great.setUpdateTime(new Date());
				forumGreatMapper.insertSelective(great);
				//文章点赞数加1
				ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(id);
				int num = (int)forumTopic.getLikeCount();
				forumTopic.setLikeCount(num + 1);
				forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
			} 
		} catch (Exception e) {
			
		}		
	}

	/**
	 * 发帖
	 * @param forumTopic
	 */
	@Transactional
	public void insert(ForumTopic forumTopic) {
		forumTopic.setCreateTime(new Date());
		forumTopic.setUpdateTime(new Date());
		forumTopic.setStatus(0);
		forumTopic.setLikeCount(0);
		forumTopic.setReplyCount(0);
		forumTopic.setTop(0);
		forumTopic.setViewCount(0);
		//帖子中图片存储
		/*if(forumTopic.getTopicUrl() !=null) {
			UserPicture userPicture = new UserPicture();
			userPicture.setTid(forumTopic.getUid());
			userPicture.setTcode("");
			userPicture.setTurl(forumTopic.getTopicUrl());
			userPictureMapper.insertSelective(userPicture);
		}*/
		if(forumTopic.getUid() !=null) {
			ForumUser forumUser1 = forumUserMapper.selectByPrimaryKey(forumTopic.getUid());
			ForumUser forumUser = new ForumUser();
			if(forumUser1 !=null && forumUser1.getUid() != null ) {				
				int integral = (int)forumUser1.getIntegral();
				forumUser.setIntegral(integral+0);//发帖增加积分
				forumUser.setUpdateTime(new Date());
				forumUserMapper.updateByPrimaryKey(forumUser);
			}else {
				forumUser.setUid(forumTopic.getUid());
				forumUser.setCreateTime(new Date());
				forumUser.setGrade(10);
				forumUser.setIntegral(10000);
				forumUser.setStatus(0);
				forumUser.setType(1);
				forumUser.setUpdateTime(new Date());
				forumUserMapper.insertSelective(forumUser);	
			}						
		}		
		forumTopicMapper.insertSelective(forumTopic);		
	}

	/**
	 * 删除帖子
	 * @param id
	 */
	public void deleteTopic(Integer id) {
		forumTopicMapper.updateByStatus(id);
	}

	/**
	 * 通过帖子ID查询帖子内容
	 * @param id
	 * @return
	 */
	public String queryTopicCotent(Integer id) {
		String content = forumTopicMapper.queryTopicCotent(id);
		return content;
	}

	@Transactional
	public int addBrowseVolume(Integer id) {
		ForumTopic forumTopic = forumTopicMapper.selectByPrimaryKey(id);
		if(forumTopic != null && forumTopic.getViewCount() != null) {
			int num = forumTopic.getViewCount();
			forumTopic.setViewCount(num+1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
			return 2;
		}else if(forumTopic != null && forumTopic.getViewCount() == null){
			forumTopic.setViewCount(1);
			forumTopic.setUpdateTime(new Date());
			forumTopicMapper.updateByPrimaryKeySelective(forumTopic);
			return 2;
		}
		return 1;		
	}

	/**
	 * 管理员操作用户状态(-1:禁言;0:正常)
	 * @param uid
	 * @param status
	 */
	public void operatingUserStatus(Integer uid, Integer status) {
		forumUserMapper.updateUserStatus(uid,status);	
	}

	/**
	 * 
	 */
}
