package com.easyder.club.module.me.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.PartnerDetailVo;

/**
 * Author：sky on 2019/7/1 16:08.
 * Email：xcode126@126.com
 * Desc：门店列表
 */

public class ShopListAdapter extends BaseQuickAdapter<PartnerDetailVo.DatasBean, BaseRecyclerHolder> {

    public ShopListAdapter() {
        super(R.layout.item_shop_list);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, PartnerDetailVo.DatasBean item) {
        helper.setText(R.id.tv_store_name, item.shopname);
        helper.setText(R.id.tv_status_distance, String.format("%1$skm", item.linkman));
        helper.setText(R.id.tv_store_address, item.address);
    }
}
