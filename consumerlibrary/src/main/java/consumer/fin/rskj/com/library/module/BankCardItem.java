package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/7/24.
 */

public class BankCardItem implements Parcelable {
    private String bankCode;
    private String bankName;
    private String acctId;
    private String isMain;
    private String phone;
    private String userName;

    private int imageRes;
    private boolean check;

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


    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
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

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public boolean isCheck() {
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
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.acctId);
        dest.writeString(this.isMain);
        dest.writeString(this.phone);
        dest.writeString(this.userName);
        dest.writeInt(this.imageRes);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
    }

    public BankCardItem() {
    }

    protected BankCardItem(Parcel in) {
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.acctId = in.readString();
        this.isMain = in.readString();
        this.phone = in.readString();
        this.userName = in.readString();
        this.imageRes = in.readInt();
        this.check = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BankCardItem> CREATOR = new Parcelable.Creator<BankCardItem>() {
        @Override
        public BankCardItem createFromParcel(Parcel source) {
            return new BankCardItem(source);
        }

        @Override
        public BankCardItem[] newArray(int size) {
            return new BankCardItem[size];
        }
    };


    @Override
    public String toString() {
        return "BankCardItem{" +
                "bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", acctId='" + acctId + '\'' +
                ", isMain='" + isMain + '\'' +
                ", phone='" + phone + '\'' +
                ", userName='" + userName + '\'' +
                ", imageRes=" + imageRes +
                ", check=" + check +
                '}';
    }
}
