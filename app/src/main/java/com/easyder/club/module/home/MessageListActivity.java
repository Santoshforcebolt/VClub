package com.easyder.club.module.home;

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
import com.easyder.club.module.home.adapter.MessageListAdapter;
import com.easyder.club.module.home.vo.MessageListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;


/**
 * Author: sky on 2020/11/23 11:46
 * Email: xcode126@126.com
 * Desc:
 */
public class MessageListActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private int page, totalPage;
    private MessageListAdapter listAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, MessageListActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_message_center));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        listAdapter = new MessageListAdapter();
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, mRecyclerView);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageListVo.RowsBean item = listAdapter.getItem(position);
                startActivity(MessageDetailActivity.getIntent(mActivity,item.id,item.masterid));
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getMessageList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getMessageList(++page);
        } else {
            listAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_MESSAGE_LIST)) {
            processMessageList((MessageListVo) dataVo);
        }
    }

    /**
     * @param page
     */
    private void getMessageList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_MESSAGE_LIST, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), MessageListVo.class);
    }

    /**
     * process message list
     *
     * @param dataVo
     */
    private void processMessageList(MessageListVo dataVo) {
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
