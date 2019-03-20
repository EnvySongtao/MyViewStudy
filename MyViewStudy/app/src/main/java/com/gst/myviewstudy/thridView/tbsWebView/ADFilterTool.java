package com.gst.myviewstudy.thridView.tbsWebView;

import android.content.Context;
import android.content.res.Resources;

import com.gst.myviewstudy.R;

/**
 * author: GuoSongtao on 2018/2/2 17:40
 * email: 157010607@qq.com
 */

public class ADFilterTool {


    public static boolean hasAd(Context context, String url){
        Resources res= context.getApplicationContext().getResources();
        String[] adUrls =res.getStringArray(R.array.adBlockUrl);
        for(String adUrl :adUrls){
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }
}
