package com.easyder.club.module.me;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.home.MessageListActivity;
import com.easyder.club.module.me.order.RefundOrderActivity;
import com.easyder.club.module.me.order.ShopOrderActivity;
import com.easyder.club.module.me.order.StoreOrderActivity;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;


import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: sky on 2020/11/16 18:30
 * Email: xcode126@126.com
 * Desc:
 */
public class MeFragment extends WrapperMvpFragment<CommonPresenter> implements NestedRefreshLayout.OnRefreshListener {

    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_lever)
    TextView tvLever;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_ticket)
    TextView tvTicket;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_me;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor("#333945").init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNestedRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        if (App.getCustomer() != null) {
            handlePerson(App.getCustomer());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenter != null) {
            onRefresh();
        }
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_PERSON_CHANGE:
            case AccountIml.ACCOUNT_INTEGRAL_CHANGE:
            case AccountIml.ACCOUNT_WALLET_CHANGE:
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        getPersonCenter();
    }

    @OnClick({R.id.iv_header, R.id.ll_balance, R.id.ll_integral, R.id.ll_ticket, R.id.ll_shop_order,
            R.id.ll_store_order, R.id.ll_exit_money, R.id.ll_school, R.id.ll_appoint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                startActivity(PersonActivity.getIntent(_mActivity));
                break;
            case R.id.ll_balance://我的余额
                String balance = tvBalance.getText().toString().trim();
                startActivity(BalanceActivity.getIntent(_mActivity, balance));
                break;
            case R.id.ll_integral://积分商城
                String integral = tvIntegral.getText().toString().trim();
                startActivity(IntegralShopActivity.getIntent(_mActivity, integral));
                break;
            case R.id.ll_ticket://优惠券
                startActivity(MyTicketActivity.getIntent(_mActivity));
                break;
            case R.id.ll_shop_order://商城订单
                startActivity(ShopOrderActivity.getIntent(_mActivity));
                break;
            case R.id.ll_store_order://门店订单
                startActivity(StoreOrderActivity.getIntent(_mActivity));
                break;
            case R.id.ll_exit_money://售后单
                startActivity(RefundOrderActivity.getIntent(_mActivity));
                break;
            case R.id.ll_school:
                startActivity(VClubSchoolActivity.getIntent(_mActivity));
                break;
            case R.id.ll_appoint:
                startActivity(AppointActivity.getIntent(_mActivity));
                break;
        }
    }

    @OnClick({R.id.ll_code, R.id.iv_code, R.id.ll_spread, R.id.ll_feedback, R.id.ll_help, R.id.ll_set})
    public void onListViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_spread://我的推广
                startActivity(SpreadActivity.getIntent(_mActivity));
                break;
            case R.id.iv_code:
            case R.id.ll_code://邀请码
                startActivity(InviteActivity.getIntent(_mActivity));
                break;
            case R.id.ll_feedback://意见反馈
                startActivity(FeedbackActivity.getIntent(_mActivity));
                break;
            case R.id.ll_help://帮助中心
                startActivity(MessageListActivity.getIntent(_mActivity));
//                startActivity(AddressListActivity.getIntent(_mActivity, false));
                break;
            case R.id.ll_set://设置
                startActivity(SetActivity.getIntent(_mActivity));
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_PERSON_CENTER)) {
            processPersonData((CustomerBean) dataVo);
        }
    }

    /**
     * get person center
     */
    private void getPersonCenter() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_PERSON_CENTER, new RequestParams().putCid().get(), CustomerBean.class);
    }

    /**
     * process person data
     *
     * @param dataVo
     */
    private void processPersonData(CustomerBean dataVo) {
        App.setCustomerBean(dataVo);
        handlePerson(dataVo);
        mNestedRefreshLayout.refreshFinish();
    }

    /**
     * @param customer
     */
    private void handlePerson(CustomerBean customer) {
        if (customer != null) {
            ImageManager.load(_mActivity, ivHeader, customer.icourl, R.drawable.default_header, R.drawable.default_header);
            tvName.setText(customer.nickname);
            tvLever.setText(customer.gradename);
            tvBalance.setText(String.format("%1$.2f", customer.newbalance));
            tvIntegral.setText(String.format("%1$s", customer.score));
            tvTicket.setText(Html.fromHtml(String.format("<font color ='#F42122'>%1$s </font>%2$s", customer.couponcashnum, getString(R.string.a_unit_zhang))));
        }
    }

    @Override
    public void onDestroyView() {
        ImmersionBar.with(_mActivity).destroy(this);
        super.onDestroyView();
    }

}
