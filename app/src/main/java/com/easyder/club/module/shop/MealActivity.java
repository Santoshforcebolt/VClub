package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.adapter.MealAdapter;
import com.easyder.club.module.shop.vo.MealVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/4 17:02
 * Email: xcode126@126.com
 * Desc: 全部套餐
 */
public class MealActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    @BindView(R.id.tv_complex)
    TextView tvComplex;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.ll_sales)
    LinearLayout llSales;
    @BindView(R.id.iv_price)
    ImageView ivPrice;
    @BindView(R.id.iv_sales)
    ImageView ivSales;
    @BindView(R.id.tv_select_dot)
    TextView tvSelectDot;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;

    private int page, totalPage;
    private MealAdapter mealAdapter;
    private String packagename, order, sort;
    private int endPrice, beginPrice;
    private MealOptionFragment optionFragment;

    public static Intent getIntent(Context context) {
        return new Intent(context, MealActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_meal;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_all_meal));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mealAdapter = new MealAdapter();
        mRecyclerView.setAdapter(mealAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        mealAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mealAdapter.setOnItemClickListener((adapter, view, position) -> {
            MealVo.RowsBean item = mealAdapter.getItem(position);
            startActivity(MealDetailActivity.getIntent(mActivity, item.packagecode));
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getMealList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getMealList(++page);
        } else {
            mealAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_MEAL_LIST)) {
            processMealList((MealVo) dataVo);
        }
    }

    /**
     * @param page
     */
    private void getMealList(int page) {
        getMealList(page, false);
    }

    /**
     * @param page
     * @param isNeedDialog
     */
    private void getMealList(int page, boolean isNeedDialog) {
        presenter.setNeedDialog(isNeedDialog);
        presenter.postData(ApiConfig.API_MEAL_LIST, new RequestParams().putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putWithoutEmpty("packagename", packagename)
                .putWithoutEmpty("endPrice", endPrice)
                .putWithoutEmpty("beginPrice", beginPrice)
//                .putWithoutEmpty("brandcode", brandcode)
//                .putWithoutEmpty("groupcode", groupcode)
                .putWithoutEmpty("order", order)
                .putWithoutEmpty("sort", sort)
                .get(), MealVo.class);
    }

    /**
     * process meal list
     *
     * @param dataVo
     */
    private void processMealList(MealVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                mealAdapter.getData().clear();
                mealAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                mealAdapter.notifyDataSetChanged();
            } else {
                mealAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            mealAdapter.addData(dataVo.rows);
            mealAdapter.loadMoreComplete();
        }
    }

    @OnClick({R.id.tv_complex, R.id.ll_price, R.id.ll_sales, R.id.rl_select})
    public void onSelectClicked(View view) {
        setAllSelected(view);
        switch (view.getId()) {
            case R.id.tv_complex:
                order = "";
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_price:
                sort = "price";
                setOrder(ivPrice);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_sales:
                sort = "salesvolume";
                setOrder(ivSales);
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
        llPrice.setSelected(llPrice == view);
        llSales.setSelected(llSales == view);
//        llGood.setSelected(llGood == view);
        rlSelect.setSelected(rlSelect == view);

        ivPrice.setBackgroundResource(R.drawable.expand_gray);
        ivSales.setBackgroundResource(R.drawable.expand_gray);
//        ivGood.setBackgroundResource(R.drawable.expand_gray);

        beginPrice = 0;
        endPrice = 0;
//        brandcode = 0;
//        groupcode = 0;
        sort = "";
        packagename="";
    }

    /**
     * set asc
     *
     * @param view
     */
    private void setOrder(View view) {
        if (TextUtils.isEmpty(order)) {
            order = "ASC";//升序
            view.setBackgroundResource(R.drawable.expand_bottom_gray);
            return;
        }
        if (order.equals("desc")) {
            order = "ASC";//升序
            view.setBackgroundResource(R.drawable.expand_bottom_gray);
            return;
        }
        if (order.equals("ASC")) {
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
            optionFragment = MealOptionFragment.newInstance();
            optionFragment.setOptionResultCallback((tempMinPrice, tempMaxPrice, temKeyword) -> {
                if (tempMinPrice > 0 || tempMaxPrice > 0 || !TextUtils.isEmpty(temKeyword)) {
                    tvSelectDot.setVisibility(View.VISIBLE);
                } else {
                    tvSelectDot.setVisibility(View.INVISIBLE);
                }
                beginPrice = tempMinPrice;
                endPrice = tempMaxPrice;
                packagename = temKeyword;
//                groupcode = tempGroupCode;
//                brandcode = tempBrandCode;
                sort = "";
                order = "";
//                etKeyword.setText(temKeyword);
                mNestedRefreshLayout.froceRefreshToState(true);
            });
        }
        optionFragment.show(getFragmentManager(), "11");
    }

}
