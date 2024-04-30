package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;

import java.util.List;

/**
 * Author: sky on 2020/11/18 14:38
 * Email: xcode126@126.com
 * Desc:
 */
public class RechargeAdapter extends BaseQuickAdapter<Integer, BaseRecyclerHolder> {

    private int selected = -1;

    public RechargeAdapter(List<Integer> data) {
        super(R.layout.item_rechare, data);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, Integer item) {
        helper.setText(R.id.tv_content, String.format("%1$s", item));
//        helper.setText(R.id.et_amount, String.valueOf(item));
        helper.setGone(R.id.et_amount, (mData.size() - 1) == helper.getAdapterPosition());
        helper.setGone(R.id.tv_content, (mData.size() - 1) != helper.getAdapterPosition());
        helper.getView(R.id.tv_content).setSelected(helper.getAdapterPosition() == selected);
        helper.addOnClickListener(R.id.tv_content);
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public int getSelectNumber() {
        if (selected == -1) {
            return 0;
        }
        return mData.get(selected);
    }
}
