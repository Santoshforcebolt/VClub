package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.vo.SpreadVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/18 16:11
 * Email: xcode126@126.com
 * Desc: 推广收益
 */
public class SpreadActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.tv_month_earnings)
    TextView tvMonthEarnings;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_can)
    TextView tvCan;
    @BindView(R.id.tv_yet)
    TextView tvYet;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.tv_order)
    TextView tvOrder;

    public static Intent getIntent(Context context) {
        return new Intent(context, SpreadActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_spread;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_spread_earnings));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getSpreadData();
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_INTEGRAL_CHANGE:
                getSpreadData();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SPREAD)) {
            processSpread((SpreadVo) dataVo);
        }
    }

    @OnClick({R.id.tv_extract, R.id.tv_member_detail, R.id.tv_order_detail, R.id.fl_invite,
            R.id.ll_total_earnings,R.id.ll_yet_extract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_extract://提取收益
                startActivity(ExtractEarningsActivity.getIntent(mActivity));
                break;
            case R.id.tv_member_detail://查看推广会员明细
                startActivity(SpreadHistoryActivity.getIntent(mActivity));
                break;
            case R.id.tv_order_detail://查看推广订单明细
                startActivity(SpreadOrderActivity.getIntent(mActivity));
                break;
            case R.id.fl_invite://立即邀请
                startActivity(InviteActivity.getIntent(mActivity));
                break;
            case R.id.ll_total_earnings://累计收益
                startActivity(TotalEarningsActivity.getIntent(mActivity));
                break;
            case R.id.ll_yet_extract://已提取收益
                startActivity(YetEarningsActivity.getIntent(mActivity));
                break;
        }
    }

    /**
     * get spread data
     */
    private void getSpreadData() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SPREAD, new RequestParams().putCid().get(), SpreadVo.class);
    }

    /**
     * process spread
     *
     * @param dataVo
     */
    private void processSpread(SpreadVo dataVo) {
        tvMonthEarnings.setText(String.format("%1$.2f", dataVo.monthCommis));
        tvTotal.setText(String.format("%1$.2f", dataVo.accumulativeCommis));
        tvCan.setText(String.format("%1$.2f", dataVo.extractable));
        tvYet.setText(String.format("%1$.2f", dataVo.haveextracted));
        tvMember.setText(dataVo.customercount);
        tvOrder.setText(dataVo.total);
    }

}
