package com.mylove.store.adapter;

/**
 * Created by Administrator on 2018/7/16.
 */

import android.content.Context;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.store.R;


public class ListAdapter extends CommonRecyclerViewAdapter<String> {
    private boolean isVertical;
    public ListAdapter(Context context, boolean isVertical) {
        super(context);
        this.isVertical = isVertical;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return isVertical ? R.layout.module_store_item_vertical : R.layout.module_store_item_horizontal;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, String item, int position) {
        helper.getHolder().setText(R.id.store_list_title, item);
    }
}
