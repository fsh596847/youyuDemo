package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 * 还款记录列表Item实体
 **/
public class HXPayMoneyRecordListItemBean implements Serializable
{
	private String repayLoanId; //还款ID
	private String repayLoanNum; //还款编号
	private String transactionNo; //交易编号
	private String customerNum;//客户编号
	private String customerName;//客户名称
	private String projectId;//项目ID
	private String projectNo;//项目编码
	private String contractId;//合同ID
	private String contractNum;//合同编号
	private String startDate;//贷款起始日期
	private String expirationDate; //贷款截止日期
	private String loanNum; //贷款编号
	private String loanAmt; //贷款金额
	private String contractBalance;//贷款余额
	private String currencyCd;//币种
	private String creditProduct;//产品ID
	private String loanInt;//贷款利率
	private String applyOrgCd;//申请机构
	private String applyUserNum;//申请人
	private String repayDate;//还款日期
	private String payableDate; //应还日期
	private String payableTotalAmt; //应还总额
	private String payablePrincipal; //应还本金
	private String payableInt;//应还利息
	private String overduePi;//应还逾期本息
	private String overdueInt;//应还逾期利息
	private String actualOverdueInt;//实还逾期利息
	private String actualOverduePi;//实还拖欠本息
	private String actualPricipal;//实还本金
	private String actualInt;//实还利息
	private String repayAmt;//还款金额
	private String penalty;//违约金
	private String perRepaymenRate;//违约金比例
    private String remarks;//备注
    private String repamentStatus;//还款状态
    private String cleanCutCd;//还款结清方式
    private String createDate;//创建日期
    private String updateDate;//系统更新时间
    private String origLastOverdueDate;//最后逾期日期
    private String origLastRepayDate;//最近一次还款日期
    private String origOverdueDays;//累计逾期天数
    private String promisePenalty;//约定违约金
    private String fundsSource;//资金来源
    private String tableinterest;//表内利息
    private String intersetDeduction;//利息减免
    private String principalRelief;//本金减免
    private String loanId;//贷款ID
    private String periods;//还款期次
    private String repayType;//还款类型
    
	public String getRepayLoanId() {
		return repayLoanId;
	}


	public void setRepayLoanId(String repayLoanId) {
		this.repayLoanId = repayLoanId;
	}


	public String getRepayLoanNum() {
		return repayLoanNum;
	}


	public void setRepayLoanNum(String repayLoanNum) {
		this.repayLoanNum = repayLoanNum;
	}


