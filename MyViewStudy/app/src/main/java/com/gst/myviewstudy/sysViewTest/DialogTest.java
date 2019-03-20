package com.gst.myviewstudy.sysViewTest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.gst.myviewstudy.R;

/**
 * author: GuoSongtao on 2018/1/30 17:53
 * email: 157010607@qq.com
 */

public class DialogTest {


    static class MyDialog extends Dialog {
        private Context mAct;
        private ImageView iv_show;
        //android:minWidth="10000dp" 可以设置全屏宽度

        protected MyDialog(Context context) {
            super(context, R.style.New_Dialog_WrapContent);
            mAct = context;
            init();
        }

        private void init() {
            View view = View.inflate(mAct, R.layout.sys_view_dialog_full_width, null);
            setContentView(view);

            iv_show = (ImageView) view.findViewById(R.id.iv_show);
//            Glide.with(mAct.getApplicationContext()).load(resouceId).into(iv_notice);

//        iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
//        iv_cancel.setOnClickListener(this);
            setCancelable(true);
        }
    }

    /**
     * 测试铺满 屏幕的dialog
     *
     * @param mAct
     */
    public static void showFullScreenWidthDialog(Activity mAct) {
        mAct.setContentView(R.layout.act_only_textview_layout);
        DialogTest.MyDialog dalog = new DialogTest.MyDialog(mAct);
        mAct.findViewById(R.id.sample_text).setOnClickListener((View view) -> {
            dalog.show();
        });
    }

}
