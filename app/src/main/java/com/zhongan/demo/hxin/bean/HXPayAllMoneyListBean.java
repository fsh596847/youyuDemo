package com.zhongan.demo.hxin.bean;

import java.io.Serializable;
import java.util.List;

/**
 *全额还款试算
 **/
public class HXPayAllMoneyListBean implements Serializable
{
	private String transCode;//交易码
	private String channelNo;//渠道标识
	private String clientToken;//令牌
	private String returnCode;//交易信息码
	private String returnMsg;//交易返回信息
	private String totalAmt;//还款总额
	private String totalIntAmt;//利息总额
	
	private String repayMode;//还款方式
	
	private String cuAmt;//应还当期本金
	private String cuInt;//应还当期利息
	
	private String cuTotalAmt;//本次应还总额
	
	private String penalty;//违约金
	
	
	private List<HXPayAllMoneyListItemBean> rows; //借款列表

	
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
	public String getCuAmt() {
		return cuAmt;
	}
	public void setCuAmt(String cuAmt) {
		this.cuAmt = cuAmt;
	}
	public String getCuInt() {
		return cuInt;
	}
	public void setCuInt(String cuInt) {
		this.cuInt = cuInt;
	}
	public String getCuTotalAmt() {
		return cuTotalAmt;
	}
	public void setCuTotalAmt(String cuTotalAmt) {
		this.cuTotalAmt = cuTotalAmt;
	}
	
	public String getPenalty() {
		return penalty;
	}
	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}
	public List<HXPayAllMoneyListItemBean> getRows() {
		return rows;
	}
	public void setRows(List<HXPayAllMoneyListItemBean> rows) {
		this.rows = rows;
	}
	
	
	
}
