package com.mylove.launcher.adapter;

/**
 * Created by Administrator on 2018/7/16.
 */

import android.content.Context;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;


public abstract class StringAdapter extends CommonRecyclerViewAdapter<String> {
    private Context mContext;
    private int layoutId;
    public StringAdapter(Context context, int layoutId) {
        super(context);
        this.mContext = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return this.layoutId;
    }

}
