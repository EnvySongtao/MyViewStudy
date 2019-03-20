package com.gst.myviewstudy.utils;

import android.app.Application;
import android.content.res.Resources;

import com.gst.myviewstudy.MyApp;

/**
 * author: GuoSongtao on 2018/1/19 10:06
 * email: 157010607@qq.com
 */

public class ContextHelper {

    /**
     * 获取 appplication实例 manifest.xml发生变化时 也需要改变
     *
     * @return
     */
    public static Application getAppInstance() {
        return MyApp.getInstance();
    }

    /**
     * 获取 appplication实例 manifest.xml发生变化时 也需要改变
     *
     * @return
     */
    public static Resources getAppRescources() {
        return getAppInstance().getResources();
    }
}
