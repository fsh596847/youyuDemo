package com.zhongan.demo.hxin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 还款方式
 **/
public class HXPayTypeBean implements Serializable
{
	private String transCode;//交易码
	private String channelNo;//渠道标识
	private String clientToken;//令牌
	private String returnCode;//交易信息码
	private String returnMsg;//交易返回信息
	
	private String totalLoanAmt; //贷款总额
	private String totalMonths;//总期数
	
	private String totalAmt; //还款总额
	private String totalIntAmt; //利息总额
	private String repayMode; //还款方式
	private String loanChangeId;//贷款变更ID
	private String repayingPlanId;//还款统计ID
	private String totalPrincipal;//本金
	private String totalFeeAmt;//总手续费
	private List<HXPayTypeItemBean> repayPlanList; //每期还款信息列表
	
	
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
	
	public String getTotalLoanAmt() {
		return totalLoanAmt;
	}
	public void setTotalLoanAmt(String totalLoanAmt) {
		this.totalLoanAmt = totalLoanAmt;
	}
	public String getTotalMonths() {
		return totalMonths;
	}
	public void setTotalMonths(String totalMonths) {
		this.totalMonths = totalMonths;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getTotalIntAmt() {
		return totalIntAmt;
	}
	public void setTotalIntAmt(String totalIntAmt) {
		this.totalIntAmt = totalIntAmt;
	}
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getLoanChangeId() {
		return loanChangeId;
	}
	public void setLoanChangeId(String loanChangeId) {
		this.loanChangeId = loanChangeId;
	}
	public String getRepayingPlanId() {
		return repayingPlanId;
	}
	public void setRepayingPlanId(String repayingPlanId) {
		this.repayingPlanId = repayingPlanId;
	}
	public String getTotalPrincipal() {
		return totalPrincipal;
	}
	public void setTotalPrincipal(String totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}
	public List<HXPayTypeItemBean> getRepayPlanList() {
		return repayPlanList;
	}
	public void setRepayPlanList(List<HXPayTypeItemBean> repayPlanList) {
		this.repayPlanList = repayPlanList;
	}
	public String getTotalFeeAmt() {
		return totalFeeAmt;
	}
	public void setTotalFeeAmt(String totalFeeAmt) {
		this.totalFeeAmt = totalFeeAmt;
	}
	@Override
	public String toString() {
		return "HXPayTypeBean [transCode=" + transCode + ", channelNo="
				+ channelNo + ", clientToken=" + clientToken + ", returnCode="
				+ returnCode + ", returnMsg=" + returnMsg + ", totalLoanAmt="
				+ totalLoanAmt + ", totalMonths=" + totalMonths + ", totalAmt="
				+ totalAmt + ", totalIntAmt=" + totalIntAmt + ", repayMode="
				+ repayMode + ", loanChangeId=" + loanChangeId
				+ ", repayingPlanId=" + repayingPlanId + ", totalPrincipal="
				+ totalPrincipal + ", totalFeeAmt=" + totalFeeAmt
				+ ", repayPlanList=" + repayPlanList + "]";
	}
	
	
}
