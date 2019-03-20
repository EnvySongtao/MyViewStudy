package com.gst.myviewstudy.sysViewTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.gst.myviewstudy.Constant;
import com.gst.myviewstudy.MainActivity;
import com.gst.myviewstudy.R;
import com.gst.myviewstudy.baseConfig.AppHelper;
import com.gst.myviewstudy.thridView.tbsWebView.ADFilterTool;
import com.gst.myviewstudy.thridView.tbsWebView.CookiesManager;
import com.gst.myviewstudy.utils.LogUtil;
import com.gst.myviewstudy.utils.ToastUtil;

import java.util.HashMap;

/**
 * author: GuoSongtao on 2018/2/2 15:58
 * email: 157010607@qq.com
 */

public class WebViewTest {
    private Activity mAct;

    public WebViewTest() {
    }

    public void testSysWebViewJS(Activity mAct) {
        this.mAct = mAct;
        mAct.setContentView(R.layout.sys_view_web_view);
        WebView wv_test = mAct.findViewById(R.id.wv_test);
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


    private void initWebViewSetting(WebView wv_show) {

        WebSettings webSetting = wv_show.getSettings();
        webSetting.setJavaScriptEnabled(true);
        wv_show.addJavascriptInterface(this, "ActWebviewTest");//添加能使用javaScript的方法
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(false);
        webSetting.setDatabaseEnabled(false);
        webSetting.setDomStorageEnabled(false);
        webSetting.setGeolocationEnabled(false);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//		 webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv_show.setWebChromeClient(new WebChromeClient());//使网页自带的Alert()可以弹出显示


    }

    private class MyWebChromeClient extends WebViewClient {

        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //每次请求Url都同步一下cooikes
            synSysWebViewCookies(view, view.getContext(), url);
            //load(url,map<>) 添加请求头
            loadUrlWithRequestHeaderCooikes(view, view.getContext(), url);
//            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            url = url.toLowerCase();
            //去除广告
            if (!ADFilterTool.hasAd(AppHelper.getAppInstance(), url)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }
        }
    }


    /**
     * 自带的cookieManager 根据url同步cooike
     *
     * @param mCommonWebview
     * @param context
     * @param url
     */
    public static void synSysWebViewCookies(WebView mCommonWebview, Context context, String url) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieSyncManager.createInstance(context);
        }

        /**
         * webview 自带cooikes
         */
        android.webkit.CookieSyncManager.createInstance(context);
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();

        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString(Constant.SP_SESSIONID, null);
//        LogUtil.i("requestUrl=cookies：" + cookie);
//        cookieManager.setCookie(url, "MTPSESSIONID=" + cookie);//cookies是在HttpClient中获得的cookie

        StringBuilder sbCookie = new StringBuilder();//创建一个拼接cookie的容器,为什么这么拼接，大家查阅一下http头Cookie的结构
        sbCookie.append("MTPSESSIONID=" + cookie);//拼接sessionId
        sbCookie.append(String.format(";domain=%s", "")); //域（貌似不设也无所谓）
        sbCookie.append(String.format(";path=%s", "")); //路径（貌似不设也无所谓）
        cookieManager.setCookie(url, "device=Android"); //你想设置的参数
        String cookieValue = sbCookie.toString();
//        SystemClock.sleep(1000);
        cookieManager.setCookie(url, cookieValue);

        // Android 5.0以上的手机使用原生WebView浏览网页，在进行登录的时候会提示验证码错误，通过查找5.0以上系统的api文档，
        // 发现5.0以上版本的webview做了较大的改动，如：同步cookie的操作已经可以自动同步、但前提是我们必须开启第三方cookie的支持。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mCommonWebview, true);
        }

        if (Build.VERSION.SDK_INT < 21) {
            android.webkit.CookieSyncManager.getInstance().sync();
        } else {
            android.webkit.CookieManager.getInstance().flush();
        }
        //  cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        LogUtil.i("requestUrl,newCookie=" + newCookie);

    }

    /**
     * webview 请求头带上 cooikes
     */
    public static void loadUrlWithRequestHeaderCooikes(WebView mCommonWebview, Context context, String url) {
        HashMap<String, String> headerParams = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString(Constant.SP_SESSIONID, null);
        headerParams.put("Cookie", "MTPSESSIONID=" + cookie);
        headerParams.put("device", "Android");//设备标识(前面是key，后面是value)
        mCommonWebview.loadUrl(url, headerParams);
    }


    /**
     * 设置 html的js Alert提示框 使用android系统AlertDialog或者自定义dialog
     *
     * @param wv_show
     */
    private void changeJsAlertToAndroid(WebView wv_show) {
        //设置响应js 的Alert()函数
        wv_show.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(mAct);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(mAct);
                b.setTitle("Confirm");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }
//            //设置响应js 的Prompt()函数
//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//                final View v = View.inflate(mAct, R.layout.prompt_dialog, null);
//                ((TextView) v.findViewById(R.id.prompt_message_text)).setText(message);
//                ((EditText) v.findViewById(R.id.prompt_input_field)).setText(defaultValue);
//                AlertDialog.Builder b = new AlertDialog.Builder(TestAlertActivity.this);
//                b.setTitle("Prompt");
//                b.setView(v);
//                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String value = ((EditText) v.findViewById(R.id.prompt_input_field)).getText().toString();
//                        result.confirm(value);
//                    }
//                });
//                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                });
//                b.create().show();
//                return true;
//            }
        });
    }
}
