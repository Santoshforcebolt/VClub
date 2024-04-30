package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.StoreListVo;

/**
 * Author: sky on 2020/11/17 16:49
 * Email: xcode126@126.com
 * Desc:
 */
public class StoreListAdapter extends BaseQuickAdapter<StoreListVo.RowsBean, BaseRecyclerHolder> {

    private int selected;

    public StoreListAdapter() {
        super(R.layout.item_store_lit);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, StoreListVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.deptname)
                .setText(R.id.tv_address, item.addr)
                .setText(R.id.tv_length, String.format("%1$s%2$s", item.distance, "Km"))
                .setText(R.id.tv_time, String.format("%1$s%2$s", mContext.getString(R.string.a_business_time_), item.businesstime))
                .setText(R.id.tv_tel, String.format("%1$s%2$s", mContext.getString(R.string.a_contact_tel_), item.tel));
        helper.addOnClickListener(R.id.iv_tel);
        helper.setGone(R.id.iv_check, helper.getAdapterPosition() == selected);
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }
}
