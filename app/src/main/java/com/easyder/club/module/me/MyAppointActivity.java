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
import com.easyder.club.module.me.adapter.AppointAdapter;
import com.easyder.club.module.me.vo.AppointListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/17 10:52
 * Email: xcode126@126.com
 * Desc: 我的预约
 */
public class MyAppointActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private AppointAdapter appointAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context context) {
        return new Intent(context, MyAppointActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_my_appoint));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        appointAdapter = new AppointAdapter();
        mRecyclerView.setAdapter(appointAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        appointAdapter.setOnLoadMoreListener(this, mRecyclerView);
        appointAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            AppointListVo.RowsBean item = appointAdapter.getItem(position);
            cancelAppoint(item.orderno);
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getAppointList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getAppointList(++page);
        } else {
            appointAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_APPOINT_LIST)) {
            processAppointList((AppointListVo) dataVo);
        } else if (url.contains(ApiConfig.API_CANCEL_APPOINT)) {
            onRefresh();
        }
    }

    /**
     * get appoint list
     *
     * @param page
     */
    private void getAppointList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_APPOINT_LIST, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), AppointListVo.class);
    }

    /**
     * @param orderno
     */
    private void cancelAppoint(String orderno) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CANCEL_APPOINT, new RequestParams()
                .putCid()
                .put("orderno", orderno)
                .get(), BaseVo.class);
    }

    /**
     * @param dataVo
     */
    private void processAppointList(AppointListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                appointAdapter.getData().clear();
                appointAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                appointAdapter.notifyDataSetChanged();
            } else {
                appointAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            appointAdapter.addData(dataVo.rows);
            appointAdapter.loadMoreComplete();
        }
    }

}
