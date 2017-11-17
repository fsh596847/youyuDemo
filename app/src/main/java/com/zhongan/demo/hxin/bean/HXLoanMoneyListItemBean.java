package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 * 借款列表Item数据
 **/
public class HXLoanMoneyListItemBean implements Serializable
{
	private String contractId; //合同ID
	private String contractNum; //合同编号
	private String projectId; //业务ID
	private String projectNo;//业务编号
	private String productId;//贷款产品ID
	private String productName;//产品名称
	private String productNum;//产品编号
	private String contractTerm;//合同期限
	private String contractTermUnit;//合同期限单位
	private String currency;//币种
	
	
	
	private String contractAmt; //贷款金额
	private String repayMode; //还款方式
	private String repayPrincipalMonthes; //还款周期月数
	private String purpose;//贷款用途
	private String contractRate;//贷款利率
	private String cumulativePayoutAmt;//合同累计发放金额
	private String cumulativeRepayAmt;//合同累计还款金额
	private String contractBalance;//合同余额
	private String contractAvaliableAmt;//合同可用金额
	private String lastRepayDate;//最近一次还款日
	
	
	private String loanStatus; //贷款状态
	private String contractValidState; //合同生效状态
	private String repayingPlanDetailId; //还款计划明细ID
	private String repayingPlanId;//还款计划ID
	private String currentPeriod;//期数
	private String currentEndDate;//计划还款日
	private String currentPrincipal;//应还本金
	private String currentInterest;//应还利息
	private String currentPrincipalInterest;//应还总额本息
	private String overInt;//逾期利息
	
	
	private String overFee;//逾期手续费
	private String status;//状态
	private String loanId;//贷款ID
	private String startDate;//合同起始日期
	private String expirationDate;//合同到期日期
    private String notPayPrincipal;//未还金额
    private String notPayInterest;//未还利息
    private String dayRate;//日利率
    private String loanBank;//贷款银行
    private String payBankName;//还款银行名称
    private String payBankType;//还款银行类型
    private String payBankAcct;//还款银卡卡号后四位
    private String contractOverdueRate;//逾期利率
    private String notPayFee;//未还手续费
    private String contractUrl;//合同URL

	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	public String getContractTermUnit() {
		return contractTermUnit;
	}
	public void setContractTermUnit(String contractTermUnit) {
		this.contractTermUnit = contractTermUnit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getContractAmt() {
		return contractAmt;
	}
	public void setContractAmt(String contractAmt) {
		this.contractAmt = contractAmt;
	}
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getRepayPrincipalMonthes() {
		return repayPrincipalMonthes;
	}
	public void setRepayPrincipalMonthes(String repayPrincipalMonthes) {
		this.repayPrincipalMonthes = repayPrincipalMonthes;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getContractRate() {
		return contractRate;
	}
	public void setContractRate(String contractRate) {
		this.contractRate = contractRate;
	}
	public String getCumulativePayoutAmt() {
		return cumulativePayoutAmt;
	}
	public void setCumulativePayoutAmt(String cumulativePayoutAmt) {
		this.cumulativePayoutAmt = cumulativePayoutAmt;
	}
	public String getCumulativeRepayAmt() {
		return cumulativeRepayAmt;
	}
	public void setCumulativeRepayAmt(String cumulativeRepayAmt) {
		this.cumulativeRepayAmt = cumulativeRepayAmt;
	}
	public String getContractBalance() {
		return contractBalance;
	}
	public void setContractBalance(String contractBalance) {
		this.contractBalance = contractBalance;
	}
	public String getContractAvaliableAmt() {
		return contractAvaliableAmt;
	}
	public void setContractAvaliableAmt(String contractAvaliableAmt) {
		this.contractAvaliableAmt = contractAvaliableAmt;
	}
	public String getLastRepayDate() {
		return lastRepayDate;
	}
	public void setLastRepayDate(String lastRepayDate) {
		this.lastRepayDate = lastRepayDate;
	}
	public String getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	public String getContractValidState() {
		return contractValidState;
	}
	public void setContractValidState(String contractValidState) {
		this.contractValidState = contractValidState;
	}
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
	public String getOverInt() {
		return overInt;
	}
	public void setOverInt(String overInt) {
		this.overInt = overInt;
	}
	public String getOverFee() {
		return overFee;
	}
	public void setOverFee(String overFee) {
		this.overFee = overFee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
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
	public String getDayRate() {
		return dayRate;
	}
	public void setDayRate(String dayRate) {
		this.dayRate = dayRate;
	}
	public String getLoanBank() {
		return loanBank;
	}
	public void setLoanBank(String loanBank) {
		this.loanBank = loanBank;
	}
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	public String getPayBankType() {
		return payBankType;
	}
	public void setPayBankType(String payBankType) {
		this.payBankType = payBankType;
	}
	public String getPayBankAcct() {
		return payBankAcct;
	}
	public void setPayBankAcct(String payBankAcct) {
		this.payBankAcct = payBankAcct;
	}
	public String getContractOverdueRate() {
		return contractOverdueRate;
	}
	public void setContractOverdueRate(String contractOverdueRate) {
		this.contractOverdueRate = contractOverdueRate;
	}
	public String getNotPayFee() {
		return notPayFee;
	}
	public void setNotPayFee(String notPayFee) {
		this.notPayFee = notPayFee;
	}
	public String getContractUrl() {
		return contractUrl;
	}
	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
	@Override
	public String toString() {
		return "HXLoanMoneyListItemBean [contractId=" + contractId
				+ ", contractNum=" + contractNum + ", projectId=" + projectId
				+ ", projectNo=" + projectNo + ", productId=" + productId
				+ ", productName=" + productName + ", productNum=" + productNum
				+ ", contractTerm=" + contractTerm + ", contractTermUnit="
				+ contractTermUnit + ", currency=" + currency
				+ ", contractAmt=" + contractAmt + ", repayMode=" + repayMode
				+ ", repayPrincipalMonthes=" + repayPrincipalMonthes
				+ ", purpose=" + purpose + ", contractRate=" + contractRate
				+ ", cumulativePayoutAmt=" + cumulativePayoutAmt
				+ ", cumulativeRepayAmt=" + cumulativeRepayAmt
				+ ", contractBalance=" + contractBalance
				+ ", contractAvaliableAmt=" + contractAvaliableAmt
				+ ", lastRepayDate=" + lastRepayDate + ", loanStatus="
				+ loanStatus + ", contractValidState=" + contractValidState
				+ ", repayingPlanDetailId=" + repayingPlanDetailId
				+ ", repayingPlanId=" + repayingPlanId + ", currentPeriod="
				+ currentPeriod + ", currentEndDate=" + currentEndDate
				+ ", currentPrincipal=" + currentPrincipal
				+ ", currentInterest=" + currentInterest
				+ ", currentPrincipalInterest=" + currentPrincipalInterest
				+ ", overInt=" + overInt + ", overFee=" + overFee + ", status="
				+ status + ", loanId=" + loanId + ", startDate=" + startDate
				+ ", expirationDate=" + expirationDate + ", notPayPrincipal="
				+ notPayPrincipal + ", notPayInterest=" + notPayInterest
				+ ", dayRate=" + dayRate + ", loanBank=" + loanBank
				+ ", payBankName=" + payBankName + ", payBankType="
				+ payBankType + ", payBankAcct=" + payBankAcct
				+ ", contractOverdueRate=" + contractOverdueRate
				+ ", notPayFee=" + notPayFee + ", contractUrl=" + contractUrl
				+ "]";
	}
	
}
