package com.heshicaihao.fileexplorer.utils;


import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * 项目名称：wms
 * 创建人: heshicaihao
 * 创建时间：2017/08/13 10:20
 */
public class LogUtils {

    private static final String GLOBAL_TAG = "heshicaihao";
    private static boolean sEnableLog = true;

    public static void v(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.v(GLOBAL_TAG + "." + tag, "" + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.d(GLOBAL_TAG + "." + tag, msg);
        }
    }

    public static void d(JsonObject content) {
        if (sEnableLog) {
            Log.d(GLOBAL_TAG + ".", content.toString());
        }
    }

    public static void d(JSONObject content) {
        if (sEnableLog) {
            Log.d(GLOBAL_TAG + ".", content.toString());
        }
    }

    public static void d(String content) {
        if (sEnableLog) {
            Log.d(GLOBAL_TAG + ".", content.toString());
        }
    }

    public static void i(String content) {
        if (sEnableLog) {
            Log.i(GLOBAL_TAG + ".", content);
        }
    }

    public static void i(String tag, String content) {
        if (sEnableLog) {
            Log.i(GLOBAL_TAG + "." + tag, content);
        }
    }

    public static void e(String content) {
        if (sEnableLog) {
            Log.e(GLOBAL_TAG + ".", content);
        }
    }

    public static void e(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.e(GLOBAL_TAG + "." + tag, "" + msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.e(GLOBAL_TAG + "." + tag, "" + msg, e);
        }
    }

}