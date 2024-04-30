package com.easyder.club.module.enjoy;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.adapter.EnjoyAdapter;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/13 14:49
 * Email: xcode126@126.com
 * Desc: 评鉴
 */
public class EnjoyChildFragment extends WrapperStatusFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private EnjoyAdapter enjoyAdapter;
    private int page, totalPage;

    private String type;

    public static EnjoyChildFragment newInstance(String type) {
        EnjoyChildFragment fragment = new EnjoyChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        type = getArguments().getString("type");
        initAdapter();
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_ENJOY_CHANGE:
            case AccountIml.ACCOUNT_PUBLISH_ENJOY:
                onRefresh();
                break;
        }
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
            enjoyAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ENJOY_LIST)) {
            processEnjoyList((EnjoyListVo) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_WISH)) {
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_ENJOY_CHANGE));
            showToast(getString(R.string.a_operate_success));
            onRefresh();
        }
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    /**
     * @param evalid
     * @param optionvalue
     */
    private void changeCollect(int evalid, String type, int optionvalue) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_WISH, new RequestParams()
                .putCid()
                .put("evalid", evalid)
                .put("optiontype", type)
                .put("optionvalue", optionvalue)
                .get(), BaseVo.class);
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
                .putWithoutEmpty("optiontype", type)
                .get(), EnjoyListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processEnjoyList(EnjoyListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                enjoyAdapter.getData().clear();
                enjoyAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                enjoyAdapter.notifyDataSetChanged();
            } else {
                enjoyAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            enjoyAdapter.addData(dataVo.rows);
            enjoyAdapter.loadMoreComplete();
        }
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        enjoyAdapter = new EnjoyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setAdapter(enjoyAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        enjoyAdapter.setOnLoadMoreListener(this, mRecyclerView);
        enjoyAdapter.setOnItemClickListener((adapter, view, position) -> {
            EnjoyListVo.RowsBean item = enjoyAdapter.getItem(position);
            startActivityForResult(EnjoyDetailActivity.getIntent(_mActivity, item.id), 0x01);
        });
        enjoyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            EnjoyListVo.RowsBean item = enjoyAdapter.getItem(position);
            switch (view.getId()) {
                case R.id.ll_collect:
                    int c = item.collection == 0 ? 1 : 0;
                    changeCollect(item.id, "collection", c);
                    break;
                case R.id.ll_wish:
                    int w = item.wish == 0 ? 1 : 0;
                    changeCollect(item.id, "wish", w);
                    break;
            }
        });
    }
}
