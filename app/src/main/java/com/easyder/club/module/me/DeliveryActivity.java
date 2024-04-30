package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.DeliveryAdapter;
import com.easyder.club.module.me.vo.LogisticsInfoBean;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 11:52
 * Email: xcode126@126.com
 * Desc: 物流信息
 */
public class DeliveryActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private DeliveryAdapter deliveryAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, DeliveryActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_delivery;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_delivery_msg));
        deliveryAdapter = new DeliveryAdapter(mActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(deliveryAdapter);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        List<LogisticsInfoBean> list = new ArrayList<>();
        list.add(new LogisticsInfoBean());
        list.add(new LogisticsInfoBean());
        list.add(new LogisticsInfoBean());
        list.add(new LogisticsInfoBean());
        list.add(new LogisticsInfoBean());
        list.add(new LogisticsInfoBean());
        deliveryAdapter.setNewData(list);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }
}
