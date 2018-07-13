package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.store.contract.MainContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/7/13.
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter{

    @Inject
    MainPresenter(){

    }
}
