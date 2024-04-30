package com.easyder.club.module.collect;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.collect.adapter.CollectAdapter;
import com.easyder.club.module.collect.vo.CollectListVo;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 16:32
 * Email: xcode126@126.com
 * Desc: 收藏：使用一个界面
 */
@Deprecated
public class CollectFragment_v2 extends WrapperMvpFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.tv_wine)
    TextView tvWine;
    @BindView(R.id.tv_wine_bucket)
    TextView tvWineBucket;

    private CollectAdapter collectAdapter;
    private int page, totalPage, customercode;
    private String volumetype = "bottle";

    public static CollectFragment_v2 newInstance() {
        return new CollectFragment_v2();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_collect;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor("#F5F5F5").init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setAllSelected(tvWine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        collectAdapter = new CollectAdapter();
        mRecyclerView.setAdapter(collectAdapter);
//        collectAdapter.setHeaderAndEmpty(true);
        mNestedRefreshLayout.setOnRefreshListener(this);
        collectAdapter.setOnLoadMoreListener(this, mRecyclerView);
        collectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CollectListVo.RowsBean item = collectAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.dtv_expand:
                        item.isExpand = !item.isExpand;
                        collectAdapter.notifyItemChanged(position + 1);
                        break;
                    case R.id.ll_pack_up:
                        item.isExpand = false;
                        collectAdapter.notifyItemChanged(position + 1);
                        break;
                    case R.id.iv_num_edit:
                        editNumDialog(item);
                        break;
                    case R.id.iv_price_edit:
                        editPriceDialog(false, item);
                        break;
                    case R.id.iv_market_edit:
                        editPriceDialog(true, item);
                        break;
                    case R.id.iv_way_edit:
                        editWayDialog(item);
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        if (App.getCustomer() != null) {
            customercode = App.getCustomer().customercode;
        }
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getCollectList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getCollectList(++page);
        } else {
            collectAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_COLLECT_LIST)) {
            processCollectList((CollectListVo) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_COLLECT)) {
            onRefresh();
        }
    }

    /**
     * @param dataVo
     */
    private void processCollectList(CollectListVo dataVo) {
        CustomerBean customer = App.getCustomer();
        if (customer != null && customer.winestored == 0) {
            collectAdapter.getData().clear();
            collectAdapter.setEmptyView(getEmptyView(false));
            collectAdapter.notifyDataSetChanged();
            mNestedRefreshLayout.refreshFinish();
            return;
        }
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                collectAdapter.getData().clear();
                collectAdapter.setEmptyView(getEmptyView(true));
                collectAdapter.notifyDataSetChanged();
            } else {
                collectAdapter.setNewData(dataVo.rows);
            }
            if (collectAdapter.getHeaderLayoutCount() > 0) {
                collectAdapter.removeAllHeaderView();
                collectAdapter.notifyDataSetChanged();
            }
            collectAdapter.setHeaderView(getHeaderView(dataVo.totalnum, dataVo.totalmarketprice, dataVo.totalsaleprice));
            mNestedRefreshLayout.refreshFinish();
            tvWine.setText(String.format("%1$s(%2$s)", getString(R.string.a_whisky), dataVo.bottlecount));
            tvWineBucket.setText(String.format("%1$s(%2$s)", getString(R.string.a_whisky_wine_bucket), dataVo.bucketcount));
        } else {
            collectAdapter.addData(dataVo.rows);
            collectAdapter.loadMoreComplete();
        }
    }

    /**
     * get collect list
     *
     * @param page
     */
    private void getCollectList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_COLLECT_LIST, new RequestParams()
                .putCid()
                .put("volumetype", volumetype)
                .put("pictype", "product")
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), CollectListVo.class);
    }

    /**
     * @param id
     * @param marketprice
     * @param number
     * @param purchasedat
     * @param saleprice
     */
    private void changeCollect(int id, double marketprice, int number, String purchasedat, double saleprice) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_COLLECT, new RequestParams()
                .putCid()
                .put("id", id)
                .putWithoutEmpty("customercode", customercode)
                .putWithoutEmpty("marketprice", marketprice)
                .putWithoutEmpty("number", number)
                .putWithoutEmpty("purchasedat", purchasedat)
                .putWithoutEmpty("saleprice", saleprice)
                .get(), BaseVo.class);
    }

    /**
     * @param total
     * @param marketPrice
     * @param totalPrice
     * @return
     */
    private View getHeaderView(int total, double marketPrice, double totalPrice) {
        return getHelperView(mRecyclerView, R.layout.header_collect, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_total, String.valueOf(total));
                helper.setText(R.id.tv_market_total_price, String.format("%1$.2f", marketPrice));
                helper.setText(R.id.tv_total_price, String.format("%1$.2f", totalPrice));
            }
        });
    }

    /**
     * get empty view
     *
     * @return
     */
    private View getEmptyView(boolean isCanCollect) {
        return getHelperView(mRecyclerView, R.layout.common_empty, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                if (!isCanCollect) {
                    helper.setText(R.id.tv_tip, getString(R.string.a_collect_lever_lack_tip));
                }
            }
        });
    }

    /**
     * edit num dialog
     *
     * @param item
     */
    private void editNumDialog(CollectListVo.RowsBean item) {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_edit_num;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 586, 386, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.et_num, String.valueOf(item.number));
                helper.setOnClickListener(v -> {
                    EditText etNum = helper.getView(R.id.et_num);
                    String trim = etNum.getText().toString().trim();
                    int num = TextUtils.isEmpty(trim) ? 0 : Integer.parseInt(trim);
                    switch (v.getId()) {
                        case R.id.btn_reduce:
                            if (num <= 0) {
                                return;
                            }
                            num--;
                            etNum.setText(num + "");
                            break;
                        case R.id.btn_add:
                            num++;
                            etNum.setText(num + "");
                            break;
                        case R.id.btn_confirm:
                            changeCollect(item.id, item.marketprice, num, item.purchasedat, item.saleprice);
                            dismiss();
                            break;
                        case R.id.btn_cancel:
                        case R.id.iv_close:
                            dismiss();
                            break;
                    }
                }, R.id.btn_reduce, R.id.btn_add, R.id.btn_cancel, R.id.btn_confirm, R.id.iv_close);
            }
        }.show();
    }

    /**
     * edit price dialog
     *
     * @param isMarket
     * @param item
     */
    private void editPriceDialog(boolean isMarket, CollectListVo.RowsBean item) {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_edit_price;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 586, 386, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_title, getString(isMarket ? R.string.a_change_market_price : R.string.a_change_buy_price));
                helper.setHint(R.id.et_price, isMarket ? String.valueOf(item.marketprice) : String.valueOf(item.saleprice));
                helper.setOnClickListener(v -> {
                    EditText etPrice = helper.getView(R.id.et_price);
                    String trim = etPrice.getText().toString().trim();
                    double price = TextUtils.isEmpty(trim) ? 0 : Double.parseDouble(trim);
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            if (price <= 0) {
                                showToast(getString(R.string.a_input_change_price));
                                return;
                            }
                            if (isMarket) {
                                changeCollect(item.id, price, item.number, item.purchasedat, item.saleprice);
                            } else {
                                changeCollect(item.id, item.marketprice, item.number, item.purchasedat, price);
                            }
                            break;
                    }
                    dismiss();
                }, R.id.btn_cancel, R.id.btn_confirm, R.id.iv_close);
            }
        }.show();
    }

    /**
     * eidt way dialog
     *
     * @param item
     */
    private void editWayDialog(CollectListVo.RowsBean item) {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_edit_way;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 586, 386, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.et_content, item.purchasedat);
                EditText etContent = helper.getView(R.id.et_content);
                etContent.setSelection(etContent.getText().length());
                helper.setOnClickListener(v -> {

                    String content = etContent.getText().toString().trim();
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            if (TextUtils.isEmpty(content)) {
                                showToast(getString(R.string.a_input_buy_way));
                                return;
                            }
                            changeCollect(item.id, item.marketprice, item.number, content, item.saleprice);
                            break;
                    }
                    dismiss();
                }, R.id.btn_cancel, R.id.btn_confirm, R.id.iv_close);
            }
        }.show();
    }

    @OnClick({R.id.tv_wine, R.id.tv_wine_bucket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wine:
                volumetype = "bottle";
                setAllSelected(view);
                break;
            case R.id.tv_wine_bucket:
                volumetype = "bucket";
                setAllSelected(view);
                break;
        }
        onRefresh();
    }

    /**
     * set all select
     *
     * @param view
     */
    private void setAllSelected(View view) {
        tvWine.setSelected(view == tvWine);
        tvWineBucket.setSelected(view == tvWineBucket);
    }

}
