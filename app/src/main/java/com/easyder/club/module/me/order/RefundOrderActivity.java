package com.easyder.club.module.me.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.RefundOrderAdapter;
import com.easyder.club.module.me.vo.RefundOrderListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Author: sky on 2020/12/9 16:35
 * Email: xcode126@126.com
 * Desc: 售后订单列表
 */
public class RefundOrderActivity extends WrapperSwipeActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private int page, totalPage;
    private RefundOrderAdapter refundOrderAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, RefundOrderActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_refund_order_list));
        initAdapter();
    }

    @Subscribe
    public void orderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_AFTER_SALE_STATE_CHANGE://售后订单状态发生改变
                onRefresh();
                break;
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getList(++page);
        } else {
            refundOrderAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_REFUND_ORDER_LIST)) {
            processList((RefundOrderListVo) dataVo);
        }
    }

    /**
     * get list
     *
     * @param page
     */
    private void getList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_REFUND_ORDER_LIST, new RequestParams()
                .putCid()
                .putWithoutEmpty("page", page)
                .putWithoutEmpty("rows", AppConfig.PAGE_SIZE)
                .get(), RefundOrderListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processList(RefundOrderListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                refundOrderAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                refundOrderAdapter.getData().clear();
                refundOrderAdapter.notifyDataSetChanged();
            } else {
                refundOrderAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            refundOrderAdapter.addData(dataVo.rows);
            refundOrderAdapter.loadMoreComplete();
        }
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        refundOrderAdapter = new RefundOrderAdapter();
        mRecyclerView.setAdapter(refundOrderAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        refundOrderAdapter.setOnLoadMoreListener(this, mRecyclerView);
        refundOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RefundOrderListVo.RowsBean item = refundOrderAdapter.getItem(position);
                startActivity(RefundDetailActivity.getIntent(mActivity, item.orderno));
            }
        });
    }

}
