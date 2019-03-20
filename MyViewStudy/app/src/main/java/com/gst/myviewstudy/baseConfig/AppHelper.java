package com.gst.myviewstudy.baseConfig;

import android.app.Application;

import com.gst.myviewstudy.MyApp;
import com.gst.myviewstudy.utils.ToastUtil;

/**
 * author: GuoSongtao on 2018/2/2 16:15
 * email: 157010607@qq.com
 */

public class AppHelper {

    public static Application getAppInstance() {
        return MyApp.getInstance();
    }
}
