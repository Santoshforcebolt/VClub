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
import com.easyder.club.module.me.adapter.YetEarningsAdapter;
import com.easyder.club.module.me.vo.YetEarningsVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/12/19 15:18
 * Email: xcode126@126.com
 * Desc: 已提取收益明细
 */
public class YetEarningsActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private int page, totalPage;
    private YetEarningsAdapter earningsAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, YetEarningsActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_extract_earnings_detail));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        earningsAdapter = new YetEarningsAdapter();
        mRecyclerView.setAdapter(earningsAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        earningsAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getDetail(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getDetail(++page);
        } else {
            earningsAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_YET_EARNINGS_DETAIL)) {
            processDetail((YetEarningsVo) dataVo);
        }
    }

    /**
     * @param dataVo
     */
    private void processDetail(YetEarningsVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                earningsAdapter.getData().clear();
                earningsAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                earningsAdapter.notifyDataSetChanged();
            } else {
                earningsAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            earningsAdapter.addData(dataVo.rows);
            earningsAdapter.loadMoreComplete();
        }
    }

    /**
     * get detail
     *
     * @param page
     */
    private void getDetail(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_YET_EARNINGS_DETAIL, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), YetEarningsVo.class);
    }

}
