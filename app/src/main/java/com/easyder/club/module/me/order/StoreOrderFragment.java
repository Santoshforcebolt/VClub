package com.easyder.club.module.me.order;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.StoreOrderAdapter;
import com.easyder.club.module.me.vo.StoreOrderVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/12/3 9:48
 * Email: xcode126@126.com
 * Desc: 门店订单
 */
public class StoreOrderFragment extends WrapperStatusFragment<CommonPresenter> implements
        NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private StoreOrderAdapter storeOrderAdapter;
    private int page, totalPage;

    public static StoreOrderFragment newInstance() {
        StoreOrderFragment fragment = new StoreOrderFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getData(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (totalPage > page) {
            getData(++page);
        } else {
            storeOrderAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_STORE_ORDER_LIST)) {
            processShopOrder((StoreOrderVo) dataVo);
        }
    }

    /**
     * @param page
     */
    private void getData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_STORE_ORDER_LIST, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), StoreOrderVo.class);
    }

    /**
     * 处理数据信息
     *
     * @param dataVo
     */
    private void processShopOrder(StoreOrderVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total == 0) {
                storeOrderAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                storeOrderAdapter.getData().clear();
                storeOrderAdapter.notifyDataSetChanged();
            } else {
                storeOrderAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            storeOrderAdapter.addData(dataVo.rows);
            storeOrderAdapter.loadMoreComplete();
        }
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        storeOrderAdapter = new StoreOrderAdapter();
        mRecyclerView.setAdapter(storeOrderAdapter);
        storeOrderAdapter.setOnLoadMoreListener(this, mRecyclerView);
        storeOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (view.getId()) {
//                    case R.id.btn_right:
//                        startActivity(EvaluateActivity.getIntent(mActivity,storeOrderAdapter.getItem(position).orderno,"0"));
//                        break;
//                    case R.id.btn_left:
//                        break;
//                }
            }
        });
        storeOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreOrderVo.RowsBean item = storeOrderAdapter.getItem(position);
                startActivity(StoreOrderDetailActivity.getIntent(_mActivity, item.orderno));
            }
        });
        mNestedRefreshLayout.setOnRefreshListener(this);
    }
}
