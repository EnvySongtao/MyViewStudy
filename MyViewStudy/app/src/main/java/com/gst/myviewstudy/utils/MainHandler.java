package com.gst.myviewstudy.utils;

import android.os.Handler;
import android.os.Looper;


/**
 * author: GuoSongtao on 2018/1/12 11:11
 * email: 157010607@qq.com
 */

public class MainHandler extends Handler {
    private static MainHandler ourInstance = new MainHandler(Looper.getMainLooper());;

//    public static MainHandler getInstance() {
//        return ourInstance;
//    }


    private MainHandler(Looper mainLooper) {
        super(mainLooper);
    }

    /**
     * 在Application的onCreate中调用，懒汉式会导致抛出主线程还未准备好的异常
     */
    public static void initMainHandler() {
        if (ourInstance == null) {
            ourInstance = new MainHandler(Looper.getMainLooper());
            LogUtil.i("初始化MainHandler，防止第一次在子线程调用出错");
        }
    }

    public static void postMain(Runnable runnable) {
        if (ourInstance != null) {
            ourInstance.post(runnable);
        } else {
            LogUtil.i("ourInstance 为空!");
        }
    }
}
