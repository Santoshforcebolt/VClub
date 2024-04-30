package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.IntegralShopAdapter;
import com.easyder.club.module.me.vo.IntegralBalanceVo;
import com.easyder.club.module.me.vo.IntegralVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 15:35
 * Email: xcode126@126.com
 * Desc: 积分商城
 */
public class IntegralShopActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private IntegralShopAdapter shopAdapter;
    private int page, totalPage;
    private String integral;

    public static Intent getIntent(Context context, String integral) {
        return new Intent(context, IntegralShopActivity.class).putExtra("integral", integral);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_integral_shop));
        integral = intent.getStringExtra("integral");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        shopAdapter = new IntegralShopAdapter();
        mRecyclerView.setAdapter(shopAdapter);
        shopAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mNestedRefreshLayout.setOnRefreshListener(this);
        shopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntegralVo.RowsBean item = shopAdapter.getItem(position);
                integralExchangeDialog(item);
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getIntegralData(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getIntegralData(++page);
        } else {
            shopAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_SCORE_PRODUCT_LIST)) {
            processIntegralData((IntegralVo) dataVo);
        } else if (url.contains(ApiConfig.API_INTEGRAL_EXCHANGE)) {
            processExchangeSuccess((IntegralBalanceVo) dataVo);
        }
    }

    /**
     * 获取积分数据
     *
     * @param page
     */
    private void getIntegralData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SCORE_PRODUCT_LIST, new RequestParams()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), IntegralVo.class);
    }

    /**
     * 请求兑换
     *
     * @param number
     * @param scoreproductcode
     */
    private void requestExchange(int number, String scoreproductcode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_INTEGRAL_EXCHANGE, new RequestParams()
                .put("number", number)
                .put("scoreproductcode", scoreproductcode)
                .putCid()
                .get(), IntegralBalanceVo.class);
    }

    /**
     * 处理积分兑换成功
     *
     * @param dataVo
     */
    private void processExchangeSuccess(IntegralBalanceVo dataVo) {
        this.integral = dataVo.currentscore;
        showToast(getString(R.string.a_operate_success));
        EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_INTEGRAL_CHANGE));
        onRefresh();
    }

    /**
     * 处理积分数据
     *
     * @param dataVo
     */
    private void processIntegralData(IntegralVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                shopAdapter.getData().clear();
                shopAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                shopAdapter.notifyDataSetChanged();
            } else {
                shopAdapter.setNewData(dataVo.rows);
            }
            if (shopAdapter.getHeaderLayoutCount() > 0) {
                shopAdapter.removeAllHeaderView();
                shopAdapter.notifyDataSetChanged();
            }
            shopAdapter.setHeaderView(getHeaderView(integral));
            mNestedRefreshLayout.refreshFinish();
        } else {
            shopAdapter.addData(dataVo.rows);
            shopAdapter.loadMoreComplete();
        }
    }

    /**
     * 获取头部视图
     *
     * @return
     */
    private View getHeaderView(String integral) {
        return getHelperView(mRecyclerView, R.layout.header_integral, helper -> {
            helper.setText(R.id.tv_balance, integral);
            helper.setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.dtv_detail:
                        startActivity(MyIntegralActivity.getIntent(mActivity, integral));
                        break;
                    case R.id.dtv_history:
                        startActivity(IntegralExchangeActivity.getIntent(mActivity));
                        break;
                }
            }, R.id.dtv_detail, R.id.dtv_history);
        });
    }

    /**
     * 积分兑换礼品
     *
     * @param item
     */
    private void integralExchangeDialog(final IntegralVo.RowsBean item) {
        new WrapperDialog(mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_exchange_integral;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogParams(dialog, ViewGroup.LayoutParams.MATCH_PARENT, 650, Gravity.BOTTOM);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_need, String.valueOf(item.score));
                helper.setText(R.id.tv_can, integral);
                helper.setText(R.id.tv_name, item.scoreproductname);
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.btn_exchange:
                                int userAllScore = TextUtils.isEmpty(integral) ? 0 : Integer.parseInt(integral);
                                if (item.score > userAllScore) {
                                    showToast("积分余额不足");
                                    return;
                                }
                                requestExchange(1, item.scoreproductcode);
                                break;
                            case R.id.tv_look:
                                startActivity(ShopListActivity.getIntent(mActivity, item.partnercode));
                                break;
                        }
                        dismiss();
                    }
                }, R.id.btn_exchange, R.id.tv_look, R.id.iv_close);
            }
        }.show();
    }

}
