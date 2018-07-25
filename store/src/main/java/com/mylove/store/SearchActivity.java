package com.mylove.store;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mylove.module_base.adapter.CommonRecyclerViewAdapter;
import com.mylove.module_base.base.BaseActivity;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.component.ApplicationComponent;
import com.mylove.module_base.focus.FocusBorder;
import com.mylove.module_base.helper.LocaleHelper;
import com.mylove.module_base.module.ApplicationModule;
import com.mylove.module_base.utils.KeybordUtil;
import com.mylove.module_common.RouterURL;
import com.mylove.store.adapter.GridAdapter;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.PageData;
import com.mylove.store.component.DaggerStoreComponent;
import com.mylove.store.contract.SearchContract;
import com.mylove.store.model.StoreApiService;
import com.mylove.store.module.StoreModule;
import com.mylove.store.presenter.SearchPresenter;
import com.owen.tvrecyclerview.widget.SimpleOnItemListener;
import com.owen.tvrecyclerview.widget.TvRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


@Route(path = RouterURL.StoreSearch)
public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    @BindView(R2.id.edit_search)
    EditText editSearch;

    @BindView(R2.id.store_search_apps)
    TvRecyclerView storeSearchApps;

    private CommonRecyclerViewAdapter mGridAdapter;
    private List<AppData> appDatas = new ArrayList<AppData>();

    public static final long STORE_SEND_MSG_DURATION = 600;
    public static final int STORE_SEARCH_APPS = 0x1214;

    private Handler mHandler = new MyHandler(this);
    protected FocusBorder mFocusBorder;

    private String searchKey;
    private PageData mPageData;

    private int CurrentPage = 1;
    public boolean showMore = false;
    @Override
    public int getContentLayout() {
        return R.layout.module_store_activity_search;
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

        storeSearchApps.setSpacingWithMargins(10, 10);
        mGridAdapter = new GridAdapter(this);
        mGridAdapter.setDatas(appDatas);
        storeSearchApps.setAdapter(mGridAdapter);
    }

    protected void onMoveFocusBorder(View focusedView, float scale, float roundRadius) {
        if(null != mFocusBorder) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale, roundRadius));
        }
    }

    private void setListener() {

        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    onMoveFocusBorder(v, 1.0f, 40);
                    KeybordUtil.showSoftInput(SearchActivity.this,editSearch);
                }else{
                    KeybordUtil.closeKeybord(SearchActivity.this);
                }
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeMessages(STORE_SEARCH_APPS);
                CurrentPage = 1;       //重置初始值
                showMore = false;
                searchKey = s.toString();
                mHandler.sendEmptyMessageDelayed(STORE_SEARCH_APPS,STORE_SEND_MSG_DURATION);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        storeSearchApps.setOnItemListener(new SimpleOnItemListener() {
            @Override
            public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                onMoveFocusBorder(itemView, 1.0f, 8);
                if(mPageData != null){
                    //加载更多
                    int pos = position % StoreApiService.sSize + 1;   //光标在那里判断加载
                    int pagep = position / StoreApiService.sSize + 1;  //当前页
                    if(pagep < mPageData.getTotalPage()){
                        pagep ++;             //当前要加载的页
                    }
                    if(pos > StoreApiService.sSize/2 && pagep > CurrentPage){  //当光标移到每页的下半部分里加载，且不重复加载
                        showMore = true;
                        CurrentPage = pagep;
                        mHandler.sendEmptyMessage(STORE_SEARCH_APPS);
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
    public void initData() {
//        mPresenter.getSearchApps(LocaleHelper.getLanguage(BaseApplication.getAppContext()).getLanguage(),
//                "4","1001",
//                String.valueOf(CurrentPage),
//                String.valueOf(StoreApiService.sSize));     //推荐
    }

    @Override
    public void showSearchApps(PageData pageData) {
        if(pageData == null) return;
        this.mPageData = pageData;

        if(!showMore){
            mGridAdapter.clearDatas();
            mGridAdapter.appendDatas(pageData.getPageData());
            mGridAdapter.notifyDataSetChanged();
        }else{  //加载更多
            mGridAdapter.appendDatas(pageData.getPageData());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SearchActivity> mActivity;

        public MyHandler(SearchActivity activity) {
            mActivity = new WeakReference<SearchActivity>(activity);
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
            case STORE_SEARCH_APPS:

                mPresenter.getSearchApps(LocaleHelper.getLanguage(BaseApplication.getAppContext()).getLanguage(),
                        String.valueOf(CurrentPage),
                        String.valueOf(StoreApiService.sSize),searchKey);

                break;
        }
    }
}
