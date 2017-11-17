package com.zhongan.demo.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/8/11.
 */

public class BannerItem implements Parcelable {

    private String advertTitle;
    private String advertPic;
    private String advertUrl;
    private String productId = "";

    public String getAdvertTitle() {
        return advertTitle;
    }

    public void setAdvertTitle(String advertTitle) {
        this.advertTitle = advertTitle;
    }

    public String getAdvertPic() {
        return advertPic;
    }

    public void setAdvertPic(String advertPic) {
        this.advertPic = advertPic;
    }

    public String getAdvertUrl() {
        return advertUrl;
    }

    public void setAdvertUrl(String advertUrl) {
        this.advertUrl = advertUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "BannerItem{" +
                "advertTitle='" + advertTitle + '\'' +
                ", advertPic='" + advertPic + '\'' +
                ", advertUrl='" + advertUrl + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advertTitle);
        dest.writeString(this.advertPic);
        dest.writeString(this.advertUrl);
        dest.writeString(this.productId);
    }

    public BannerItem() {
    }

    protected BannerItem(Parcel in) {
        this.advertTitle = in.readString();
        this.advertPic = in.readString();
        this.advertUrl = in.readString();
        this.productId = in.readString();
    }

    public static final Creator<BannerItem> CREATOR = new Creator<BannerItem>() {
        @Override
        public BannerItem createFromParcel(Parcel source) {
            return new BannerItem(source);
        }

        @Override
        public BannerItem[] newArray(int size) {
            return new BannerItem[size];
        }
    };
}
