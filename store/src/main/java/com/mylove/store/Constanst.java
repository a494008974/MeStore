package com.mylove.store;

import com.mylove.module_base.base.BaseApplication;
import com.mylove.module_base.utils.FileUtils;

import java.io.File;

/**
 * Created by Administrator on 2018/7/18.
 */

public class Constanst {

    public static String DIRECTORY = "store_down";
    public static String downPath;

    public static String getPath(){
        if(downPath != null){
            return downPath;
        }
        if (ExistSDCard()){
            downPath = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator + DIRECTORY;
        }else{
            downPath = BaseApplication.getAppContext().getCacheDir().getPath() + File.separator + DIRECTORY;
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
}
