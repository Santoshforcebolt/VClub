package com.easyder.club.widget;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.easyder.club.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Author:  winds
 * Data:    2018/2/3
 * Version: 1.0
 * Desc:    点击全屏 直接进入横向全屏
 */


public class FullJVideoPlayer extends JZVideoPlayerStandard {
    public static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    private LinkedHashMap map;

    public FullJVideoPlayer(Context context) {
        super(context);
    }

    public FullJVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void startWindowFullscreen() {
        Log.i(TAG, "startWindowFullscreen " + " [" + this.hashCode() + "] ");
        hideSupportActionBar(getContext());
        JZUtils.getAppCompActivity(getContext()).setRequestedOrientation(FULLSCREEN_ORIENTATION);

        ViewGroup vp = (ViewGroup) (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(FULLSCREEN_ID);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            Constructor<FullJVideoPlayer> constructor = (Constructor<FullJVideoPlayer>) FullJVideoPlayer.this.getClass().getConstructor(Context.class);
            FullJVideoPlayer jzVideoPlayer = constructor.newInstance(getContext());
            jzVideoPlayer.setId(FULLSCREEN_ID);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzVideoPlayer, lp);
            jzVideoPlayer.setUp(getParentFieldValue(this, "urlMap"), 0, JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, objects);
//            jzVideoPlayer.setUp(map, 0, JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, objects);
            jzVideoPlayer.setState(currentState);
            jzVideoPlayer.addTextureView();
            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);
            onStateNormal();
            jzVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fullscreen:
                if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                    //quit fullscreen
                    backPress();
                } else {
                    Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                    onEvent(JZUserAction.ON_ENTER_FULLSCREEN);
                    startWindowFullscreen();
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    /**
     * 反射获取父类的属性
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public LinkedHashMap getParentFieldValue(Object obj, String fieldName) {
        Class<?> cls = obj.getClass();
        for (; cls != Object.class; cls = cls.getSuperclass()) {
            try {
                Field field = cls.getDeclaredField(fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    Object o = field.get(obj);
                    if (o instanceof LinkedHashMap) {
                        return (LinkedHashMap) o;
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }


    public static void startFullscreen(Context context, Class _class, LinkedHashMap urlMap, int defaultUrlMapIndex, Object... objects) {
        hideSupportActionBar(context);
        JZUtils.getAppCompActivity(context).setRequestedOrientation(FULLSCREEN_ORIENTATION); //设置横屏
        ViewGroup vp = (ViewGroup) (JZUtils.scanForActivity(context))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(JZVideoPlayer.FULLSCREEN_ID);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            Constructor<JZVideoPlayer> constructor = _class.getConstructor(Context.class);
            final JZVideoPlayer jzVideoPlayer = constructor.newInstance(context);
            jzVideoPlayer.setId(JZVideoPlayer.FULLSCREEN_ID);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzVideoPlayer, lp);
            jzVideoPlayer.setUp(urlMap, defaultUrlMapIndex, JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, objects);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            jzVideoPlayer.startButton.performClick();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUp(String url, int screen, Object... objects) {
        map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        setUp(map, 0, screen, objects);
    }
}
