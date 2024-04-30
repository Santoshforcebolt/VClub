package com.easyder.club.module.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CarPresenter;
import com.easyder.club.module.shop.adapter.GetTicketAdapter;
import com.easyder.club.module.shop.adapter.ShopCarAdapter;
import com.easyder.club.module.shop.vo.CalculateOrderVo;
import com.easyder.club.module.shop.vo.GetTicketVo;
import com.easyder.club.module.shop.vo.ShopCarVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 16:23
 * Email: xcode126@126.com
 * Desc: 购物车
 */
public class ShopCarActivity extends WrapperStatusActivity<CarPresenter>
        implements NestedRefreshLayout.OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.ll_all_select)
    LinearLayout llAllSelect;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.btn_order)
    Button btnOrder;

    private TitleView titleView;
    private ShopCarAdapter shopCarAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, ShopCarActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        setMultiTitle(titleView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        shopCarAdapter = new ShopCarAdapter();
        mRecyclerView.setAdapter(shopCarAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
//        shopCarAdapter.setOnLoadMoreListener(this, mRecyclerView);
        shopCarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopCarVo.RowsBean item = shopCarAdapter.getItem(position);
                startActivity(GoodsDetailActivity.getIntent(mActivity,item.itemcode));
            }
        });
        shopCarAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopCarVo.RowsBean item = shopCarAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.iv_select:
                        shopCarAdapter.toggleSelection(item);
                        if (!shopCarAdapter.isEdit()) {
                            updatePrice(true);
                        }
                        break;
                    case R.id.btn_add:
                        if (item.number >= item.availablestocknum) {
                            showToast(getString(R.string.a_no_repertory_tip));
                            return;
                        }
                        item.number++;
                        requestUpdateCar(item.id, item.number);
                        break;
                    case R.id.btn_reduce:
                        if (item.number <= 1) {
                            return;
                        }
                        item.number--;
                        requestUpdateCar(item.id, item.number);
                        break;
                    case R.id.fl_get_ticket:
                        GetTicketFragment.newInstance().show(getFragmentManager(),"GetTicketFragment");
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getCarData();
    }

    @OnClick({R.id.ll_all_select, R.id.btn_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_all_select:
                llAllSelect.setSelected(!llAllSelect.isSelected());
                shopCarAdapter.toggleAllSelection(llAllSelect.isSelected());
                if (!shopCarAdapter.isEdit()) {
                    updatePrice(true);
                }
                break;
            case R.id.btn_order:
                handleBtnOrder();
                break;
        }
    }

    @Subscribe
    public void OrderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_ORDER_COMMIT_SUCCESS:
                onRefresh();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_QUERY_CAR)) {
            processCarData((ShopCarVo) dataVo);
        } else if (url.contains(ApiConfig.API_UPDATE_CAR)) {
            processUpdateCar(dataVo);
        } else if (url.contains(ApiConfig.API_CALCULATE_PRICE)) {
            processCalculatePrice((CalculateOrderVo) dataVo);
        } else if (url.contains(ApiConfig.API_DELETE_CAR)) {
            processDeleteCar();
        }
    }

    /**
     * 确认领取优惠券
     *
     * @param couponcode
     */
    private void confirmGetCoupon(int couponcode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_MEMBER_GET_TICKET, new RequestParams()
                .put("couponcode", couponcode)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * get car data
     */
    private void getCarData() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_QUERY_CAR, new RequestParams()
                .putCid()
                .get(), ShopCarVo.class);
    }

    /**
     * @param id
     * @param number
     */
    private void requestUpdateCar(int id, int number) {
        presenter.setNeedDialog(true);
        presenter.updateCar(id, number);
    }

    /**
     * 删除购物车
     *
     * @param ids
     */
    private void requestDeleteCar(String ids) {
        presenter.setNeedDialog(true);
        presenter.deleteCar(ids);
    }

    /**
     * 计算价格
     *
     * @param orderJsonStr
     */
    private void calculatePrice(String orderJsonStr) {
        presenter.setNeedDialog(true);
        presenter.calculatePrice(orderJsonStr);
    }

    /**
     * @param dataVo
     */
    private void processCarData(ShopCarVo dataVo) {
        if (dataVo.rows != null && dataVo.rows.size() > 0) {
            shopCarAdapter.setNewData(dataVo.rows);
        } else {
            shopCarAdapter.getData().clear();
            shopCarAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
            shopCarAdapter.notifyDataSetChanged();
        }
        mNestedRefreshLayout.refreshFinish();
        llAllSelect.setSelected(false);
        updatePrice(false);
    }

    /**
     * 处理更新数据
     *
     * @param dataVo
     */
    private void processUpdateCar(BaseVo dataVo) {
        updatePrice(true);
        shopCarAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_CAR_CHANGE));
    }

    /**
     * process delete car
     */
    private void processDeleteCar() {
        EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_CAR_CHANGE));
        onRefresh();
    }

    /**
     * 处理计算价格后的信息
     *
     * @param dataVo
     */
    private void processCalculatePrice(CalculateOrderVo dataVo) {
        int totalNumber = 0;
        if (dataVo.detailedList != null && dataVo.detailedList.size() > 0) {
            for (CalculateOrderVo.DetailedListBean bean : dataVo.detailedList) {
                totalNumber = bean.number + totalNumber;
            }
        }
        updatePrice(dataVo.actualmoney, totalNumber, dataVo.discountmoney);
    }

    /**
     * 更新价格
     *
     * @param isCalculate 是否重新计算
     */
    private void updatePrice(boolean isCalculate) {
        if (isCalculate && shopCarAdapter.getSelectedCount() > 0) {
            calculatePrice(shopCarAdapter.getCalculateParam());
        } else {
            updatePrice(0, 0, 0);
        }
    }

    /**
     * 更新价格
     *
     * @param totalPrice
     */
    private void updatePrice(double totalPrice, int num, double discountPrice) {
        tvPrice.setText(String.format("%1$s%2$.2f", "£", totalPrice));
        String totalStr = String.format("%1$s%2$s%3$s%4$s%5$s", getString(R.string.a_total), num, getString(R.string.a_unit_jian), getString(R.string.a_goods), ",");
        String discount = String.format("%1$s%2$s%3$.2f", getString(R.string.a_yet_discount_), "£", discountPrice);
        tvDiscount.setText(String.format("%1$s%2$s", totalStr, discount));
    }

    /**
     * @param dataVo
     */
    private void getTicketDialog(GetTicketVo dataVo) {
        new WrapperDialog(mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_get_ticket;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogParams(dialog, WindowManager.LayoutParams.MATCH_PARENT,
                        AutoUtils.getPercentHeightSize(707), Gravity.BOTTOM);
            }

            @Override
            public void help(ViewHelper helper) {
                RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                GetTicketAdapter ticketAdapter = new GetTicketAdapter();
                mRecyclerView.setAdapter(ticketAdapter);
                if (dataVo.total <= 0) {
                    ticketAdapter.getData().clear();
                    ticketAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                    ticketAdapter.notifyDataSetChanged();
                }
                ticketAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    GetTicketVo.RowsBean item = ticketAdapter.getItem(position);
                    confirmGetCoupon(item.couponcode);
                    dismiss();
                });
                helper.setOnClickListener(R.id.iv_close, v -> dismiss());
            }
        }.show();
    }

    /**
     * set multi title
     *
     * @param titleView
     */
    private void setMultiTitle(TitleView titleView) {
        this.titleView = titleView;
        titleView.setTitle(getString(R.string.a_shop_car));
        titleView.setText(R.id.tv_title_right, getString(R.string.a_edit));
        final TextView rightTitle = titleView.getView(R.id.tv_title_right);
        titleView.setChildClickListener(R.id.tv_title_right, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!rightTitle.isSelected());
                setShopCarStatus(v.isSelected());
            }
        });
    }

    /**
     * 设置购物车状态
     *
     * @param isEdit
     */
    private void setShopCarStatus(boolean isEdit) {
        titleView.setText(R.id.tv_title_right, isEdit ? getString(R.string.a_complete) : getString(R.string.a_edit));
        shopCarAdapter.setEdit(isEdit);
        btnOrder.setText(isEdit ? getString(R.string.a_delete) : getString(R.string.a_now_order));
        llPrice.setVisibility(isEdit ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 处理下单和删除按钮
     */
    private void handleBtnOrder() {
        //未选中任何东西，不处理
        if (shopCarAdapter.getSelectedCount() <= 0) {
            llAllSelect.setSelected(false);
            return;
        }
        //编辑模式执行删除操作
        if (shopCarAdapter.isEdit()) {
            requestDeleteCar(shopCarAdapter.getIds());
            return;
        }
        //传入参数
        String jsonStr = shopCarAdapter.getCalculateParam();
        if (!TextUtils.isEmpty(jsonStr)) {
            startActivity(ConfirmOrderActivity.getIntent(mActivity,jsonStr));
        }
    }
}
