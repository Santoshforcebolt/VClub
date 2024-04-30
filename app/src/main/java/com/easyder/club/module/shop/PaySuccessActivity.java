package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.MainActivity;
import com.easyder.club.module.me.order.ShopOrderDetailActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 11:07
 * Email: xcode126@126.com
 * Desc: 支付成功
 */
public class PaySuccessActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;

    private String orderNo;

    public static Intent getIntent(Context context, String orderNo, double amount, int integral) {
        return new Intent(context, PaySuccessActivity.class)
                .putExtra("orderNo", orderNo)
                .putExtra("amount", amount)
                .putExtra("integral", integral);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setBackgroundColor(Color.parseColor("#2C3340"));
        layoutTitle.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        ImmersionBar.with(mActivity).statusBarColor("#2C3340").init();

        orderNo = intent.getStringExtra("orderNo");
        double amount = intent.getDoubleExtra("amount", 0);
        int integral = intent.getIntExtra("integral", 0);
        tvAmount.setText(String.format("%1$s%2$.2f","£", amount));
        tvIntegral.setText(String.format("%1$s", integral));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    @OnClick({R.id.btn_detail, R.id.btn_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_detail:
                startActivity(ShopOrderDetailActivity.getIntent(mActivity, orderNo));
                break;
            case R.id.btn_home:
                startActivity(MainActivity.getResetIntent(mActivity));
                break;
        }
        finish();
    }
}
