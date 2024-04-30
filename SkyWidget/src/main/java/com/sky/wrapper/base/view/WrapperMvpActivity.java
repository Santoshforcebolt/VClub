package com.sky.wrapper.base.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.base.presenter.MvpPresenter;
import com.sky.wrapper.core.NetworkChanged;
import com.sky.wrapper.core.network.ResponseInfo;
import com.sky.wrapper.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.ParameterizedType;


/**
 * Auther:  winds
 * Data:    2017/4/11
 * Desc:
 */

public abstract class WrapperMvpActivity<P extends MvpBasePresenter> extends WrapperActivity implements MvpView {
    protected P presenter;
    protected WrapperDialog progressDialog;
    private boolean loadSuccess; //数据是否加载成功
    private boolean loadFinish; //请求是否成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决键盘覆盖输入框问题
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        presenter.attachView(this);
        progressDialog = createLoadingDialog(this);
        EventBus.getDefault().register(this);
        loadData(savedInstanceState, getIntent());
    }

    @Override
    public void beforeSuccess() {
        loadSuccess = true;
        loadFinish = true;
    }

    @Override
    public void onLoading() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                loadSuccess = false;
            }
        }
    }

    public void showLoadingView() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        loadFinish = true;
    }

    /**
     * 网络状态改变事件，由子类重写实现相关的逻辑
     *
     * @param networkStateEvent
     */
    @Subscribe
    public void onEvent(NetworkChanged networkStateEvent) {
        if (!networkStateEvent.hasNetworkConnected) {
            showToast("网络连接已断开!");
        } else {
            if (loadFinish && !loadSuccess) {
                //网络已连接数据没有加载成功，重新加载
                loadData(null, getIntent());
            }
        }
    }

    /**
     * 从intent中获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     * @param intent
     */
    protected abstract void loadData(Bundle savedInstanceState, Intent intent);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopLoading();
        progressDialog = null;
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

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    protected void setInVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.INVISIBLE : View.GONE);
    }

    protected void setGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }
}

