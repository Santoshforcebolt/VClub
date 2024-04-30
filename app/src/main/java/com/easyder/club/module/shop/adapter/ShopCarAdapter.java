package com.easyder.club.module.shop.adapter;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.ShopCarVo;
import com.easyder.club.module.shop.vo.TempCalculateVo;
import com.sky.wrapper.base.adapter.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sky on 2020/11/26 18:19
 * Email: xcode126@126.com
 * Desc:
 */
public class ShopCarAdapter extends BaseQuickAdapter<ShopCarVo.RowsBean, BaseRecyclerHolder> implements Selectable<ShopCarVo.RowsBean> {

    private boolean isEdit;
    private List<ShopCarVo.RowsBean> sectionList;

    public ShopCarAdapter() {
        super(R.layout.item_shop_car);
        sectionList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, ShopCarVo.RowsBean item) {
        helper.setGone(R.id.fl_get_ticket, mData.indexOf(item) == 0);
        helper.getView(R.id.iv_select).setSelected(isSelected(item));
        helper.addOnClickListener(R.id.btn_reduce);
        helper.addOnClickListener(R.id.btn_add);
        helper.addOnClickListener(R.id.iv_select);
        helper.addOnClickListener(R.id.fl_get_ticket);

        helper.setVisible(R.id.ll_operate, isEdit ? false : true);
        helper.setImageManager(mContext, R.id.iv_image, item.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_name, item.itemname);
        helper.setText(R.id.tv_num, item.number + "");
        helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));
    }

    /**
     * 批量操作
     *
     * @param isSelect
     */
    public void toggleAllSelection(boolean isSelect) {
        if (isSelect) {
            AddSelection();
        } else {
            clearSelection();
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean isSelected(ShopCarVo.RowsBean item) {
        return sectionList.contains(item);
    }

    @Override
    public void toggleSelection(ShopCarVo.RowsBean item) {
        if (isSelected(item)) {
            sectionList.remove(item);
        } else {
            sectionList.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clearSelection() {
        sectionList.clear();
    }

    /**
     * 批量选中所有
     */
    public void AddSelection() {
        clearSelection();
        sectionList.addAll(mData);
    }

    @Override
    public int getSelectedCount() {
        return sectionList.size();
    }

    @Override
    public List<ShopCarVo.RowsBean> getSelection() {
        return sectionList;
    }

    /**
     * 设置编辑状态
     *
     * @param edit
     */
    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    /**
     * is edit
     *
     * @return
     */
    public boolean isEdit() {
        return isEdit;
    }

    /**
     * 获取计算价格的参数
     *
     * @return
     */
    public String getCalculateParam() {
        TempCalculateVo calculateVo = new TempCalculateVo();
        calculateVo.detailedList = new ArrayList<>();
        for (ShopCarVo.RowsBean bean : sectionList) {
            calculateVo.detailedList.add(new TempCalculateVo.DetailedListBean(bean.itemcode, bean.number));
        }
        return JSONObject.toJSONString(calculateVo);
    }

    /**
     * 获取选中商品的id
     * @return
     */
    public String getIds() {
        StringBuilder sb = new StringBuilder();
        for (ShopCarVo.RowsBean bean : sectionList) {
            sb.append(bean.id);
            sb.append(",");
        }
        return sb.toString().trim();
    }

}
