package com.ligootech.webdtu.entity.core;

import java.util.List;

public class PageDtuMongo {
	private long pageCount;
	private int pageIndex;
	private List<DtuMongo> list;
	
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<DtuMongo> getList() {
		return list;
	}
	public void setList(List<DtuMongo> list) {
		this.list = list;
	}
	

	
}
