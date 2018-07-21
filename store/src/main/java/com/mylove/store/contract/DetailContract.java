package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;
import com.mylove.store.bean.DetailData;

/**
 * Created by zhou on 2018/7/14.
 */

public class DetailContract implements BaseContract{

    public interface View extends BaseContract.BaseView{
        void showProgress(int progress);
        void showDetail(DetailData detailData);
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
