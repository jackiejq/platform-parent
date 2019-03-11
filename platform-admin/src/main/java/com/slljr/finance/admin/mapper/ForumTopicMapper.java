package com.slljr.finance.admin.mapper;

import java.util.List;

import com.slljr.finance.common.pojo.model.ForumTopic;
import com.slljr.finance.common.pojo.vo.ForumTopicListVO;
import com.slljr.finance.common.pojo.vo.FroumUserTopicListVO;

public interface ForumTopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumTopic record);

    int insertSelective(ForumTopic record);

    ForumTopic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumTopic record);

    int updateByPrimaryKeyWithBLOBs(ForumTopic record);

    int updateByPrimaryKey(ForumTopic record);

    /**
             * 查询所有帖子列表
     * @return
     */
    List<ForumTopicListVO> queryForumTopiclist(ForumTopic forumTopic);

	/**
	 * 根据帖子ID查询帖子详情
	 * @param id
	 * @return
	 */
	ForumTopicListVO queryForumTopicDetails(Integer id);

	/**
	 * 查询用户发帖列表
	 * @return
	 */
	List<FroumUserTopicListVO> queryUserTopicList(Integer uid);

	/**
	 * 删除帖子
	 * @param id
	 */
	void updateByStatus(Integer id);

	/**
	 * 通过帖子ID查询帖子内容
	 * @param id
	 * @return
	 */
	String queryTopicCotent(Integer id);

	/**
	 * 增加帖子浏览量
	 * @param id
	 */
	void addBrowseVolume(Integer id);

	


}