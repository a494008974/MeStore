package com.mylove.launcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.widget.ViewFlipper;

import com.mylove.launcher.bean.HttpEvent;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.MainContract;
import com.mylove.launcher.fragment.FragmentPicture;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.MainPresenter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.module.ApplicationModule;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View,BaseFragment.FocusBorderHelper {

    private static final String DEFAULT_FRAGMENT = "com.mylove.launcher.fragment.FragmentPicture";

    @BindView(R.id.launcher_view_flipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.launcher_tv_recycle_view)
    TvRecyclerView mTvRecyclerView;

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
    }

    public FocusBorder getFocusBorder() {
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
        return mFocusBorder;
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }
}
