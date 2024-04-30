package com.easyder.club.module.me.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.DeliveryActivity;
import com.easyder.club.module.me.vo.OrderDetailVo;
import com.easyder.club.module.me.vo.TempApplyBean;
import com.easyder.club.module.me.vo.TempPayBean;
import com.easyder.club.module.shop.PayActivity;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/6/17 10:13.
 * Email：xcode126@126.com
 * Desc：商城订单详情
 */

public class ShopOrderDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.layout_goods)
    LinearLayout layoutGoods;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_actual)
    TextView tvActual;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.btn_look)
    Button btnLook;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String orderno;
    private OrderDetailVo detailVo;

    public static Intent getIntent(Context mContext, String orderno) {
        return new Intent(mContext, ShopOrderDetailActivity.class).putExtra("orderno", orderno);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_shop_order_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_order_detail));
        orderno = intent.getStringExtra("orderno");
    }

    @Subscribe
    public void orderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_APPLY_AFTER_SALE_SUCCESS://订单申请售后成功
            case OrderIml.SIGN_AFTER_SALE_STATE_CHANGE://售后订单状态发生改变
                getOrderData(orderno);
                break;
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getOrderData(orderno);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ORDER_DETAIL)) {
            processOrderDetail((OrderDetailVo) dataVo);
        } else if (url.contains(ApiConfig.API_CANCEL_ORDER)) {
            showToast(getString(R.string.a_operate_success));
            getOrderData(orderno);
        } else if (url.contains(ApiConfig.API_CONFIRM_ORDER)) {
            showToast(getString(R.string.a_operate_success));
            getOrderData(orderno);
        }
    }

    /**
     * 获取订单详情
     *
     * @param orderno
     */
    public void getOrderData(String orderno) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ORDER_DETAIL, new RequestParams()
                .put("orderno", orderno)
                .putCid()
                .get(), OrderDetailVo.class);
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CANCEL_ORDER, new RequestParams()
                .putCid()
                .put("orderno", orderno)
                .get(), BaseVo.class);
    }

    /**
     * 确认收货
     */
    private void confirmReceived() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CONFIRM_ORDER, new RequestParams()
                .putCid()
                .put("orderno", orderno)
                .get(), BaseVo.class);
    }

    /**
     * 处理订单详情
     *
     * @param dataVo
     */
    private void processOrderDetail(OrderDetailVo dataVo) {
        this.detailVo = dataVo;
        tvNo.setText(String.format("%1$s%2$s", getString(R.string.a_order_number_), orderno));
        tvStatus.setText(OperateUtils.getInstance().getStatus(mActivity, dataVo.orderstate));
        tvName.setText(String.format("%1$s %2$s", dataVo.customername, dataVo.tel));
        tvAddress.setText(String.format("%1$s", dataVo.address));
        //商品
        layoutGoods.removeAllViews();
        if (dataVo.detailedList != null && dataVo.detailedList.size() > 0) {
            for (OrderDetailVo.DetailedListBean bean : dataVo.detailedList) {
                layoutGoods.addView(getGoodsView(layoutGoods, bean));
            }
        }
        convertState(dataVo.orderstate);
        tvRemark.setText(dataVo.remark);
        tvTime.setText(dataVo.orderdate);
        //价格信息
        tvTotalMoney.setText(String.format("%1$s%2$s", "£", dataVo.totalmoney));
        tvFreight.setText(String.format("%1$s%2$s", "£", dataVo.expressmoney));
        tvDiscount.setText(String.format("%1$s%2$s", "-£", dataVo.discountmoney));
        tvActual.setText(String.format("%1$s%2$s", "£", dataVo.actualmoney));
    }

    /**
     * get goods view
     *
     * @param layoutGoods
     * @param item
     * @return
     */
    private View getGoodsView(LinearLayout layoutGoods, OrderDetailVo.DetailedListBean item) {
        return getHelperView(layoutGoods, R.layout.item_goods_order, helper -> {
            helper.setImageManager(mActivity, R.id.iv_image, item.previewimg, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, item.piname);
            helper.setText(R.id.tv_num, String.format("%1$s%2$s", "x", item.number));
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));
            //已完成和部分产品已退款状态可以申请售后
            if (detailVo.orderstate == ShopOrderFragment.ORDER_STATE_YET_COMPLETE
                    || detailVo.orderstate == ShopOrderFragment.ORDER_STATE_YET_HALF_EXIT) {
                helper.setViewVisible(R.id.btn_apply);
            } else {
                helper.setViewGone(R.id.btn_apply);
            }
            //只要售后数据不为空，就可以查看售后
            if (item.afterSalesOrder != null) {
                helper.setText(R.id.btn_apply, getString(R.string.a_look_refund));
                helper.setViewVisible(R.id.btn_apply);
            }
            helper.setOnClickListener(R.id.btn_apply, v -> {
                Button btnApply = helper.getView(R.id.btn_apply);
                String s = btnApply.getText().toString().trim();
                if (TextUtils.equals(s, getString(R.string.a_look_refund))) {
                    startActivity(RefundDetailActivity.getIntent(mActivity, item.afterSalesOrder.orderno));
                } else {
                    TempApplyBean applyBean = new TempApplyBean(detailVo.orderstate, detailVo.orderno);
                    applyBean.detailedList = new ArrayList<>();
                    applyBean.detailedList.add(item);
                    startActivity(ApplyRefundActivity.getIntent(mActivity, applyBean));
                }
            });
        });
    }

    /**
     * 处理订单状态
     *
     * @param orderstate
     */
    private void convertState(int orderstate) {
        setBtnHide();
        switch (orderstate) {
            case ShopOrderFragment.ORDER_STATE_UNPAID://待支付
                btnCancel.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.VISIBLE);
                break;
            case ShopOrderFragment.ORDER_STATE_WAIT_SEND://待发货
                btnApply.setVisibility(isCanApply() ? View.VISIBLE : View.GONE);
                break;
            case ShopOrderFragment.ORDER_STATE_WAIT_RECEIVE://待收货
                btnConfirm.setVisibility(View.VISIBLE);
                break;
            case ShopOrderFragment.ORDER_STATE_YET_COMPLETE://已完成
                btnApply.setVisibility(isCanApply() ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @OnClick({R.id.btn_cancel, R.id.btn_pay, R.id.btn_look, R.id.btn_apply, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                showCancelDialog(true);
                break;
            case R.id.btn_pay:
                if (detailVo != null) {
                    TempPayBean payBean = new TempPayBean(TempPayBean.TEMP_PAY_SHOP_ORDER, detailVo.orderno, detailVo.actualmoney);
                    startActivity(PayActivity.getIntent(mActivity, payBean));
                }
                break;
            case R.id.btn_look:
                startActivity(DeliveryActivity.getIntent(mActivity));
                break;
            case R.id.btn_confirm:
                showCancelDialog(false);
                break;
            case R.id.btn_apply:
                if (detailVo != null) {
                    TempApplyBean applyBean = new TempApplyBean(detailVo.orderstate, detailVo.orderno);
                    applyBean.detailedList = new ArrayList<>();
                    for (int i = 0; i < detailVo.detailedList.size(); i++) {
                        OrderDetailVo.DetailedListBean bean = detailVo.detailedList.get(i);
                        if (bean.afterSalesOrder == null) {
                            applyBean.detailedList.add(bean);
                        }
                    }
                    startActivity(ApplyRefundActivity.getIntent(mActivity, applyBean));
                }
                break;
        }
    }

    /**
     * cancel order dialog
     *
     * @param isCancel
     */
    private void showCancelDialog(boolean isCancel) {
        new WrapperDialog(mActivity) {
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
                                cancelOrder();
                            } else {
                                confirmReceived();
                            }
                            break;
                    }
                    dismiss();
                }, R.id.btn_confirm, R.id.btn_cancel);
            }
        }.show();
    }

    /**
     * 遍历所有商品，查看是否所有商品都已经申请售后，如果有商品没有申请售后，则可以申请
     *
     * @return
     */
    private boolean isCanApply() {
        if (detailVo != null) {
            for (OrderDetailVo.DetailedListBean bean : detailVo.detailedList) {
                if (bean.afterSalesOrder == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * set btn hide
     */
    private void setBtnHide(){
        btnCancel.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        btnLook.setVisibility(View.GONE);
        btnApply.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
    }
}
