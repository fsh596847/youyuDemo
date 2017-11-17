package com.zhongan.demo.hxin.bean;

import java.util.List;

public class HXHomeProduct {

	private String transCode;

	private String channelNo;

	private String returnCode;

	private String returnMsg;

	private int totalCount;

	private int rowsCount;

	private List<HomeProductItem> rows ;

	public void setTransCode(String transCode){
		this.transCode = transCode;
	}
	public String getTransCode(){
		return this.transCode;
	}
	public void setChannelNo(String channelNo){
		this.channelNo = channelNo;
	}
	public String getChannelNo(){
		return this.channelNo;
	}
	public void setReturnCode(String returnCode){
		this.returnCode = returnCode;
	}
	public String getReturnCode(){
		return this.returnCode;
	}
	public void setReturnMsg(String returnMsg){
		this.returnMsg = returnMsg;
	}
	public String getReturnMsg(){
		return this.returnMsg;
	}
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}
	public int getTotalCount(){
		return this.totalCount;
	}
	public void setRowsCount(int rowsCount){
		this.rowsCount = rowsCount;
	}
	public int getRowsCount(){
		return this.rowsCount;
	}
	public void setRows(List<HomeProductItem> rows){
		this.rows = rows;
	}
	public List<HomeProductItem> getRows(){
		return this.rows;
	}
}
