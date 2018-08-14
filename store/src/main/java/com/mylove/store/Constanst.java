package com.mylove.store;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.utils.FileUtils;
import com.mylove.store.model.UpdateApiService;

import java.io.File;

/**
 * Created by Administrator on 2018/7/18.
 */

public class Constanst {

    public static String DIRECTORY = "storedown";
    public static String SPLASHNAME = "splash";

    public static final String UMENG_CHANNEL = "UMENG_CHANNEL";
    public static final String UPDATE_URL = UpdateApiService.sUpdate + "index.php/update/app?data=%s";

    public static String getUpdateUrl(String data){
        return String.format(UPDATE_URL, data);
    }

    public static String getMeta(Context ctx, String key){
        try{
            ApplicationInfo ai =  ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            return ai.metaData.getString(key);
        }catch(Exception e){

        }
        return null;
    }

    public static String getPath(){
        String downPath = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // android 7.0 以上
            downPath = BaseApplication.getAppContext().getCacheDir().getPath() + File.separator + DIRECTORY;
        }else{
            if (ExistSDCard()){
                downPath = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + DIRECTORY;
            }else{
                downPath = BaseApplication.getAppContext().getCacheDir().getPath() + File.separator + DIRECTORY;
            }
        }

        if(!FileUtils.isFileExists(new File(downPath))){
            File file = new File(downPath);
            file.mkdir();
        }
        return downPath;
    }

    public static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static String getSplashPath(){
        String splashPath = BaseApplication.getAppContext().getFilesDir().getPath() + File.separator + SPLASHNAME;;
        return splashPath;
    }

}
