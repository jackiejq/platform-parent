package com.slljr.finance.common.pojo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 1、商品分类的实体类
 * @author XueYi
 * 2018年12月4日 下午1:49:54
 */
public class GoodsCategory implements Serializable{
    /**
	 * 序列化
	 */
	private static final long serialVersionUID = -8640138209732743076L;

	//主键id
	private Integer id;

	//商品类别
	private String name;
	
	private Integer parentId;

    //状态[-1删除,0正常]
    private Integer status;

    //创建时间
    private Date createTime;

    //修改时间
    private Date updateTime;
    
    //子节点数据
   private List<GoodsCategory> categories = new ArrayList<GoodsCategory>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<GoodsCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<GoodsCategory> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "GoodsCategory [id=" + id + ", name=" + name + ", parentId=" + parentId + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", categories=" + categories + "]";
	}
}