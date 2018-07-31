package com.mylove.store;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.module_base.helper.LocaleHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_base.net.down.DownloadCallback;
import com.mylove.module_base.net.down.DownloadListener;
import com.mylove.module_base.net.down.DownloadRecord;
import com.mylove.module_base.net.down.DownloadRequest;
import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.module_base.utils.FileUtils;
import com.mylove.module_base.utils.Md5;
import com.mylove.module_common.RouterURL;
import com.mylove.store.adapter.PicAdapter;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.DetailData;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.DetailContract;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.DetailPresenter;
import com.mylove.store.utils.ApkUtils;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RouterURL.StoreDetail)
public class DetailActivity extends BaseActivity<DetailPresenter> implements DetailContract.View{

    @BindView(R2.id.app_icon)
    ImageView appIcon;
    @BindView(R2.id.app_title)
    TextView tvAppTitle;
    @BindView(R2.id.store_down)
    FrameLayout storeDown;
    @BindView(R2.id.store_down_progress)
    ProgressBar storeDownProgressBar;
    @BindView(R2.id.store_progress)
    TextView storeProgress;
    @BindView(R2.id.store_detail_pic)
    TvRecyclerView storeDetailPic;
    @BindView(R2.id.store_detail_context)
    RelativeLayout storeDetailContext;
    @BindView(R2.id.store_detail_count)
    TextView storeDetailCount;
    @BindView(R2.id.store_detail_version)
    TextView storeDetailVersion;
    @BindView(R2.id.store_detail_info)
    TextView storeDetailInfo;

    private AppReceiver mAppReceiver;
    private AppData appData;
    private DetailData detailData;
    private DownloadRequest request;

    private static final int STORE_STATUS_START = 0x10100;
    private static final int STORE_STATUS_RUN = 0x10101;
    private static final int STORE_STATUS_DOWNING = 0x10103;
    private static final int STORE_STATUS_INSTALL = 0x10102;
    private static int STORE_STATUS = STORE_STATUS_START;
    private String fileName;

    protected FocusBorder mFocusBorder;
    private PicAdapter mListAdapter;
    private List<String> picUrls = new ArrayList<String>();

