package com.easyder.club.module.shop;

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
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.PublishEnjoyActivity;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.module.shop.adapter.GoodsListAdapter;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/3 15:07
 * Email: xcode126@126.com
 * Desc:
 */
public class GoodsListActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.et_keyword)
    EditText etKeyword;
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
    @BindView(R.id.ll_good)
    LinearLayout llGood;

    @BindView(R.id.iv_price)
    ImageView ivPrice;
    @BindView(R.id.iv_sales)
    ImageView ivSales;
    @BindView(R.id.iv_good)
    ImageView ivGood;

    @BindView(R.id.tv_select_dot)
    TextView tvSelectDot;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;

    private GoodsListAdapter listAdapter;
    private int page, totalPage;
    private String productname, order, sort;
    private int brandcode, groupcode, maxprice, minprice;
    private OptionFragment optionFragment;

    public static Intent getIntent(Context context) {
        return getIntent(context, "");
    }

    public static Intent getIntent(Context context, String productname) {
        return new Intent(context, GoodsListActivity.class).putExtra("productname", productname);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_list_goods;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        ImmersionBar.with(mActivity).statusBarView(R.id.status_bar_view).statusBarColor(R.color.colorFore).init();
        productname = intent.getStringExtra("productname");
        etKeyword.setText(productname);
        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
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
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_GOODS_LIST)) {
            processList((GoodsListVo) dataVo);
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

    @OnClick({R.id.iv_left, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_search:
                productname = etKeyword.getText().toString().trim();
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
        }
    }

    /**
     * init adapter
     */
    private void initAdapter() {
        listAdapter = new GoodsListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnLoadMoreListener(this, mRecyclerView);
        listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            GoodsListVo.ProductsBean item = listAdapter.getItem(position);
            startActivity(PublishEnjoyActivity.getIntent(mActivity, item));
        });
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsListVo.ProductsBean item = listAdapter.getItem(position);
            startActivity(GoodsDetailActivity.getIntent(mActivity, item.productcode));
        });
    }

    @OnClick({R.id.tv_complex, R.id.ll_price, R.id.ll_sales, R.id.ll_good, R.id.rl_select})
    public void onSelectClicked(View view) {
        setAllSelected(view);
        switch (view.getId()) {
            case R.id.tv_complex:
                order = "";
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_price:
                sort = "saleprice";
                setOrder(ivPrice);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_sales:
                sort = "salenum";
                setOrder(ivSales);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.ll_good:
                sort = "totalscore";
                setOrder(ivGood);
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
            case R.id.rl_select:
                order = "";
                showOptionDialog();
                break;
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
        optionFragment.show(getFragmentManager(), "11");
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
        llGood.setSelected(llGood == view);
        rlSelect.setSelected(rlSelect == view);

        ivPrice.setBackgroundResource(R.drawable.expand_gray);
        ivSales.setBackgroundResource(R.drawable.expand_gray);
        ivGood.setBackgroundResource(R.drawable.expand_gray);

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
}
