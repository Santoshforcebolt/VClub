package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.SpreadOrderVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/12/19 15:56
 * Email: xcode126@126.com
 * Desc: 累计收益明细
 */
public class TotalEarningsAdapter extends BaseQuickAdapter<SpreadOrderVo.ListBean, BaseRecyclerHolder> {

    public TotalEarningsAdapter() {
        super(R.layout.item_total_earnings);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, SpreadOrderVo.ListBean item) {
        helper.setText(R.id.tv_type, item.commissname);
        helper.setText(R.id.tv_order_no, String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_order_number_), item.orderno));

        helper.setText(R.id.tv_amount, item.showval);
        if (item.showval.contains("+")) {
            helper.setTextColor(R.id.tv_amount, UIUtils.getColor(R.color.textMain));
            helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income4));
        } else {
            helper.setTextColor(R.id.tv_amount, UIUtils.getColor(R.color.colorRed));
            helper.setImageDrawable(R.id.iv_image, UIUtils.getDrawable(R.drawable.income5));
        }
        helper.setText(R.id.tv_time, item.commtime);
    }
}
