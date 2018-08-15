package com.mylove.launcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mylove.launcher.adapter.StringAdapter;
import com.mylove.launcher.bean.Bizhi;
import com.mylove.launcher.bean.HttpEvent;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.fragment.FragmentPicture;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.MainPresenter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private static final String DEFAULT_FRAGMENT = "com.mylove.launcher.fragment.FragmentPicture";

    @BindView(R.id.launcher_view_flipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.launcher_tv)
    TextView mTextView;

    @BindView(R.id.launcher_tv_recycle_view)
    TvRecyclerView mTvRecyclerView;

    StringAdapter stringAdapter;
    private float scale = 1.0f;

    protected FocusBorder mFocusBorder;
    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerLauncherComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .launcherModule(new LauncherModule())
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        showFragment(DEFAULT_FRAGMENT);

        initFocusBorder();
        setListener();

        stringAdapter = new StringAdapter(this,R.layout.launcher_item) {
            @Override
            public void onBindItemHolder(CommonRecyclerViewHolder helper, String item, int position) {
//                helper.getHolder().setText(R.id.app_grid_title,"position = "+position);
            }
        };
        String[] strs = getResources().getStringArray(R.array.Launcher_Default);
        List<String> items = Arrays.asList(strs);
        stringAdapter.setDatas(items);
        mTvRecyclerView.setSpacingWithMargins(40, 40);
        mTvRecyclerView.setAdapter(stringAdapter);
    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    private void setListener() {
        mTvRecyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, scale, 15);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });
    }

    private void initFocusBorder() {
// 移动框
        if(null == mFocusBorder) {
            mFocusBorder = new FocusBorder.Builder()
                    .asColor()
                    .borderColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 1)
                    .shadowColor(getResources().getColor(R.color.launcher_item_shadow_color))
                    .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 15)
                    .animDuration(180L)
                    .noShimmer()
                    .build(this);
        }

    }

    public void showFragment(String classStr){
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = FragmentPicture.instantiate(this,classStr);
            transaction.replace(R.id.launcher_main,fragment);
            transaction.commit();
        }catch (Exception e){
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HttpEvent(HttpEvent event){
        switch (event.getEvent()){
            case HttpEvent.CHANGE_STYLE:
                showFragment((String)event.getObj());
                break;
            case HttpEvent.UPLOAD_EVENT:
                break;
            case HttpEvent.DOWNLOAD_EVENT:
                break;
            case HttpEvent.SUBMIT_EVENT:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startServer(this);
    }

    @Override
    public void initData() {
//        mPresenter.showBizhi(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.stopServer();
        super.onDestroy();
        BaseApplication.getAppContext().exitApp();
    }

    @Override
    public void showBizhi(List<Bizhi> bizhis) {
        if(mViewFlipper == null) return;
        for (Bizhi bizhi : bizhis){
            mViewFlipper.addView(createShowView(bizhi));
        }
        mViewFlipper.setInAnimation(this,R.anim.launcher_fade_in);
        mViewFlipper.setOutAnimation(this,R.anim.launcher_fade_out);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(6000);
        mViewFlipper.startFlipping();
    }

    public View createShowView(Bizhi bizhi){
        ImageView imageView = new ImageView(this);
        ImageLoaderHelper.getInstance().loadForCache(this,bizhi.getImage(),imageView);
        return imageView;
    }
}
