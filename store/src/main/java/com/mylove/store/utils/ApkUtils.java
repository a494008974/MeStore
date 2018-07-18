package com.mylove.store.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ApkUtils {

    public static boolean checkInstall(Context ctx, String apk) {
        // TODO Auto-generated method stub
        boolean install = false;
        PackageManager pm = ctx.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(apk, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (info != null && info.activities.length > 0) {
                install = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return install;
    }

    public static void install(Context ctx, String filename) {
        Uri uri = Uri.fromFile(new File(filename));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    public static void uninstall(Context ctx, String apk) {
        Uri packageURI = Uri.parse("package:" + apk);// "package:com.demo.CanavaCancel"
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(uninstallIntent);
    }
}
