package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.ShopIndexVo;

import java.util.List;

/**
 * Author: sky on 2020/11/25 10:19
 * Email: xcode126@126.com
 * Desc: 获取模板四的商品item
 */
public class ShopTemplateFourAdapter extends BaseQuickAdapter<ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean, BaseRecyclerHolder> {

    public ShopTemplateFourAdapter(List<ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean> data) {
        super(R.layout.item_shop_template_four,data);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean item) {
        helper.setGone(R.id.ll_look_more, mData.indexOf(item) == mData.size()-1);
        helper.addOnClickListener(R.id.ll_look_more);
        helper.setImageManager(mContext,R.id.SelectableRoundedImageView,item.imgurl,R.drawable.ic_placeholder_1,R.drawable.ic_placeholder_1);
    }
}
