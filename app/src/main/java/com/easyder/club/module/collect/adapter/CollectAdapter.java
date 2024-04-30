package com.easyder.club.module.collect.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.collect.vo.CollectListVo;
import com.easyder.club.utils.OperateUtils;

/**
 * Author: sky on 2020/12/1 10:15
 * Email: xcode126@126.com
 * Desc:
 */
public class CollectAdapter extends BaseQuickAdapter<CollectListVo.RowsBean, BaseRecyclerHolder> {

    public CollectAdapter() {
        super(R.layout.item_collect);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, CollectListVo.RowsBean item) {
        helper.addOnClickListener(R.id.dtv_expand);
        helper.addOnClickListener(R.id.ll_pack_up);
        helper.addOnClickListener(R.id.iv_num_edit);
        helper.addOnClickListener(R.id.iv_price_edit);
        helper.addOnClickListener(R.id.iv_market_edit);
        helper.addOnClickListener(R.id.iv_way_edit);
        helper.setGone(R.id.ll_more, item.isExpand);
        helper.setVisible(R.id.dtv_expand, !item.isExpand);

        helper.setImageManager(mContext, R.id.iv_image, OperateUtils.getFirstImage(item.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.pictname);
        helper.setText(R.id.tv_num, Html.fromHtml(String.format("%1$s<font color='#333333'>%2$s</font>", mContext.getString(R.string.a_wine_collect_num_), item.number)));
        helper.setText(R.id.tv_price, Html.fromHtml(String.format("%1$s<font color='#333333'>£%2$.2f</font>", mContext.getString(R.string.a_buy_price_), item.saleprice)));
        helper.setText(R.id.tv_market_price, Html.fromHtml(String.format("%1$s<font color='#333333'>£%2$.2f</font>", mContext.getString(R.string.a_market_price_), item.marketprice)));
        helper.setText(R.id.tv_buy_way, Html.fromHtml(String.format("%1$s<font color='#333333'>%2$s</font>", mContext.getString(R.string.a_buy_way_), item.purchasedat)));

        helper.setText(R.id.tv_volume, String.format("%1$s%2$s", item.capacity, "ml"));
        helper.setText(R.id.tv_degree, String.format("%1$s%2$s", item.alcoholic, "%vol"));
        helper.setText(R.id.tv_date, TextUtils.isEmpty(item.distillationyear) ? "0" : item.distillationyear);
        helper.setGone(R.id.tv_volume, item.capacity > 0);
        helper.setGone(R.id.tv_degree, item.alcoholic > 0);
        helper.setGone(R.id.tv_date, !TextUtils.isEmpty(item.distillationyear));
    }


}
