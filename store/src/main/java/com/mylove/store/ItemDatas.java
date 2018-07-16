package com.mylove.store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 */

public class ItemDatas {
    public static String[] types = {"全部应用","电视直播","视频点播","其它应用"};

    public static List<String> getDatas(String type,int count) {
        return getMovietDatas(type,count);
    }

    public static List<String> getMovietDatas(String type,int count) {
        List<String> mItems = new ArrayList<>();
        String itemBean;
        for(int i=0; i<count; i++) {
            itemBean = type+"--"+i;
            mItems.add(itemBean);
        }
        return mItems;
    }
}
