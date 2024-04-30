package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.ShopIndexVo;

import java.util.List;

/**
 * Author: sky on 2020/11/25 10:19
 * Email: xcode126@126.com
 * Desc: 获取模板二的商品item
 */
public class ShopTemplateTwoAdapter extends BaseQuickAdapter<ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean, BaseRecyclerHolder> {

    public ShopTemplateTwoAdapter(List<ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean> data) {
        super(R.layout.item_shop_template_two,data);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean item) {

        helper.setImageManager(mContext, R.id.iv_image, item.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.itemname);
        helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));
        helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "£", item.marketprice));
        helper.setVisible(R.id.tv_original_price, item.marketprice > item.price);

        helper.setGone(R.id.ll_look_more, mData.indexOf(item) == mData.size()-1);
        helper.addOnClickListener(R.id.ll_look_more);
    }
}
