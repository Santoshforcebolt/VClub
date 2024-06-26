package com.sky.wrapper.base.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.widget.usage.ToastView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Auther:  winds
 * Data:    2017/5/26
 * Desc:
 */

public abstract class WrapperFragment extends SupportFragment {

    protected View contentView;
    private Unbinder unbinder;
    protected FragmentActivity _mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getViewLayout(), container, false);
        return contentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        initView(savedInstanceState);
        loadData(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    /**
     * 实例化对应layoutId的view同时生成ViewHelper
     *
     * @param group    可为null
     * @param layoutId
     * @param listener
     * @return
     */
    protected View getHelperView(ViewGroup group, int layoutId, OnViewHelper listener) {
        ViewHelper helper = new ViewHelper(getActivity().getLayoutInflater().inflate(layoutId, group == null ? null : group instanceof RecyclerView ? (ViewGroup) group.getParent() : group, false));
        if (listener != null) {
            listener.help(helper);
        }
        return helper.getItemView();
    }

    /**
     * 设置键盘显否
     *
     * @param v
     * @param visible
     */
    protected void setKeyboardVisible(View v, boolean visible) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (visible) {
                imm.showSoftInput(v, 0);
            } else {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    protected void showToast(String msg) {
        ToastView.showToastInCenter(_mActivity, msg);
    }

    protected void showToast(String msg, int resId) {
        ToastView.showToastInCenter(_mActivity, msg, resId);
    }

}

