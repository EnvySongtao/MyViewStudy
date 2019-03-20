package com.gst.myviewstudy.thridView.tbsWebView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.MimeTypeMap;

import com.gst.myviewstudy.Constant;
import com.gst.myviewstudy.baseConfig.AppHelper;
import com.gst.myviewstudy.utils.LogUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * author: GuoSongtao on 2018/2/2 17:38
 * email: 157010607@qq.com
 */

public class X5WebView extends WebView {

    private WebViewClient client = null;

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);

        client = new RequestHeaderWebViewClient2();
        this.setWebViewClient(client);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable(true);
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
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


        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }


    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(0);
    }


    /**
     * Webview.loadUrl(url, headerParams) 有限制，带body不行
     * 生产环境实验session 以后用其他试试
     * 带请求头的WebViewClient1
     */
    public class RequestHeaderWebViewClient1 extends com.tencent.smtt.sdk.WebViewClient {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //每次请求Url都同步一下cooikes
            synTBSCookies(X5WebView.this, view.getContext(), url);
            //load(url,map<>) 添加请求头，
            loadUrlWithRequestHeaderCooikes(X5WebView.this, view.getContext(), url);
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
     * 带请求头的WebViewClient2
     */
    public class RequestHeaderWebViewClient2 extends com.tencent.smtt.sdk.WebViewClient {

        /**
         * 防止加载网页时调起系统浏览器
         * 同步cooike
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            //每次请求Url都同步一下cooikes
            synTBSCookies(X5WebView.this, webView.getContext(), url);
            webView.loadUrl(url);
            return true;
        }

        /**
         * 添加请求头 + 去除广告
         *
         * @param webView
         * @param url
         * @return
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
            url = url.toLowerCase();
            //去除广告
            if (ADFilterTool.hasAd(AppHelper.getAppInstance(), url))
                return new WebResourceResponse(null, null, null);

            try {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
                String cookie = sharedPreferences.getString(Constant.SP_SESSIONID, null);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().
                        addHeader("Cookie", "MTPSESSIONID=" + cookie)
                        .addHeader("device", "Android")
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                return new WebResourceResponse(getMimeType(url),
                        response.header("content-encoding", "utf-8"),
                        response.body().byteStream());
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
        }

        //get mime type by url
        public String getMimeType(String url) {

            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            if (extension != null) {
                if (extension.equals("js")) {
                    return "text/javascript";
                } else if (extension.equals("woff")) {
                    return "application/font-woff";
                } else if (extension.equals("woff2")) {
                    return "application/font-woff2";
                } else if (extension.equals("ttf")) {
                    return "application/x-font-ttf";
                } else if (extension.equals("eot")) {
                    return "application/vnd.ms-fontobject";
                } else if (extension.equals("svg")) {
                    return "image/svg+xml";
                }
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type;
        }

    }


    /*******************多处X5WebVIew都会使用该同步方法 Start************************/

    /**
     * webview 同步cookie
     *
     * @param mCommonWebview
     * @param context
     * @param url
     */
    public static void synTBSCookies(X5WebView mCommonWebview, Context context, String url) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            com.tencent.smtt.sdk.CookieSyncManager.createInstance(context);
        }
        /**
         * webview 自带cooikes
         */
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString(Constant.SP_SESSIONID, null);

//        LogUtil.i("requestUrl=cookies：" + cookie);
////        SystemClock.sleep(1000);
//        //如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
//        cookieManager.setCookie(url, "MTPSESSIONID=" + cookie);//cookies是在HttpClient中获得的cookie

        StringBuilder sbCookie = new StringBuilder();//创建一个拼接cookie的容器,为什么这么拼接，大家查阅一下http头Cookie的结构
        sbCookie.append("MTPSESSIONID=" + cookie);//拼接sessionId
        sbCookie.append(String.format(";domain=%s", "")); //域（貌似不设也无所谓）
        sbCookie.append(String.format(";path=%s", "")); //路径（貌似不设也无所谓）
        cookieManager.setCookie(url, "device=Android"); //你想设置的参数
        String cookieValue = sbCookie.toString();
//        SystemClock.sleep(1000);
        cookieManager.setCookie(url, cookieValue);

        //接受第三方cookieManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mCommonWebview, true);
        }

        //同步cookies
        if (Build.VERSION.SDK_INT < 21) {
            com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
        } else {
            com.tencent.smtt.sdk.CookieManager.getInstance().flush();
        }

        CookieSyncManager.getInstance().sync();

    }

    /**
     * webview 请求头加上 cooikes
     */
    public static void loadUrlWithRequestHeaderCooikes(X5WebView mCommonWebview, Context context, String url) {
        HashMap<String, String> headerParams = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        String cookie = sharedPreferences.getString(Constant.SP_SESSIONID, null);
        headerParams.put("Cookie", "MTPSESSIONID=" + cookie);
        headerParams.put("device", "Android");//设备标识(前面是key，后面是value)
        mCommonWebview.loadUrl(url, headerParams);
    }
    /*******************多处X5WebVIew都会使用该同步方法  end ************************/


//	@Override
//	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
////		boolean ret = super.drawChild(canvas, child, drawingTime);
////		canvas.save();
////		Paint paint = new Paint();
////		paint.setColor(0x7fff0000);
////		paint.setTextSize(24.f);
////		paint.setAntiAlias(true);
////		if (getX5WebViewExtension() != null) {
////			canvas.drawText(this.getContext().getPackageName() + "-pid:"
////					+ android.os.Process.myPid(), 10, 50, paint);
////			canvas.drawText(
////					"X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
////					100, paint);
////		} else {
////			canvas.drawText(this.getContext().getPackageName() + "-pid:"
////					+ android.os.Process.myPid(), 10, 50, paint);
////			canvas.drawText("Sys Core", 10, 100, paint);
////		}
////		canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
////		canvas.drawText(Build.MODEL, 10, 200, paint);
////		canvas.restore();
//		return ret;
//	}

}
