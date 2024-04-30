package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.MyTicketFragment;
import com.easyder.club.module.me.vo.MyTicketVo;

/**
 * Author：sky on 2019/6/3 11:23.
 * Email：xcode126@126.com
 * Desc：优惠券-adapter
 */

public class MyTicketAdapter extends BaseQuickAdapter<MyTicketVo.RowsBean, BaseRecyclerHolder> {
    private int type;

    public MyTicketAdapter(int type) {
        super(R.layout.item_my_ticket);
        this.type = type;
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, MyTicketVo.RowsBean item) {
//        helper.addOnClickListener(R.id.tv_send);
//        helper.setGone(R.id.tv_send, type == MyTicketFragment.TYPE_NOT_USE);
        helper.setImageResource(R.id.iv_img, type == MyTicketFragment.TYPE_NOT_USE ? R.drawable.cash_ticket : R.drawable.cash_ticket_gray);

        helper.setText(R.id.tv_name, item.showname);
        helper.setText(R.id.tv_desc, item.effectiverangename);
        helper.setText(R.id.tv_time, String.format("%1$s-%2$s", item.adddate, item.enddate));
    }
}
