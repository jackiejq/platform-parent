package com.slljr.finance.common.pojo.model;

public class ForumParentChild {
    /**
	* 主键
	*/
    private Integer id;

    /**
	* 父ID
	*/
    private Integer parentId;

    /**
	* 子ID
	*/
    private Integer childId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }
}