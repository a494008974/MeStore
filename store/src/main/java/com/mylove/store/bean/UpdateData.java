package com.mylove.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/7/25.
 */
public class UpdateData implements Parcelable {

    /**
     * status : 2
     * verName : 1.0.2
     * remark : aaa
     * url : http://down.on-best.com/2018-07/25/6536b527cd85ace069e936eb44fb7218
     * tip : 无需升级
     */

    private String status;
    private String verName;
    private String remark;
    private String url;
    private String tip;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.verName);
        dest.writeString(this.remark);
        dest.writeString(this.url);
        dest.writeString(this.tip);
    }

    public UpdateData() {
    }

    protected UpdateData(Parcel in) {
        this.status = in.readString();
        this.verName = in.readString();
        this.remark = in.readString();
        this.url = in.readString();
        this.tip = in.readString();
    }

    public static final Parcelable.Creator<UpdateData> CREATOR = new Parcelable.Creator<UpdateData>() {
        @Override
        public UpdateData createFromParcel(Parcel source) {
            return new UpdateData(source);
        }

        @Override
        public UpdateData[] newArray(int size) {
            return new UpdateData[size];
        }
    };
}