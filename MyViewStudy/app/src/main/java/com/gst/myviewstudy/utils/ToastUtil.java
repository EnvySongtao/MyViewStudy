package com.gst.myviewstudy.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.gst.myviewstudy.baseConfig.AppHelper;


public class ToastUtil {
    private static ToastUtil instance = null;

    private ToastUtil() {
        context = AppHelper.getAppInstance().getApplicationContext();
        t = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        MainHandler.postMain(() -> Log.i("ToastUtil: ", "初始化MainHandler，防止第一次在子线程调用show出错"));
    }

    private static Toast t;
    private static Context context;

    public static void initToastUtil() {
        if (instance == null) instance = new ToastUtil();
    }

    /**
     * @param msg
     * @param time 多少时间
     */
    public static void showAsTime(CharSequence msg, int time) {
//        t.setText(msg);
//        t.setDuration(time);
//        instance.instanceShow();
        instance.instanceShow(msg, -1, -1, time);
    }

    /**
     * 4.0后不适用 预留改为handler
     *
     * @param resId
     * @param time
     */
    public static void showAsTime(int resId, int time) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        showAsTime(msg, time);
    }

    public static void show(CharSequence msg, int gravity) {
//        t.setText(msg);
//        t.setDuration(Toast.LENGTH_SHORT);
//        t.setGravity(gravity, 0, 0);
//        instance.instanceShow();
        instance.instanceShow(msg, Toast.LENGTH_SHORT, gravity, -1);
    }

    /**
     * 默认短时
     *
     * @param msg
     */
    public static void show(CharSequence msg) {
//        t.setText(msg);
//        t.setDuration(Toast.LENGTH_SHORT);
//        instance.instanceShow();
        instance.instanceShow(msg, Toast.LENGTH_SHORT, -1, -1);
    }

    public static void show(int resId) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        show(msg);
    }

    public static void showLong(CharSequence msg) {
//        t.setText(msg);
//        t.setDuration(Toast.LENGTH_LONG);
//        instance.instanceShow();
        instance.instanceShow(msg, Toast.LENGTH_LONG, -1, -1);
    }

    public static void showLong(int resId) {
        CharSequence msg = "";
        try {
            msg = context.getText(resId);
        } catch (Exception e) {
            msg = resId + "";
            e.printStackTrace();
        }
        showLong(msg);
    }


    private void instanceShow(CharSequence msg, int duration, int gravity, int time) {
        MainHandler.postMain(() -> {
            t.setText(msg);

            if (gravity > 0) {
                t.setGravity(gravity, 0, 0);
            }

            if (duration == Toast.LENGTH_LONG || duration == Toast.LENGTH_SHORT) {
                t.setDuration(duration);
            } else if (time > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                t.setDuration(time);
            } else {
                t.setDuration(Toast.LENGTH_SHORT);
            }

            t.show();
        });
    }

    /**
     * 保证主线程外 也可以弹出Toast
     */
    private void instanceShow() {
        MainHandler.postMain(() -> t.show());
    }
}
