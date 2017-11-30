package com.dxngxhl.utils;

/**
 * Created by Cool on 2017/11/30.
 */

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;

/**
 * 测试
 * */
public class DebugUtil {
    private static DebugUtil debugUtil = new DebugUtil();
    private static Context mContext;
    private static String TAG = "Debug-WM-";
    private static boolean isDebug = true;
    public static void Init(Context context){
        mContext = context;
        isDebug = isApkInDebug();
    }
    /**
     * 判断当前应用是否是debug状态
     */
    private static boolean isApkInDebug() {
        try {
            ApplicationInfo info = mContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static void d(String str){
        if (isDebug){
            Log.d(TAG,str);
        }
    }
    public static void e(String str){
        if (isDebug){
            Log.e(TAG,str);
        }
    }
    public static void toast(String str){
        if (isDebug){
            Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
        }
    }
}
