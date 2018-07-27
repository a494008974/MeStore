package com.mylove.store.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.mylove.module_base.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ApkUtils {

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static void openApp(Context ctx, String pkgname){
        try {
            PackageManager pm = ctx.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(pkgname);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }catch (Exception e){
        }
    }

    public static void install(Context ctx, String filename) {
        if(!FileUtils.isFileExists(filename)) return;
        File apk = new File(filename);
        Uri data;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(ctx, "com.mylove.store.install", apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apk);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    public static void uninstall(Context ctx, String apk) {
        Uri packageURI = Uri.parse("package:" + apk);// "package:com.demo.CanavaCancel"
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(uninstallIntent);
    }
}
