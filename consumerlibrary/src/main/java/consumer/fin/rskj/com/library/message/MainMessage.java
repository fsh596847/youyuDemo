package consumer.fin.rskj.com.library.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 2017/9/11.
 */

public class MainMessage implements Parcelable {
    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
    }

    public MainMessage() {
    }

    @Override
    public String toString() {
        return "MainMessage{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    protected MainMessage(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<MainMessage> CREATOR = new Parcelable.Creator<MainMessage>() {
        @Override
        public MainMessage createFromParcel(Parcel source) {
            return new MainMessage(source);
        }

        @Override
        public MainMessage[] newArray(int size) {
            return new MainMessage[size];
        }
    };
}
