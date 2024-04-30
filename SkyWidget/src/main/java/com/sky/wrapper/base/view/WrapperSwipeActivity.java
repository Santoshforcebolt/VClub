package com.sky.wrapper.base.view;

import android.os.Bundle;
import android.view.View;

import com.sky.widget.cluster.swipe.ISwipeBack;
import com.sky.widget.cluster.swipe.SwipeBackHelper;
import com.sky.widget.cluster.swipe.SwipeBackLayout;
import com.sky.widget.cluster.swipe.SwipeUtils;
import com.sky.wrapper.base.presenter.MvpBasePresenter;


/**
 * Auther:  winds
 * Data:    2017/4/11
 * Desc:    滑动退出专用
 */

public abstract class WrapperSwipeActivity<P extends MvpBasePresenter> extends WrapperMvpActivity<P> implements ISwipeBack {

    private SwipeBackHelper mHelper;

    @Override
    protected void initContentView() {
        super.initContentView();
        mHelper = new SwipeBackHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    /**
     * 设置禁止滑动退出
     * @param enable
     */
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        SwipeUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
