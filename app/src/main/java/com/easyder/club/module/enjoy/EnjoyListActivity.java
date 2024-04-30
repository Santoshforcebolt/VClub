package com.easyder.club.module.enjoy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.easyder.club.module.enjoy.adapter.EnjoyListAdapter;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.module.shop.OptionFragment;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;


import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: sky on 2020/11/16 18:15
 * Email: xcode126@126.com
 * Desc:
 */
public class EnjoyListActivity extends WrapperSwipeActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    @BindView(R.id.tv_complex)
    TextView tvComplex;
    @BindView(R.id.iv_volume)
    ImageView ivVolume;
    @BindView(R.id.iv_year)
    ImageView ivYear;
    @BindView(R.id.ll_volume)
    LinearLayout llVolume;
    @BindView(R.id.ll_year)
    LinearLayout llYear;

    @BindView(R.id.tv_select_dot)
    TextView tvSelectDot;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;

    private EnjoyListAdapter listAdapter;
    private int page, totalPage;
    private String productname, order, sort;
    private int brandcode, groupcode, maxprice, minprice;
    private OptionFragment optionFragment;

    public static Intent getIntent(Context context) {
        return new Intent(context, EnjoyListActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_list_enjoy;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        ImmersionBar.with(mActivity).statusBarView(R.id.status_bar_view).statusBarColor(R.color.colorFore).init();

        listAdapter = new EnjoyListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, mRecyclerView);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListVo.ProductsBean item = listAdapter.getItem(position);
                startActivity(PublishEnjoyActivity.getIntent(mActivity, item));
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_PUBLISH_ENJOY:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        getList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getList(++page);
        } else {
            listAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GOODS_LIST)) {
            processList((GoodsListVo) dataVo);
        }
    }

    /**
     * @param dataVo
     */
    private void processList(GoodsListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                listAdapter.getData().clear();
                listAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                listAdapter.notifyDataSetChanged();
            } else {
                listAdapter.setNewData(dataVo.products);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            listAdapter.addData(dataVo.products);
            listAdapter.loadMoreComplete();
        }

    }

    /**
     * get list
     *
     * @param page
     */
    private void getList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GOODS_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putWithoutEmpty("productname", productname)
                .putWithoutEmpty("maxprice", maxprice)
                .putWithoutEmpty("minprice", minprice)
                .putWithoutEmpty("brandcode", brandcode)
                .putWithoutEmpty("groupcode", groupcode)
                .putWithoutEmpty("order", order)
                .putWithoutEmpty("sort", sort)
                .get(), GoodsListVo.class);
    }

    @OnClick({R.id.iv_left, R.id.tv_search, R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_search:
                productname = etKeyword.getText().toString().trim();
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.tv_create:
                startActivity(PublishEnjoyActivity.getIntent(mActivity));
                finish();
                break;
        }
    }

    @OnClick({R.id.tv_complex, R.id.ll_volume, R.id.ll_year, R.id.rl_select})
    public void onSelectClicked(View view) {
        setAllSelected(view);
        switch (view.getId()) {
            case R.id.tv_complex:
                order = "";
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_volume:
                sort = "capacity";
                setOrder(ivVolume);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_year:
                sort = "bottled";
                setOrder(ivYear);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.rl_select:
                order = "";
                showOptionDialog();
                break;
        }
    }

    /**
     * set all selected
     *
     * @param view
     */
    private void setAllSelected(View view) {
        tvComplex.setSelected(tvComplex == view);
        llVolume.setSelected(llVolume == view);
        llYear.setSelected(llYear == view);
        rlSelect.setSelected(rlSelect == view);
        ivVolume.setBackgroundResource(R.drawable.expand_gray);
        ivYear.setBackgroundResource(R.drawable.expand_gray);

        minprice = 0;
        maxprice = 0;
        brandcode = 0;
        groupcode = 0;
        sort = "";
    }

    /**
     * set asc
     *
     * @param view
     */
    private void setOrder(View view) {
        if (TextUtils.isEmpty(order)) {
            order = "asc";//升序
            view.setBackgroundResource(R.drawable.expand_bottom_gray);
            return;
        }
        if (order.equals("desc")) {
            order = "asc";//升序
            view.setBackgroundResource(R.drawable.expand_bottom_gray);
            return;
        }
        if (order.equals("asc")) {
            order = "desc";//降序
            view.setBackgroundResource(R.drawable.expand_top_gray);
            return;
        }
    }

    /**
     * option dialog
     */
    private void showOptionDialog() {
        if (optionFragment == null) {
            optionFragment = OptionFragment.newInstance();
            optionFragment.setOptionResultCallback((tempMinPrice, tempMaxPrice, temKeyword, tempGroupCode, tempBrandCode) -> {
                if (tempMinPrice > 0 || tempMaxPrice > 0 || !TextUtils.isEmpty(temKeyword) || tempGroupCode > 0 || tempBrandCode > 0) {
                    tvSelectDot.setVisibility(View.VISIBLE);
                } else {
                    tvSelectDot.setVisibility(View.INVISIBLE);
                }
                minprice = tempMinPrice;
                maxprice = tempMaxPrice;
                productname = temKeyword;
                groupcode = tempGroupCode;
                brandcode = tempBrandCode;
                sort = "";
                order = "";
                etKeyword.setText(temKeyword);
                mNestedRefreshLayout.froceRefreshToState(true);
            });
        }
        optionFragment.show(getFragmentManager(), "OptionFragment");
    }

}
