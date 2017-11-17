package consumer.fin.rskj.com.library.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by HP on 2017/9/8.
 */

public class PayModule implements Parcelable {

    private String transCode;

    private String version;

    @Override
    public String toString() {
        return "PayModule{" +
                "transCode='" + transCode + '\'' +
                ", version='" + version + '\'' +
                ", legalPerNum='" + legalPerNum + '\'' +
                ", channelNo='" + channelNo + '\'' +
                ", clientToken='" + clientToken + '\'' +
                ", rows=" + rows +
                '}';
    }

    private String legalPerNum;

    private String channelNo;

    private String clientToken;

    private List<Rows> rows ;

    public void setTransCode(String transCode){
        this.transCode = transCode;
    }
    public String getTransCode(){
        return this.transCode;
    }
    public void setVersion(String version){
        this.version = version;
    }
    public String getVersion(){
        return this.version;
    }
    public void setLegalPerNum(String legalPerNum){
        this.legalPerNum = legalPerNum;
    }
    public String getLegalPerNum(){
        return this.legalPerNum;
    }
    public void setChannelNo(String channelNo){
        this.channelNo = channelNo;
    }
    public String getChannelNo(){
        return this.channelNo;
    }
    public void setClientToken(String clientToken){
        this.clientToken = clientToken;
    }
    public String getClientToken(){
        return this.clientToken;
    }
    public void setRows(List<Rows> rows){
        this.rows = rows;
    }
    public List<Rows> getRows(){
        return this.rows;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transCode);
        dest.writeString(this.version);
        dest.writeString(this.legalPerNum);
        dest.writeString(this.channelNo);
        dest.writeString(this.clientToken);
        dest.writeTypedList(this.rows);
    }

    public PayModule() {
    }

    protected PayModule(Parcel in) {
        this.transCode = in.readString();
        this.version = in.readString();
        this.legalPerNum = in.readString();
        this.channelNo = in.readString();
        this.clientToken = in.readString();
        this.rows = in.createTypedArrayList(Rows.CREATOR);
    }

    public static final Parcelable.Creator<PayModule> CREATOR = new Parcelable.Creator<PayModule>() {
        @Override
        public PayModule createFromParcel(Parcel source) {
            return new PayModule(source);
        }

        @Override
        public PayModule[] newArray(int size) {
            return new PayModule[size];
        }
    };
}
