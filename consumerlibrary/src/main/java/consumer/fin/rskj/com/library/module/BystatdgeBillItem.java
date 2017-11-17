package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/7/24.
 */

public class BystatdgeBillItem  {

    private boolean check;

    public String getRepayingPlanDetailId() {
        return repayingPlanDetailId;
    }

    public void setRepayingPlanDetailId(String repayingPlanDetailId) {
        this.repayingPlanDetailId = repayingPlanDetailId;
    }

    private String repayingPlanDetailId;//还款计划明细ID
    private String rowsCount;//总条数
    private String currentPeriod;	//	当期期数
    private String currentEndDate;	//	计划还款日
    private String currentPrincipal;	//	应还本金
    private String currentInterest;	//	应还利息
    private String currentPrincipalInterest;	//应还总额本息
    private String status;	//	状态
    private String isCurrentTerm;	//	是否当期
    private String overdueredFlag;	//	逾期可还标识

    public String getOverInt() {
        return overInt;
    }

    public void setOverInt(String overInt) {
        this.overInt = overInt;
    }

    private String overInt = "0.00";	//	逾期利息（罚息）


    public String getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(String rowsCount) {
        this.rowsCount = rowsCount;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCurrentTerm() {
        return isCurrentTerm;
    }

    public void setIsCurrentTerm(String isCurrentTerm) {
        this.isCurrentTerm = isCurrentTerm;
    }

    public String getOverdueredFlag() {
        return overdueredFlag;
    }

    public void setOverdueredFlag(String overdueredFlag) {
        this.overdueredFlag = overdueredFlag;
    }
}
