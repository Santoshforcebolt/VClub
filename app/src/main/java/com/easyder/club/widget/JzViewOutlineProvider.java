package com.easyder.club.widget;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

/**
 * Author：sky on 2018/11/6 0006 17:49.
 * Email：xcode126@126.com
 * Desc：
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JzViewOutlineProvider extends ViewOutlineProvider {


    private float mRadius;

    public JzViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }

}
