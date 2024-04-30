package com.sky.wrapper.base.view;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.base.presenter.MvpPresenter;
import com.sky.widget.usage.ToastView;
import com.sky.wrapper.core.NetworkChanged;
import com.sky.wrapper.core.network.ResponseInfo;
import com.sky.wrapper.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.ParameterizedType;
import butterknife.ButterKnife;

/**
 * @author 刘琛慧
 *         date 2015/8/11.
 */
public abstract class WrapperDialogFragment<P extends MvpBasePresenter> extends DialogFragment implements MvpView {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getViewLayout(), container, false);
        ButterKnife.bind(this, contentView);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题栏
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        loadData(savedInstanceState);
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

    protected void showToast(Context context, String msg) {
        ToastView.showToastInCenter(context, msg);
    }


    @Override
    public void beforeSuccess() {
        loadSuccess = true;
        loadFinish = true;
    }

    @Override
    public void onLoading() {
        if (!progressDialog.isShowing()) {
            loadSuccess = false;
            progressDialog.show();
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        loadFinish = true;
    }

    @Subscribe
    public void onEvent(NetworkChanged networkStateEvent) {
        if (!networkStateEvent.hasNetworkConnected) {
            ToastView.showToastInCenter(getActivity(), "网络连接已断开", Toast.LENGTH_LONG);
        } else {
            if (loadFinish && !loadSuccess) {
                //网络已连接数据没有加载成功，重新加载
                loadData(null);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        presenter.detachView();
        presenter = null;
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 普通Toast提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastView.showToastInCenter(getActivity(), msg, Toast.LENGTH_LONG);
    }

    /**
     * 图文样式的Toast提示
     *
     * @param msg
     * @param resId
     */
    protected void showToast(String msg, int resId) {
        ToastView.showVerticalToast(getActivity(), msg, resId);
    }

}
