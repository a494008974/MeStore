package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;

/**
 * Created by zhou on 2018/7/14.
 */

public class DetailContract implements BaseContract{

    public interface View extends BaseContract.BaseView{
        public void showProgress(int progress);
        public void downPause();
        public void downStart();
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
