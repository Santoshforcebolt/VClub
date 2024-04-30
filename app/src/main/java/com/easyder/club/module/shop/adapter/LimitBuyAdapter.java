package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.LimitBuyVo;
import com.easyder.club.utils.DoubleUtil;
import com.sky.widget.component.HorizontalProgressBar;

import java.math.BigDecimal;


/**
 * Author: sky on 2020/11/23 11:48
 * Email: xcode126@126.com
 * Desc:
 */
public class LimitBuyAdapter extends BaseQuickAdapter<LimitBuyVo.DetailedListBean, BaseRecyclerHolder> {

    public LimitBuyAdapter() {
        super(R.layout.item_shop_template_three);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, LimitBuyVo.DetailedListBean item) {
        helper.setImageManager(mContext, R.id.iv_image, item.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.itemname);
        helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));
        helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "£", item.marketprice));
        helper.setVisible(R.id.tv_original_price, item.marketprice > item.price);
        helper.setText(R.id.tv_last, String.format("%1$s%2$s%3$s", mContext.getString(R.string.a_last), item.stocknum, mContext.getString(R.string.a_unit_jian)));
        HorizontalProgressBar mHorizontalProgressBar = helper.getView(R.id.mHorizontalProgressBar);
        if (item.totalnum == 0 || item.totalnum - item.stocknum <= 0) {
            mHorizontalProgressBar.setProgress(100);
            helper.setText(R.id.tv_progress, "100%");
//                    helper.setVisible(R.id.ll_progress_bar, false);
        } else {
//                    helper.setVisible(R.id.ll_progress_bar, true);
            int progress = (int) Math.rint(DoubleUtil.round((item.totalnum - (item.stocknum == -1 ? 0 : item.stocknum)) / item.totalnum * 100, 2, BigDecimal.ROUND_HALF_UP));
            mHorizontalProgressBar.setProgress(progress);
            helper.setText(R.id.tv_progress, String.format("%1$s%2$s", progress, "%"));
        }
    }
}
