package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.YetEarningsVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/12/19 15:28
 * Email: xcode126@126.com
 * Desc:
 */
public class YetEarningsAdapter extends BaseQuickAdapter<YetEarningsVo.RowsBean, BaseRecyclerHolder> {

    public YetEarningsAdapter() {
        super(R.layout.item_yet_earnings);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, YetEarningsVo.RowsBean item) {
        switch (item.extractiontype) {
            case 1:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_extract_balance));
                break;
            case 2:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_extract_integral));
                break;
            case 3:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_buy_order_exchange));
                break;
            case 4:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_extract_card));
                break;
            case 5:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_extract_wechat_balance));
                break;
            case 6:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income4));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_earnings_return));
                break;
            case 7:
                helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income4));
                helper.setText(R.id.tv_type, mContext.getString(R.string.a_earnings_return));
                break;

        }
        String b = item.extractiontype <= 5 ? "-" : "+";
        helper.setText(R.id.tv_amount, String.format("%1$s%2$.2f", b, Math.abs(item.commismoney)));
        helper.setTextColor(R.id.tv_amount, UIUtils.getColor(R.color.colorRed));
        helper.setText(R.id.tv_order_no, String.format(String.format("%1$s%2$s", UIUtils.getContext().getString(R.string.a_order_number_), item.orderno)));
//        helper.setText(R.id.tv_detail_time, item.ordertime.substring(0, item.ordertime.length() - 5));
        helper.setText(R.id.tv_time, item.ordertime);
    }
}
