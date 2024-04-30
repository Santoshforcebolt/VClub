package com.easyder.club.module.enjoy.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.utils.OperateUtils;

/**
 * Author: sky on 2020/11/16 18:30
 * Email: xcode126@126.com
 * Desc:
 */
public class EnjoyListAdapter extends BaseQuickAdapter<GoodsListVo.ProductsBean, BaseRecyclerHolder> {

    public EnjoyListAdapter() {
        super(R.layout.item_enjoy_list);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, GoodsListVo.ProductsBean item) {
        helper.setImageManager(mContext, R.id.iv_goods, OperateUtils.getFirstImage(item.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.productname);
        helper.setText(R.id.tv_volume, String.format("%1$s%2$s", item.capacity, "ml"));
        helper.setText(R.id.tv_degree, String.format("%1$s%2$s", item.alcoholic, "%vol"));
        helper.setText(R.id.tv_date, TextUtils.isEmpty(item.distillationyear) ? "0" : item.distillationyear);
        helper.setGone(R.id.tv_volume, item.capacity > 0);
        helper.setGone(R.id.tv_degree, item.alcoholic > 0);
        helper.setGone(R.id.tv_date, !TextUtils.isEmpty(item.distillationyear));
    }
}
