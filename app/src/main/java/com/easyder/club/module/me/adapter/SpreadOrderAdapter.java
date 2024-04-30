package com.easyder.club.module.me.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.SpreadOrderVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/11/18 18:16
 * Email: xcode126@126.com
 * Desc:
 */
public class SpreadOrderAdapter extends BaseQuickAdapter<SpreadOrderVo.ListBean, BaseRecyclerHolder> {

    public SpreadOrderAdapter() {
        super(R.layout.item_spread_order);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, SpreadOrderVo.ListBean item) {
        helper.setText(R.id.tv_money, Html.fromHtml(String.format("%1$s<font color ='#C5995B'>%2$s%3$.2f</font>", mContext.getString(R.string.a_order_money_), "\n" + "¥", item.salemoney)));
        helper.setText(R.id.tv_name, String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_buy_order_people_),item.scustomername));
        helper.setText(R.id.tv_integral, String.format("%1$.2f%2$s", item.commission, mContext.getString(R.string.a_unit_points)));
        helper.setText(R.id.tv_no, String.format("%1$s%2$s", mContext.getString(R.string.a_order_num_), item.orderno));
        helper.setText(R.id.tv_time, String.format("%1$s%2$s", mContext.getString(R.string.a_time_), item.commtime));
    }
}
