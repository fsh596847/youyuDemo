package com.zhongan.demo.hxin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 借款列表实体
 **/
public class HXLoanMoneyDetailListBean implements Serializable
{
	private String transCode;//交易码
	private String channelNo;//渠道标识
	private String clientToken;//令牌
	private String returnCode;//交易信息码
	private String returnMsg;//交易返回信息
	private String overdueCount;//是否逾期 0：未逾期 1:有逾期
	
	private List<HXLoanMoneyDetailListItemBean> rows; //借款列表

	
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
	
	public String getOverdueCount() {
		return overdueCount;
	}
	public void setOverdueCount(String overdueCount) {
		this.overdueCount = overdueCount;
	}
	public List<HXLoanMoneyDetailListItemBean> getRows() {
		return rows;
	}
	public void setRows(List<HXLoanMoneyDetailListItemBean> rows) {
		this.rows = rows;
	}
	
	
	
}
