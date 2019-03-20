package com.gst.myviewstudy.thridView.tbsWebView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.WebView;

import com.gst.myviewstudy.Constant;
import com.gst.myviewstudy.utils.LogUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * author: GuoSongtao on 2018/2/5 17:54
 * email: 157010607@qq.com
 */

public class CookiesManager implements CookieJar {
    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url, cookies);
        cookieStore.put(HttpUrl.parse("http://192.168.31.231:8080/shiro-2"), cookies);
        for (Cookie cookie : cookies) {
            System.out.println("cookie Name:" + cookie.name());
            System.out.println("cookie Path:" + cookie.path());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://192.168.31.231:8080/shiro-2"));
        if (cookies == null) {
            System.out.println("没加载到cookie");
        }
        return cookies != null ? cookies : new ArrayList<Cookie>();

    }



}
