package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.ShopIndexVo;

import java.util.List;

/**
 * Author: sky on 2020/11/17 15:45
 * Email: xcode126@126.com
 * Desc:
 */

public class ShopMenuAdapter extends BaseQuickAdapter<ShopIndexVo.ListIndexMenusBean, BaseRecyclerHolder> {

    public ShopMenuAdapter(List<ShopIndexVo.ListIndexMenusBean> mDataList) {
        super(R.layout.item_shop_menu, mDataList);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopIndexVo.ListIndexMenusBean item) {
        helper.setImageManager(mContext, R.id.iv_image, item.menuimg, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.menuname);
//        SelectableRoundedImageView mSelectableRoundedImageView = viewHolder.getViewById(R.id.mSelectableRoundedImageView);
//        TextView tvName = viewHolder.getViewById(R.id.tv_name);
//        mSelectableRoundedImageView.setEnabled(false);
//        tvName.setEnabled(false);
//        ImageManager.load(mContext, mSelectableRoundedImageView, item.menuimg, R.drawable.ic_placeholder_1);
//        viewHolder.setText(R.id.tv_name, item.menuname);
    }

}
