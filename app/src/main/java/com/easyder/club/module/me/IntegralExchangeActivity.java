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
import com.easyder.club.module.me.adapter.IntegralExchangeAdapter;
import com.easyder.club.module.me.vo.IntegralExchangeVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author：sky on 2019/6/5 09:59.
 * Email：xcode126@126.com
 * Desc：积分兑换记录
 */

public class IntegralExchangeActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private IntegralExchangeAdapter historyAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, IntegralExchangeActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_integral_exchange_history));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        historyAdapter = new IntegralExchangeAdapter();
        mRecyclerView.setAdapter(historyAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        historyAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getHistoryData(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getHistoryData(++page);
        } else {
            historyAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SCORE_EXCHANGE_HISTORY)) {
            processHistoryData((IntegralExchangeVo) dataVo);
        }
    }

    /**
     * 处理积分兑换记录
     *
     * @param dataVo
     */
    private void processHistoryData(IntegralExchangeVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                historyAdapter.getData().clear();
                historyAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                historyAdapter.notifyDataSetChanged();
            }
            historyAdapter.setNewData(dataVo.rows);
            mNestedRefreshLayout.refreshFinish();

        } else {
            historyAdapter.addData(dataVo.rows);
            historyAdapter.loadMoreComplete();
        }
    }

    /**
     * 获取积分兑换记录数据
     *
     * @param page
     */
    private void getHistoryData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SCORE_EXCHANGE_HISTORY, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), IntegralExchangeVo.class);
    }
}
