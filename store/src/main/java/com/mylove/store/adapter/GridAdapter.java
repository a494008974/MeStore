package com.mylove.store.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.store.Constanst;
import com.mylove.store.R;
import com.mylove.store.bean.AppData;

/**
 * Created by Administrator on 2018/7/16.
 */

public class GridAdapter extends CommonRecyclerViewAdapter<AppData> {
    private Context mContext;
    public GridAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.module_store_grid_item;
    }

    @Override
    public void onBindItemHolder(CommonRecyclerViewHolder helper, AppData item, int position) {
        helper.getHolder().setText(R.id.app_grid_title, item.getName());
        System.out.println(item.getIcon());
        ImageLoaderHelper.getInstance().load(mContext,item.getIcon(),(ImageView) helper.getHolder().getView(R.id.app_grid_icon));
        helper.getHolder().setText(R.id.app_grid_version,item.getVersion());
    }
}