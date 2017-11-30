package com.dxngxhl;

import android.app.Application;

import com.dxngxhl.utils.DebugUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Cool on 2017/11/30.
 */

public class WMApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        DebugUtil.Init(this);
    }
}
