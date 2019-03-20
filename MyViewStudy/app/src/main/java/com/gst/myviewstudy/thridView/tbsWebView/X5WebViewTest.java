package com.gst.myviewstudy.thridView.tbsWebView;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import com.gst.myviewstudy.MainActivity;
import com.gst.myviewstudy.R;
import com.gst.myviewstudy.utils.ToastUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;

/**
 * author: GuoSongtao on 2018/2/2 15:58
 * email: 157010607@qq.com
 */

public class X5WebViewTest {
    private Activity mAct;

    public X5WebViewTest() {
    }

    public void testX5WebViewJS(Activity mAct) {
        this.mAct = mAct;
        mAct.setContentView(R.layout.third_view_web_view);
        X5WebView wv_test = mAct.findViewById(R.id.wv_test);
        Button btn_android = mAct.findViewById(R.id.btn_android);
        initWebViewSetting(wv_test);
        wv_test.loadUrl("file:///android_asset/jstest.html");
        btn_android.setOnClickListener(v -> {
//            webView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
            wv_test.loadUrl("javascript:androidCall('" + "来自android按钮" + "')");
        });
    }


    @JavascriptInterface
    public void toDoAndroid(String action) {
        ToastUtil.show(action);
        Intent intent = new Intent(mAct, MainActivity.class);
        mAct.startActivity(intent);
    }

    @JavascriptInterface
    public void toActivity(String activity) {
        if ("MainAct".equals(activity)) {
            Intent intent = new Intent(mAct, MainActivity.class);
            mAct.startActivity(intent);
        } else {
            ToastUtil.show("gotoActivity(" + activity + ")");
        }
    }

    @JavascriptInterface
    public void clickMoreOnAndroid() {
        ToastUtil.show("window.ActWebviewTest.clickMoreOnAndroid()");
    }


    private void initWebViewSetting(X5WebView wv_show) {

        WebSettings webSetting = wv_show.getSettings();
        webSetting.setJavaScriptEnabled(true);
        wv_show.addJavascriptInterface(this, "ActWebviewTest");//添加能使用javaScript的方法

        wv_show.setWebChromeClient(new WebChromeClient());//使网页自带的Alert()可以弹出显示
    }
}
