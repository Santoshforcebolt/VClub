package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.ShopRecommendVo;
import com.easyder.club.utils.OperateUtils;

/**
 * Author: sky on 2020/11/17 15:49
 * Email: xcode126@126.com
 * Desc:
 */
public class ShopAdapter extends BaseQuickAdapter<ShopRecommendVo.RowsBean, BaseRecyclerHolder> {

    public ShopAdapter() {
        super(R.layout.item_shop_recommend);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopRecommendVo.RowsBean item) {
        helper.setGone(R.id.ll_recommend, mData.indexOf(item) == 0);
        helper.setImageManager(mContext, R.id.iv_goods, OperateUtils.getFirstImage(item.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.picname);
        helper.setText(R.id.tv_evaluate, String.format("%1$s%2$s", mContext.getString(R.string.a_average_evaluate_), item.totalscore));
        helper.setText(R.id.tv_num, String.format("%1$s%2$s", mContext.getString(R.string.a_wine_collect_num_), item.collectnumber));
        helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.saleprice));
//        helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "£", item.marketprice));
//        helper.setVisible(R.id.tv_original_price, item.marketprice > item.saleprice);
        helper.addOnClickListener(R.id.tv_enjoy);
    }
}
