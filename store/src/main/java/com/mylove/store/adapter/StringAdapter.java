package com.mylove.store.adapter;

/**
 * Created by Administrator on 2018/7/16.
 */

import android.content.Context;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.store.R;


public class StringAdapter extends CommonRecyclerViewAdapter<String> {
    private Context mContext;
    private int layoutId;
    public StringAdapter(Context context,int layoutId) {
        super(context);
        this.mContext = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return this.layoutId;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, String item, int position) {
        helper.getHolder().setText(R.id.store_list_title, item);

    }
}
