package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;
import com.mylove.store.bean.PageData;

/**
 * Created by Administrator on 2018/7/24.
 */

public class SearchContract implements BaseContract {
    public interface View extends BaseContract.BaseView{
        void showSearchApps(PageData pageData);
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
