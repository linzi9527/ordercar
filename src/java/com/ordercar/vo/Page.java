package com.ordercar.vo;

import java.util.List;

/**
 * 分页实体
 * @author zlzhaoe
 * @version	2020年09月08日
 */
public class Page<T> {
	
	private int pageSize=10;//每页显示多少条记录
	private int pageNow;//希望显示第几页
	private int pageCount;//
	private int rowCount;//一共有多少一共有多少页条记录
	private List<?> list=null;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNow() {
		return pageNow;
	}
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	

	public int getPageCount(){
		if(rowCount%pageSize==0){
			pageCount=rowCount/pageSize;
		}else{
			pageCount=rowCount/pageSize+1;
		}
		return pageCount;
	}
	
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}

}
