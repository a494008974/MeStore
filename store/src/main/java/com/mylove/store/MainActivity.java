package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.adapter.GridAdapter;
import com.mylove.store.adapter.ListAdapter;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.MainContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.MainPresenter;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Route(path = RouterURL.StoreMain)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R2.id.store_type)
    TvRecyclerView storeType;

    @BindView(R2.id.store_apps)
    TvRecyclerView storeApps;

    @BindView(R2.id.store_progressbar)
    ImageView storeProgressbar;

    protected FocusBorder mFocusBorder;

    private ListAdapter mListAdapter;
    private CommonRecyclerViewAdapter mGridAdapter;

    private List<String> typeDatas = new ArrayList<String>();
    private List<String> appDatas = new ArrayList<String>();

    private int selPosition = -1,cPosition = -1;

    public static final long STORE_SEND_MSG_DURATION = 600;
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
                    mHandler.removeMessages(STORE_APPS);
                    if(mListAdapter != null && mListAdapter.getItemCount() > selPosition){
                        String item = mListAdapter.getItem(selPosition);
                        if(cPosition != -1 && cPosition != selPosition) {
                            storeApps.setVisibility(View.INVISIBLE);
                            storeProgressbar.setVisibility(View.VISIBLE);
                        }
                        cPosition = selPosition;
                        mPresenter.getStoreApps(item,90);
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
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.module_store_item_shadow_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 1)
                    .shadowColor(getResources().getColor(R.color.module_store_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 20)
                    .animDuration(200L)
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
        mListAdapter.setDatas(typeDatas);
        storeType.setAdapter(mListAdapter);

        storeApps.setSpacingWithMargins(10, 10);
        mGridAdapter = new GridAdapter(this);
        mGridAdapter.setDatas(appDatas);
        storeApps.setAdapter(mGridAdapter);
    }

    private void setListener() {
        storeType.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                mHandler.removeMessages(STORE_APPS);
                onMoveFocusBorder(itemView, 1.0f, 40);
                if(position != cPosition){
                    selPosition = position;
                    mHandler.sendEmptyMessageDelayed(STORE_APPS,STORE_SEND_MSG_DURATION);
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                storeType.setSelection(position);
                mHandler.sendEmptyMessageDelayed(STORE_APPS,STORE_SEND_MSG_DURATION);
            }
        });

        storeApps.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 8);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                String item = (String) mGridAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("appid",item);
                ARouter.getInstance().build(RouterURL.StoreDetail).with(bundle).navigation();
            }
        });

        storeType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(storeApps !=null && storeApps.hasFocus() && !hasFocus)
                    return;
                mFocusBorder.setVisible(hasFocus);
            }
        });

        storeApps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(storeType !=null && storeType.hasFocus() && !hasFocus)
                    return;
                mFocusBorder.setVisible(hasFocus);
            }
        });

    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    @Override
    public void initData() {
        mPresenter.getStoreTypes();
    }

    @Override
    public void showStoreTypes(List<String> types) {
        typeDatas = types;
        mListAdapter.clearDatas();
        mListAdapter.setDatas(typeDatas);
        mListAdapter.notifyDataSetChanged();
        storeType.requestFocus();
        if(types != null && types.size() > 0){
            storeType.setSelection(0);
        }
    }

    @Override
    public void showStoreApps(List<String> apps) {
        appDatas = apps;
        mGridAdapter.clearDatas();
        mGridAdapter.setDatas(appDatas);
        mGridAdapter.notifyDataSetChanged();

        if(mGridAdapter.getItemCount() > 0){
            storeApps.smoothScrollToPosition(0);
        }
        storeProgressbar.setVisibility(View.GONE);
        storeApps.setVisibility(View.VISIBLE);

    }
}
