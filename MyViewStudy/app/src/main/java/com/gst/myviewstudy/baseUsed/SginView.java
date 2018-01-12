package com.gst.myviewstudy.baseUsed;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: GuoSongtao on 2018/1/12 17:05
 * email: 157010607@qq.com
 */

public class SginView extends View {
    public SginView(Context context) {
        this(context,null);
    }

    public SginView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        
    }
}
