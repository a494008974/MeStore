package com.mylove.launcher.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.mylove.launcher.MainActivity;
import com.mylove.launcher.R;
import com.mylove.launcher.adapter.StringAdapter;
import com.mylove.launcher.bean.Bizhi;
import com.mylove.launcher.component.DaggerLauncherComponent;
import com.mylove.launcher.contract.PictureContract;
import com.mylove.launcher.module.LauncherModule;
import com.mylove.launcher.presenter.PicturePresenter;
import com.mylove.module_base.adapter.CommonRecyclerViewHolder;
import com.mylove.module_base.base.BaseFragment;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/15.
 */

public class FragmentPicture extends BaseFragment<PicturePresenter> implements PictureContract.View{

    @BindView(R.id.picture_view_flipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.picture_tv_recycle_view)
    TvRecyclerView mTvRecyclerView;

    StringAdapter stringAdapter;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_picture;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerLauncherComponent.builder()
                .launcherModule(new LauncherModule())
                .applicationModule(new ApplicationModule(getContext()))
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        setListener();

        stringAdapter = new StringAdapter(getActivity(),R.layout.launcher_item) {
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

    private void setListener() {
        mTvRecyclerView.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                System.out.println("position ========= " + position);
                onMoveFocusBorder(itemView, 1.0f, 15);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {

            }
        });
    }

    @Override
    public void initData() {
        mPresenter.showBizhi(getActivity());
    }

    @Override
    public void showBizhi(List<Bizhi> bizhis) {
        if(mViewFlipper == null) return;
        for (Bizhi bizhi : bizhis){
            mViewFlipper.addView(createShowView(bizhi));
        }
        mViewFlipper.setInAnimation(getActivity(),R.anim.launcher_fade_in);
        mViewFlipper.setOutAnimation(getActivity(),R.anim.launcher_fade_out);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(8000);
        mViewFlipper.startFlipping();
    }

    public View createShowView(Bizhi bizhi){
        ImageView imageView = new ImageView(getActivity());
        ImageLoaderHelper.getInstance().loadForCache(getActivity(),bizhi.getImage(),imageView);
        return imageView;
    }


}
