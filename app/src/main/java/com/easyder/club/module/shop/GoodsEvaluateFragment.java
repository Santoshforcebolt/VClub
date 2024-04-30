package com.easyder.club.module.shop;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CustomerPresenter;
import com.easyder.club.module.enjoy.EnjoyDetailActivity;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.module.shop.adapter.EvaluateListAdapter;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/25 18:26
 * Email: xcode126@126.com
 * Desc: 商品详情-评论页面
 */
public class GoodsEvaluateFragment extends WrapperStatusFragment<CustomerPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private int page, totalPage;
    private EvaluateListAdapter listAdapter;
    private String productcode;

    public static GoodsEvaluateFragment newInstance(String productcode) {
        GoodsEvaluateFragment fragment = new GoodsEvaluateFragment();
        Bundle args = new Bundle();
        args.putString("productcode", productcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_evaluate;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        productcode = getArguments().getString("productcode");
        listAdapter = new EvaluateListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, mRecyclerView);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            EnjoyListVo.RowsBean item = listAdapter.getItem(position);
            startActivity(EnjoyDetailActivity.getIntent(_mActivity, item.id));
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getEnjoyList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getEnjoyList(++page);
        } else {
            listAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ENJOY_LIST)) {
            processEnjoyList((EnjoyListVo) dataVo);
        }
    }

    /**
     * get enjoy list
     *
     * @param page
     */
    private void getEnjoyList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ENJOY_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .put("productcode", productcode)
                .get(), EnjoyListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processEnjoyList(EnjoyListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                listAdapter.getData().clear();
                listAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                listAdapter.notifyDataSetChanged();
            } else {
                listAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            listAdapter.addData(dataVo.rows);
            listAdapter.loadMoreComplete();
        }
    }
}
