package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.AddressListVo;

/**
 * Author：sky on 2019/5/30 18:53.
 * Email：xcode126@126.com
 * Desc：地址列表
 */

public class AddressListAdapter extends BaseQuickAdapter<AddressListVo.RowsBean, BaseRecyclerHolder> {

    private boolean isSelect;

    public AddressListAdapter(boolean isSelect) {
        super(R.layout.item_address_list);
        this.isSelect = isSelect;
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, AddressListVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.receivername);
        helper.setText(R.id.tv_tel, item.receivertel);
        helper.setText(R.id.tv_address, String.format("%1$s",  item.detailedaddre));
        helper.getView(R.id.dtv_select).setSelected(item.defaultflag == 1);//1:是默认地址
        helper.setGone(R.id.iv_delete, !isSelect);
        helper.addOnClickListener(R.id.dtv_select);
        helper.addOnClickListener(R.id.iv_edit);
        helper.addOnClickListener(R.id.iv_delete);
    }

}
