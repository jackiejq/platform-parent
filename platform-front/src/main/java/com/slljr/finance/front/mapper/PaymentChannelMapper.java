package com.slljr.finance.front.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.slljr.finance.common.pojo.model.PaymentChannel;

public interface PaymentChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaymentChannel record);

    int insertSelective(PaymentChannel record);

    PaymentChannel selectByPrimaryKey(Integer id);

    List<PaymentChannel> findByType(@Param("type")Integer type);

    List<PaymentChannel> findByStatus(@Param("status")Integer status);

    /**
     *
     * @param type
     * @param status 状态[-1维护中, 0停用, 1启用]
     * @return
     */
    List<PaymentChannel> findByTypeAndStatus(@Param("type")Integer type,@Param("status")Integer status);



    int updateByPrimaryKeySelective(PaymentChannel record);

    int updateByPrimaryKey(PaymentChannel record);
}