package com.zhongan.demo.hxin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 产品实体
 **/
public class HXLoanProductListBean implements Serializable {
	private String transCode;// 交易码
	private String channelNo;// 渠道标识
	private String clientToken;// 令牌
	private String returnCode;// 交易信息码
	private String returnMsg;// 交易返回信息

	private String prodId; // 产品id
	private String prodNum; // 产品编号
	private String prodName; // 产品名称
	private String prodSort;// 贷款产品分类
	private String prodType;// 贷款产品类型
	private String currency;// 币种
	private String prodBrief;// 产品简介
	private String prodNote;// 产品备注
	private String effectiveFlag;// 生效标识
	private String remark;// remark
	private String applyTime; // 产品申请时间
	private String prodApprovalTime; // 审批通过时间
	private String addTime; // 发布时间
	private String updateTime;// 更新时间
	private String endTime;// 产品截止时间
	private String prodPict;// 产品图片资源id
	private String perculNegoRateUp;// 挪用利率上浮比例
	private String overdueRateUp;// 逾期利率上浮比例
	private String preRepayScale;// 提前还款违约比例
	private String creditType;// 授信类型
	private String gracePeriod; // 免息期
	private String circleRate; // 循环贷不分期利率
	private String minLoanAmt; // 产品最小允许贷款金额
	private String maxLoanAmt; // 产品最大允许贷款金额

	private String rulePricingName;// 规则定价模型名称

	private List<HXLoanProductRateListItemBean> rows; // 产品定价利率
	private List<HXLoanProductPayWayListItemBean> repayWay; // 产品还款方式

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

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdSort() {
		return prodSort;
	}

	public void setProdSort(String prodSort) {
		this.prodSort = prodSort;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProdBrief() {
		return prodBrief;
	}

	public void setProdBrief(String prodBrief) {
		this.prodBrief = prodBrief;
	}

	public String getProdNote() {
		return prodNote;
	}

	public void setProdNote(String prodNote) {
		this.prodNote = prodNote;
	}

	public String getEffectiveFlag() {
		return effectiveFlag;
	}

	public void setEffectiveFlag(String effectiveFlag) {
		this.effectiveFlag = effectiveFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getProdApprovalTime() {
		return prodApprovalTime;
	}

	public void setProdApprovalTime(String prodApprovalTime) {
		this.prodApprovalTime = prodApprovalTime;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProdPict() {
		return prodPict;
	}

	public void setProdPict(String prodPict) {
		this.prodPict = prodPict;
	}

	public String getPerculNegoRateUp() {
		return perculNegoRateUp;
	}

	public void setPerculNegoRateUp(String perculNegoRateUp) {
		this.perculNegoRateUp = perculNegoRateUp;
	}

	public String getOverdueRateUp() {
		return overdueRateUp;
	}

	public void setOverdueRateUp(String overdueRateUp) {
		this.overdueRateUp = overdueRateUp;
	}

	public String getPreRepayScale() {
		return preRepayScale;
	}

	public void setPreRepayScale(String preRepayScale) {
		this.preRepayScale = preRepayScale;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public String getGracePeriod() {
		return gracePeriod;
	}

	public void setGracePeriod(String gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	public String getCircleRate() {
		return circleRate;
	}

	public void setCircleRate(String circleRate) {
		this.circleRate = circleRate;
	}

	

	public String getMinLoanAmt() {
		return minLoanAmt;
	}

	public void setMinLoanAmt(String minLoanAmt) {
		this.minLoanAmt = minLoanAmt;
	}

	public String getMaxLoanAmt() {
		return maxLoanAmt;
	}

	public void setMaxLoanAmt(String maxLoanAmt) {
		this.maxLoanAmt = maxLoanAmt;
	}

	public String getRulePricingName() {
		return rulePricingName;
	}

	public void setRulePricingName(String rulePricingName) {
		this.rulePricingName = rulePricingName;
	}

	public List<HXLoanProductRateListItemBean> getRows() {
		return rows;
	}

	public void setRows(List<HXLoanProductRateListItemBean> rows) {
		this.rows = rows;
	}

	public List<HXLoanProductPayWayListItemBean> getRepayWay() {
		return repayWay;
	}

	public void setRepayWay(List<HXLoanProductPayWayListItemBean> repayWay) {
		this.repayWay = repayWay;
	}

}
