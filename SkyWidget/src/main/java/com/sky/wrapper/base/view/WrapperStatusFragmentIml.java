package com.sky.wrapper.base.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.sky.widget.R;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.ResponseInfo;
import com.sky.wrapper.status.DefaultStatus;
import com.sky.wrapper.status.OnDefaultStatusListener;
import com.sky.wrapper.status.OnStatusListener;
import com.sky.wrapper.status.StatusManager;
import com.sky.wrapper.status.StatusProvider;


/**
 * @deprecated
 * Auther:  winds
 * Data:    2018/2/3
 * Version: 1.0
 * Desc:    封装Status的包装类    以供初次加载时 提供加载等状态页面
 * 注意如果使用了懒加载，需要覆盖{@link WrapperStatusFragmentIml#isLazyLoad()}方法，默认未使用懒加载
 * 参考{@link WrapperStatusFragment}
 */

public abstract class WrapperStatusFragmentIml<P extends MvpBasePresenter> extends WrapperMvpFragment<P> {
    protected StatusManager statusManager;
    protected boolean isStateUse = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (isStateUse) {
            statusManager = new StatusManager(getContentView());
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (isStateUse) {
            showStatus(DefaultStatus.STATUS_LOADING);
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        processStatusOnSuccess();
    }


    @Override
    public void onError(ResponseInfo responseInfo) {
        super.onError(responseInfo);
        if (isStateUse) {
            onErrorStatus(responseInfo);
        }
    }

    /**
     * 设置是否使用状态布局   此方法 请在initView调用前使用
     * 比如在{@link WrapperMvpFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}调用
     *
     * @param isStateUse
     */
    public void setStateUse(boolean isStateUse) {
        this.isStateUse = isStateUse;
    }

    public boolean isStateUse() {
        return isStateUse;
    }

    /**
     * 展示对应的状态布局
     *
     * @param status
     */
    protected void showStatus(String status) {
        if (statusManager != null) {
            statusManager.show(status);
        }
    }

    /**
     * 展示对应StatusProvider的状态布局
     *
     * @param provider
     * @param listener
     */
    protected void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (statusManager != null) {
            statusManager.show(provider, listener);
        }
    }

    /**
     * 移除当前的状态布局
     */
    public void removeStatus() {
        removeStatus(null);
    }

    /**
     * 移除对应的状态布局
     *
     * @param status
     */
    protected void removeStatus(String status) {
        if (statusManager != null) {
            if (TextUtils.isEmpty(status)) {
                statusManager.removeStatus();
            } else {
                statusManager.removeStatus(status);
            }
        }
    }

    /**
     * 提供默認的處理statusManager處理方法，按界面需求重寫
     */
    protected void processStatusOnSuccess() {
        if (presenter != null && presenter.getRequestCount() <= 0 && statusManager != null) {
            statusManager.removeStatus();
        }
    }

    /**
     * 移除当前的状态页面
     * 先判断当前的状态是否时没有网络的状态，此种情况下不做处理
     * 再判断当前页面网络请求数目  当小于等于0时，再移除掉状态页面
     */
    protected void processStatusOnError(ResponseInfo responseInfo) {
        if (statusManager == null || statusManager.getCurrentStatus().equals(DefaultStatus.STATUS_NO_NETWORK)
                || statusManager.getCurrentStatus().equals(DefaultStatus.STATUS_LOAD_ERROR)) {
            return;
        }
        processStatusOnSuccess();
    }

    /**
     * 正确做法是  先判断当前的状态是否时没有网络，再判断当前页面网络请求数量  若小于等于0时 调用此方法
     *
     * @param responseInfo
     */
    protected void onErrorStatus(ResponseInfo responseInfo) {
        processErrorStatus(responseInfo);
    }

    /**
     * 提供指定连接默认的无网络的处理方法，在非无网络时  执行默认的处理方法
     * 此方法需要考虑当前连接是否需要执行此方式
     *
     * @param responseInfo
     */
    protected void processErrorStatus(ResponseInfo responseInfo) {
        switch (responseInfo.getState()) {
            //网络问题
            case ResponseInfo.NO_INTERNET_ERROR:
            case ResponseInfo.TIME_OUT:
                processNetError(responseInfo);
                break;
            case ResponseInfo.FAILURE: //连接服务器失败
                processFailureError(responseInfo);
                break;
            default:
                processStatusOnError(responseInfo);
                break;
        }
    }

    /**
     * 提供指定连接默认的无网络的处理方法，在非无网络时  执行默认的处理方法
     * 此方法需要考虑当前连接是否需要执行此方式
     *
     * @param responseInfo
     */
    protected void processNetError(ResponseInfo responseInfo) {
        if (statusManager != null && statusManager.getCurrentStatus().equals(DefaultStatus.STATUS_LOADING)) {
            processNetErrorStatusDefault();
        } else {
            //此时表示非从loadData请求的其他接口
            processNetErrorStatus(responseInfo);
        }
    }

    /**
     * 非load方法的错误处理
     * 默认不提供实现
     */
    protected void processNetErrorStatus(ResponseInfo responseInfo) {

    }

    /**
     * 访问请求失败时回调
     */
    protected void processFailureError(ResponseInfo responseInfo) {
        if (statusManager != null && statusManager.getCurrentStatus().equals(DefaultStatus.STATUS_LOADING)) {
            processFailureErrorStatusDefault();
        } else {
            //此时表示非从loadData请求的其他接口 默认不处理，如需处理 请实现此方法

        }
    }

    protected void processFailureErrorStatusDefault() {
        /**
         * 从loadData进入的方法
         * @see #loadData(Bundle)
         */
        statusManager.show(DefaultStatus.STATUS_LOAD_ERROR, new OnDefaultStatusListener() {
            @Override
            public void onStatusViewCreate(String status, View statusView) {
                statusView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusManager.show(DefaultStatus.STATUS_LOADING);
//                        if (isLazyLoad()) {
//                            onLazyInitView(null);
//                        } else {
                            loadData(null);
//                        }
                    }
                });
            }
        });
    }

    /**
     * 处理网络异常问题时的状态显示
     * 此方法的回调仅实现重新初始化
     * 如使用懒加载请重写    {@link #isLazyLoad()}
     */
    protected void processNetErrorStatusDefault() {
        /**
         * 从loadData进入的方法
         * @see #loadData(Bundle)
         */
        statusManager.show(DefaultStatus.STATUS_NO_NETWORK, new OnDefaultStatusListener() {
            @Override
            public void onStatusViewCreate(String status, View statusView) {
                statusView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusManager.show(DefaultStatus.STATUS_LOADING);
//                        if (isLazyLoad()) {
//                            onLazyInitView(null);
//                        } else {
                            loadData(null);
//                        }
                    }
                });
            }
        });
    }

    /**
     * 判断当前是否使用懒加载  默认不使用  若使用请重写此方法
     * 此方法仅用于在处理网络异常时的重新初始化 参考{@link #processNetErrorStatusDefault()}
     * 若其他地方使用请考虑适用性
     *
     * @return
     */
//    protected boolean isLazyLoad() {
//        return false;
//    }

}
