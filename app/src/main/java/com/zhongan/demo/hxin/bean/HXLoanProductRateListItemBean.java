package com.zhongan.demo.hxin.bean;


import java.io.Serializable;

/**
 * 产品定价利率实体
 **/
public class HXLoanProductRateListItemBean implements Serializable
{
	private String bizRateId;//产品利率id
	private String prodId; //产品id
	private String prodNum; //产品编号
	private String term; //贷款期限
	private String termUnit;//期限单位
	private String rate;//贷款利率
	public String getBizRateId() {
		return bizRateId;
	}
	public void setBizRateId(String bizRateId) {
		this.bizRateId = bizRateId;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdNum() {
		return prodNum;
	}
	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTermUnit() {
		return termUnit;
	}
	public void setTermUnit(String termUnit) {
		this.termUnit = termUnit;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	@Override
	public String toString() {
		return "HXLoanProductRateListItemBean [bizRateId=" + bizRateId + ", prodId="
				+ prodId + ", prodNum=" + prodNum + ", term=" + term
				+ ", termUnit=" + termUnit + ", rate=" + rate + "]";
	}
	
}
