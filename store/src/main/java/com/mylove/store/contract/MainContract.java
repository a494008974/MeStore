package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;

/**
 * Created by Administrator on 2018/7/13.
 */

public class MainContract implements BaseContract {

    public interface View extends BaseContract.BaseView{

    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
