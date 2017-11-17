package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/7/26.
 * 借还款记录item
 */

public class PLItem implements Parcelable {

    private String productName;//产品名称
    private String contractTerm;//合同期限单位（总期数）
    private String contractAmt;//贷款金额

    private String currentPeriod;//期数（当前期数）
    private String currentPrincipal;//应还本金

    private String status;//0: 未还,1: 未还清,2: 已还,3: 已逾期,4: 停止计息,5: 已冲正.
    private String dayRate;//日利率
    private String startDate;//起始日
    private String loanId;	//贷款Id

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }

    public String getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(String contractAmt) {
        this.contractAmt = contractAmt;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getCurrentPrincipal() {
        return currentPrincipal;
    }

    public void setCurrentPrincipal(String currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayRate() {
        return dayRate;
    }

    public void setDayRate(String dayRate) {
        this.dayRate = dayRate;
    }


    public PLItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.contractTerm);
        dest.writeString(this.contractAmt);
        dest.writeString(this.currentPeriod);
        dest.writeString(this.currentPrincipal);
        dest.writeString(this.status);
        dest.writeString(this.dayRate);
        dest.writeString(this.startDate);
        dest.writeString(this.loanId);
    }

    protected PLItem(Parcel in) {
        this.productName = in.readString();
        this.contractTerm = in.readString();
        this.contractAmt = in.readString();
        this.currentPeriod = in.readString();
        this.currentPrincipal = in.readString();
        this.status = in.readString();
        this.dayRate = in.readString();
        this.startDate = in.readString();
        this.loanId = in.readString();
    }

    public static final Creator<PLItem> CREATOR = new Creator<PLItem>() {
        @Override
        public PLItem createFromParcel(Parcel source) {
            return new PLItem(source);
        }

        @Override
        public PLItem[] newArray(int size) {
            return new PLItem[size];
        }
    };
}
