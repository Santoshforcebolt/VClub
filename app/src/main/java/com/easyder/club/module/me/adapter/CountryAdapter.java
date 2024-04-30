package com.easyder.club.module.me.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.me.vo.CountryListVo;

import java.util.Collections;
import java.util.List;

/**
 * Author: sky on 2020/12/2 16:50
 * Email: xcode126@126.com
 * Desc:
 */
public class CountryAdapter extends BaseQuickAdapter<CountryListVo.RowsBean, BaseRecyclerHolder> {

    public CountryAdapter() {
        super(R.layout.item_country);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, CountryListVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.countryname);
        handlePinYin(helper, item);
    }

    /**
     * 处理分区标题
     * @param helper
     * @param item
     */
    private void handlePinYin(BaseRecyclerHolder helper, CountryListVo.RowsBean item) {
//        String word = item.getPinyin().substring(0, 1);
//        helper.setText(R.id.tv_section, word);
//        if (helper.getAdapterPosition() == 0) {
//            helper.setGone(R.id.tv_section, true);
//        } else {
//            //获取上一个位置字母
//            String preWord = mData.get(helper.getAdapterPosition() - 1).getPinyin().substring(0, 1);
//            if (preWord.equals(word)) {
//                helper.setGone(R.id.tv_section, false);
//            } else {
//                helper.setGone(R.id.tv_section, true);
//            }
//        }
    }

    /**
     *
     * @param data
     */
    public void operateData(List<CountryListVo.RowsBean> data){
        //排序
        Collections.sort(data, (lhs, rhs) -> lhs.getPinyin().compareTo(rhs.getPinyin()));
        setNewData(data);
    }

}
