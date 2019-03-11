package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.vo.UserInviteCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    UserAccount selectByPrimaryKey(Integer id);

    UserAccount selectByUid(Integer id);

    int updateByPrimaryKeySelective(UserAccount record);

    int updateByUid(UserAccount record);

    int updateByPrimaryKey(UserAccount record);



    /**
     * 查询代理用户邀请人数
     *
     * @author uncle.quentin
     * @date   2019/2/13 17:58
     * @param   minCount
     * @param   maxCount
     * @return com.slljr.finance.common.pojo.vo.UserInviteCountVO
     * @version 1.0
     */
    List<UserInviteCountVO> selectUserInviteCount(@Param("minCount") Integer minCount, @Param("maxCount") Integer maxCount, @Param("lastMonth") boolean lastMonth);

    /**
     * 根据用户ID批量更新用户账户代理等级
     *
     * @author uncle.quentin
     * @date   2019/2/14 9:21
     * @param   uids
     * @param   agentLevelId
     * @return int
     * @version 1.0
     */
    int batchUpdateAgentLevelByUids(@Param("uids") List<Integer> uids, @Param("agentLevelId") Integer agentLevelId);
}