package com.easyder.club.module.me.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.order.ShopOrderFragment;
import com.easyder.club.module.me.vo.ShopOrderVo;
import com.easyder.club.utils.OperateUtils;
import com.joooonho.SelectableRoundedImageView;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.wrapper.utils.UIUtils;


/**
 * Author：sky on 2019/6/3 17:00.
 * Email：xcode126@126.com
 * Desc：商城订单adapter
 */

public class ShopOrderAdapter extends BaseQuickAdapter<ShopOrderVo.RowsBean, BaseRecyclerHolder> {

    public ShopOrderAdapter() {
        super(R.layout.item_shop_order);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopOrderVo.RowsBean item) {
        helper.setText(R.id.tv_no, String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_order_number_), item.orderno));
        helper.setText(R.id.tv_status, OperateUtils.getInstance().getStatus(mContext, item.orderstate));
        String oughtStr = String.format("%1$s%2$s%3$s %4$s%5$s%6$.2f", mContext.getString(R.string.a_total), item.productnum, mContext.getString(R.string.a_unit_jian), mContext.getString(R.string.a_ought_pay), "£", item.actualmoney);
        helper.setText(R.id.tv_total, oughtStr);
        convertGoods(helper, item);
        convertState(helper, item);
    }

    /**
     * 处理商品信息
     *
     * @param helper
     * @param item
     */
    private void convertGoods(BaseRecyclerHolder helper, ShopOrderVo.RowsBean item) {
        if (item.detailedList != null && item.detailedList.size() > 1) {
            helper.setGone(R.id.layout_one, false);
            helper.setGone(R.id.layout_multi, true);
            LinearLayout layoutMulti = helper.getView(R.id.layout_multi);
            layoutMulti.removeAllViews();
            for (int i = 0; i < (item.detailedList.size() > 3 ? 3 : item.detailedList.size()); i++) {
                ShopOrderVo.RowsBean.DetailedListBean detailedListBean = item.detailedList.get(i);
                addImageItem(helper, layoutMulti, detailedListBean.previewimg);
            }

        } else {
            helper.setGone(R.id.layout_one, true);
            helper.setGone(R.id.layout_multi, false);

            ShopOrderVo.RowsBean.DetailedListBean bean = item.detailedList.get(0);
            helper.setImageManager(mContext, R.id.iv_goods, bean.previewimg, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, bean.piname);
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "¥", bean.originalprice));
            helper.setText(R.id.tv_spec, R.string.a_default_spec);
            helper.setText(R.id.tv_number, String.format("%1$s%2$s", "x", bean.number));
        }
    }

    /**
     * 处理订单状态
     * 待支付：取消订单、支付
     * 待发货：申请售后（售后选项只有仅退款，且仅可整单申请售后）
     * 待收货：确认收货
     * 已取消：无按钮
     * 已完成：申请售后（售后选项有：退货退款、换货，可整单且未售后的商品可单独申请）
     * 支付后又取消的按钮：无按钮
     * 产品已退款：无按钮（若还有其他产品未售后的，可以单独选择申请售后）
     *
     * @param helper
     * @param item
     */
    private void convertState(BaseRecyclerHolder helper, ShopOrderVo.RowsBean item) {
        setBtnHide(helper);
        helper.setGone(R.id.ll_bottom, true);
        switch (item.orderstate) {
            case ShopOrderFragment.ORDER_STATE_UNPAID://待支付
                helper.setGone(R.id.btn_cancel, true);
                helper.setGone(R.id.btn_pay, true);
                break;
            case ShopOrderFragment.ORDER_STATE_WAIT_SEND://待发货
//                helper.setGone(R.id.btn_apply, true);
                helper.setGone(R.id.ll_bottom, false);
                break;
            case ShopOrderFragment.ORDER_STATE_WAIT_RECEIVE://待收货
                helper.setGone(R.id.btn_confirm, true);
                break;
            case ShopOrderFragment.ORDER_STATE_YET_COMPLETE://已完成
//                helper.setGone(R.id.btn_apply, true);
                helper.setGone(R.id.ll_bottom, false);
                break;
            default:
                helper.setGone(R.id.ll_bottom, false);
                break;
        }
    }

    /**
     * 添加图片Item项
     *
     * @param helper
     * @param container
     * @param url
     */
    private void addImageItem(BaseRecyclerHolder helper, LinearLayout container, String url) {
        ImageView imageView = getImageView(140, 140);
        helper.setImageManager(mContext, imageView, url, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2);
        container.addView(imageView);
    }

    /**
     * 获取一个ImageView
     *
     * @param width
     * @param height
     * @return
     */
    private SelectableRoundedImageView getImageView(int width, int height) {
        SelectableRoundedImageView imageView = new SelectableRoundedImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOval(false);
        imageView.setCornerRadiiDP(5, 5, 5, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(width),
                AutoUtils.getPercentWidthSize(height));
        layoutParams.rightMargin = AutoUtils.getPercentWidthSize(25);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    /**
     * set btn hide
     *
     * @param helper
     */
    private void setBtnHide(BaseRecyclerHolder helper) {
        helper.addOnClickListener(R.id.btn_cancel);
        helper.addOnClickListener(R.id.btn_pay);
        helper.addOnClickListener(R.id.btn_look);
        helper.addOnClickListener(R.id.btn_apply);
        helper.addOnClickListener(R.id.btn_confirm);

        helper.setGone(R.id.btn_cancel, false);
        helper.setGone(R.id.btn_pay, false);
        helper.setGone(R.id.btn_look, false);
        helper.setGone(R.id.btn_apply, false);
        helper.setGone(R.id.btn_confirm, false);
    }

}
