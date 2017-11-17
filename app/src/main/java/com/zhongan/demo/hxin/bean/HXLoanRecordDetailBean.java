package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 * 还款方式
 **/
public class HXLoanRecordDetailBean implements Serializable
{
	private String transCode;//交易码
	private String channelNo;//渠道标识
	private String clientToken;//令牌
	private String returnCode;//交易信息码
	private String returnMsg;//交易返回信息
	
	private String customerName; //客户名称
	private String identityCardNo;//身份证号
	
	private String applyAmt; //贷款金额
	private String period; //贷款期限
	private String startDate; //起始日期
	private String endDate;//结束日期
	private String dayRate;//日利率
	private String repayMode;//还款方式
	private String repayBankName;//还款银行名称
	private String repayBankId;//还款银行卡号末四位
	private String payLoanBankName;//放款银行
	private String contractUrl;//合同URL
	

	
	
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getClientToken() {
		return clientToken;
	}
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIdentityCardNo() {
		return identityCardNo;
	}
	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}
	public String getApplyAmt() {
		return applyAmt;
	}
	public void setApplyAmt(String applyAmt) {
		this.applyAmt = applyAmt;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDayRate() {
		return dayRate;
	}
	public void setDayRate(String dayRate) {
		this.dayRate = dayRate;
	}
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getRepayBankName() {
		return repayBankName;
	}
	public void setRepayBankName(String repayBankName) {
		this.repayBankName = repayBankName;
	}
	public String getRepayBankId() {
		return repayBankId;
	}
	public void setRepayBankId(String repayBankId) {
		this.repayBankId = repayBankId;
	}
	public String getPayLoanBankName() {
		return payLoanBankName;
	}
	public void setPayLoanBankName(String payLoanBankName) {
		this.payLoanBankName = payLoanBankName;
	}
	public String getContractUrl() {
		return contractUrl;
	}
	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
	@Override
	public String toString() {
		return "HXLoanRecordDetailBean [transCode=" + transCode + ", channelNo="
				+ channelNo + ", clientToken=" + clientToken + ", returnCode="
				+ returnCode + ", returnMsg=" + returnMsg + ", customerName="
				+ customerName + ", identityCardNo=" + identityCardNo
				+ ", applyAmt=" + applyAmt + ", period=" + period
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", dayRate=" + dayRate + ", repayMode=" + repayMode
				+ ", repayBankName=" + repayBankName + ", repayBankId="
				+ repayBankId + ", payLoanBankName=" + payLoanBankName
				+ ", contractUrl=" + contractUrl + "]";
	}
	
}
