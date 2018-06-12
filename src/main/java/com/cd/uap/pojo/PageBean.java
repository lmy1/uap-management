package com.cd.uap.pojo;

import java.util.List;

/**
 * 分页bean
 */

public class PageBean<T> {

    // 总条数
    private Long totalNum;
    // 分页结果
    private List<T> items;

    public PageBean() {
        super();
    }

	public PageBean(Long totalNum, List<T> items) {
		super();
		this.totalNum = totalNum;
		this.items = items;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

   
}
