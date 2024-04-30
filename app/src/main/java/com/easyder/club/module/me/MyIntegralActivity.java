package com.easyder.club.module.me;

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
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.MyIntegralAdapter;
import com.easyder.club.module.me.vo.MyIntegralVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/17 11:48
 * Email: xcode126@126.com
 * Desc: 我的积分
 */
public class MyIntegralActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private MyIntegralAdapter integralAdapter;
    private int page, totalPage;
    private String integral;

    public static Intent getIntent(Context context, String integral) {
        return new Intent(context, MyIntegralActivity.class).putExtra("integral", integral);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_my_integral));
        integral = intent.getStringExtra("integral");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        integralAdapter = new MyIntegralAdapter();
        mRecyclerView.setAdapter(integralAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        integralAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getScoreData(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getScoreData(++page);
        } else {
            integralAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SCORE_DETAIL)) {
            processScoreDetail((MyIntegralVo) dataVo);
        }
    }

    /**
     * 处理积分明细数据
     *
     * @param dataVo
     */
    private void processScoreDetail(MyIntegralVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                integralAdapter.getData().clear();
                integralAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                integralAdapter.notifyDataSetChanged();
            } else {
                integralAdapter.setNewData(dataVo.rows);
            }
            if (integralAdapter.getHeaderLayoutCount() > 0) {
                integralAdapter.removeAllHeaderView();
                integralAdapter.notifyDataSetChanged();
            }
            integralAdapter.setHeaderView(getHeaderView());
            mNestedRefreshLayout.refreshFinish();
        } else {
            integralAdapter.addData(dataVo.rows);
            integralAdapter.loadMoreComplete();
        }
    }

    /**
     * 获取积分明细数据
     *
     * @param page
     */
    private void getScoreData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SCORE_DETAIL, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), MyIntegralVo.class);
    }

    /**
     * 获取头部视图
     *
     * @return
     */
    private View getHeaderView() {
        return getHelperView(mRecyclerView, R.layout.header_my_integral, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_integral, integral);
            }
        });
    }

}
