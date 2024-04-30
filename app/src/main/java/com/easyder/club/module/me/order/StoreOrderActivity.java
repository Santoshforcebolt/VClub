package com.easyder.club.module.me.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

/**
 * Author: sky on 2020/11/24 11:30
 * Email: xcode126@126.com
 * Desc: 门店订单
 */
public class StoreOrderActivity extends WrapperSwipeActivity<CommonPresenter> {

    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, StoreOrderActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_store_order));
        loadRootFragment(R.id.container, StoreOrderFragment.newInstance(), false, false);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }


}
