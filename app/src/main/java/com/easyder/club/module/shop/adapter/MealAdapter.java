package com.easyder.club.module.shop.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.shop.vo.MealVo;
import com.easyder.club.utils.OperateUtils;
import com.joooonho.SelectableRoundedImageView;
import com.sky.widget.autolayout.utils.AutoUtils;


/**
 * Author: sky on 2020/12/4 17:18
 * Email: xcode126@126.com
 * Desc:
 */
public class MealAdapter extends BaseQuickAdapter<MealVo.RowsBean, BaseRecyclerHolder> {

    public MealAdapter() {
        super(R.layout.item_meal);
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, MealVo.RowsBean item) {
        helper.setText(R.id.tv_name, item.packagename);
        helper.setText(R.id.tv_price, String.format("£%1$.2f", item.price));
        helper.setText(R.id.tv_sale, String.format("%1$s%2$s", mContext.getString(R.string.a_sale), item.salesvolume));
        helper.setText(R.id.tv_discount, String.format("£%1$.2f", item.preferentialamount));

        LinearLayout layoutPicture = helper.getView(R.id.layout_picture);
        if (item.packageGroupItems != null && item.packageGroupItems.size() > 0) {
            layoutPicture.removeAllViews();
            for (int i = 0; i < item.packageGroupItems.size(); i++) {
                addImageItem(helper, layoutPicture, item.packageGroupItems.get(i));
            }
            helper.setText(R.id.dtv_num, String.format("%1$s%2$s", item.packageGroupItems.size(), mContext.getString(R.string.a_goods)));
        } else {
            helper.setText(R.id.dtv_num, String.format("%1$s%2$s", 0, mContext.getString(R.string.a_goods)));
        }
    }

    /**
     * 添加图片Item项
     *
     * @param helper
     * @param container
     * @param bean
     */
    private void addImageItem(BaseRecyclerHolder helper, LinearLayout container, MealVo.RowsBean.PackageGroupItemsBean bean) {
        ImageView imageView = getImageView(140, 140);
        helper.setImageManager(mContext, imageView, OperateUtils.getFirstImage(bean.imgurl),
                R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2);
        container.addView(imageView);
    }

    /**
     * 获取一个ImageView
     *
     * @param width
     * @param height
     * @return
     */
    private SelectableRoundedImageView getImageView(int width, int height) {
        SelectableRoundedImageView imageView = new SelectableRoundedImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOval(false);
        imageView.setCornerRadiiDP(5, 5, 5, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(width),
                AutoUtils.getPercentWidthSize(height));
        layoutParams.rightMargin = AutoUtils.getPercentWidthSize(25);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }
}
