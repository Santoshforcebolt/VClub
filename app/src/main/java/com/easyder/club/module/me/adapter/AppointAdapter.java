package com.easyder.club.module.me.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.AppointListVo;

/**
 * Author: sky on 2020/11/17 10:56
 * Email: xcode126@126.com
 * Desc:
 */
public class AppointAdapter extends BaseQuickAdapter<AppointListVo.RowsBean, BaseRecyclerHolder> {
    public AppointAdapter() {
        super(R.layout.item_appoint);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, AppointListVo.RowsBean item) {
        helper.setImageManager(mContext, R.id.iv_image, item.deptimgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.deptname);
        helper.setText(R.id.tv_address, item.deptaddress);
        helper.setText(R.id.tv_status, getStatus(item.orderstate));
        helper.setText(R.id.tv_time, Html.fromHtml(String.format("%1$s<font color = '#333333'>%2$s</font>", mContext.getString(R.string.a_appoint_arrive_time_), item.appointmenttime)));
        helper.setText(R.id.tv_num, Html.fromHtml(String.format("%1$s<font color = '#333333'>%2$s%3$s</font>", mContext.getString(R.string.a_appoint_arrive_num_), item.toshopnumber, mContext.getString(R.string.a_people))));
        helper.setText(R.id.tv_box, Html.fromHtml(String.format("%1$s<font color = '#333333'>%2$s</font>", mContext.getString(R.string.a_is_need_box_), getBox(item.box))));
        helper.setGone(R.id.tv_appoint, item.orderstate == 1);
        helper.addOnClickListener(R.id.tv_appoint);
    }

    private String getBox(int box) {
        return mContext.getString(box == 0 ? R.string.a_no : R.string.a_yes);
    }

    private String getStatus(int state) {
//        状态 1待审核，2取消预约，3已完成,4未到店而超时失败
        if (state == 1) {
            return mContext.getString(R.string.a_wait_check);
        } else if (state == 2) {
            return mContext.getString(R.string.a_cancel_appoint);
        } else if (state == 3) {
            return mContext.getString(R.string.a_yet_complete);
        } else if (state == 4) {
            return mContext.getString(R.string.a_appoint_limit);
        } else {
            return "";
        }

    }
}
