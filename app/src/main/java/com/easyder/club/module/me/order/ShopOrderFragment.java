package com.easyder.club.module.me.order;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.DeliveryActivity;
import com.easyder.club.module.me.adapter.ShopOrderAdapter;
import com.easyder.club.module.me.vo.ShopOrderVo;
import com.easyder.club.module.me.vo.TempPayBean;
import com.easyder.club.module.shop.PayActivity;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Author：sky on 2019/6/3 16:32.
 * Email：xcode126@126.com
 * Desc：商城订单列表
 */

public class ShopOrderFragment extends WrapperMvpFragment<CommonPresenter> implements
        NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    //订单状态 1：待支付,2：待发货，3：待收货，4：待评价，5：已完成，6：已取消  8: 支付后又取消订单了。9：产品已退款
    public static final int ORDER_STATE_ALL = -1;//全部类型
    public static final int ORDER_STATE_UNPAID = 1;//待支付
    public static final int ORDER_STATE_WAIT_SEND = 2;//待发货
    public static final int ORDER_STATE_WAIT_RECEIVE = 3;//待收货
    public static final int ORDER_STATE_WAIT_EVALUATE = 4;//待评价
    public static final int ORDER_STATE_YET_COMPLETE = 5;//已完成
    public static final int ORDER_STATE_YET_CANCEL = 6;//已取消
    public static final int ORDER_STATE_PAY_CANCEL = 8;//支付后又取消订单了
    public static final int ORDER_STATE_YET_EXIT = 9;//全部产品已退款
    public static final int ORDER_STATE_YET_HALF_EXIT = 10;//部分产品已退款

    private ShopOrderAdapter shopOrderAdapter;
    private int page, totalPage, orderstate;

    public static ShopOrderFragment newInstance(int orderstate) {
        ShopOrderFragment fragment = new ShopOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderstate", orderstate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        orderstate = getArguments().getInt("orderstate");
        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        mNestedRefreshLayout.froceRefreshToState(true);
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
            shopOrderAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_SHOP_ORDER_LIST)) {
            processShopOrder((ShopOrderVo) dataVo);
        } else if (url.contains(ApiConfig.API_CANCEL_ORDER)) {
            showToast(getString(R.string.a_operate_success));
            onRefresh();
        } else if (url.contains(ApiConfig.API_CONFIRM_ORDER)) {
            showToast(getString(R.string.a_operate_success));
            onRefresh();
        }
//        else if (url.contains(ApiConfig.API_CONFIRM_RECEIVED)) {
//            showToast("确认收货成功");
//            alertDialog.dismiss();
//            mNestedRefreshLayout.froceRefreshToState(true);
//        }else if (url.contains(ApiConfig.API_GET_PICK_UP_CODE)) {
//            showPickUpCodeDialog((PickUpCodeVo) dataVo);
//        }
    }

    /**
     * 状态 1：待支付,2：待发货，3：待收货，4：待评价;
     *
     * @param page
     */
    private void getData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SHOP_ORDER_LIST, new RequestParams()
                .putWithoutEmpty("orderstate", orderstate)
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), ShopOrderVo.class);
    }

    /**
     * 处理数据信息
     *
     * @param dataVo
     */
    private void processShopOrder(ShopOrderVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total == 0) {
                shopOrderAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                shopOrderAdapter.getData().clear();
                shopOrderAdapter.notifyDataSetChanged();
            } else {
                shopOrderAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            shopOrderAdapter.addData(dataVo.rows);
            shopOrderAdapter.loadMoreComplete();
        }
    }

    @Subscribe
    public void orderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_ORDER_PAY_SUCCESS://订单支付成功
            case OrderIml.SIGN_AFTER_SALE_STATE_CHANGE://售后订单更改
                onRefresh();
                break;
        }
    }

    /**
     * 取消订单
     *
     * @param orderNO
     */
    private void cancelOrder(String orderNO) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CANCEL_ORDER, new RequestParams()
                .putCid()
                .put("orderno", orderNO)
                .get(), BaseVo.class);
    }

    /**
     * 确认收货
     *
     * @param orderNO
     */
    private void confirmReceived(String orderNO) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CONFIRM_ORDER, new RequestParams()
                .putCid()
                .put("orderno", orderNO)
                .get(), BaseVo.class);
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        shopOrderAdapter = new ShopOrderAdapter();
        mRecyclerView.setAdapter(shopOrderAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        shopOrderAdapter.setOnLoadMoreListener(this, mRecyclerView);
        shopOrderAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopOrderVo.RowsBean item = shopOrderAdapter.getItem(position);
            startActivity(ShopOrderDetailActivity.getIntent(_mActivity, item.orderno));
        });
        shopOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopOrderVo.RowsBean item = shopOrderAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        showCancelDialog(item.orderno, true);
                        break;
                    case R.id.btn_pay:
                        TempPayBean payBean = new TempPayBean(TempPayBean.TEMP_PAY_SHOP_ORDER, item.orderno, item.actualmoney);
                        startActivity(PayActivity.getIntent(_mActivity, payBean));
                        break;
                    case R.id.btn_look:
                        startActivity(DeliveryActivity.getIntent(_mActivity));
                        break;
                    case R.id.btn_confirm:
                        showCancelDialog(item.orderno, false);
                        break;
                    case R.id.btn_apply:
                        startActivity(ShopOrderDetailActivity.getIntent(_mActivity, item.orderno));
//                        startActivity(ApplyRefundActivity.getIntent(_mActivity, item));
                        break;
                }
            }
        });
    }

    /**
     * cancel order dialog
     *
     * @param orderno
     * @param isCancel
     */
    private void showCancelDialog(String orderno, boolean isCancel) {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_confirm;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 586, 486, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_content, getString(isCancel ? R.string.a_cancel_order_tip : R.string.a_receive_order_tip));
                helper.setOnClickListener(v -> {
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            if (isCancel) {
                                cancelOrder(orderno);
                            } else {
                                confirmReceived(orderno);
                            }
                            break;
                    }
                    dismiss();
                }, R.id.btn_confirm, R.id.btn_cancel);
            }
        }.show();
    }
}
