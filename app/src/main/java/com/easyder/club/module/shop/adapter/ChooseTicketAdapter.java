package com.easyder.club.module.shop.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.MyTicketVo;

/**
 * Author: sky on 2020/11/20 15:41
 * Email: xcode126@126.com
 * Desc:
 */
public class ChooseTicketAdapter extends BaseQuickAdapter<MyTicketVo.RowsBean, BaseRecyclerHolder> {

    private String selectedInstanceCode;

    public ChooseTicketAdapter(String selectedInstanceCode) {
        super(R.layout.item_choose_ticket);
        this.selectedInstanceCode = selectedInstanceCode;
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, MyTicketVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.showname);
        helper.setText(R.id.tv_desc, item.effectiverangename);
        helper.setText(R.id.tv_time, String.format("%1$s-%2$s", item.adddate, item.enddate));
        helper.addOnClickListener(R.id.iv_select);
        helper.getView(R.id.iv_select).setSelected(TextUtils.equals(selectedInstanceCode, item.instancecode));
    }

    public void setSelected(String instancecode) {
        this.selectedInstanceCode = instancecode;
        notifyDataSetChanged();
    }
}
