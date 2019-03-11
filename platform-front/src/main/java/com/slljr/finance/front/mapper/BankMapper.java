package com.slljr.finance.front.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.slljr.finance.common.pojo.model.Bank;

public interface BankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bank record);

    int insertSelective(Bank record);

    Bank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);

    Bank findByCode(@Param("code") String code);

    /**
     * 根据银行编号和名称查询银行信息
     *
     * @param code
     * @param name
     * @return com.slljr.finance.common.pojo.model.Bank
     * @author uncle.quentin
     * @date 2018/12/26 16:21
     * @version 1.0
     */

    Bank findByCodeOrName(@Param("code") String code, @Param("name") String name);

    /**
     * 根据银行名称查询
     *
     * @param name
     * @return com.slljr.finance.common.pojo.model.Bank
     * @author uncle.quentin
     * @date 2018/12/26 17:48
     * @version 1.0
     */
    Bank findByName(@Param("name") String name);

    /**
     * 根据银行名称修改小图标URL
     *
     * @param updatedSmallLogoUrl 小图标URL
     * @param name                银行名称
     * @return int
     * @author uncle.quentin
     * @date 2018/12/26 19:23
     * @version 1.0
     */
    int updateSmallLogoUrlByName(@Param("updatedSmallLogoUrl") String updatedSmallLogoUrl, @Param("name") String name);


}