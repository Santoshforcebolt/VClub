package com.easyder.club.module.shop;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.adapter.GetTicketAdapter;
import com.easyder.club.module.shop.vo.GetTicketVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperDialogFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/3 16:50
 * Email: xcode126@126.com
 * Desc:
 */
public class GetTicketFragment extends WrapperDialogFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private int page, totalPage;
    private GetTicketAdapter ticketAdapter;

    public static GetTicketFragment newInstance() {
        GetTicketFragment fragment = new GetTicketFragment();
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.dialog_get_ticket;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNestedRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ticketAdapter = new GetTicketAdapter();
        mRecyclerView.setAdapter(ticketAdapter);
        ticketAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            GetTicketVo.RowsBean item = ticketAdapter.getItem(position);
            confirmGetCoupon(item.couponcode);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setWindowAnimations(R.style.TransDialogAnim);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(707));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getTicket(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getTicket(++page);
        } else {
            ticketAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_CAN_GET_TICKET)) {
            processTicket((GetTicketVo) dataVo);
        } else if (url.contains(ApiConfig.API_MEMBER_GET_TICKET)) {
            processGetTicket();
        }
    }

    /**
     * get ticket
     */
    private void getTicket(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_CAN_GET_TICKET, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), GetTicketVo.class);
    }

    /**
     * 确认领取优惠券
     *
     * @param couponcode
     */
    private void confirmGetCoupon(int couponcode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_MEMBER_GET_TICKET, new RequestParams()
                .put("couponcode", couponcode)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * process get ticket
     */
    private void processGetTicket() {
        showToast(getString(R.string.a_operate_success));
        EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PERSON_CHANGE));
        onRefresh();
    }

    /**
     * @param dataVo
     */
    private void processTicket(GetTicketVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                ticketAdapter.getData().clear();
                ticketAdapter.setEmptyView(getEmptyView());
                ticketAdapter.notifyDataSetChanged();
            } else {
                ticketAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            ticketAdapter.addData(dataVo.rows);
            ticketAdapter.loadMoreComplete();
        }
    }

    /**
     * get empty view
     *
     * @return
     */
    private View getEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.common_empty, null);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        dismissAllowingStateLoss();
    }
}
