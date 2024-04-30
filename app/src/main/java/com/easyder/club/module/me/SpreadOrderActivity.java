package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.SpreadOrderAdapter;
import com.easyder.club.module.me.vo.SpreadOrderVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 18:14
 * Email: xcode126@126.com
 * Desc: 推广订单列表
 */
public class SpreadOrderActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private SpreadOrderAdapter orderAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context context) {
        return new Intent(context, SpreadOrderActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_spread_order_list));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        orderAdapter = new SpreadOrderAdapter();
        mRecyclerView.setAdapter(orderAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        orderAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getOrderList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getOrderList(++page);
        } else {
            orderAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SPREAD_ORDER_LIST)) {
            processHistoryList((SpreadOrderVo) dataVo);
        }
    }

    /**
     * get order list
     *
     * @param page
     */
    private void getOrderList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SPREAD_ORDER_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), SpreadOrderVo.class);
    }

    /**
     * process ticket list
     *
     * @param dataVo
     */
    private void processHistoryList(SpreadOrderVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                orderAdapter.getData().clear();
                orderAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                orderAdapter.notifyDataSetChanged();
            } else {
                orderAdapter.setNewData(dataVo.list);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            orderAdapter.addData(dataVo.list);
            orderAdapter.loadMoreComplete();
        }
    }
}
