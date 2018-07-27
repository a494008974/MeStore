package com.mylove.store;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mylove.module_base.helper.ImageLoaderHelper;
import com.mylove.store.bean.UpdateData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/21.
 */

public class ShowUpdate extends DialogFragment {

    private UpdateData updateData;
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private TextView storeUpdateTvMsg;
    private TextView storeUpdateTvProgress;
    private Button storeUpdateBtnOk;
    private Button storeUpdateBtnCancle;
    private ShowUpdateListener showUpdateListener;

    public void setShowUpdateListener(ShowUpdateListener showUpdateListener) {
        this.showUpdateListener = showUpdateListener;
    }

    public ShowUpdate() {
        this.setStyle(0, R.style.Transparent);
        this.setCancelable(false);
    }

    public static ShowUpdate newInstance(UpdateData updateData){
        ShowUpdate showPic = new ShowUpdate();
        Bundle bundle = new Bundle();
        bundle.putParcelable("DATAS",updateData);
        showPic.setArguments(bundle);
        return showPic;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        updateData = bundle.getParcelable("DATAS");
        View view = inflater.inflate(R.layout.module_store_update,container);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.store_update_prop);
        frameLayout = (FrameLayout)view.findViewById(R.id.store_update_progress);
        storeUpdateTvMsg = (TextView) view.findViewById(R.id.store_update_tv_msg);
        storeUpdateTvProgress = (TextView) view.findViewById(R.id.store_update_tv_progress);
        storeUpdateBtnOk = (Button) view.findViewById(R.id.store_update_btn_ok);
        storeUpdateBtnCancle = (Button) view.findViewById(R.id.store_update_btn_cancle);
        storeUpdateTvMsg.setText(updateData.getRemark());
        storeUpdateBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                if(showUpdateListener != null){
                    showUpdateListener.down(updateData.getUrl());
                }
            }
        });
        storeUpdateBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public void setProgress(int progress){
        if(storeUpdateTvProgress != null){
            storeUpdateTvProgress.setText(progress+"%");
        }
    }

    public interface ShowUpdateListener{
        void down(String url);
    }
}
