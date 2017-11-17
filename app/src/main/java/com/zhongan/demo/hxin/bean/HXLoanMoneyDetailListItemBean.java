package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 * 借款列表Item数据
 **/
public class HXLoanMoneyDetailListItemBean implements Serializable
{

	private String repayingPlanDetailId; //还款计划明细ID
	private String repayingPlanId; //还款计划ID
	private String currentPeriod; //期数
	private String currentEndDate;//计划还款日
	private String currentPrincipal;//应还本金
	private String currentInterest;//应还利息
	private String currentPrincipalInterest;//应还总额本息
	private String endCurrentPrincipal;//截止当期累计应还本
	private String endCurrentPrincipalBalance;//截至当期本金余额
	private String endCurrentInterest;//截止当期累计应还息
	private String overFee; //逾期手续费
	private String overInt; //逾期利息
	private String status; //状态
	private String repayingDate;//实际还款日期
	private String repayedPrincipal;//已还本金
	private String repayedInterest;//已还利息
	private String repayedImposeInterest;//已还逾期利息
	private String repayedTotalamount;//已还总金额
	private String overdueDays;//逾期天数
	private String notPayPrincipal;//未还本金
	private String notPayInterest; //未还利息
	private String notPayOverdueInt; //未还逾期利息
	private String notPayTotalAmt;//未还总额
	private String currentFee;//手续费
	private String isCurrentTerm;//是否当期 0：当期 1:不是当期
	private String overdueredFlag;//逾期可还标识 00：可还  01:不可还
	private String interestAddFee;//利息加费用
	public String getRepayingPlanDetailId() {
		return repayingPlanDetailId;
	}
	public void setRepayingPlanDetailId(String repayingPlanDetailId) {
		this.repayingPlanDetailId = repayingPlanDetailId;
	}
	public String getRepayingPlanId() {
		return repayingPlanId;
	}
	public void setRepayingPlanId(String repayingPlanId) {
		this.repayingPlanId = repayingPlanId;
	}
	public String getCurrentPeriod() {
		return currentPeriod;
	}
	public void setCurrentPeriod(String currentPeriod) {
		this.currentPeriod = currentPeriod;
	}
	public String getCurrentEndDate() {
		return currentEndDate;
	}
	public void setCurrentEndDate(String currentEndDate) {
		this.currentEndDate = currentEndDate;
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
	public String getCurrentPrincipalInterest() {
		return currentPrincipalInterest;
	}
	public void setCurrentPrincipalInterest(String currentPrincipalInterest) {
		this.currentPrincipalInterest = currentPrincipalInterest;
	}
	public String getEndCurrentPrincipal() {
		return endCurrentPrincipal;
	}
	public void setEndCurrentPrincipal(String endCurrentPrincipal) {
		this.endCurrentPrincipal = endCurrentPrincipal;
	}
	public String getEndCurrentPrincipalBalance() {
		return endCurrentPrincipalBalance;
	}
	public void setEndCurrentPrincipalBalance(String endCurrentPrincipalBalance) {
		this.endCurrentPrincipalBalance = endCurrentPrincipalBalance;
	}
	public String getEndCurrentInterest() {
		return endCurrentInterest;
	}
	public void setEndCurrentInterest(String endCurrentInterest) {
		this.endCurrentInterest = endCurrentInterest;
	}
	public String getOverFee() {
		return overFee;
	}
	public void setOverFee(String overFee) {
		this.overFee = overFee;
	}
	public String getOverInt() {
		return overInt;
	}
	public void setOverInt(String overInt) {
		this.overInt = overInt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRepayingDate() {
		return repayingDate;
	}
	public void setRepayingDate(String repayingDate) {
		this.repayingDate = repayingDate;
	}
	public String getRepayedPrincipal() {
		return repayedPrincipal;
	}
	public void setRepayedPrincipal(String repayedPrincipal) {
		this.repayedPrincipal = repayedPrincipal;
	}
	public String getRepayedInterest() {
		return repayedInterest;
	}
	public void setRepayedInterest(String repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	public String getRepayedImposeInterest() {
		return repayedImposeInterest;
	}
	public void setRepayedImposeInterest(String repayedImposeInterest) {
		this.repayedImposeInterest = repayedImposeInterest;
	}
	public String getRepayedTotalamount() {
		return repayedTotalamount;
	}
	public void setRepayedTotalamount(String repayedTotalamount) {
		this.repayedTotalamount = repayedTotalamount;
	}
	public String getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(String overdueDays) {
		this.overdueDays = overdueDays;
	}
	public String getNotPayPrincipal() {
		return notPayPrincipal;
	}
	public void setNotPayPrincipal(String notPayPrincipal) {
		this.notPayPrincipal = notPayPrincipal;
	}
	public String getNotPayInterest() {
		return notPayInterest;
	}
	public void setNotPayInterest(String notPayInterest) {
		this.notPayInterest = notPayInterest;
	}
	public String getNotPayOverdueInt() {
		return notPayOverdueInt;
	}
	public void setNotPayOverdueInt(String notPayOverdueInt) {
		this.notPayOverdueInt = notPayOverdueInt;
	}
	public String getNotPayTotalAmt() {
		return notPayTotalAmt;
	}
	public void setNotPayTotalAmt(String notPayTotalAmt) {
		this.notPayTotalAmt = notPayTotalAmt;
	}
	public String getCurrentFee() {
		return currentFee;
	}
	public void setCurrentFee(String currentFee) {
		this.currentFee = currentFee;
	}
	
	public String getIsCurrentTerm() {
		return isCurrentTerm;
	}
	public void setIsCurrentTerm(String isCurrentTerm) {
		this.isCurrentTerm = isCurrentTerm;
	}
	public String getOverdueredFlag() {
		return overdueredFlag;
	}
	public void setOverdueredFlag(String overdueredFlag) {
		this.overdueredFlag = overdueredFlag;
	}
	public String getInterestAddFee() {
		return interestAddFee;
	}
	public void setInterestAddFee(String interestAddFee) {
		this.interestAddFee = interestAddFee;
	}
	@Override
	public String toString() {
		return "HXLoanMoneyDetailListItemBean [repayingPlanDetailId="
				+ repayingPlanDetailId + ", repayingPlanId=" + repayingPlanId
				+ ", currentPeriod=" + currentPeriod + ", currentEndDate="
				+ currentEndDate + ", currentPrincipal=" + currentPrincipal
				+ ", currentInterest=" + currentInterest
				+ ", currentPrincipalInterest=" + currentPrincipalInterest
				+ ", endCurrentPrincipal=" + endCurrentPrincipal
				+ ", endCurrentPrincipalBalance=" + endCurrentPrincipalBalance
				+ ", endCurrentInterest=" + endCurrentInterest + ", overFee="
				+ overFee + ", overInt=" + overInt + ", status=" + status
				+ ", repayingDate=" + repayingDate + ", repayedPrincipal="
				+ repayedPrincipal + ", repayedInterest=" + repayedInterest
				+ ", repayedImposeInterest=" + repayedImposeInterest
				+ ", repayedTotalamount=" + repayedTotalamount
				+ ", overdueDays=" + overdueDays + ", notPayPrincipal="
				+ notPayPrincipal + ", notPayInterest=" + notPayInterest
				+ ", notPayOverdueInt=" + notPayOverdueInt
				+ ", notPayTotalAmt=" + notPayTotalAmt + ", currentFee="
				+ currentFee + ", isCurrentTerm=" + isCurrentTerm
				+ ", overdueredFlag=" + overdueredFlag + ", interestAddFee="
				+ interestAddFee + "]";
	}
	
}
