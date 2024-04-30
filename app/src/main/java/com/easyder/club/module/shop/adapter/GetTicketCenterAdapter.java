package com.easyder.club.module.shop.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.GetTicketCenterVo;
import com.sky.wrapper.utils.UIUtils;

/**
 * Author: sky on 2020/11/24 15:37
 * Email: xcode126@126.com
 * Desc:  领券中心
 */
public class GetTicketCenterAdapter extends BaseQuickAdapter<GetTicketCenterVo.CouponCashsBean, BaseRecyclerHolder> {

    public GetTicketCenterAdapter() {
        super(R.layout.item_get_ticket_center);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, GetTicketCenterVo.CouponCashsBean item) {
//        helper.setImageResource(R.id.iv_flag, item.coupontype.equals("item") ? R.drawable.voucher2 : R.drawable.voucher1);
        helper.setText(R.id.tv_name, item.showname);
        helper.setText(R.id.tv_desc, item.effectiverangename);
        helper.setText(R.id.tv_day, String.format("%1$s%2$s", item.effenum, UIUtils.getString(mContext,R.string.a_day)));
        helper.setBackgroundRes(R.id.btn_get, item.state != 0 ? R.drawable.shape_bg_red_r5 : R.drawable.shape_bg_deep_gray_r5);
        helper.setText(R.id.btn_get, item.state != 0 ? UIUtils.getString(mContext,R.string.a_now_get) : UIUtils.getString(mContext,R.string.a_yet_get));
        helper.addOnClickListener(R.id.btn_get);
        helper.getView(R.id.btn_get).setEnabled(item.state != 0);
//        helper.setText(R.id.tv_limit, String.format("%1$s%2$s", UIUtils.getString(R.string.a_limit_), item.addtime));
    }
}