    private ShowPic showPic;
    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_detail;
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
                    .animDuration(0L)
                    .noShimmer()
                    .build(this);
        }
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

        initFocusBorder();
        setListener();

        storeDetailPic.setSpacingWithMargins(0, 10);
        mListAdapter = new PicAdapter(this, false);
        mListAdapter.setDatas(picUrls);
        storeDetailPic.setAdapter(mListAdapter);

        appData = getIntent().getParcelableExtra("appData");
        if(appData != null){
            tvAppTitle.setText(appData.getName());
            ImageLoaderHelper.getInstance().load(this,appData.getIcon(),appIcon);
            storeDetailCount.setText(String.format(getResources().getString(R.string.module_store_app_mem),appData.getSize()));
            storeDetailVersion.setText(String.format(getResources().getString(R.string.module_store_app_ver),appData.getVersion()));
            mPresenter.getStoreDetail(LocaleHelper.getLanguage(BaseApplication.getAppContext()).getLanguage(),appData.getId());
        }
    }
    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    private void setListener() {
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    onMoveFocusBorder(v, 1.0f, 8);
                }
            }
        };
        storeDown.setOnFocusChangeListener(focusChangeListener);
        storeDetailContext.setOnFocusChangeListener(focusChangeListener);
        storeDetailPic.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 8);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                if(detailData != null){
                    showPic = ShowPic.newInstance(position, (ArrayList<String>) detailData.getRe_pic());
                    showPic.show(getFragmentManager(),"SHOWPIC");
                }
            }
        });
    }

    @Override
    public void initData() {
        mAppReceiver = new AppReceiver();
        IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filterAPP.addDataScheme("package");
        filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(mAppReceiver, filterAPP);
    }

    private void fetchStatu(){
        //判断apk是否安装
        //是 状态为RUN
        //否 判断是否有当前apk下载过的文件
            //是 状态为INSTALL
            //否 状态为START
        if(ApkUtils.isAppInstalled(DetailActivity.this,detailData.getPackagename())){
            STORE_STATUS = STORE_STATUS_RUN;
        }else{
            if(FileUtils.isFileExists(fileName)){
                if(detailData.getMd5().equals(Md5.getFileMD5(fileName))){
                    STORE_STATUS = STORE_STATUS_INSTALL;
                }else{
                    FileUtils.deleteFile(fileName);
                    STORE_STATUS = STORE_STATUS_START;
                }
            }else{
                STORE_STATUS = STORE_STATUS_START;
            }
        }
        showStatu();
    }

    private void showStatu(){
        if(STORE_STATUS == STORE_STATUS_RUN){
            storeDownProgressBar.setMax(100);
            storeDownProgressBar.setProgress(100);
            storeProgress.setText(R.string.module_store_run);
        }else if(STORE_STATUS == STORE_STATUS_INSTALL){
            storeDownProgressBar.setMax(100);
            storeDownProgressBar.setProgress(100);
            storeProgress.setText(R.string.module_store_install);
        }else{
            storeDownProgressBar.setMax(100);
            storeDownProgressBar.setProgress(0);
            storeProgress.setText(R.string.module_store_down);
        }
    }

    @OnClick(R2.id.store_down)
    public void downClick(View view){
        if(STORE_STATUS == STORE_STATUS_RUN){
            ApkUtils.openApp(DetailActivity.this,detailData.getPackagename());
        }else if(STORE_STATUS == STORE_STATUS_INSTALL){
            ApkUtils.install(DetailActivity.this,fileName);
        }else{
            if(request == null) return;
            DownloadRecord record = DownloadUtil.parseRecord(request);
            if(record == null){
                if(detailData != null){
                    storeDownProgressBar.setProgress(0);
                    storeDownProgressBar.setMax(100);
                    storeDownProgressBar.setVisibility(View.VISIBLE);
                    DownloadUtil.get().removeTask(request);
                    STORE_STATUS = STORE_STATUS_DOWNING;
                    mPresenter.downStart(request);
                }
            }else{
                if(record.getDownloadState() == DownloadUtil.STATE_FINISHED){
                    if(detailData != null && ApkUtils.isAppInstalled(DetailActivity.this,detailData.getPackagename())){
                        ApkUtils.openApp(DetailActivity.this,detailData.getPackagename());
                    }else{
                        ApkUtils.install(DetailActivity.this,record.getFilePath());
                    }
                }
            }
        }
    }

    @Override
    public void showProgress(int progress) {
        storeDownProgressBar.setProgress(progress);
        storeProgress.setText(String.valueOf(progress)+"%");
    }

    @Override
    public void showDetail(DetailData detailData) {
        this.detailData = detailData;
        if(detailData != null){
            fileName = Constanst.getPath() + File.separator + DownloadUtil.getMD5(detailData.getUrl());
            fetchStatu();
            String url = detailData.getUrl();
            DownloadListener downloadListener = new DownloadCallback() {
                @Override
                public void onProgress(DownloadRecord record) {
                    showProgress(record.getProgress());
                }

                @Override
                public void onFailed(DownloadRecord record, String errMsg) {
                    FileUtils.deleteFile(fileName);
                }

                @Override
                public void onFinish(DownloadRecord record) {
                    STORE_STATUS = STORE_STATUS_INSTALL;
                    showStatu();
                    ApkUtils.install(DetailActivity.this,record.getFilePath());
                }

                @Override
                public void onCanceled(DownloadRecord record) {
                }
            };
            request = DownloadRequest.newBuilder()
                    .downloadName(DownloadUtil.getMD5(url))
                    .downloadDir(Constanst.getPath())
                    .downloadUrl(url)
                    .downloadListener(downloadListener)
                    .build();
        }

        storeDetailInfo.setText(String.format(getResources().getString(R.string.module_store_detail_info),detailData.getApp_detail()));

        mListAdapter.clearDatas();
        mListAdapter.appendDatas(detailData.getRe_pic());
        mListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(request != null) {
            DownloadUtil.get().removeTask(request);
            if(STORE_STATUS != STORE_STATUS_INSTALL){
                FileUtils.deleteFile(fileName);
            }
        }
        if(mAppReceiver != null){
            unregisterReceiver(mAppReceiver);
        }
    }

    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String packageName = intent.getDataString();
            packageName = packageName.split(":")[1];
            if (intent.getAction()
                    .equals("android.intent.action.PACKAGE_ADDED")) {
                if(detailData != null && detailData.getPackagename().equals(packageName)){
                    STORE_STATUS = STORE_STATUS_RUN;
                    showStatu();
                    FileUtils.deleteFile(fileName);
                }
            } else if (intent.getAction().equals(
                    "android.intent.action.PACKAGE_REMOVED")) {

            }
        }
    }


}
