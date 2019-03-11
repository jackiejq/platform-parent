package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserBankCard;import com.slljr.finance.common.pojo.vo.UserBankCardVo;import org.apache.ibatis.annotations.Param;import java.util.List;

public interface UserBankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBankCard record);

    int insertSelective(UserBankCard record);

    UserBankCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBankCard record);

    int updateByPrimaryKey(UserBankCard record);

    List<UserBankCardVo> findByUid(@Param("uid")Integer uid);


}