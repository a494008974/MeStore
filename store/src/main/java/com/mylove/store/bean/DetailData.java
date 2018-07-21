package com.mylove.store.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class DetailData {

    /**
     * appid : 2
     * name : 爱奇艺
     * packagename : com.gala.video.app.epg.HomeActivity
     * appicon : http://appstore.bsw-inc.com/uploads/2018-07-19/91b755c18369c0e50b97fec33433dca3.png
     * url : http://appstore.bsw-inc.com/uploads/2018-07-19/8f1b5af8658211c6ec00e7fdeac3fc0b
     * version : 8.6.1.76232
     * md5 : 2cc1bf5b973e2da23c4314c5adae9532
     * size : 8.8M
     * re_pic : ["http://appstore.bsw-inc.com/uploads/2018-07-19/91b755c18369c0e50b97fec33433dca3.png"]
     * app_detail : null
     * create_time : 1531985897
     */

    private String appid;
    private String name;
    private String packagename;
    private String appicon;
    private String url;
    private String version;
    private Object app_detail;
    private String create_time;
    private List<String> re_pic;
    private String md5;
    private String size;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getAppicon() {
        return appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getApp_detail() {
        return app_detail;
    }

    public void setApp_detail(Object app_detail) {
        this.app_detail = app_detail;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<String> getRe_pic() {
        return re_pic;
    }

    public void setRe_pic(List<String> re_pic) {
        this.re_pic = re_pic;
    }

    @Override
    public String toString() {
        return "DetailData{" +
                "appid='" + appid + '\'' +
                ", name='" + name + '\'' +
                ", packagename='" + packagename + '\'' +
                ", appicon='" + appicon + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", app_detail=" + app_detail +
                ", create_time='" + create_time + '\'' +
                ", re_pic=" + re_pic +
                '}';
    }
}
