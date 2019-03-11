package com.slljr.finance.front.mapper;
import com.slljr.finance.common.pojo.vo.IdcardAddrVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.slljr.finance.common.pojo.model.IdcardAddr;

public interface IdcardAddrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdcardAddr record);

    int insertSelective(IdcardAddr record);

    IdcardAddr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdcardAddr record);

    int updateByPrimaryKey(IdcardAddr record);

    IdcardAddrVO findByCode(@Param("code")String code);

}