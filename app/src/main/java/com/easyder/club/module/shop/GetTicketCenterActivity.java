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
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.adapter.GetTicketCenterAdapter;
import com.easyder.club.module.shop.vo.GetTicketCenterVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/24 15:22
 * Email: xcode126@126.com
 * Desc: 领券中心
 */
public class GetTicketCenterActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private GetTicketCenterAdapter centerAdapter;
    private int page, totalPage;

    public static Intent getIntent(Context context) {
        return new Intent(context, GetTicketCenterActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_get_ticket_center));

        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getTicketList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getTicketList(++page);
        } else {
            centerAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_GET_TICKET_CENTER)) {
            processTicketList((GetTicketCenterVo) dataVo);
        } else if (url.contains(ApiConfig.API_MEMBER_GET_TICKET)) {
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PERSON_CHANGE));
            onRefresh();
        }
    }

    /**
     * 确认领取优惠券
     *
     * @param couponcode
     */
    private void confirmGetCoupon(String couponcode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_MEMBER_GET_TICKET, new RequestParams()
                .put("couponcode", couponcode)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * 获取领券数据信息
     *
     * @param page
     */
    private void getTicketList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_TICKET_CENTER, new RequestParams()
                .putCid()
//                .put("coupontype", type == COUPON_CASH ? "cash" : "item")
                .put("coupontype", "cash")
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), GetTicketCenterVo.class);

    }

    /**
     * process ticket list
     *
     * @param dataVo
     */
    private void processTicketList(GetTicketCenterVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                centerAdapter.getData().clear();
                centerAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                centerAdapter.notifyDataSetChanged();
            } else {
                centerAdapter.setNewData(dataVo.couponCashs);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            centerAdapter.addData(dataVo.couponCashs);
            centerAdapter.loadMoreComplete();
        }
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        centerAdapter = new GetTicketCenterAdapter();
        mRecyclerView.setAdapter(centerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        centerAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mNestedRefreshLayout.setOnRefreshListener(this);
        centerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GetTicketCenterVo.CouponCashsBean item = centerAdapter.getItem(position);
                confirmGetCoupon(item.couponcode);
            }
        });
    }
}
