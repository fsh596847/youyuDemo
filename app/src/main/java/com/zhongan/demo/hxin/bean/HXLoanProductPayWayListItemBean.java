package com.zhongan.demo.hxin.bean;


import java.io.Serializable;

/**
 * 产品还款方式实体
 **/
public class HXLoanProductPayWayListItemBean implements Serializable
{
	private String repayWayId;//还款方式id
	private String repayMode;// 还款方式
	private String repayWayName; //还款方式名称
	private String repaymentDateType; //还款日类型
	private String repayDay; //还款日
	private String graceDay;//免息天数
	public String getRepayWayId() {
		return repayWayId;
	}
	public void setRepayWayId(String repayWayId) {
		this.repayWayId = repayWayId;
	}
	
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getRepayWayName() {
		return repayWayName;
	}
	public void setRepayWayName(String repayWayName) {
		this.repayWayName = repayWayName;
	}
	public String getRepaymentDateType() {
		return repaymentDateType;
	}
	public void setRepaymentDateType(String repaymentDateType) {
		this.repaymentDateType = repaymentDateType;
	}
	public String getRepayDay() {
		return repayDay;
	}
	public void setRepayDay(String repayDay) {
		this.repayDay = repayDay;
	}
	public String getGraceDay() {
		return graceDay;
	}
	public void setGraceDay(String graceDay) {
		this.graceDay = graceDay;
	}
	@Override
	public String toString() {
		return "HXLoanProductPayWayListItemBean [repayWayId=" + repayWayId
				+ ", repayMode=" + repayMode + ", repayWayName=" + repayWayName
				+ ", repaymentDateType=" + repaymentDateType + ", repayDay="
				+ repayDay + ", graceDay=" + graceDay + "]";
	}
	
	
}
