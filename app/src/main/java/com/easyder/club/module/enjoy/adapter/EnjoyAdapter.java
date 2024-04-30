package com.easyder.club.module.enjoy.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.utils.OperateUtils;

/**
 * Author: sky on 2020/11/13 18:04
 * Email: xcode126@126.com
 * Desc:
 */
public class EnjoyAdapter extends BaseQuickAdapter<EnjoyListVo.RowsBean, BaseRecyclerHolder> {

    public EnjoyAdapter() {
        super(R.layout.item_enjoy);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, EnjoyListVo.RowsBean item) {
        helper.setImageManager(mContext, R.id.iv_image, OperateUtils.getFirstImage(item.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.productname);
        helper.setText(R.id.tv_time, item.createdate);

        helper.setText(R.id.tv_volume, String.format("%1$s%2$s", item.capacity, "ml"));
        helper.setText(R.id.tv_degree, String.format("%1$s%2$s", item.alcoholic, "%vol"));
        helper.setText(R.id.tv_date, TextUtils.isEmpty(item.distillationyear) ? "0" : item.distillationyear);
        helper.setGone(R.id.tv_volume, item.capacity > 0);
        helper.setGone(R.id.tv_degree, item.alcoholic > 0);
        helper.setGone(R.id.tv_date, !TextUtils.isEmpty(item.distillationyear));

        helper.setText(R.id.tv_evaluate, item.totalscore + "");
        helper.getView(R.id.ll_wish).setSelected(item.wish == 1);
        helper.getView(R.id.ll_collect).setSelected(item.collection == 1);


        helper.addOnClickListener(R.id.ll_wish);
        helper.addOnClickListener(R.id.ll_collect);
    }
}
