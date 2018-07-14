package com.mylove.module_base.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylove.module_base.component.ApplicationComponent;

public interface IBase {

    View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    View getView();

    int getContentLayout();

    void initInjector(ApplicationComponent appComponent);

    void bindView(View view, Bundle savedInstanceState);

    void initData();

}
