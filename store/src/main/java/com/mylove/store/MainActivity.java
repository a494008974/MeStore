package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.LocaleHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_common.RouterURL;
import com.mylove.store.adapter.GridAdapter;
import com.mylove.store.adapter.ListAdapter;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.MenuData;
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

    private List<MenuData> typeDatas = new ArrayList<MenuData>();
    private List<AppData> appDatas = new ArrayList<AppData>();

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
                        MenuData item = mListAdapter.getItem(selPosition);
                        if(cPosition != -1 && cPosition != selPosition) {
                            storeApps.setVisibility(View.INVISIBLE);
                            storeProgressbar.setVisibility(View.VISIBLE);
                        }
                        cPosition = selPosition;
                        mPresenter.getStoreApps(LocaleHelper.getLanguage(BaseApplication.getAppContext()).getLanguage(),item.getId());
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
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 15)
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
                AppData item = (AppData) mGridAdapter.getItem(position);
                ARouter.getInstance().build(RouterURL.StoreDetail).withParcelable("appData",item).navigation();
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
        mPresenter.getStoreTypes(LocaleHelper.getLanguage(this).getLanguage());
    }

    @Override
    public void showStoreTypes(List<MenuData> types) {
        mListAdapter.clearDatas();
        mListAdapter.appendDatas(types);
        mListAdapter.notifyDataSetChanged();
        storeType.requestFocus();
        if(types != null && types.size() > 0){
            storeType.setSelection(0);
        }
    }

    @Override
    public void showStoreApps(List<AppData> apps) {
        mGridAdapter.clearDatas();
        mGridAdapter.appendDatas(apps);
        mGridAdapter.notifyDataSetChanged();
        if(mGridAdapter.getItemCount() > 0){
            storeApps.smoothScrollToPosition(0);
        }
        storeProgressbar.setVisibility(View.GONE);
        storeApps.setVisibility(View.VISIBLE);

    }
}
