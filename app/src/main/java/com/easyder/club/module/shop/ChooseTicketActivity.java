package com.easyder.club.module.shop;

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
import com.easyder.club.module.me.MyTicketFragment;
import com.easyder.club.module.me.vo.MyTicketVo;
import com.easyder.club.module.shop.adapter.ChooseTicketAdapter;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/20 13:58
 * Email: xcode126@126.com
 * Desc: 选择优惠券
 */
public class ChooseTicketActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private ChooseTicketAdapter ticketAdapter;
    private int page, totalPage;
    private String selectedInstanceCode;

    public static Intent getIntent(Context context, String selectedInstanceCode) {
        return new Intent(context, ChooseTicketActivity.class).putExtra("selectedInstanceCode", selectedInstanceCode);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_choose_ticket));

        selectedInstanceCode = intent.getStringExtra("selectedInstanceCode");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ticketAdapter = new ChooseTicketAdapter(selectedInstanceCode);
        mRecyclerView.setAdapter(ticketAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        ticketAdapter.setOnLoadMoreListener(this, mRecyclerView);
        ticketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyTicketVo.RowsBean item = ticketAdapter.getItem(position);
                ticketAdapter.setSelected(item.instancecode);
                Intent intent1 = new Intent();
                intent1.putExtra("instancecode", item.instancecode);
                intent1.putExtra("showname", item.showname);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getTicketData(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getTicketData(++page);
        } else {
            ticketAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_MY_TICKET)) {
            processMyTicket((MyTicketVo) dataVo);
        }
    }

    /**
     * 获取优惠券数据
     *
     * @param page
     */
    private void getTicketData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_MY_TICKET, new RequestParams()
                .put("state", MyTicketFragment.TYPE_NOT_USE)
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), MyTicketVo.class);
    }

    /**
     * @param dataVo
     */
    private void processMyTicket(MyTicketVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                ticketAdapter.getData().clear();
                ticketAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                ticketAdapter.notifyDataSetChanged();
            } else {
                ticketAdapter.setNewData(dataVo.rows);
            }
        } else {
            ticketAdapter.addData(dataVo.rows);
            ticketAdapter.loadMoreComplete();
        }
    }
}
