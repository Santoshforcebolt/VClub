package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.IntegralExchangeVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author：sky on 2019/6/4 18:38.
 * Email：xcode126@126.com
 * Desc：积分明细
 */

public class IntegralExchangeAdapter extends BaseQuickAdapter<IntegralExchangeVo.RowsBean, BaseRecyclerHolder> {
    public IntegralExchangeAdapter() {
        super(R.layout.item_integral_exchange);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, IntegralExchangeVo.RowsBean item) {
        helper.setText(R.id.tv_type, item.scoreproductname);
        helper.setText(R.id.tv_last, String.format("%1$s%2$s", UIUtils.getString(mContext,R.string.a_last_), item.number));
        helper.setText(R.id.tv_date, item.ordertime);
        helper.setText(R.id.tv_number, item.score);
    }
}
