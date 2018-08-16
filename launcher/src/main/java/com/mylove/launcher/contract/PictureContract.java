package com.mylove.launcher.contract;

import com.mylove.launcher.bean.Bizhi;
import com.mylove.module_base.base.BaseContract;

import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */

public class PictureContract implements BaseContract{

    public interface View extends BaseView{
        void showBizhi(List<Bizhi> bizhis);
    }

    public interface Presenter extends BasePresenter<View>{
    }

}
