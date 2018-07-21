package com.mylove.store.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/7/19.
 */

public class AppData implements Parcelable {

    /**
     * id : 2
     * name : 爱奇艺
     * version : 8.6.1.76232
     * size : 8.8M
     * icon : http://test.0755tv.net/uploads/2018-07-19/91b755c18369c0e50b97fec33433dca3.png
     */

    private String id;
    private String name;
    private String version;
    private String icon;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.version);
        dest.writeString(this.icon);
        dest.writeString(this.size);
    }

    public AppData() {
    }

    protected AppData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.version = in.readString();
        this.icon = in.readString();
        this.size = in.readString();
    }

    public static final Parcelable.Creator<AppData> CREATOR = new Parcelable.Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel source) {
            return new AppData(source);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };
}
