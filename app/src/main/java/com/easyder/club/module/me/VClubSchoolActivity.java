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
import com.easyder.club.module.home.NewsDetailActivity;
import com.easyder.club.module.home.adapter.HomeAdapter;
import com.easyder.club.module.home.vo.NewsListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 11:22
 * Email: xcode126@126.com
 * Desc:
 */
public class VClubSchoolActivity extends WrapperSwipeActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private HomeAdapter homeAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context context) {
        return new Intent(context, VClubSchoolActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_vclub_school));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        homeAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(homeAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsListVo.ArticlelistBean item = homeAdapter.getItem(position);
                startActivity(NewsDetailActivity.getIntent(mActivity, item.id + ""));
            }
        });
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
            homeAdapter.loadMoreEnd();
        }
    }


    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_NEWS_LIST)) {
            processNewsList((NewsListVo) dataVo);
        }
    }

    /**
     * get list
     *
     * @param page
     */
    private void getList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_NEWS_LIST, new RequestParams()
                .putCid()
                .put("academy", 1)
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), NewsListVo.class);
    }

    /**
     * process news list
     *
     * @param dataVo
     */
    private void processNewsList(NewsListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                homeAdapter.getData().clear();
                homeAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                homeAdapter.notifyDataSetChanged();
            } else {
                homeAdapter.setNewData(dataVo.articlelist);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            homeAdapter.addData(dataVo.articlelist);
            homeAdapter.loadMoreComplete();
        }
    }

}
