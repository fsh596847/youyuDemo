package com.zhongan.demo.hxin.bean;

public class HomeProductItem {
	private String id;

	private String productNum;

	private String productName="asdfasfdasd";

	private String productType;

	private String remark;

	private String effectiveDate;

	private String expirationDate="asdfasddfbsdfh";

	private String status;

	private String updateTime;

	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
	public void setProductNum(String productNum){
		this.productNum = productNum;
	}
	public String getProductNum(){
		return this.productNum;
	}
	public void setProductName(String productName){
		this.productName = productName;
	}
	public String getProductName(){
		return this.productName;
	}
	public void setProductType(String productType){
		this.productType = productType;
	}
	public String getProductType(){
		return this.productType;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public String getRemark(){
		return this.remark;
	}
	public void setEffectiveDate(String effectiveDate){
		this.effectiveDate = effectiveDate;
	}
	public String getEffectiveDate(){
		return this.effectiveDate;
	}
	public void setExpirationDate(String expirationDate){
		this.expirationDate = expirationDate;
	}
	public String getExpirationDate(){
		return this.expirationDate;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return this.status;
	}
	public void setUpdateTime(String updateTime){
		this.updateTime = updateTime;
	}
	public String getUpdateTime(){
		return this.updateTime;
	}

}
