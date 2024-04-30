package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.SpreadHistoryVo;

/**
 * Author: sky on 2020/11/18 17:56
 * Email: xcode126@126.com
 * Desc:
 */
public class SpreadHistoryAdapter extends BaseQuickAdapter<SpreadHistoryVo.ListBean, BaseRecyclerHolder> {
    public SpreadHistoryAdapter() {
        super(R.layout.item_spread_history);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, SpreadHistoryVo.ListBean item) {
        helper.setImageManager(mContext, R.id.iv_header, item.icourl, R.drawable.default_header, R.drawable.default_header);
        helper.setText(R.id.tv_name, item.scustomername);
        helper.setText(R.id.tv_tel, item.tel);
        helper.setText(R.id.tv_lever, item.level+"");
        helper.setText(R.id.tv_time, String.format("%1$s%2$s", mContext.getString(R.string.a_develop_time_), item.createdate));
        helper.setText(R.id.tv_order_amount, String.format("%1$.2f",item.salemoney));
        helper.setText(R.id.tv_order_earnings, String.format("%1$.2f",item.commission));
        helper.setText(R.id.tv_my_earnings, String.format("%1$.2f",item.commission));
    }
}
