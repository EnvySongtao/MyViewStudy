package com.gst.myviewstudy.ui.base;


import android.app.Dialog;

import com.gst.myviewstudy.sysViewTest.ActSysViewTest;
import com.gst.myviewstudy.sysViewTest.DialogTest;
import com.gst.myviewstudy.ui.bean.ViewInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * author: GuoSongtao on 2018/2/8 16:35
 * email: 157010607@qq.com
 */

public class ConstantBeans {
    public static List<ViewInfo> viewInfos = new ArrayList<>();

    static {
        for (String mothed : ActSysViewTest.TESTMETHODNAME) {
            viewInfos.add(new ViewInfo(mothed));
        }
    }
}
