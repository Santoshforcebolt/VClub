package com.easyder.club.module.me.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.StoreOrderVo;
import com.joooonho.SelectableRoundedImageView;
import com.sky.widget.autolayout.utils.AutoUtils;


/**
 * Author：sky on 2019/6/3 17:00.
 * Email：xcode126@126.com
 * Desc：门店订单列表adapter
 */

public class StoreOrderAdapter extends BaseQuickAdapter<StoreOrderVo.RowsBean, BaseRecyclerHolder> {

    //1:未付款;2:已完成;3:已取消;4:支付完成;5:已撤单
    public static final int ORDER_STATE_UNPAID = 1;
    public static final int ORDER_STATE_FINISH = 2;
    public static final int ORDER_STATE_CANCEL = 3;
    public static final int ORDER_STATE_PAY_COMPLETE = 4;
    public static final int ORDER_STATE_REVOKE = 5;

    public StoreOrderAdapter() {
        super(R.layout.item_store_order);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, StoreOrderVo.RowsBean item) {
        helper.setText(R.id.tv_store, String.format("%1$s", item.deptname));
        helper.setText(R.id.tv_status, getStatus(mContext, item.orderstate));
        String totalStr = String.format("%1$s%2$s%3$s ", mContext.getString(R.string.a_total), (item.productnum + item.itemnum),
                mContext.getString(R.string.a_unit_jian));
        String actualStr = String.format("%1$s%2$s%3$.2f", mContext.getString(R.string.a_actual_pay), "£", item.actualmoney);
        helper.setText(R.id.tv_total, String.format("%1$s%2$s", totalStr, actualStr));
        convertGoods(helper, item);
    }

    /**
     * 处理商品信息
     *
     * @param helper
     * @param item
     */
    private void convertGoods(BaseRecyclerHolder helper, StoreOrderVo.RowsBean item) {
        if (item.detailedlist != null && item.detailedlist.size() > 1) {
            helper.setGone(R.id.layout_container, true);
            helper.setGone(R.id.layout_multi, false);

            LinearLayout layoutContainer = helper.getView(R.id.layout_container);
            layoutContainer.removeAllViews();
            for (int i = 0; i < (item.detailedlist.size() > 4 ? 4 : item.detailedlist.size()); i++) {
                StoreOrderVo.RowsBean.DetailedlistBean detailedListBean = item.detailedlist.get(i);
                addImageItem(helper, layoutContainer, detailedListBean.imgurl);
            }

        } else {
            helper.setGone(R.id.layout_container, false);
            helper.setGone(R.id.layout_multi, true);

            StoreOrderVo.RowsBean.DetailedlistBean bean = item.detailedlist.get(0);
            helper.setImageManager(mContext, R.id.iv_goods, "", R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, bean.piname);
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "¥", bean.actualmoney));
//            helper.setText(R.id.tv_spec,bean.s);
            helper.setText(R.id.tv_number, String.format("%1$s%2$s", "x", bean.number));
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(width), AutoUtils.getPercentWidthSize(height));
        layoutParams.rightMargin = AutoUtils.getPercentWidthSize(25);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    /**
     * get status
     *
     * @param orderstate
     * @param mContext
     * @return
     */
    public String getStatus(Context mContext, int orderstate) {
        if (orderstate == ORDER_STATE_UNPAID) {//待支付
            return mContext.getString(R.string.a_wait_pay);
        } else if (orderstate == ORDER_STATE_FINISH) {//已完成
            return mContext.getString(R.string.a_yet_complete);
        } else if (orderstate == ORDER_STATE_CANCEL) {//已取消
            return mContext.getString(R.string.a_yet_cancel);
        } else if (orderstate == ORDER_STATE_PAY_COMPLETE) {//支付完成
            return mContext.getString(R.string.a_pay_complete);
        } else if (orderstate == ORDER_STATE_REVOKE) {//已撤单
            return mContext.getString(R.string.a_yet_revoke);
        } else {
            return "";
        }
    }
}
