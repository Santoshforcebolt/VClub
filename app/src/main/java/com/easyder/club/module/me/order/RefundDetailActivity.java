package com.easyder.club.module.me.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.RefundOrderAdapter;
import com.easyder.club.module.me.vo.RefundOrderDetailVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Author: sky on 2020/12/9 17:26
 * Email: xcode126@126.com
 * Desc: 售后单详情
 */
public class RefundDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;

    private String orderNo;

    public static Intent getIntent(Context context, String orderNO) {
        return new Intent(context, RefundDetailActivity.class)
                .putExtra("orderNo", orderNO);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_refund_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_after_sale_detail));
        orderNo = intent.getStringExtra("orderNo");
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getDetailData(orderNo);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_GET_REFUND_ORDER_DETAIL)) {
            processDetailData((RefundOrderDetailVo) dataVo);
        } else if (url.contains(ApiConfig.API_COMMIT_REFUND_EXPRESS_INFO)) {
            getDetailData(orderNo);
            EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_AFTER_SALE_STATE_CHANGE));
        } else if (url.contains(ApiConfig.API_AFTER_SALE_COMPLETE)) {
            getDetailData(orderNo);
            EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_AFTER_SALE_STATE_CHANGE));
            finish();
        } else if (url.contains(ApiConfig.API_AFTER_SALES_CANCEL)) {
            EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_AFTER_SALE_STATE_CHANGE));
            getDetailData(orderNo);
            showToast(getString(R.string.a_operate_success));
        }
    }

    /**
     * get detail data
     *
     * @param orderNo
     */
    private void getDetailData(String orderNo) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_REFUND_ORDER_DETAIL, new RequestParams()
                .putCid()
                .putWithoutEmpty("orderno", orderNo)
                .get(), RefundOrderDetailVo.class);
    }

    /**
     * cancel refund
     */
    private void cancelRefund() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_AFTER_SALES_CANCEL, new RequestParams()
                .putCid().put("orderno", orderNo)
                .get(), BaseVo.class);
    }

    /**
     * 提交退回商品的物流信息
     *
     * @param company
     * @param expressNo
     */
    private void commitExpressInfo(String company, String expressNo) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_COMMIT_REFUND_EXPRESS_INFO, new RequestParams()
                .putWithoutEmpty("orderno", orderNo)
                .putWithoutEmpty("expressno", expressNo)
                .putWithoutEmpty("expressname", company)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * @param orderno
     */
    private void afterSaleComplete(String orderno) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_AFTER_SALE_COMPLETE, new RequestParams()
                .put("orderno", orderno)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * process detail data
     *
     * @param dataVo
     */
    private void processDetailData(final RefundOrderDetailVo dataVo) {
        layoutContainer.removeAllViews();
        //1 审核中 2审核失败 3寄回商品 4会员已寄出 5商家已收取商品 6会员填写地址 7商家重新发货 8完成 9已取消
        switch (dataVo.afterSalesOrder.orderstate) {
            case RefundOrderAdapter.ORDER_STATUS_UNDER_REVIEW:
                ConstructHeadView(R.drawable.after_sale, getString(R.string.a_checking), getString(R.string.a_one_day_complete), R.color.colorRed);
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_AUDIT_FAILURE:
                ConstructHeadView(R.drawable.after_sale2, getString(R.string.a_check_fail), String.format("%1$s%2$s", getString(R.string.a_reason_),
                        dataVo.afterSalesOrder.remark), R.color.colorRed);
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_RETURN_THE_GOODS:
                handlePickUpOrder(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_IT_IS_SENT:
                ConstructHeadView(R.drawable.after_sale4, getString(R.string.a_yet_send_return),
                        getString(R.string.a_receive_now_process), R.color.colorGreen);
                ConstructRefundAddressView(dataVo);
                if (dataVo.afterSalesOrder.returntype == 1) {//2019-10-15 修改
                    constructSendBackExpressInfo(dataVo);
                }
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_MERCHANT_RECEIVES_GOODS:
                ConstructHeadView(R.drawable.after_sale5, getString(R.string.a_send_goods_yet_receive), getString(R.string.a_wait_process_resend), R.color.colorGreen);
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_MEMBER_INPUT_ADDRESS:
                ConstructHeadView(R.drawable.after_sale3, getString(R.string.a_member_input_address), getString(R.string.a_return_goods_by_address), R.color.colorGreen);
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_MERCHANT_RESHIPMENT:
                ConstructHeadView(R.drawable.after_sale6, getString(R.string.a_goods_yet_send), getString(R.string.a_resend_order_address), R.color.colorGreen);
                constructReissueExpressInfo(dataVo);
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_FINISH:
                // 售后类型（ 1仅退款 2退货退款 3换货）
                //区分售后类型 订单状态描述不一致 所以区分一下
                switch (dataVo.afterSalesOrder.aftersalestype) {
                    case RefundOrderAdapter.REFUND_TYPE_REFUND:
                        ConstructHeadView(R.drawable.after_sale7, getString(R.string.a_exit_money_success), getString(R.string.a_exit_money_yet_to_your_account), R.color.colorGreen);
                        break;
                    case RefundOrderAdapter.REFUND_TYPE_RETURN_REFUND:
                        ConstructHeadView(R.drawable.after_sale7, getString(R.string.a_exit_money_goods_success),getString(R.string.a_exit_money_yet_to_your_account), R.color.colorGreen);
                        break;
                    case RefundOrderAdapter.REFUND_TYPE_EXCHANGE:
                        ConstructHeadView(R.drawable.after_sale7, getString(R.string.a_exchange_goods_success), getString(R.string.a_welcome_next_buy), R.color.colorGreen);
                        break;
                }
                ConstructOrderInfoView(dataVo);
                break;
            case RefundOrderAdapter.ORDER_STATUS_CANCEL:
                ConstructHeadView(R.drawable.after_sale2, getString(R.string.a_sale_yet_cancel), getString(R.string.a_member_yet_cancel_sale_order), R.color.colorGreen);
                ConstructOrderInfoView(dataVo);
                break;
        }
    }

    /**
     * handle pickup order
     *
     * @param dataVo
     */
    private void handlePickUpOrder(RefundOrderDetailVo dataVo) {
        //快递上门
        if (dataVo.afterSalesOrder.returntype == 1
                || dataVo.afterSalesOrder.returntype == 0) {
            ConstructHeadView(R.drawable.after_sale3, getString(R.string.a_check_pass_return_goods),
                    getString(R.string.a_return_goods_by_address), R.color.colorGreen);
            ConstructRefundAddressView(dataVo);
            constructInputExpressInfoView();
            ConstructOrderInfoView(dataVo);
        } else if (dataVo.afterSalesOrder.returntype == 2) { //门店自提
            ConstructHeadView(R.drawable.after_sale3, getString(R.string.a_check_pass_go_store_exchange_goods),
                    getString(R.string.a_please_go_store_exchange_goods), R.color.colorGreen);
            ConstructRefundStoreAddressView(dataVo);
            constructConfirmServiceBtn(dataVo);
            ConstructOrderInfoView(dataVo);
        }
    }

    /**
     * 构建退货地址
     *
     * @param dataVo
     */
    private void ConstructRefundAddressView(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_return_goods_address, helper -> {
            if (dataVo.afterSalesReturnAddress != null) {
                helper.setText(R.id.tv_customer_name_phone, String.format("%1$s%2$s%3$s", dataVo.afterSalesReturnAddress.addresseename,
                        " ", dataVo.afterSalesReturnAddress.addresseetel));
                helper.setText(R.id.tv_address, dataVo.afterSalesReturnAddress.address);
            }
            helper.setText(R.id.tv_title, R.string.a_exit_goods_address_);
        }));
    }

    /**
     * 构建退货地址
     *
     * @param dataVo
     */
    private void ConstructRefundStoreAddressView(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_return_goods_address, helper -> {
            if (dataVo.afterSalesReturnAddress != null) {
                helper.setText(R.id.tv_customer_name_phone, String.format("%1$s%2$s%3$s", dataVo.returnDeptInfo.deptname,
                        " ", dataVo.returnDeptInfo.tel));
                helper.setText(R.id.tv_address, dataVo.returnDeptInfo.addr);
            }
            helper.setText(R.id.tv_title, R.string.a_store_address_);
        }));
    }

    /**
     * 寄回物流信息
     *
     * @param dataVo
     */
    private void constructSendBackExpressInfo(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_express_info, helper -> {
            //快递公司名字
            helper.setText(R.id.tv_express_company, dataVo.afterSalesOrder.expressname);
            helper.setText(R.id.tv_express_no, dataVo.afterSalesOrder.expressno);
            helper.setVisible(R.id.btn_confirm, false);
        }));
    }

    /**
     * 构建自提订单已经把货送到门店确认按钮
     *
     * @param dataVo
     */
    private void constructConfirmServiceBtn(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_goods_service,
                helper -> helper.setOnClickListener(R.id.btn_confirm, v -> {
            //确认收货
            commitExpressInfo(null, null);
        })));
    }

    /**
     * 重新发出物流信息
     *
     * @param dataVo
     */
    private void constructReissueExpressInfo(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_express_info, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                //快递公司名字
                helper.setText(R.id.tv_express_company, dataVo.afterSalesOrder.reexpressname);
                helper.setText(R.id.tv_express_no, dataVo.afterSalesOrder.reexpressno);
                helper.setVisible(R.id.btn_confirm, true);
                helper.setOnClickListener(R.id.btn_confirm, v -> afterSaleComplete(dataVo.afterSalesOrder.orderno));
            }
        }));
    }

    /**
     * 构建寄回商品物流信息
     */
    private void constructInputExpressInfoView() {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_input_express_info, new OnViewHelper() {
            @Override
            public void help(final ViewHelper helper) {
                helper.setOnClickListener(R.id.btn_confirm, v -> {
                    String expressCompany = ((EditText) helper.getView(R.id.et_express_company)).getText().toString();
                    String expressNo = ((EditText) helper.getView(R.id.et_express_no)).getText().toString();
                    if (TextUtils.isEmpty(expressCompany) || TextUtils.isEmpty(expressNo)) {
                        showToast(getString(R.string.a_input_delivery_msg));
                        return;
                    }
                    commitExpressInfo(expressCompany, expressNo);
                });
            }
        }));
    }

    /**
     * 构建头部订单信息
     *
     * @param drawableId
     * @param status
     * @param desc
     * @param statusTextColor
     */
    private void ConstructHeadView(final int drawableId, final String status, final String desc, final int statusTextColor) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.header_after_sale_order_detail_status, helper -> {
            helper.setImageResource(R.id.iv_order_status, drawableId);
            helper.setText(R.id.tv_status, status);
            helper.setTextColor(R.id.tv_status, UIUtils.getColor(statusTextColor));
            helper.setText(R.id.tv_status_desc, desc);
            helper.setVisible(R.id.btn_cancel, TextUtils.equals(status, getString(R.string.a_checking)));
            helper.setOnClickListener(R.id.btn_cancel, v -> cancelRefund());
        }));
    }

    /**
     * 构建订单信息
     *
     * @param dataVo
     */
    private void ConstructOrderInfoView(final RefundOrderDetailVo dataVo) {
        layoutContainer.addView(getHelperView(layoutContainer, R.layout.footer_after_sale_order_info, helper -> {
            TextView refundTypeView = helper.getView(R.id.tv_refund_type);
//               售后类型（ 1仅退款 2退货退款 3换货）
            switch (dataVo.afterSalesOrder.aftersalestype) {
                case 1:
                    refundTypeView.setText(getString(R.string.a_only_exit_money));
                    break;
                case 2:
                    refundTypeView.setText(getString(R.string.a_exit_money_goods));
                    break;
                case 3:
                    refundTypeView.setText(getString(R.string.a_exchange_goods));
                    break;
            }
            helper.setText(R.id.tv_amount, String.format("%1$s%2$.2f", "£", dataVo.afterSalesOrder.actualmoney));
            helper.setText(R.id.tv_reason, dataVo.afterSalesOrder.reason);
            helper.setText(R.id.tv_order_no, dataVo.afterSalesOrder.orderno);
            helper.setText(R.id.tv_apply_time, dataVo.afterSalesOrder.ordertime);
            LinearLayout layoutPicture = helper.getView(R.id.layout_picture);
            if (dataVo.afterSalesOrder != null && !TextUtils.isEmpty(dataVo.afterSalesOrder.imgurl)) {
                layoutPicture.setVisibility(View.VISIBLE);
                String[] images = dataVo.afterSalesOrder.imgurl.split(";");
                for (String image : images) {
                    ImageView photo = new ImageView(mActivity);
                    photo.setLayoutParams(new ViewGroup.LayoutParams(UIUtils.dip2px(70), UIUtils.dip2px(70)));
                    ImageManager.load(mActivity, photo, image);
                    layoutPicture.addView(photo);
                }
            } else {
                layoutPicture.setVisibility(View.GONE);
            }
        }));
    }
}
