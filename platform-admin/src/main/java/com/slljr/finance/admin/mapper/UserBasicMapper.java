package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.StatisticsResultVO;
import com.slljr.finance.common.pojo.vo.UserBasicByVO;

import org.apache.ibatis.annotations.Param;import java.util.List;

public interface UserBasicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBasic record);

    int insertSelective(UserBasic record);

    UserBasic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBasic record);

    int updateByPrimaryKey(UserBasic record);

    List<UserBasicByVO> findPtUserByAll(UserBasic userBasic);

    List<UserBasic> findAdminUserByAll(UserBasic userBasic);

    UserBasic findByPhoneAndPasswordAndType(@Param("phone") String phone, @Param("password") String password, @Param("type") Integer type);

    /**
     * 近30天按天统计新增会员数
     *
     * @param days
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsNewUserByDay(@Param("days") List<Integer> days);

    /**
     * 近12个月按天统计新增会员数
     *
     * @param months
     * @return java.util.List<com.slljr.finance.common.pojo.vo.StatisticsResultVO>
     * @author uncle.quentin
     * @date 2019/1/22 14:04
     * @version 1.0
     */
    List<StatisticsResultVO> statisticsNewUserByMonth(@Param("months") List<Integer> months);
}