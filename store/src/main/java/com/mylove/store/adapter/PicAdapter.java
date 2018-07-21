package com.mylove.store.adapter;

/**
 * Created by Administrator on 2018/7/16.
 */

import android.content.Context;
import android.widget.ImageView;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.store.R;
import com.mylove.store.bean.MenuData;


public class PicAdapter extends CommonRecyclerViewAdapter<String> {
    private boolean isVertical;
    public PicAdapter(Context context, boolean isVertical) {
        super(context);
        this.isVertical = isVertical;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return isVertical ? R.layout.module_store_item_vertical : R.layout.module_store_item_horizontal;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, String item, int position) {
        ImageLoaderHelper.getInstance().load(mContext,item,(ImageView) helper.getHolder().getView(R.id.store_detail_tv_pic),8);
    }
}
