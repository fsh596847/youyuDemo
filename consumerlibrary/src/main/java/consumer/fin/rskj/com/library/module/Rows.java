package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/9/8.
 */

public class Rows implements Parcelable {
    @Override
    public String toString() {
        return "{" +
                "loanId:'" + loanId + '\'' +
                ", repayAm:'" + repayAmt + '\'' +
                '}';
    }

    private String loanId;

    private String repayAmt;

    public void setLoanId(String loanId){
        this.loanId = loanId;
    }
    public String getLoanId(){
        return this.loanId;
    }
    public void setRepayAmt(String repayAmt){
        this.repayAmt = repayAmt;
    }
    public String getRepayAmt(){
        return this.repayAmt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loanId);
        dest.writeString(this.repayAmt);
    }

    public Rows() {
    }

    protected Rows(Parcel in) {
        this.loanId = in.readString();
        this.repayAmt = in.readString();
    }

    public static final Parcelable.Creator<Rows> CREATOR = new Parcelable.Creator<Rows>() {
        @Override
        public Rows createFromParcel(Parcel source) {
            return new Rows(source);
        }

        @Override
        public Rows[] newArray(int size) {
            return new Rows[size];
        }
    };
}
