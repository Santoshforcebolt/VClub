package com.easyder.club.module.me.adapter;

import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.RefundOrderListVo;
import com.easyder.club.utils.OperateUtils;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/12/9 16:38
 * Email: xcode126@126.com
 * Desc:
 */
public class RefundOrderAdapter extends BaseQuickAdapter<RefundOrderListVo.RowsBean, BaseRecyclerHolder> {

//    售后单状态 1 审核中 2审核失败 3寄回商品 4会员已寄出 5商家已收取商品 6会员填写地址
//    换货：1 审核中 2审核失败 3寄回商品 4会员已寄出 5商家已收取商品 6会员已填写地址
//    退货退款：1 审核中 2审核失败 3寄回商品 4会员已寄出 * 7商家重新发货 8完成 9已取消（1，3）
//    退款：1 审核中 2审核失败

    //1 审核中 2审核失败 3寄回商品 4会员已寄出 5商家已收取商品 6会员填写地址 7商家重新发货 8完成 9已取消
    public static final int ORDER_STATUS_UNDER_REVIEW = 1;
    public static final int ORDER_STATUS_AUDIT_FAILURE = 2;
    public static final int ORDER_STATUS_RETURN_THE_GOODS = 3;
    public static final int ORDER_STATUS_IT_IS_SENT = 4;
    public static final int ORDER_STATUS_MERCHANT_RECEIVES_GOODS = 5;
    public static final int ORDER_STATUS_MEMBER_INPUT_ADDRESS = 6;
    public static final int ORDER_STATUS_MERCHANT_RESHIPMENT = 7;
    public static final int ORDER_STATUS_FINISH = 8;
    public static final int ORDER_STATUS_CANCEL = 9;
    //售后类型 售后状态（1仅退款 2退货退款 3换货）
    public static final int REFUND_TYPE_REFUND = 1;
    public static final int REFUND_TYPE_RETURN_REFUND = 2;
    public static final int REFUND_TYPE_EXCHANGE = 3;

    public RefundOrderAdapter() {
        super(R.layout.item_refund_order);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, RefundOrderListVo.RowsBean item) {
        //售后单号
        helper.setText(R.id.tv_no, String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_refund_order_no_), item.orderno));
//        售后单状态（1 审核中 2审核失败 3寄回商品 4会员已寄出 5商家已收取商品 6会员填写地址 7商家重新发货 8完成 9已取消）
        TextView orderStatusView = helper.getView(R.id.tv_status);
        switch (item.orderstate) {
            case ORDER_STATUS_UNDER_REVIEW:
                orderStatusView.setText(mContext.getString(R.string.a_checking));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_AUDIT_FAILURE:
                orderStatusView.setText(mContext.getString(R.string.a_check_fail));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_RETURN_THE_GOODS:
                orderStatusView.setText(mContext.getString(R.string.a_return_goods));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_IT_IS_SENT:
                orderStatusView.setText(mContext.getString(R.string.a_member_yet_send));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_MERCHANT_RECEIVES_GOODS:
                orderStatusView.setText(mContext.getString(R.string.a_member_yet_receive_goods));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_MEMBER_INPUT_ADDRESS:
                orderStatusView.setText(mContext.getString(R.string.a_member_input_address));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_MERCHANT_RESHIPMENT:
                orderStatusView.setText(mContext.getString(R.string.a_shoper_resend));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
            case ORDER_STATUS_FINISH:
                orderStatusView.setText(mContext.getString(R.string.a_complete));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.textMinor));
                break;
            case ORDER_STATUS_CANCEL:
                orderStatusView.setText(mContext.getString(R.string.a_yet_cancel));
                orderStatusView.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
        }
        //商品图片
        helper.setImageManager(mContext, R.id.iv_goods, OperateUtils.getFirstImage(item.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        //商品名称
        helper.setText(R.id.tv_name, item.itemname);
        //申请售后数量
        helper.setText(R.id.tv_num, String.format("%1$s%2$d", UIUtils.getString(mContext,R.string.a_apply_num), item.number));
        //售后类型 售后状态（1仅退款 2退货退款 3换货）
        TextView afterSaleTypeView = helper.getView(R.id.tv_type);
        switch (item.aftersalestype) {
            case REFUND_TYPE_REFUND:
                afterSaleTypeView.setText(String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_refund_order_type_), mContext.getString(R.string.a_only_exit_money)));
                break;
            case REFUND_TYPE_RETURN_REFUND:
                afterSaleTypeView.setText(String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_refund_order_type_), mContext.getString(R.string.a_exit_money_goods)));
                break;
            case REFUND_TYPE_EXCHANGE:
                afterSaleTypeView.setText(String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_refund_order_type_), mContext.getString(R.string.a_exchange_goods)));
                break;
        }
        //查看详情
        helper.addOnClickListener(R.id.btn_detail);
    }
}
