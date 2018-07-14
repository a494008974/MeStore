package com.mylove.store.presenter;

import com.mylove.module_base.base.BasePresenter;
import com.mylove.store.contract.DetailContract;

import javax.inject.Inject;

/**
 * Created by zhou on 2018/7/14.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    @Inject
    public DetailPresenter() {

    }
}
