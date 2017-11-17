package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 *全额还款试算
 **/
public class HXPayAllMoneyListItemBean implements Serializable
{
	private String currentPeriod;//期数
	private String currentPrincipalInterest;//应还总额
	private String currentPrincipal;//应还本金
	private String currentInterest;//应还利息
	private String endCurrentPrincipalBalance;//剩余本金
	private String repayDate;//还款日期
	public String getCurrentPeriod() {
		return currentPeriod;
	}
	public void setCurrentPeriod(String currentPeriod) {
		this.currentPeriod = currentPeriod;
	}
	public String getCurrentPrincipalInterest() {
		return currentPrincipalInterest;
	}
	public void setCurrentPrincipalInterest(String currentPrincipalInterest) {
		this.currentPrincipalInterest = currentPrincipalInterest;
	}
	public String getCurrentPrincipal() {
		return currentPrincipal;
	}
	public void setCurrentPrincipal(String currentPrincipal) {
		this.currentPrincipal = currentPrincipal;
	}
	public String getCurrentInterest() {
		return currentInterest;
	}
	public void setCurrentInterest(String currentInterest) {
		this.currentInterest = currentInterest;
	}
	public String getEndCurrentPrincipalBalance() {
		return endCurrentPrincipalBalance;
	}
	public void setEndCurrentPrincipalBalance(String endCurrentPrincipalBalance) {
		this.endCurrentPrincipalBalance = endCurrentPrincipalBalance;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	
}
