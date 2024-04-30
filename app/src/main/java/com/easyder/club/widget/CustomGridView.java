package com.easyder.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.sky.widget.autolayout.utils.AutoUtils;


/**
 * Author: sky on 2020/11/24 17:39
 * Email: xcode126@126.com
 * Desc:
 */
public class CustomGridView extends GridView {

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AutoUtils.auto(this);
    }

    public CustomGridView(Context context) {
        super(context);
        AutoUtils.auto(this);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        AutoUtils.auto(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
