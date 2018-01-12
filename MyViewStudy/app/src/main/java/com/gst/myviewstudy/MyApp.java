package com.gst.myviewstudy;

import android.app.Application;

/**
 * author: GuoSongtao on 2018/1/12 11:25
 * email: 157010607@qq.com
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Contants.setTag();
    }
}
