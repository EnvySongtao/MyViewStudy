package com.gst.myviewstudy.sysViewTest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.gst.myviewstudy.R;
import com.gst.myviewstudy.utils.PhoneManufacturer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * author: GuoSongtao on 2018/2/6 16:00
 * email: 157010607@qq.com
 */

public class LauncherBadgeNumberTest {

    private static final LauncherBadgeNumberTest.Impl IMPL;

    public void testBadgeNumber(Activity mAct) {
        mAct.setContentView(R.layout.sys_view_radiobutton);
        IMPL.setBadgeNumber(mAct, 8);
    }

    public static void setHuaWeiBadgeNumber(Context context, int number) {
        try {
            if (number < 0) number = 0;
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOppoBadgeNumber(Context context, int number) {
        try {
            if (number == 0) {
                number = -1;
            }
            Intent intent = new Intent("com.oppo.unsettledevent");
            intent.putExtra("pakeageName", context.getPackageName());
            intent.putExtra("number", number);
            intent.putExtra("upgradeNumber", number);
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            } else {
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", number);
                    context.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", null, extras);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }


    public static void setVivoBadgeNumber(Context context, int number) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
            intent.putExtra("className", launchClassName);
            intent.putExtra("notificationNum", number);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //在调用NotificationManager.notify(notifyID, notification)这个方法之前先设置角标显示的数目
    public static void setMiuiBadgeNumber(Notification notification, int number) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    interface Impl {
        void setBadgeNumber(Context context, int number);
    }

    static class ImplHuaWei implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            setHuaWeiBadgeNumber(context, number);
        }
    }

    static class ImplVIVO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            setVivoBadgeNumber(context, number);
        }
    }

    static class ImplOppo implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            setOppoBadgeNumber(context, number);
        }
    }

    static class ImplMiui implements Impl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!");
            setMiuiBadgeNumber(builder.mNotification, number);
            int notifyId = 0x005;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(notifyId, builder.build());
        }
    }

    static class ImplSamsung implements Impl {
        @Override
        public void setBadgeNumber(Context context, int number) {

        }
    }


    static class ImplBase implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            //do nothing
        }
    }


    static {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase(PhoneManufacturer.MANUFACTURER_HUAWEI)) {
            IMPL = new ImplHuaWei();
        } else if (manufacturer.equalsIgnoreCase(PhoneManufacturer.MANUFACTURER_VIVO)) {
            IMPL = new ImplVIVO();
        } else if (manufacturer.equalsIgnoreCase(PhoneManufacturer.MANUFACTURER_OPPO)) {
            //其他品牌机型的实现类
            IMPL = new ImplOppo();
        } else if (manufacturer.equalsIgnoreCase(PhoneManufacturer.MANUFACTURER_XIAOMI)) {
            //其他品牌机型的实现类
            IMPL = new ImplMiui();
        } else if (manufacturer.equalsIgnoreCase(PhoneManufacturer.MANUFACTURER_SAMSUNG)) {
            //其他品牌机型的实现类
            IMPL = new ImplSamsung();
        } else {
            IMPL = new ImplBase();
        }
    }
}
