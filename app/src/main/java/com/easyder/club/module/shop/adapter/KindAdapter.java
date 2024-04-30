package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.KindVo;

/**
 * Author: sky on 2020/12/7 16:05
 * Email: xcode126@126.com
 * Desc:
 */
public class KindAdapter extends BaseQuickAdapter<KindVo.DataBean, BaseRecyclerHolder> {

    private int selected = -1;

    public KindAdapter() {
        super(R.layout.item_kind);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, KindVo.DataBean item) {
        helper.setText(R.id.tv_name, item.groupname);
        helper.getView(R.id.tv_name).setSelected(helper.getAdapterPosition() == selected);
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public int getGroupCode() {
        if (selected == -1) {
            return selected;
        }
        if (mData == null) {
            return selected;
        }
        return mData.get(selected).groupcode;
    }
}
