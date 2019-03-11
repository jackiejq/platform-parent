package com.slljr.finance.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.slljr.finance.common.pojo.model.ForumGreat;

/**
 * 点赞
 * @author LXK
 *
 */
public interface ForumGreatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumGreat record);

    int insertSelective(ForumGreat record);

    ForumGreat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumGreat record);

    int updateByPrimaryKey(ForumGreat record);

    /**
             * 查询评论点赞信息
     * @param id
     * @param parent_reply_uid
     * @return
     */
	List<ForumGreat> selectByInfo(@Param("id")Integer id,@Param("parentReplyUid") Integer parentReplyUid);

	/**
	 * 查询发帖点赞
	 * @param id
	 * @param parentReplyUid
	 * @return
	 */
	List<ForumGreat> selectByForumInfo(@Param("id")Integer id,@Param("parentReplyUid") Integer parentReplyUid);

	/**
	 * 查询当前用户是否给当前评论点过赞
	 * @param id
	 * @param uid
	 * @return
	 */
	ForumGreat selectByGreat(Integer id, Integer uid);
	
	/**
	 * 查询帖子用户点赞
	 */
	ForumGreat selectByGreatInfo(Integer id, Integer uid);
}