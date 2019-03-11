package com.slljr.finance.front.mapper;
import com.slljr.finance.common.pojo.dto.UserSignLogDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.slljr.finance.common.pojo.model.UserSignLog;

public interface UserSignLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSignLog record);

    int insertSelective(UserSignLog record);

    UserSignLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSignLog record);

    int updateByPrimaryKey(UserSignLog record);

    List<UserSignLog> findByUidAndSignTime(@Param("uid")Integer uid,@Param("signTime")Date signTime);

    List<UserSignLog> findByAll(UserSignLogDTO userSignLog);

}