	public String getTransactionNo() {
		return transactionNo;
	}


	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}


	public String getCustomerNum() {
		return customerNum;
	}


	public void setCustomerNum(String customerNum) {
		this.customerNum = customerNum;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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


	public String getLoanNum() {
		return loanNum;
	}


	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}


	public String getLoanAmt() {
		return loanAmt;
	}


	public void setLoanAmt(String loanAmt) {
		this.loanAmt = loanAmt;
	}


	public String getContractBalance() {
		return contractBalance;
	}


	public void setContractBalance(String contractBalance) {
		this.contractBalance = contractBalance;
	}


	public String getCurrencyCd() {
		return currencyCd;
	}


	public void setCurrencyCd(String currencyCd) {
		this.currencyCd = currencyCd;
	}


	public String getCreditProduct() {
		return creditProduct;
	}


	public void setCreditProduct(String creditProduct) {
		this.creditProduct = creditProduct;
	}


	public String getLoanInt() {
		return loanInt;
	}


	public void setLoanInt(String loanInt) {
		this.loanInt = loanInt;
	}


	public String getApplyOrgCd() {
		return applyOrgCd;
	}


	public void setApplyOrgCd(String applyOrgCd) {
		this.applyOrgCd = applyOrgCd;
	}


	public String getApplyUserNum() {
		return applyUserNum;
	}


	public void setApplyUserNum(String applyUserNum) {
		this.applyUserNum = applyUserNum;
	}


	public String getRepayDate() {
		return repayDate;
	}


	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}


	public String getPayableDate() {
		return payableDate;
	}


	public void setPayableDate(String payableDate) {
		this.payableDate = payableDate;
	}


	public String getPayableTotalAmt() {
		return payableTotalAmt;
	}


	public void setPayableTotalAmt(String payableTotalAmt) {
		this.payableTotalAmt = payableTotalAmt;
	}


	public String getPayablePrincipal() {
		return payablePrincipal;
	}


	public void setPayablePrincipal(String payablePrincipal) {
		this.payablePrincipal = payablePrincipal;
	}


	public String getPayableInt() {
		return payableInt;
	}


	public void setPayableInt(String payableInt) {
		this.payableInt = payableInt;
	}


	public String getOverduePi() {
		return overduePi;
	}


	public void setOverduePi(String overduePi) {
		this.overduePi = overduePi;
	}


	public String getOverdueInt() {
		return overdueInt;
	}


	public void setOverdueInt(String overdueInt) {
		this.overdueInt = overdueInt;
	}


	public String getActualOverdueInt() {
		return actualOverdueInt;
	}


	public void setActualOverdueInt(String actualOverdueInt) {
		this.actualOverdueInt = actualOverdueInt;
	}


	public String getActualOverduePi() {
		return actualOverduePi;
	}


	public void setActualOverduePi(String actualOverduePi) {
		this.actualOverduePi = actualOverduePi;
	}


	public String getActualPricipal() {
		return actualPricipal;
	}


	public void setActualPricipal(String actualPricipal) {
		this.actualPricipal = actualPricipal;
	}


	public String getActualInt() {
		return actualInt;
	}


	public void setActualInt(String actualInt) {
		this.actualInt = actualInt;
	}


	public String getRepayAmt() {
		return repayAmt;
	}


	public void setRepayAmt(String repayAmt) {
		this.repayAmt = repayAmt;
	}


	public String getPenalty() {
		return penalty;
	}


	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}


	public String getPerRepaymenRate() {
		return perRepaymenRate;
	}


	public void setPerRepaymenRate(String perRepaymenRate) {
		this.perRepaymenRate = perRepaymenRate;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getRepamentStatus() {
		return repamentStatus;
	}


	public void setRepamentStatus(String repamentStatus) {
		this.repamentStatus = repamentStatus;
	}


	public String getCleanCutCd() {
		return cleanCutCd;
	}


	public void setCleanCutCd(String cleanCutCd) {
		this.cleanCutCd = cleanCutCd;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}


	public String getOrigLastOverdueDate() {
		return origLastOverdueDate;
	}


	public void setOrigLastOverdueDate(String origLastOverdueDate) {
		this.origLastOverdueDate = origLastOverdueDate;
	}


	public String getOrigLastRepayDate() {
		return origLastRepayDate;
	}


	public void setOrigLastRepayDate(String origLastRepayDate) {
		this.origLastRepayDate = origLastRepayDate;
	}


	public String getOrigOverdueDays() {
		return origOverdueDays;
	}


	public void setOrigOverdueDays(String origOverdueDays) {
		this.origOverdueDays = origOverdueDays;
	}


	public String getPromisePenalty() {
		return promisePenalty;
	}


	public void setPromisePenalty(String promisePenalty) {
		this.promisePenalty = promisePenalty;
	}


	public String getFundsSource() {
		return fundsSource;
	}


	public void setFundsSource(String fundsSource) {
		this.fundsSource = fundsSource;
	}


	public String getTableinterest() {
		return tableinterest;
	}


	public void setTableinterest(String tableinterest) {
		this.tableinterest = tableinterest;
	}


	public String getIntersetDeduction() {
		return intersetDeduction;
	}


	public void setIntersetDeduction(String intersetDeduction) {
		this.intersetDeduction = intersetDeduction;
	}


	public String getPrincipalRelief() {
		return principalRelief;
	}


	public void setPrincipalRelief(String principalRelief) {
		this.principalRelief = principalRelief;
	}


	public String getLoanId() {
		return loanId;
	}


	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}


	public String getPeriods() {
		return periods;
	}


	public void setPeriods(String periods) {
		this.periods = periods;
	}


	public String getRepayType() {
		return repayType;
	}


	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}


	@Override
	public String toString() {
		return "HXPayMoneyRecordListItemBean [repayLoanId=" + repayLoanId
				+ ", repayLoanNum=" + repayLoanNum + ", transactionNo="
				+ transactionNo + ", customerNum=" + customerNum
				+ ", customerName=" + customerName + ", projectId=" + projectId
				+ ", projectNo=" + projectNo + ", contractId=" + contractId
				+ ", contractNum=" + contractNum + ", startDate=" + startDate
				+ ", expirationDate=" + expirationDate + ", loanNum=" + loanNum
				+ ", loanAmt=" + loanAmt + ", contractBalance="
				+ contractBalance + ", currencyCd=" + currencyCd
				+ ", creditProduct=" + creditProduct + ", loanInt=" + loanInt
				+ ", applyOrgCd=" + applyOrgCd + ", applyUserNum="
				+ applyUserNum + ", repayDate=" + repayDate + ", payableDate="
				+ payableDate + ", payableTotalAmt=" + payableTotalAmt
				+ ", payablePrincipal=" + payablePrincipal + ", payableInt="
				+ payableInt + ", overduePi=" + overduePi + ", overdueInt="
				+ overdueInt + ", actualOverdueInt=" + actualOverdueInt
				+ ", actualOverduePi=" + actualOverduePi + ", actualPricipal="
				+ actualPricipal + ", actualInt=" + actualInt + ", repayAmt="
				+ repayAmt + ", penalty=" + penalty + ", perRepaymenRate="
				+ perRepaymenRate + ", remarks=" + remarks
				+ ", repamentStatus=" + repamentStatus + ", cleanCutCd="
				+ cleanCutCd + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", origLastOverdueDate=" + origLastOverdueDate
				+ ", origLastRepayDate=" + origLastRepayDate
				+ ", origOverdueDays=" + origOverdueDays + ", promisePenalty="
				+ promisePenalty + ", fundsSource=" + fundsSource
				+ ", tableinterest=" + tableinterest + ", intersetDeduction="
				+ intersetDeduction + ", principalRelief=" + principalRelief
				+ ", loanId=" + loanId + ", periods=" + periods
				+ ", repayType=" + repayType + "]";
	}


	
    
}
