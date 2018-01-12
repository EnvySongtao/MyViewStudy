package com.gst.myviewstudy;

import com.gst.myviewstudy.utils.LogUtil;

/**
 * author: GuoSongtao on 2018/1/12 11:27
 * email: 157010607@qq.com
 */

public class Contants {
    public final static boolean isDebug = true;

    /**
     * 配置大量信息 环境 dubug等
     */
    public static void setTag() {
        LogUtil.setAllowAll(isDebug);
    }
}
