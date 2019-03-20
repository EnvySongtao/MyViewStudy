package com.gst.myviewstudy.sysViewTest;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;

import com.gst.myviewstudy.R;
import com.gst.myviewstudy.utils.LogUtil;

/**
 * author: GuoSongtao on 2018/1/31 16:09
 * email: 157010607@qq.com
 */

public class ViewStubTest {

    /**
     * ViewStub 的使用
     * <p>
     * 可以考虑使用ViewStub的情况有：
     * 1. 在程序的运行期间，某个布局在Inflate后，就不会有变化，除非重新启动。
     * 因为ViewStub只能Inflate一次，之后会被置空，所以无法指望后面接着使用ViewStub来控制布局。
     * 所以当需要在运行时不止一次的显示和隐藏某个布局，那么ViewStub是做不到的。这时就只能使用View的可见性来控制了。
     * 2. 想要控制显示与隐藏的是一个布局文件，而非某个View。
     * 因为设置给ViewStub的只能是某个布局文件的Id，所以无法让它来控制某个View。
     * 所以，如果想要控制某个View(如Button或TextView)的显示与隐藏，或者想要在运行时不断的显示与隐藏某个布局或View，
     * 只能使用View的可见性来控制。
     *
     * @param mAct
     */
    public static void testViewStub(Activity mAct) {
        mAct.setContentView(R.layout.sys_view_viewstub_test);
        //不显示 ViewStub
        ViewStub viewStub = mAct.findViewById(R.id.vs_content);
        viewStub.setLayoutResource(R.layout.act_percent_linearlayout_test);

        //判断viewStub 是否被 inflate()
        if (viewStub.getParent() != null) {
            LogUtil.i("viewStub 已经被 inflate过");
        }

        //显示 ViewStub
//        ViewStub viewStub = mAct.findViewById(R.id.vs_content);
//        //ViewStub must have a valid layoutResource
//        viewStub.setLayoutResource(R.layout.act_percent_linearlayout_test);
//         //viewStub.setVisibility(View.VISIBLE);//不inflate() 用VISIBLE 会自动inflate
//        viewStub.inflate();
//         // TODO: 2018/1/31  findViewById 一定要在 inflate或setVisibility(View.VISIBLE)后
//        //ViewStub设置的不是自己，而是拿到关联的那个Layout设置visible。ViewStub此时并未销毁，所以建议初始化后将其设置为空。
//        viewStub = null;
    }

}
