package com.sky.wrapper.base.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.ResponseInfo;


/**
 * Auther:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:   状态实现类
 */


public abstract class WrapperStatusFragment<P extends MvpBasePresenter> extends WrapperMvpFragment<P> {
    StatusManagerDelegate delegate;

    /**
     * 为status页面提供父布局 非一般情况 请避免重写此方法
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout parent = new FrameLayout(_mActivity);
        parent.addView(super.onCreateView(inflater, container, savedInstanceState));
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        processOnViewCreated();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showLoadingStatus();
    }


    @Override
    public void showContentView(String url, BaseVo dataVo) {
        processShowContentView();
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        super.onError(responseInfo);
        processOnErrorStatus(responseInfo);
    }

    protected void showLoadingStatus() {
        if (delegate != null) {
            delegate.showLoadingStatus();
        }
    }

    protected void processOnErrorStatus(ResponseInfo responseInfo) {
        if (delegate != null) {
            delegate.onErrorStatus(responseInfo);
        }
    }

    protected void processShowContentView() {
        if (delegate != null) {
            delegate.processStatusOnSuccess();
        }
    }

    protected void processOnViewCreated() {
        onCreateStatus(getContentView());
    }

    protected void onCreateStatus(View view) {
        if (delegate == null) {
            delegate = new StatusManagerDelegate(view) {
                @Override
                public int getRequestCount() {
                    return presenter.getRequestCount();
                }

                @Override
                public void load() {
                    if (isLazyLoad()) {
                        onLazyInitView(null);
                    } else {
                        loadData(null);
                    }
                }
            };
        }
    }

    protected StatusManagerDelegate getStatusDelegate() {
        return delegate;
    }

    protected abstract boolean isLazyLoad();
}
