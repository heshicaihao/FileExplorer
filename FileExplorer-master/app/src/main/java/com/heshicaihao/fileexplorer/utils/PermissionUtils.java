package com.heshicaihao.fileexplorer.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.heshicaihao.fileexplorer.R;
import com.heshicaihao.fileexplorer.common.SharedData;
import com.heshicaihao.fileexplorer.widget.OldDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 项目名称：wms
 * 类名称:PermissionUtils
 * 类描述:权限管理工具类
 * 创建人: wjl
 * 创建时间： 2016/11/18 14:36
 */
public class PermissionUtils {
    public static Activity mActivity;

    public static void dealNoPermission(final Context mContext) {
        mActivity = (Activity) mContext;

        OldDialog dialog = new OldDialog(mContext);
        dialog.builder();
        dialog.setTitle("权限设置");
        dialog.setMsg(mContext.getString(R.string.app_name)+"权限不足" + "\n" + "确定去设置");
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showInstalledAppDetails(mContext, mContext.getPackageName());
            }
        });
        dialog.setNegativeButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedData.setParam(mContext, "isPerm", false);
                System.exit(0);
            }
        });
        dialog.showDialog();
    }


    private static final String SCHEME = "package";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
     */
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";
    /**
     * InstalledAppDetails所在包名
     */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    /**
     * InstalledAppDetails类名
     */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    /**
     * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
     * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
     *
     * @param context
     * @param packageName 应用程序的包名
     */
    public static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            //前面已经有判断不是安卓6.0不打开权限查询功能故这里不需要再次进行判断
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        mActivity.startActivity(intent);
    }

}
