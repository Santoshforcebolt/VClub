package com.easyder.club.module.enjoy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.adapter.LocationAdapter;
import com.easyder.club.utils.OperateUtils;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/16 18:04
 * Email: xcode126@126.com
 * Desc:
 */
public class LocationActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private LocationAdapter locationAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_current_location));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        locationAdapter = new LocationAdapter();
        mRecyclerView.setAdapter(locationAdapter);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        locationAdapter.setNewData(OperateUtils.getTestList());
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

}
