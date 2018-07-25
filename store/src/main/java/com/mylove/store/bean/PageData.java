package com.mylove.store.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */

public class PageData {

    /**
     * pageData : [{"id":"1","name":"小薇直播","version":"2.3.7","size":"9.92MB","icon":"http://appstore-down.bsw-inc.com/uploads/2018-07-19/c42d0aa6c6255a53f0af1ee74c815d51.png","group_concat(C.app_id)":"1"},{"id":"2","name":"爱奇艺","version":"8.6.1.76232","size":"14.44MB","icon":"http://appstore-down.bsw-inc.com/uploads/2018-07-19/91b755c18369c0e50b97fec33433dca3.png","group_concat(C.app_id)":"2"}]
     * page : 1
     * pageSize : 12
     * totalPage : 1
     * totalCount : 2
     */

    private int page;
    private int pageSize;
    private int totalPage;
    private int totalCount;
    private List<AppData> pageData;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<AppData> getPageData() {
        return pageData;
    }

    public void setPageData(List<AppData> pageData) {
        this.pageData = pageData;
    }
}
