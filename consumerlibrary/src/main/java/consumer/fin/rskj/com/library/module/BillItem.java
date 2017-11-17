package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/7/24.
 */

public class BillItem implements Parcelable {

    private boolean check;
    private String loanId;	//贷款Id
    private String repayingPlanId;	//	还款计划ID
    private String period;	//	期数
    private String totalPay;	//	还款总金额
//    private String isCurrentMonuth;	//	本月标示
    private String currentPeriod;	//	当期期数
    private String currentEndDate;	//	计划还款日
    private String currentPrincipal;	//	应还本金
    private String currentInterest;	//	应还利息
//    private String currentPrincipalInterest;	//	应还总额本息
//    private String overFee;	//	逾期手续费
    private String overInt;	//	逾期利息（罚息）
    private String currentFee;	//	应还费用

    public String getOverInt() {
        return overInt;
    }

    public void setOverInt(String overInt) {
        this.overInt = overInt;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getRepayingPlanId() {
        return repayingPlanId;
    }

    public void setRepayingPlanId(String repayingPlanId) {
        this.repayingPlanId = repayingPlanId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(String totalPay) {
        this.totalPay = totalPay;
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

    public String getCurrentFee() {
        return currentFee;
    }

    public void setCurrentFee(String currentFee) {
        this.currentFee = currentFee;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
        dest.writeString(this.loanId);
        dest.writeString(this.repayingPlanId);
        dest.writeString(this.period);
        dest.writeString(this.totalPay);
        dest.writeString(this.currentPeriod);
        dest.writeString(this.currentEndDate);
        dest.writeString(this.currentPrincipal);
        dest.writeString(this.currentInterest);
        dest.writeString(this.overInt);
        dest.writeString(this.currentFee);
    }

    public BillItem() {
    }

    protected BillItem(Parcel in) {
        this.check = in.readByte() != 0;
        this.loanId = in.readString();
        this.repayingPlanId = in.readString();
        this.period = in.readString();
        this.totalPay = in.readString();
        this.currentPeriod = in.readString();
        this.currentEndDate = in.readString();
        this.currentPrincipal = in.readString();
        this.currentInterest = in.readString();
        this.overInt = in.readString();
        this.currentFee = in.readString();
    }

    public static final Parcelable.Creator<BillItem> CREATOR = new Parcelable.Creator<BillItem>() {
        @Override
        public BillItem createFromParcel(Parcel source) {
            return new BillItem(source);
        }

        @Override
        public BillItem[] newArray(int size) {
            return new BillItem[size];
        }
    };
}
