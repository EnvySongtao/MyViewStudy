package com.gst.myviewstudy.sysViewTest;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.gst.myviewstudy.R;
import com.gst.myviewstudy.thridView.tbsWebView.X5WebViewTest;
import com.gst.myviewstudy.ui.bean.ViewInfo;
import com.gst.myviewstudy.utils.LogUtil;

import java.util.HashMap;

/**
 * author: GuoSongtao on 2018/1/17 15:59
 * email: 157010607@qq.com
 */

public class ActSysViewTest extends Activity {
    public static final String TAG_MOTHED_NAME = "mothedName";

    public static final String[] TESTMETHODNAME =
            {"RadioButton", "PercentLayout", "DialogShow", "DialogShow", "ViewStub", "WebView", "LauncherBadgeNumber",
                    "testLog","WuziPanel", "SurfaceViewTwoThread"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mothedName = getIntent().getStringExtra(TAG_MOTHED_NAME);
        int index = 0;//给个默认值
        for (int i = 0; i < TESTMETHODNAME.length; i++) {
            if (TESTMETHODNAME[i].equals(mothedName)) {
                index = i;
                break;
            }
        }

        switch (index) {
            case 0:
                testRadioButton();
                break;
            case 1:
                testPercentLayout();
                break;
            case 2:
                testDialogShow();
                break;
            case 3:
                testViewStub();
                break;
            case 4:
                testWebView();
                break;
            case 5:
                testWebView();
                break;
            case 6:
                testLauncherBadgeNumber();
                break;
            case 7:
                testLog();
                break;
            case 8:
                testWuziPanel();
                break;
            case 9:
                testSurfaceViewTwoThread();
                break;
        }
    }

    /**
     *
     */
    private void testPercentLayout() {
        setContentView(R.layout.act_percent_linearlayout_test);
    }

    /**
     * radioButton的使用,
     * sys_view_radiobutton,修改了radiobutton的默认显示方式
     */
    private void testRadioButton() {
        setContentView(R.layout.sys_view_radiobutton);
    }

    /**
     *
     */
    private void testDialogShow() {

        //测试铺满 屏幕的dialog
        DialogTest.showFullScreenWidthDialog(this);
    }


    /**
     *
     */
    private void testViewStub() {
        ViewStubTest.testViewStub(this);
    }


    private void testWebView() {
//        new WebViewTest().testSysWebViewJS(this);

        new X5WebViewTest().testX5WebViewJS(this);
    }


    /**
     * 测试桌面图标加上小圆点
     */
    private void testLauncherBadgeNumber() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "张三");
        map.put("old", "21");
        map.put("class", "三年二班");
        //JSON.toJSONString(JSON.toJSONString(map))
        //json={"name":"张三","class":"三年二班","old":"21"}
        //LogUtil.i("json=" +  JSON.toJSONString(map));
        //json="{\"name\":\"张三\",\"class\":\"三年二班\",\"old\":\"21\"}"
        LogUtil.i("json=" + JSON.toJSONString(JSON.toJSONString(map)));
        new LauncherBadgeNumberTest().testBadgeNumber(this);
    }


    private void testLog() {

        setContentView(R.layout.sys_view_radiobutton);
        // publicvoidsetStreamVolume(intstreamType,intindex,intflags)其中streamType有内置的常量，去文档里面就可以看到。
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //通话音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        LogUtil.i("VIOCE_CALL+max : " + max + " current : " + current);

        //系统音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        LogUtil.i("SYSTEM  max : " + max + "current: " + current);

        //铃声音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        LogUtil.i("RING max : " + max + "current: " + current);

        //音乐音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtil.i("MUSIC max :" + max + "current: " + current);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max + 3, AudioManager.FLAG_PLAY_SOUND);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//设置完变max
        LogUtil.i("MUSIC max :" + max + "current: " + current);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 12, AudioManager.FLAG_PLAY_SOUND);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtil.i("MUSIC max :" + max + "current: " + current);


        //提示声音音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        LogUtil.i("ALARM max :" + max + "current: " + current);
    }

    private void testWuziPanel() {
        setContentView(R.layout.game_view_wuzi_panel);
    }


    private void testSurfaceViewTwoThread() {
        new SurfaceViewTest().testTwoSubThread(this);
    }

}
