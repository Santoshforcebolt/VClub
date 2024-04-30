package com.sky.widget.cluster.tabbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: sky on 2021/1/6 15:36
 * Email: xcode126@126.com
 * Desc: 自定义Tab
 */
public abstract class TabItem extends FrameLayout {
    public TabItem(@NonNull Context context) {
        super(context);
    }

    public TabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public abstract void setTabPosition(int position);


    public abstract int getTabPosition();

    /**
     * 设置选中状态
     */
    public abstract void setSelected(boolean selected);

    /**
     * 设置消息数字。注意：数字需要大于0才会显示
     */
    public abstract void setMessage(int num);

    /**
     * 设置是否显示无数字的小圆点
     */
    public abstract void setDot(boolean visible);


    /**
     * 隐藏红点和文字
     */
    public abstract void dismiss();
}
