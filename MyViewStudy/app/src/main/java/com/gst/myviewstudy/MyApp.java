package com.gst.myviewstudy;

import android.app.Application;

import com.gst.myviewstudy.utils.ToastUtil;

/**
 * author: GuoSongtao on 2018/1/12 11:25
 * email: 157010607@qq.com
 */

public class MyApp extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Contants.setTag();
        ToastUtil.initToastUtil();
    }

    public static Application getInstance() {
        return instance;
    }
}
