package com.slljr.finance.forum.mapper;

import com.slljr.finance.common.pojo.model.ForumPost;
import com.slljr.finance.common.pojo.vo.ForumCacheVO;
import com.slljr.finance.common.pojo.vo.ForumPostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ForumPostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ForumPost record);

    int insertSelective(ForumPost record);

    ForumPostVO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ForumPost record);

    int updateAddViewCount(ForumPost record);

    int updateByPrimaryKeyWithBLOBs(ForumPost record);

    int updateByPrimaryKey(ForumPost record);

    List<ForumPostVO> selectByCondition(ForumPost record);

    /**
     * 条件查询根据发布年份降序
     *
     * @author uncle.quentin
     * @date   2019/3/4 14:23
     * @param   record
     * @return java.util.List<com.slljr.finance.common.pojo.vo.ForumPostVO>
     * @version 1.0
     */
    List<ForumPostVO> selectByConditionOrderByCreateTimeDesc(ForumPost record);

    /**
     * 帖子点赞量
     *
     * @author uncle.quentin
     * @date   2019/2/28 10:14
     * @param   id
     * @return com.slljr.finance.common.pojo.vo.ForumCacheVO
     * @version 1.0
     */
    List<ForumCacheVO> selectPostLike(@Param("id") Integer id);

    /**
     * 帖子评论量量
     *
     * @author uncle.quentin
     * @date   2019/2/28 10:14
     * @param   id
     * @return com.slljr.finance.common.pojo.vo.ForumCacheVO
     * @version 1.0
     */
    List<ForumCacheVO> selectPostComment(@Param("id") Integer id);
}