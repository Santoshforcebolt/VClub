package com.sky.wrapper.base.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.base.presenter.MvpPresenter;
import com.sky.wrapper.core.NetworkChanged;
import com.sky.wrapper.core.network.ResponseInfo;
import com.sky.wrapper.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.ParameterizedType;

/**
 * 封装了网络层的Fragment
 *
 * @param <P>
 */
public abstract class WrapperMvpFragment<P extends MvpBasePresenter> extends WrapperFragment implements MvpView {
    protected P presenter;
    protected WrapperDialog progressDialog;
    private boolean loadSuccess;
    private boolean loadFinish; //请求是否成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        presenter.attachView(this);
        progressDialog = createLoadingDialog(getActivity());

        EventBus.getDefault().register(this);
    }


    @Override
    public void beforeSuccess() {
        loadSuccess = true;
        loadFinish = true;
    }


    @Override
    public void onLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            loadSuccess = false;
            progressDialog.show();
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        loadFinish = true;
        switch (responseInfo.getState()) {
            case ResponseInfo.TIME_OUT:
                showTimeOutDialog(responseInfo);
                break;
            case ResponseInfo.NO_INTERNET_ERROR:
                showOpenNetworkDialog(responseInfo);
                break;
        }

    }

    @Subscribe
    public void networkStateChanged(NetworkChanged changed) {
        if (!changed.hasNetworkConnected) {
            showToast("网络连接已断开");
        } else {
            if (loadFinish && !loadSuccess) {
                //网络已连接数据没有加载成功，重新加载
                loadData(null);
            }
        }
    }


    @Override
    public void onDestroy() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        super.onDestroy();
        presenter.detachView();
        presenter = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    /**
     * @return 布局resourceId
     */

    public abstract int getViewLayout();

    /**
     * 初始化View。或者其他view级第三方控件的初始化,及相关点击事件的绑定
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     */
    protected abstract void loadData(Bundle savedInstanceState);


    private void showOpenNetworkDialog(ResponseInfo responseInfo) {
        showToast("网络连接不可用，请打开网络！");
    }


    protected void showTimeOutDialog(ResponseInfo responseInfo) {
        showToast("连接网络超时");
    }


    protected void showLoadingDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public View getContentView() {
        return contentView;
    }

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }
    protected void setInVisible(View view, boolean isInVisible) {
        view.setVisibility(isInVisible ? View.INVISIBLE : View.VISIBLE);
    }

    protected void setGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }
}
