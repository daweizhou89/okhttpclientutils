package com.github.daweizhou89.okhttpclientutils.sample;

import android.app.Application;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;

/**
 * Created by zhoudawei on 2017/2/28.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClientUtils.config()
                .setUserAgent("daweizhou89/test")   // 设定UserAgent
                .setAssertMainThread(true)          // 打开检测UI线程执行的断言
                .setDebugLog(true);                 // 打开log
    }
}
