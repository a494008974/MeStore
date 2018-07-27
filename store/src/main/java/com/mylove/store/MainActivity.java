package com.mylove.store;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.mylove.store.adapter.StringAdapter;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.MenuData;
import com.mylove.store.bean.PageData;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.MainContract;
import com.mylove.store.model.StoreApiService;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.MainPresenter;
import com.mylove.store.update.Update;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
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

    @BindView(R2.id.store_tool)
    TvRecyclerView storeTool;

    @BindView(R2.id.store_sort)
    TvRecyclerView storeSort;

    @BindView(R2.id.store_main_record)
    TextView storeMainRecord;

    protected FocusBorder mFocusBorder;
    private PageData mPageData;

    private int CurrentPage = 1;
    public boolean showMore = false;

    private ListAdapter mListAdapter;
    private StringAdapter mSortAdapter,mOtherAdapter;
    private CommonRecyclerViewAdapter mGridAdapter;

    private List<MenuData> typeDatas = new ArrayList<MenuData>();
    private List<AppData> appDatas = new ArrayList<AppData>();

    private String[] sortValue,toolValue;

    private int selPosition = -1,cPosition = -1;
    private String sort = "1001";

    public static final long STORE_SEND_MSG_DURATION = 600;
    public static final int STORE_APPS = 0x1213;

    private Handler mHandler = new MyHandler(this);

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
        sortValue = getResources().getStringArray(R.array.sort_value);
        toolValue = getResources().getStringArray(R.array.search_value);

        initFocusBorder();
        setListener();

        storeType.setSpacingWithMargins(10, 0);
        mListAdapter = new ListAdapter(this, true);
        mListAdapter.setDatas(typeDatas);
        storeType.setAdapter(mListAdapter);

        storeTool.setSpacingWithMargins(0, 0);
        mOtherAdapter = new StringAdapter(this,R.layout.module_store_item_search);
        mOtherAdapter.setDatas(Arrays.asList(getResources().getStringArray(R.array.search)));
        storeTool.setAdapter(mOtherAdapter);
        storeTool.setVisibility(View.INVISIBLE);

        storeSort.setSpacingWithMargins(0, 0);
        mSortAdapter = new StringAdapter(this,R.layout.module_store_item_int_string);
        mSortAdapter.setDatas(Arrays.asList(getResources().getStringArray(R.array.sort)));
        storeSort.setAdapter(mSortAdapter);
        storeSort.setVisibility(View.INVISIBLE);

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
                    showMore = false;
                    CurrentPage = 1;
                    mHandler.sendEmptyMessageDelayed(STORE_APPS,STORE_SEND_MSG_DURATION);
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                storeType.setSelection(position);
                mHandler.sendEmptyMessageDelayed(STORE_APPS,STORE_SEND_MSG_DURATION);
            }
        });

        storeTool.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 40);
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                String search = toolValue[position % toolValue.length];
                if("1000".equals(search)){
                    ARouter.getInstance().build(RouterURL.StoreSearch).navigation();
                }
            }
        });

        storeSort.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                mHandler.removeMessages(STORE_APPS);
                onMoveFocusBorder(itemView, 1.0f, 8);
                String curSort = sortValue[position % sortValue.length];
                if(!sort.equals(curSort)){
                    sort = curSort;
                    showMore = false;
                    CurrentPage = 1;
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
                onMoveFocusBorder(itemView, 1.0f, 8);
                if(mPageData != null){
                    storeMainRecord.setText(String.format(getString(R.string.module_store_app_record),String.valueOf(position + 1),String.valueOf(mPageData.getTotalCount())));
                    //加载更多
                    int pos = position % StoreApiService.sSize + 1;   //光标在那里判断加载
                    int pagep = position / StoreApiService.sSize + 1;  //当前页
                    if(pagep < mPageData.getTotalPage()){
                        pagep ++;             //当前要加载的页
                    }
                    if(pos > StoreApiService.sSize/2 && pagep > CurrentPage){  //当光标移到每页的下半部分里加载，且不重复加载
                        showMore = true;
                        CurrentPage = pagep;
                        mHandler.sendEmptyMessage(STORE_APPS);
                    }
                }
            }

            @Override
            public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                AppData item = (AppData) mGridAdapter.getItem(position);
                ARouter.getInstance().build(RouterURL.StoreDetail).withParcelable("appData",item).navigation();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    @Override
    public void initData() {
        mPresenter.getStoreTypes(LocaleHelper.getLanguage(this).getLanguage());
        Update update = new Update(this,getFragmentManager());
        update.checkUpdate();
    }

    @Override
    public void showStoreTypes(List<MenuData> types) {
        mListAdapter.clearDatas();
        mListAdapter.appendDatas(types);

        storeType.requestFocus();
        storeSort.setVisibility(View.VISIBLE);
        storeTool.setVisibility(View.VISIBLE);

        if(types != null && types.size() > 0){
            storeType.setSelection(0);
        }
    }

    @Override
    public void showStoreApps(PageData pageData) {
        if(pageData == null) return;
        this.mPageData = pageData;

        if(!showMore){
            mGridAdapter.clearDatas();
            mGridAdapter.appendDatas(pageData.getPageData());
            mGridAdapter.notifyDataSetChanged();

            if(mGridAdapter.getItemCount() > 0){
                storeApps.smoothScrollToPosition(0);
                storeMainRecord.setText(String.format(getString(R.string.module_store_app_record),String.valueOf(1),String.valueOf(mPageData.getTotalCount())));
            }
            storeProgressbar.setVisibility(View.GONE);
            storeApps.setVisibility(View.VISIBLE);
        }else{
            mGridAdapter.appendDatas(pageData.getPageData());
        }

    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().todo(msg);
        }
    }

    private void todo(Message msg) {
        switch (msg.what){
            case STORE_APPS:
                    if(mListAdapter != null && mListAdapter.getItemCount() > selPosition){
                        MenuData item = mListAdapter.getItem(selPosition);
                        if(cPosition != -1 && cPosition != selPosition && !showMore) {
                            storeApps.setVisibility(View.INVISIBLE);
                            storeProgressbar.setVisibility(View.VISIBLE);
                        }
                        cPosition = selPosition;

                        mPresenter.getStoreApps(LocaleHelper.getLanguage(BaseApplication.getAppContext()).getLanguage(),
                                item.getId(),sort,
                                String.valueOf(CurrentPage),
                                String.valueOf(StoreApiService.sSize));
                    }
                break;
            }
    }
}
