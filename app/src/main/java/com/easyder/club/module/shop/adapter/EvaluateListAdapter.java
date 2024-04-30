package com.easyder.club.module.shop.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;

/**
 * Author: sky on 2020/12/3 19:29
 * Email: xcode126@126.com
 * Desc:
 */
public class EvaluateListAdapter extends BaseQuickAdapter<EnjoyListVo.RowsBean, BaseRecyclerHolder> {

    public EvaluateListAdapter() {
        super(R.layout.item_evaluate_list);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, EnjoyListVo.RowsBean item) {
        helper.setImageManager(mContext, R.id.iv_header, item.icourl, R.drawable.default_header, R.drawable.default_header);
//        String content = String.format("<font color='#000000'><b>%1$s</b> </font>%2$s <font color='#000000'>%3$s </font>%4$s",
//                item.customername, "scored this whisky", item.totalscore, "points");
        helper.setText(R.id.tv_name, item.customername);
        helper.setText(R.id.tv_score, item.totalscore + "");
        helper.setText(R.id.tv_time, item.createdate);
        helper.setText(R.id.tv_place, item.address);
        helper.setGone(R.id.tv_place, !TextUtils.isEmpty(item.address));
    }
}
