package com.mylove.store.contract;

import com.mylove.module_base.base.BaseContract;
import com.mylove.store.bean.BannerData;
import com.mylove.store.bean.BaseResponse;

/**
 * Created by Administrator on 2018/7/13.
 */

public class MainContract implements BaseContract {

    public interface View extends BaseContract.BaseView{
        void showResult(BaseResponse<BannerData> bannerDataBaseResponse);
    }

    public interface Presenter extends BaseContract.BasePresenter<View>{

    }
}
