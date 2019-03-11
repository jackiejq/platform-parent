package com.slljr.finance.forum.mapper;

import com.slljr.finance.common.pojo.model.ForumComment;
import com.slljr.finance.common.pojo.vo.ForumCommentVO;
import com.slljr.finance.common.pojo.vo.ForumStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    ForumComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKey(ForumComment record);

    /**
     * 根据帖子ID查询
     *
     * @param postId
     * @return java.util.List<com.slljr.finance.common.pojo.vo.ForumCommentVO>
     * @author uncle.quentin
     * @date 2019/2/12 17:07
     * @version 1.0
     */
    List<ForumCommentVO> selectByPostId(@Param("postId") Integer postId, @Param("currentUid") Integer currentUid);

    /**
     * 根据类型(评论或帖子)分组统计评论量
     *
     * @author uncle.quentin
     * @date   2019/2/28 15:05
     * @param   type
     * @return java.util.List<com.slljr.finance.common.pojo.vo.ForumStatisticsVO>
     * @version 1.0
     */
    List<ForumStatisticsVO> selectCommentCountByType(@Param("type") Integer type);

    /**
     * 根据类型(评论)分组统计评论量
     *
     * @author uncle.quentin
     * @date   2019/2/28 15:05
     * @return java.util.List<com.slljr.finance.common.pojo.vo.ForumStatisticsVO>
     * @version 1.0
     */
    List<ForumStatisticsVO> selectCommentCountByPostId();
}