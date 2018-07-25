package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;
import com.mylove.store.bean.AppData;
import com.mylove.store.bean.MenuData;
import com.mylove.store.bean.PageData;

import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class MainContract implements BaseContract {

    public interface View extends BaseContract.BaseView{
        void showStoreTypes(List<MenuData> types);
        void showStoreApps(PageData pageData);
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
