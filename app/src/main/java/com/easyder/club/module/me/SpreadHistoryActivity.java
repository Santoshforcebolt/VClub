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
import com.easyder.club.module.me.adapter.SpreadHistoryAdapter;
import com.easyder.club.module.me.vo.SpreadHistoryVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 17:54
 * Email: xcode126@126.com
 * Desc: 推广会员记录
 */
public class SpreadHistoryActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private SpreadHistoryAdapter historyAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context context) {
        return new Intent(context, SpreadHistoryActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_spread_member_history));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        historyAdapter = new SpreadHistoryAdapter();
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
        getHistoryList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getHistoryList(++page);
        } else {
            historyAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SPREAD_LIST)) {
            processHistoryList((SpreadHistoryVo) dataVo);
        }
    }

    /**
     * get history list
     *
     * @param page
     */
    private void getHistoryList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SPREAD_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), SpreadHistoryVo.class);
    }

    /**
     * process ticket list
     *
     * @param dataVo
     */
    private void processHistoryList(SpreadHistoryVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                historyAdapter.getData().clear();
                historyAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                historyAdapter.notifyDataSetChanged();
            } else {
                historyAdapter.setNewData(dataVo.list);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            historyAdapter.addData(dataVo.list);
            historyAdapter.loadMoreComplete();
        }
    }

}
