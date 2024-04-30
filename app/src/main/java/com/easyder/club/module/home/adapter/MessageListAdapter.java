package com.easyder.club.module.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.home.vo.MessageListVo;

/**
 * Author: sky on 2020/11/23 11:48
 * Email: xcode126@126.com
 * Desc:
 */
public class MessageListAdapter extends BaseQuickAdapter<MessageListVo.RowsBean, BaseRecyclerHolder> {

    public MessageListAdapter() {
        super(R.layout.item_message_list);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, MessageListVo.RowsBean item) {
        helper.setText(R.id.tv_title, item.title).setText(R.id.tv_time, item.gmtcreate).setText(R.id.tv_content, item.content);
    }
}
