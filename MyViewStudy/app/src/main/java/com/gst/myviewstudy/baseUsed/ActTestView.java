package com.gst.myviewstudy.baseUsed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gst.myviewstudy.R;

/**
 * author: GuoSongtao on 2018/1/12 15:22
 * email: 157010607@qq.com
 */

public class ActTestView extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_view);
    }
}
