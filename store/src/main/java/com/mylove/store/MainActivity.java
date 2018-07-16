package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.adapter.GridAdapter;
import com.mylove.store.adapter.ListAdapter;
import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.MainContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.MainPresenter;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

@Route(path = RouterURL.StoreMain)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R2.id.store_type)
    TvRecyclerView storeType;

    @BindView(R2.id.store_apps)
    TvRecyclerView storeApps;

    protected FocusBorder mListFocusBorder,mGridFocusBorder;

    private ListAdapter mListAdapter;
    private CommonRecyclerViewAdapter mGridAdapter;
    private int selPosition = -1;

    public static final long STORE_SEND_MSG_DURATION = 500;
    public static final int STORE_TYPE = 0x1212;
    public static final int STORE_APPS = 0x1213;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STORE_TYPE:

                    break;
                case STORE_APPS:
                    if(mListAdapter != null && mListAdapter.getItemCount() > selPosition){
                        String item = mListAdapter.getItem(selPosition);
                        mPresenter.getStoreApps(item,40);
                    }
                    break;
            }
        }
    };

    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerStoreComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .storeModule(new StoreModule())
                .build()
                .inject(this);
    }

    public void initFocusBorder(){
        // 移动框
        if(null == mListFocusBorder) {
            mListFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.module_store_item_activated_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2)
                    .shadowColor(getResources().getColor(R.color.module_store_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 20)
                    .animDuration(180L)
                    .noShimmer()
                    .build(this);
        }

        if(null == mGridFocusBorder) {
            mGridFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.module_store_item_activated_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2)
                    .shadowColor(getResources().getColor(R.color.module_store_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 20)
                    .animDuration(180L)
                    .noShimmer()
                    .build(this);
        }
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        initFocusBorder();
        setListener();

        storeType.setSpacingWithMargins(10, 0);
        mListAdapter = new ListAdapter(this, true);
        mListAdapter.setDatas(null);
        storeType.setAdapter(mListAdapter);

        storeApps.setSpacingWithMargins(10, 10);
        mGridAdapter = new GridAdapter(this);
        mGridAdapter.setDatas(null);
        storeApps.setAdapter(mGridAdapter);
    }

    private void setListener() {
        storeType.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 40,0);
                if(storeType.getSelectedPosition() != selPosition){
                    selPosition = position;
                    mHandler.removeMessages(STORE_APPS);
                    mHandler.sendEmptyMessageDelayed(STORE_APPS,STORE_SEND_MSG_DURATION);
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });

        storeApps.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 8,1);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });

        storeType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(storeApps !=null && storeApps.hasFocus() && !hasFocus)
                    return;
                mGridFocusBorder.setVisible(hasFocus);
            }
        });

        storeApps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(storeType !=null && storeType.hasFocus() && !hasFocus)
                    return;
                mListFocusBorder.setVisible(hasFocus);
            }
        });

    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius,int type) {
        if(type == 0){
            if(null != mGridFocusBorder) {
                mGridFocusBorder.setVisible(false);
            }
            if(null != mListFocusBorder) {
                mListFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
            }
        }else{
            if(null != mListFocusBorder) {
                mListFocusBorder.setVisible(false);
            }
            if(null != mGridFocusBorder) {
                mGridFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
            }
        }
    }

    @Override
    public void initData() {
        mPresenter.getStoreTypes();
    }

    @Override
    public void showStoreTypes(List<String> types) {
        mListAdapter.setDatas(types);
        mListAdapter.notifyDataSetChanged();
        storeType.requestFocus();
        if(types != null && types.size() > 0){
            mPresenter.getStoreApps(types.get(0),90);
        }
    }

    @Override
    public void showStoreApps(List<String> apps) {
        mGridAdapter.setDatas(apps);
        mGridAdapter.notifyDataSetChanged();
        if(mGridAdapter.getItemCount() > 0){
            storeApps.scrollToPosition(0);
        }
    }
}
