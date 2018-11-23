package com.mylove.store.update;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.net.BaseObserver;
import com.mylove.module_base.net.RxSchedulers;
import com.mylove.module_base.net.down.DownloadCallback;
import com.mylove.module_base.net.down.DownloadListener;
import com.mylove.module_base.net.down.DownloadRecord;
import com.mylove.module_base.net.down.DownloadRequest;
import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.module_base.utils.AppUtils;
import com.mylove.module_base.utils.FileUtils;
import com.mylove.store.Constanst;
import com.mylove.store.DetailActivity;
import com.mylove.store.R;
import com.mylove.store.ShowPic;
import com.mylove.store.ShowUpdate;
import com.mylove.store.bean.UpdateData;
import com.mylove.store.model.UpdateApiService;
import com.mylove.store.utils.ApkUtils;

import java.io.File;

import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018/7/25.
 */

public class Update {
    private OkHttpClient.Builder builder;
    private Context mContext;
    private String fileName;
    private ShowUpdate showUpdate;
    private FragmentManager fragmentManager;

    public Update(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        builder = BaseApplication.getAppContext().getApplicationComponent().getOkHttp();
    }

    public void checkUpdate(){
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        String updateUrl = mContext.getString(R.string.module_store_update_url);

        UpdateApiService updateApiService = retrofitBuilder
                .baseUrl(updateUrl)
                .build().create(UpdateApiService.class);

        updateApiService.getUpdate(Constanst.getUpdateUrl(SystemUtility.getParam(mContext)))
                .compose(RxSchedulers.<String>applySchedulers())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        s = DesHelper.decrypt(s,SystemUtility.DOWNKEY);
                        return s;
                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if("".equals(s)) return;

                        try {
                            Gson gson = new Gson();
                            UpdateData updateData = gson.fromJson(s,UpdateData.class);
                            if(updateData != null){
                                if("2".equals(updateData.getStatus())){
                                   showUpdate(updateData);
                                }
                            }
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
    }

    public void showUpdate(final UpdateData updateData){
        if (updateData == null) return;
        showUpdate = ShowUpdate.newInstance(updateData);
        showUpdate.setShowUpdateListener(new ShowUpdate.ShowUpdateListener() {
            @Override
            public void down(String url) {
                downUpdate(url);
            }
        });
        showUpdate.show(fragmentManager,"showUpdate");
    }

    public void downUpdate(String url){
        fileName = BaseApplication.getAppContext().getCacheDir().getPath() + File.separator + "tmp.apk";
        DownloadListener downloadListener = new DownloadCallback() {
            @Override
            public void onStart(DownloadRecord record) {
                super.onStart(record);
                if(showUpdate != null){
                    showUpdate.setProgress(0);
                }
            }

            @Override
            public void onProgress(DownloadRecord record) {
                if(showUpdate != null){
                    showUpdate.setProgress(record.getProgress());
                }
            }

            @Override
            public void onFailed(DownloadRecord record, String errMsg) {
                if(showUpdate != null){
                    showUpdate.dismiss();
                }
                FileUtils.deleteFile(fileName);
                DownloadUtil.get().removeTask(record);
            }

            @Override
            public void onFinish(DownloadRecord record) {
                if(showUpdate != null){
                    showUpdate.dismiss();
                }
                ApkUtils.install(BaseApplication.getAppContext(),record.getFilePath());
                DownloadUtil.get().removeTask(record);
            }

            @Override
            public void onCanceled(DownloadRecord record) {
            }
        };
        DownloadRequest request = DownloadRequest.newBuilder()
                .downloadName(DownloadUtil.getMD5(url))
                .downloadDir(Constanst.getPath())
                .downloadUrl(url)
                .downloadListener(downloadListener)
                .build();

        DownloadUtil.get().enqueue(request);

    }
}
