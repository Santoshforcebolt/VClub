package com.easyder.club.module.me.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.BalanceVo;

/**
 * Author: sky on 2020/11/17 14:09
 * Email: xcode126@126.com
 * Desc:
 */
public class BalanceAdapter extends BaseQuickAdapter<BalanceVo.RowsBean, BaseRecyclerHolder> {

    public BalanceAdapter() {
        super(R.layout.item_balance);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, BalanceVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.changetype);
        helper.setText(R.id.tv_date, item.changetime);

        ImageView imageView = helper.getView(R.id.iv_image);
        if (!TextUtils.isEmpty(item.showval) && item.showval.contains("+")) {
            imageView.setImageResource(R.drawable.money_in);
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext,R.color.colorBrown));
            helper.setText(R.id.tv_amount, String.format("+£%1$.2f", item.newmoney));
        } else {
            imageView.setImageResource(R.drawable.money_out);
            helper.setTextColor(R.id.tv_amount, ContextCompat.getColor(mContext,R.color.colorRed));
            helper.setText(R.id.tv_amount, String.format("-£%1$.2f", item.newmoney));
        }

    }
}
