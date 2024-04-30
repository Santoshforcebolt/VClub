package com.easyder.club.module.me.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.MyIntegralVo;

/**
 * Author: sky on 2020/11/17 11:55
 * Email: xcode126@126.com
 * Desc:
 */
public class MyIntegralAdapter extends BaseQuickAdapter<MyIntegralVo.RowsBean, BaseRecyclerHolder> {

    public MyIntegralAdapter() {
        super(R.layout.item_my_integral);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, MyIntegralVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.changename);
        helper.setText(R.id.tv_date, item.changetime);
        helper.setText(R.id.tv_amount, item.showval);
        ImageView imageView = helper.getView(R.id.iv_image);

        if (TextUtils.isEmpty(item.showval) && item.showval.contains("+")) {
            imageView.setImageResource(R.drawable.integral_add);
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext,R.color.colorBrown));
//            helper.setText(R.id.tv_amount, String.format("+%1$.2f", item.score));
        } else {
            imageView.setImageResource(R.drawable.integral_reduce);
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext,R.color.colorRed));
//            helper.setText(R.id.tv_amount, String.format("-%1$.2f", item.score));
        }
    }
}
