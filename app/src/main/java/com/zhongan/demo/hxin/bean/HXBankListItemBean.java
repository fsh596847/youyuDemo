package com.zhongan.demo.hxin.bean;

import java.io.Serializable;

/**
 * 银行列表Item数据
 **/
public class HXBankListItemBean implements Serializable {

	private String bankCode; //银行卡号
	private String bankName; //银行名称
	private String phone; //手机号
	private String userName;//用户名称
	private String isMain;//是否为主还款银行卡
	private String acctId;//银行卡主键
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	@Override
	public String toString() {
		return "HXBankListItemBean [bankCode=" + bankCode + ", bankName="
				+ bankName + ", phone=" + phone + ", userName=" + userName
				+ ", isMain=" + isMain + ", acctId=" + acctId + "]";
	}
	
	
	
}
