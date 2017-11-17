package consumer.fin.rskj.com.library.module;

import java.io.Serializable;

/**
 * 还款方式每期数据
 **/
public class PayTypeItemBean implements Serializable
{
	private String currentPeriod; //期数
	private String currentPrincipalInterest; //应还总额
	private String currentPrincipal; //应还本金
	private String currentInterest;//应还利息
	private String endCurrentPrincipalBalance;//剩余本金
	private String totalPrincipal;//本金
	private String contractRate;//利率
	private String currentEndDate;//还款日
	private String calIntDays;//计息天数
	private String repayingPlanDetailId;//还款计划明细ID
	private String currentFee;//手续费
	public String getCurrentPeriod() {
		return currentPeriod;
	}
	public void setCurrentPeriod(String currentPeriod) {
		this.currentPeriod = currentPeriod;
	}
	public String getCurrentPrincipalInterest() {
		return currentPrincipalInterest;
	}
	public void setCurrentPrincipalInterest(String currentPrincipalInterest) {
		this.currentPrincipalInterest = currentPrincipalInterest;
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
	public String getEndCurrentPrincipalBalance() {
		return endCurrentPrincipalBalance;
	}
	public void setEndCurrentPrincipalBalance(String endCurrentPrincipalBalance) {
		this.endCurrentPrincipalBalance = endCurrentPrincipalBalance;
	}
	public String getTotalPrincipal() {
		return totalPrincipal;
	}
	public void setTotalPrincipal(String totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}
	public String getContractRate() {
		return contractRate;
	}
	public void setContractRate(String contractRate) {
		this.contractRate = contractRate;
	}
	public String getCurrentEndDate() {
		return currentEndDate;
	}
	public void setCurrentEndDate(String currentEndDate) {
		this.currentEndDate = currentEndDate;
	}
	public String getCalIntDays() {
		return calIntDays;
	}
	public void setCalIntDays(String calIntDays) {
		this.calIntDays = calIntDays;
	}
	public String getRepayingPlanDetailId() {
		return repayingPlanDetailId;
	}
	public void setRepayingPlanDetailId(String repayingPlanDetailId) {
		this.repayingPlanDetailId = repayingPlanDetailId;
	}
	public String getCurrentFee() {
		return currentFee;
	}
	public void setCurrentFee(String currentFee) {
		this.currentFee = currentFee;
	}
	@Override
	public String toString() {
		return "PayTypeItemBean [currentPeriod=" + currentPeriod
				+ ", currentPrincipalInterest=" + currentPrincipalInterest
				+ ", currentPrincipal=" + currentPrincipal
				+ ", currentInterest=" + currentInterest
				+ ", endCurrentPrincipalBalance=" + endCurrentPrincipalBalance
				+ ", totalPrincipal=" + totalPrincipal + ", contractRate="
				+ contractRate + ", currentEndDate=" + currentEndDate
				+ ", calIntDays=" + calIntDays + ", repayingPlanDetailId="
				+ repayingPlanDetailId + ", currentFee=" + currentFee + "]";
	}
	
}
