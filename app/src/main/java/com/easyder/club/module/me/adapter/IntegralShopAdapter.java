package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.IntegralVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/11/17 11:55
 * Email: xcode126@126.com
 * Desc:
 */
public class IntegralShopAdapter extends BaseQuickAdapter<IntegralVo.RowsBean, BaseRecyclerHolder> {

    public IntegralShopAdapter() {
        super(R.layout.item_integral_shop);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, IntegralVo.RowsBean item) {
        helper.setImageManager(mContext, R.id.iv_image, item.imgurl, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2);
        helper.setText(R.id.tv_name, item.scoreproductname);
        helper.setText(R.id.tv_integral, String.format("%1$s%2$s", item.score, mContext.getString(R.string.a_unit_points)));
        helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "¥", item.saleprice));
//        helper.setGone(R.id.tv_original_price, item.saleprice > 0);
        helper.setText(R.id.tv_repertory, String.format("%1$s%2$s", UIUtils.getString(mContext, R.string.a_repertory), item.stocknum));
        helper.setText(R.id.tv_exchange, String.format("%1$s%2$s", UIUtils.getString(mContext, R.string.a_yet_exchange), item.useexchangenum));
    }
}
