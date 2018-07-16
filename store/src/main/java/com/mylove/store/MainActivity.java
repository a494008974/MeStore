package com.mylove.store;

import android.os.Bundle;
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

    private String[] types = {"全部应用","电视直播","视频点播","其它应用"};


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
        mListAdapter.setDatas(Arrays.asList(types));
        storeType.setAdapter(mListAdapter);

        storeApps.setSpacingWithMargins(10, 10);
        mGridAdapter = new GridAdapter(this);
        mGridAdapter.setDatas(ItemDatas.getDatas(types[0],60));
        storeApps.setAdapter(mGridAdapter);
    }

    private void setListener() {
        storeType.setOnItemListener(new SimpleOnItemListener() {

            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 40,0);
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
        mPresenter.getBanner();

        storeType.requestFocus();
    }


    @Override
    public void showResult(BaseResponse<BannerData> bannerDataBaseResponse) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<bannerDataBaseResponse.getData().size(); i++){
            BannerData bannerData = bannerDataBaseResponse.getData().get(i);
            sb.append("------------------------------\n")
                    .append("id = "+bannerData.getId()+"\n")
                    .append("imagePath = "+bannerData.getImagePath()+"\n")
                    .append("isVisible = "+bannerData.getIsVisible()+"\n")
                    .append("order = "+bannerData.getOrder()+"\n")
                    .append("title = "+bannerData.getTitle()+"\n")
                    .append("type = "+bannerData.getType()+"\n")
                    .append("url = "+bannerData.getUrl()+"\n");
        }
//        tvResult.setText(sb.toString());
    }
}
