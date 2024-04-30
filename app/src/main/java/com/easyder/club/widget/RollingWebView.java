package com.easyder.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Author: sky on 2020/6/17 18:31.
 * Email:xcode126@126.com
 * Desc:Rolling WebView
 */

public class RollingWebView extends WebView {

    ScrollInterface web;

    public RollingWebView(Context context) {
        super(context);
    }

    public RollingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollingWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        web.onSChanged(l, t, oldl, oldt);
    }

    public void setOnCustomScrollChangeListener(ScrollInterface t) {
        this.web = t;
    }

    /**
     * 定义滑动接口
     *
     * @param
     */
    public interface ScrollInterface {
        void onSChanged(int l, int t, int oldl, int oldt);
    }

    //调用此方法可滚动到底部
    public void scrollToBottom() {
        scrollTo(0, computeVerticalScrollRange());
    }
}
