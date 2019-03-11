package com.slljr.finance.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slljr.finance.common.pojo.model.UserSignLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description:
 * @author: uncle.quentin.
 * @date: 2019/1/7.
 * @time: 16:47.
 */
public class UserSignLogDTO extends UserSignLog {

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
