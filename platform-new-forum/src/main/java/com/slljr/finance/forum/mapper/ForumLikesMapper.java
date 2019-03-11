package com.slljr.finance.forum.mapper;

import com.slljr.finance.common.pojo.model.ForumLikes;
import com.slljr.finance.common.pojo.vo.ForumStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumLikes record);

    int insertSelective(ForumLikes record);

    ForumLikes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumLikes record);

    int updateByPrimaryKey(ForumLikes record);

    List<ForumLikes> findByAll(ForumLikes forumLikes);

    /**
     * 根据类型(评论或帖子)分组统计点赞量
     *
     * @author uncle.quentin
     * @date   2019/2/28 14:57
     * @param   type
     * @return java.util.List<com.slljr.finance.common.pojo.vo.ForumPostVO>
     * @version 1.0
     */
    List<ForumStatisticsVO> selectLikeCountByType(@Param("type") Integer type);

}