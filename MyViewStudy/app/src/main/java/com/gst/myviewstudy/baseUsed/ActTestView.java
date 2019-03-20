package com.gst.myviewstudy.baseUsed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gst.myviewstudy.R;
import com.gst.myviewstudy.other.CircleWaveView;
import com.gst.myviewstudy.utils.ViewHelper;

/**
 * author: GuoSongtao on 2018/1/12 15:22
 * email: 157010607@qq.com
 */

public class ActTestView extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_view);

        CircleWaveView cwv_show = ViewHelper.bindView(this, R.id.cwv_show);
        cwv_show.setWavepercent(0.596F);
        cwv_show.startSameValueAnimationThread();
    }
}
