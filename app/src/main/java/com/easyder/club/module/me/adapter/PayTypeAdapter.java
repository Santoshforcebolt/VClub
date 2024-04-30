package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.common.vo.PayTypeBean;
import com.sky.wrapper.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sky on 2020/12/8 18:06
 * Email: xcode126@126.com
 * Desc:
 */
public class PayTypeAdapter extends BaseQuickAdapter<PayTypeBean, BaseRecyclerHolder> {

    private int selected;
    private double needAmount;

    public PayTypeAdapter(double needAmount) {
        super(R.layout.item_pay_type);
        this.needAmount = needAmount;
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, PayTypeBean item) {
        helper.getView(R.id.iv_select).setSelected(mData.indexOf(item) == selected);
        helper.setImageResource(R.id.iv_image, item.image);
        helper.setText(R.id.tv_name, item.payName);
        //钱包余额
        if (item.payCode.equals(PayTypeBean.PAY_TYPE_WALLET)) {//处理钱包
            helper.setVisible(R.id.tv_balance, true);//钱包余额是否显示
            helper.setText(R.id.tv_balance, String.format("%1$s%2$.2f", "¥", item.balacne));//余额
//            helper.setVisible(R.id.mTvBalanceTips, item.balacne < needAmount);//余额不足提示
            helper.setVisible(R.id.iv_select, item.balacne > needAmount);
        } else {
            helper.setVisible(R.id.tv_balance, false);//钱包余额是否显示
        }
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    /**
     * 初始化数据
     *
     * @return
     */
    public void initPayData(double balance, boolean isWallet) {
        List<PayTypeBean> payList = new ArrayList<>();
        if (isWallet) {
            payList.add(new PayTypeBean(R.drawable.wallet, UIUtils.getString(mContext,R.string.a_account_balance_), PayTypeBean.PAY_TYPE_WALLET, balance));
        }
        payList.add(new PayTypeBean(R.drawable.visa, "VISA", PayTypeBean.PAY_TYPE_VISA, balance));
        payList.add(new PayTypeBean(R.drawable.master_card, "Mastercard", PayTypeBean.PAY_TYPE_CARD, balance));
        setNewData(payList);
    }

    /**
     * 获取选中的选项
     *
     * @return
     */
    public PayTypeBean getSelectedItem() {
        return mData.get(selected);
    }
}
