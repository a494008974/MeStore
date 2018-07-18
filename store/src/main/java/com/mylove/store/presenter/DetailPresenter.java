package com.mylove.store.presenter;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.base.BasePresenter;
import com.mylove.module_base.net.down.DownloadCallback;
import com.mylove.module_base.net.down.DownloadListener;
import com.mylove.module_base.net.down.DownloadRecord;
import com.mylove.module_base.net.down.DownloadRequest;
import com.mylove.module_base.net.down.DownloadUtil;
import com.mylove.module_base.utils.FileUtils;
import com.mylove.module_base.utils.Md5;
import com.mylove.store.Constanst;
import com.mylove.store.contract.DetailContract;
import com.mylove.store.model.StoreApi;
import com.mylove.store.utils.ApkUtils;

import javax.inject.Inject;

/**
 * Created by zhou on 2018/7/14.
 */

public class DetailPresenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    private StoreApi storeApi;
    private String taskId;

    @Inject
    public DetailPresenter(StoreApi storeApi) {
        this.storeApi = storeApi;
    }

    public void downApp(String url){
        DownloadListener listener = new DownloadCallback(){
            @Override
            public void onProgress(DownloadRecord record) {
                super.onProgress(record);
                mView.showProgress(record.getProgress());
            }

            @Override
            public void onFailed(DownloadRecord record, String errMsg) {
                super.onFailed(record, errMsg);
                FileUtils.deleteFile(record.getFilePath());
            }

            @Override
            public void onPaused(DownloadRecord record) {
                super.onPaused(record);

            }

            @Override
            public void onResume(DownloadRecord record) {
                super.onResume(record);
            }

            @Override
            public void onFinish(DownloadRecord record) {
                super.onFinish(record);

                ApkUtils.install(BaseApplication.getAppContext(),record.getFilePath());
            }

            @Override
            public void onCanceled(DownloadRecord record) {
                super.onCanceled(record);
            }
        };

        DownloadRequest request = DownloadRequest.newBuilder()
                .downloadDir(Constanst.getPath())
                .downloadUrl(url)
                .downloadName(DownloadUtil.getMD5(url)+".apk")
                .downloadListener(listener)
                .build();

        taskId = request.getId();

        DownloadUtil.get().enqueue(request);
    }

    public void pauseDown(){
        DownloadUtil.get().pause(taskId);
    }
}
