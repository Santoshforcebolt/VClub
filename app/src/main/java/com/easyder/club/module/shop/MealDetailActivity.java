package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.MainActivity;
import com.easyder.club.module.shop.vo.MealDetailVo;
import com.easyder.club.module.shop.vo.TempCalculateVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.joooonho.SelectableRoundedImageView;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/4 17:56
 * Email: xcode126@126.com
 * Desc: 套餐详情
 */
public class MealDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.iv_image)
    SelectableRoundedImageView ivImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_num)
    TextView tvNum;

    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;

    private int packagecode;
    private MealDetailVo detailVo;

    public static Intent getIntent(Context context, int packagecode) {
        return new Intent(context, MealDetailActivity.class).putExtra("packagecode", packagecode);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_meal_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_meal_detail));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        packagecode = intent.getIntExtra("packagecode", 0);
        getDetailData();
    }

    @Subscribe
    public void OrderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_ORDER_COMMIT_SUCCESS:
                finish();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_MEAL_DETAIL)) {
            processMealDetail((MealDetailVo) dataVo);
        }
    }

    /**
     * get data
     */
    private void getDetailData() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_MEAL_DETAIL, new RequestParams()
                .put("packagecode", packagecode)
                .putCid()
                .get(), MealDetailVo.class);
    }

    /**
     * process meal detail
     *
     * @param dataVo
     */
    private void processMealDetail(MealDetailVo dataVo) {
        this.detailVo = dataVo;
        tvPrice.setText(String.format("£%1$.2f", dataVo.price));
        tvSale.setText(String.format("%1$s%2$s", getString(R.string.a_sale), dataVo.salesvolume));
        tvDiscount.setText(String.format("£%1$.2f", dataVo.preferentialamount));
        tvName.setText(dataVo.packagename);
        ImageManager.load(mActivity, ivImage, OperateUtils.getFirstImage(dataVo.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);

        if (dataVo.packageGroupItems != null && dataVo.packageGroupItems.size() > 0) {
            layoutContainer.removeAllViews();
            for (int i = 0; i < dataVo.packageGroupItems.size(); i++) {
                layoutContainer.addView(getItemView(dataVo.packageGroupItems.get(i)));
            }
            tvNum.setText(String.format("%1$s%2$s", dataVo.packageGroupItems.size(), getString(R.string.a_goods)));
        } else {
            tvNum.setText(String.format("%1$s%2$s", 0, getString(R.string.a_goods)));
        }
    }

    @OnClick({R.id.ll_home, R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                startActivity(MainActivity.getResetIntent(mActivity));
                finish();
                break;
            case R.id.btn_buy:
                if (detailVo == null) {
                    showToast(getString(R.string.a_config_error_tip));
                    return;
                }
                startActivity(ConfirmOrderActivity.getIntent(mActivity, getPackageCalculateParam()));
                break;
        }
    }

    /**
     * get item view
     *
     * @param item
     * @return
     */
    private View getItemView(MealDetailVo.PackageGroupItemsBean item) {
        return getHelperView(layoutContainer, R.layout.item_meal_goods, helper -> {
            helper.setImageManager(mActivity, R.id.iv_image, OperateUtils.getFirstImage(item.imgurl),
                    R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, item.itemname);
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.saleprice));
            helper.setTextDelete(R.id.tv_one_price, String.format("%1$s%2$s%3$.2f", getString(R.string.a_one_goods), "£", item.price));
            helper.setVisible(R.id.tv_one_price, item.price <= item.saleprice);
            helper.setText(R.id.tv_num, String.format("%1$s%2$s", "x", item.quantity));
        });
    }

    /**
     * 购买组合套餐时 获取计算价格的参数信息
     *
     * @return
     */
    private String getPackageCalculateParam() {
        TempCalculateVo calculateVo = new TempCalculateVo();
        calculateVo.detailedList = new ArrayList<>();
        for (MealDetailVo.PackageGroupItemsBean bean : detailVo.packageGroupItems) {
            calculateVo.detailedList.add(new TempCalculateVo.DetailedListBean(bean.itemtype, bean.itemcode, bean.quantity));
        }
        calculateVo.selectedPackageGroup = new ArrayList<>();
        calculateVo.selectedPackageGroup.add(packagecode);
        return JSONObject.toJSONString(calculateVo);
    }
}
