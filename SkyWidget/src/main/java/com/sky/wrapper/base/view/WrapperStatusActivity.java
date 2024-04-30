package com.sky.wrapper.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.ResponseInfo;


/**
 * Auther:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:
 */


public abstract class WrapperStatusActivity<P extends MvpBasePresenter> extends WrapperSwipeActivity<P> {
    public StatusManagerDelegate delegate;     //statusManager会在getViewLayout返回为0或者-1时 未能被初始化

    /**
     * 通过重写此方法 可重写StatusManagerDelegate的部分实现
     *
     * @param view
     */
    @Override
    protected void addContentView(View view) {
        super.addContentView(view);
        delegate = new StatusManagerDelegate(view) {
            @Override
            public int getRequestCount() {
                return presenter == null ? 0 : presenter.getRequestCount();
            }

            @Override
            public void load() {
                loadData(null, getIntent().putExtra("data",true));
            }
        };
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        showLoadingStatus();
    }


    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (dataVo == null) {
            processEmptyStatus();
        } else {
            processShowContentView();
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        super.onError(responseInfo);
        processOnErrorStatus(responseInfo);
    }

    public void showLoadingStatus() {
        if (delegate != null) {
            delegate.showLoadingStatus();
        }
    }

    public void processOnErrorStatus(ResponseInfo responseInfo) {
        if (delegate != null) {
            delegate.onErrorStatus(responseInfo);
        }
    }

    public void processShowContentView() {
        if (delegate != null) {
            delegate.processStatusOnSuccess();
        }
    }

    private void processEmptyStatus() {
        if (delegate != null) {
            delegate.processEmptyStatus();
//            delegate.showEmptyStatus();
        }
    }


    public StatusManagerDelegate getStatusDelegate() {
        return delegate;
    }
}
