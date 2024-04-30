package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.BalanceAdapter;
import com.easyder.club.module.me.vo.BalanceVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.widget.DateSelector2;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 13:52
 * Email: xcode126@126.com
 * Desc: 我的余额
 */
public class BalanceActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private BalanceAdapter balanceAdapter;
    private int page, totalPage;
    private String sdate;

    public static Intent getIntent(Context context, String balance) {
        return new Intent(context, BalanceActivity.class).putExtra("balance", balance);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_balance;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_my_balance));

        tvBalance.setText(intent.getStringExtra("balance"));
        balanceAdapter = new BalanceAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(balanceAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        balanceAdapter.setOnLoadMoreListener(this, mRecyclerView);
        sdate = CommonTools.getFormatTime("yyyy-MM", CommonTools.getCurrentTime());
        tvDate.setText(sdate);
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_WALLET_CHANGE:
                finish();
                break;
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getBalanceList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getBalanceList(++page);
        } else {
            balanceAdapter.loadMoreEnd();
        }
    }

    @OnClick({R.id.tv_recharge, R.id.iv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge:
                startActivity(RechargeActivity.getIntent(mActivity));
                break;
            case R.id.iv_date:
                showDatePicker();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_BALANCE_LIST)) {
            processBalance((BalanceVo) dataVo);
        }
    }

    /**
     * get balance list
     *
     * @param page
     */
    private void getBalanceList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_BALANCE_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("sdate", String.format("%1$s%2$s",sdate,"-01"))
                .put("edate", String.format("%1$s%2$s",sdate,"-31"))
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), BalanceVo.class);
    }

    /**
     * @param dataVo
     */
    private void processBalance(BalanceVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total == 0) {
                balanceAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                balanceAdapter.getData().clear();
                balanceAdapter.notifyDataSetChanged();
            } else {
                balanceAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            balanceAdapter.addData(dataVo.rows);
            balanceAdapter.loadMoreComplete();
        }
    }

    /**
     * 展示日期选择
     */
    private void showDatePicker() {
        new DateSelector2(mActivity).addHelperAbsCallback((wrapper, dialog, helper)
                -> helper.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    sdate = ((DateSelector2) wrapper).getSelectedDate();
                    tvDate.setText(sdate);
                    onRefresh();
                    break;
            }
            dialog.dismiss();
        }, R.id.tv_cancel, R.id.tv_confirm)).show();
    }


}
