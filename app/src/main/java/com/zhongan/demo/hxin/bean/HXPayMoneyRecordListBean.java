package com.zhongan.demo.hxin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 还款记录列表实体
 **/
public class HXPayMoneyRecordListBean implements Serializable
{
	private String transCode;//交易码
	private String channelNo;//渠道标识
	private String clientToken;//令牌
	private String returnCode;//交易信息码
	private String returnMsg;//交易返回信息
	
	private List<HXPayMoneyRecordListItemBean> rows; //借款列表

	
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
	public List<HXPayMoneyRecordListItemBean> getRows() {
		return rows;
	}
	public void setRows(List<HXPayMoneyRecordListItemBean> rows) {
		this.rows = rows;
	}
	
	
	
}
