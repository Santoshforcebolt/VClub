package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/20 10:29
 * Email: xcode126@126.com
 * Desc: 限时抢购
 */
public class FlashSaleActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    public static Intent getIntent(Context context) {
        return new Intent(context, FlashSaleActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_flash_sale));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

}
