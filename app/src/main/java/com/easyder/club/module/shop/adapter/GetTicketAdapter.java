package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.GetTicketVo;

/**
 * Author: sky on 2020/11/20 17:34
 * Email: xcode126@126.com
 * Desc:
 */
public class GetTicketAdapter extends BaseQuickAdapter<GetTicketVo.RowsBean, BaseRecyclerHolder> {
    public GetTicketAdapter() {
        super(R.layout.item_get_ticket);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, GetTicketVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.couponname);
        helper.setText(R.id.tv_desc, item.showname);
        helper.setText(R.id.tv_limit, item.effectiverangename);
        helper.getView(R.id.tv_get).setEnabled(item.state != 0);
        helper.addOnClickListener(R.id.tv_get);
    }
}
