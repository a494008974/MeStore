package com.mylove.store.adapter;

import android.content.Context;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.store.R;

/**
 * Created by Administrator on 2018/7/16.
 */

public class GridAdapter extends CommonRecyclerViewAdapter<String> {
    public GridAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.module_store_grid_item;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, String item, int position) {
        helper.getHolder().setText(R.id.app_grid_title, item);
    }
}