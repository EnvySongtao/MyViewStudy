package com.gst.myviewstudy.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.gst.myviewstudy.MyApp;

/**
 * author: GuoSongtao on 2017/12/28 15:21
 * email: 157010607@qq.com
 */

public class ViewHelper {


    /**
     * findViewById
     *
     * @param activity
     * @param viewId
     * @param <T>
     * @return
     */
    public static <T> T bindView(Activity activity, int viewId) {
        if (activity == null) return null;
        try {
            return (T) activity.findViewById(viewId);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * findViewById
     *
     * @param parentView
     * @param viewId
     * @param <T>
     * @return
     */
    public static <T> T bindView(View parentView, int viewId) {
        if (parentView == null) return null;
        try {
            return (T) parentView.findViewById(viewId);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Linkify是一个辅助类，它可以自动地在TextView（或其派生类）中通过RegEx（正则表达式）模式匹配来创建超链接。
     * 当TextView中的内容匹配成功并生成超链接之后，TextView内容的下面就会出现下划线，单击则可以触发相应的操作，例如拨号，打开浏览器等。
     *
     * @param textViewId
     * @return
     */
    public static void linkify(Activity activity, int textViewId) {
        TextView textView = bindView(activity, textViewId);
        if (textView != null) Linkify.addLinks(textView, Linkify.ALL);
    }


    /**
     * @param activity
     * @param typeface Android系统默认字体支持四种字体，分别为：noraml （普通字体,系统默认使用的字体）;sans（非衬线字体）;serif （衬线字体）;monospace（等宽字体）
     *                 也可用
     * @param viewIds
     */
    public static void setTypeface(Activity activity, Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = bindView(activity, viewId);
            if (view == null) continue;
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

    /**
     * 设置应用的显示字体，比较耗时
     * @param context
     * @param ttfPath 字体文件.ttf的位置
     * @return
     */
    public static Typeface initTypeface(Context context,String ttfPath) {
        if (context == null) return null;
        //得到AssetManager
        AssetManager mgr = context.getAssets();
        //根据路径得到Typeface
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(mgr,ttfPath);
//            tf = Typeface.createFromAsset(mgr, "fonts/fangzhengpangtou.ttf");
        } catch (Exception e) {
        }
        return tf;
    }

    /**
     * 设置应用的显示字体，比较耗时
     * @param context
     * @return
     */
    public static Typeface initTypeface(Context context) {
        if (context == null) return null;
        //得到AssetManager
        AssetManager mgr = context.getAssets();
        //根据路径得到Typeface
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(mgr, "fonts/fangzhengpangtou.ttf");
        } catch (Exception e) {
        }
        return tf;
    }

    /**
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return ContextHelper.getAppRescources().getColor(colorId);
    }

    /**
     * @param colorId
     * @return
     */
    public static Drawable getDrawable(int colorId) {
        return ContextHelper.getAppRescources().getDrawable(colorId);
    }
}
