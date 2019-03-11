package com.slljr.finance.admin.scheduled;

import com.slljr.finance.admin.mapper.UserAccountMapper;
import com.slljr.finance.admin.service.AgentLevelService;
import com.slljr.finance.common.pojo.model.AgentLevel;
import com.slljr.finance.common.pojo.vo.UserInviteCountVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 代理等级定时任务
 * @author: uncle.quentin.
 * @date: 2019/1/10.
 * @time: 15:19.
 */
@Service
@EnableScheduling
public class AgentLevelTask {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private AgentLevelService agentLevelService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 更新用户代理等级
     *
     * @param
     * @return void
     * @author uncle.quentin
     * @date 2019/2/13 16:07
     * @version 1.0
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateLevel() {
        log.info("updateLevel任务开始→→→→→→→→→→→→→→→→→");
        //代理等级配置(大客户类型，累计邀请用户)
        excute(2, false);

        //代理等级配置(大客户类型，上月邀请用户)
        excute(1, true);

        log.info("updateLevel任务结束→→→→→→→→→→→→→→→→→");
    }

    /**
     * 判断并更新代理等级
     *
     * @param type      2：大客户类型 1：上月邀请用户
     * @param lastMonth true：表示查询上个月数据，false：查询所有
     * @return void
     * @author uncle.quentin
     * @date 2019/2/14 10:53
     * @version 1.0
     */
    private void excute(Integer type, Boolean lastMonth) {
        List<AgentLevel> agentLevels = agentLevelService.selectAgentLevelByType(type);
        for (AgentLevel agentLevel : agentLevels) {
            Integer min = agentLevel.getMinNewMember();
            Integer max = agentLevel.getMaxNewMember();

            //需要更新用户
            List<UserInviteCountVO> userInviteCounts = userAccountMapper.selectUserInviteCount(min, max, lastMonth);

            List<Integer> uids = new ArrayList<>();
            for (UserInviteCountVO userInviteCount : userInviteCounts) {
                uids.add(userInviteCount.getUid());
            }

            //批量更新用户代理等级
            if (uids.size() > 0) {
                userAccountMapper.batchUpdateAgentLevelByUids(uids, agentLevel.getId());
            }
        }
    }

}
