package com.easyder.club.module.shop;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.adapter.LimitBuyAdapter;
import com.easyder.club.module.shop.vo.LimitBuyVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/12/14 10:59
 * Email: xcode126@126.com
 * Desc:
 */
public class LimitBuyFragment extends WrapperStatusFragment<CommonPresenter> implements
        NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private LimitBuyAdapter buyAdapter;
    private int page, totalPage, activitycode;

    public static LimitBuyFragment newInstance(int activitycode) {
        LimitBuyFragment fragment = new LimitBuyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("activitycode", activitycode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    public int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        activitycode = getArguments().getInt("activitycode");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        buyAdapter = new LimitBuyAdapter();
        mRecyclerView.setAdapter(buyAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        buyAdapter.setOnLoadMoreListener(this, mRecyclerView);
        buyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LimitBuyVo.DetailedListBean item = buyAdapter.getItem(position);
                startActivity(GoodsDetailActivity.getIntent(_mActivity, item.itemcode));
            }
        });
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
            buyAdapter.loadMoreEnd();
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_LIMIT_BUY)) {
            processList((LimitBuyVo) dataVo);
        }
    }

    /**
     * get list
     *
     * @param page
     */
    private void getList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_LIMIT_BUY, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putWithoutEmpty("activitycode", activitycode)
                .get(), LimitBuyVo.class);
    }

    /**
     * process list
     *
     * @param dataVo
     */
    private void processList(LimitBuyVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.totalGoodsNum, AppConfig.PAGE_SIZE);
            if (dataVo.totalGoodsNum <= 0) {
                buyAdapter.getData().clear();
                buyAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                buyAdapter.notifyDataSetChanged();
            } else {
                buyAdapter.setNewData(dataVo.detailedList);
            }
            if (buyAdapter.getHeaderLayoutCount() > 0) {
                buyAdapter.removeAllHeaderView();
                buyAdapter.notifyDataSetChanged();
            }
            buyAdapter.setHeaderView(getHeaderView(dataVo.timeLimitActivityThemeTemplate));
            mNestedRefreshLayout.refreshFinish();
        } else {
            buyAdapter.addData(dataVo.detailedList);
            buyAdapter.loadMoreComplete();
        }
    }

    /**
     * get header view
     *
     * @param item
     * @return
     */
    private View getHeaderView(LimitBuyVo.TimeLimitActivityThemeTemplateBean item) {
        return getHelperView(mRecyclerView, R.layout.header_limit_buy, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                helper.setVisible(R.id.iv_image, item.templatecode == 4);
                if (item != null) {
                    helper.setImageManager(_mActivity, R.id.iv_image, item.bgurl, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2);
                }
            }
        });
    }
}
