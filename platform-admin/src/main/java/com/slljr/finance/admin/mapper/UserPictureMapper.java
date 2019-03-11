package com.slljr.finance.admin.mapper;

import com.slljr.finance.common.pojo.model.UserPicture;

public interface UserPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPicture record);

    int insertSelective(UserPicture record);

    UserPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPicture record);

    int updateByPrimaryKey(UserPicture record);
}