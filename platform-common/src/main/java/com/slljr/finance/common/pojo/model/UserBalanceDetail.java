package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
/**
 * @author XueYi
 * 2018年12月4日 下午2:04:08
 */
public class UserBalanceDetail implements Serializable{
    public enum TypeEnum{
        INTEGRAL(1,"积分"), CASH(2,"现金");
        int key;
        String msg;
        TypeEnum(int key, String msg){
            this.key = key;
            this.msg = msg;
        }
        public static TypeEnum get(int key){
            for(TypeEnum type : TypeEnum.values()){
                if (key == type.getKey()) return type;
            }

            return INTEGRAL;
        }
        public int getKey(){
            return key;
        }
        public String getMsg(){
            return msg;
        }
    }

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 3257790954938316035L;

	//主键id
    private Integer id;

    //用户id
    private Integer uid;

    //增减金额
    private Double number;

    //增减原因
    private String summary;

    //类型[1积分,2余额]
    private Integer type;

    //余额
    private Double balance;

    //状态[-1删除,0正常]
    private Integer status;

    //创建时间
    private String createTime;

    //修改时间
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "UserBalanceDetail [id=" + id + ", uid=" + uid + ", number=" + number + ", summary=" + summary
				+ ", type=" + type + ", balance=" + balance + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
}