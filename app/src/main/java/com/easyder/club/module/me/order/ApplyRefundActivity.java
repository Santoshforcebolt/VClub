package com.easyder.club.module.me.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.adapter.GridImageAdapter;
import com.easyder.club.module.enjoy.vo.GridImageVo;
import com.easyder.club.module.enjoy.vo.UploadVo;
import com.easyder.club.module.me.vo.AfterSaleCalculatorVo;
import com.easyder.club.module.me.vo.OrderDetailVo;
import com.easyder.club.module.me.vo.TempAfterSaleBean;
import com.easyder.club.module.me.vo.TempApplyBean;
import com.easyder.club.module.shop.vo.OrderNoVo;
import com.easyder.club.utils.RequestParams;
import com.luck.picture.lib.entity.LocalMedia;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperPickerActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: sky on 2020/12/9 15:08
 * Email: xcode126@126.com
 * Desc:  申请售后
 */
public class ApplyRefundActivity extends WrapperPickerActivity<CommonPresenter> {

    @BindView(R.id.layout_goods)
    LinearLayout layoutGoods;
    @BindView(R.id.tv_exit_money)
    TextView tvExitMoney;
    @BindView(R.id.tv_exit_all)
    TextView tvExitAll;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_reason)
    EditText etReason;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private GridImageAdapter imageAdapter;
    private TempApplyBean applyBean;
    private String applyOrderNo;
    private double actualmoney;

    public static Intent getIntent(Context context, TempApplyBean applyBean) {
        return new Intent(context, ApplyRefundActivity.class).putExtra("applyBean", applyBean);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_apply_refund;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_apply_after_sale));
        applyBean = (TempApplyBean) intent.getSerializableExtra("applyBean");
        initPictureAdapter();
    }

    @Override
    public void obtainMediaResult(List<LocalMedia> list, int type) {
        if (list != null && list.size() > 0) {
            LocalMedia localMedia = list.get(0);
            uploadFile(localMedia.getCompressPath());
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getAfterSaleOrderNo();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_UPLOAD_PICTURE)) {
            processUploadPicture((UploadVo) dataVo);
        } else if (url.contains(ApiConfig.API_AFTER_SALE_PRICE)) {
            processCalculatorPrice((AfterSaleCalculatorVo) dataVo);
        } else if (url.contains(ApiConfig.API_GET_APPLY_ORDER_NO)) {
            processOrderNo((OrderNoVo) dataVo);
        } else if (url.contains(ApiConfig.API_APPLY_AFTER_SALE)) {
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_APPLY_AFTER_SALE_SUCCESS));
            finish();
        }
    }

    /**
     * upload file
     *
     * @param path
     */
    private void uploadFile(String path) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_UPLOAD_PICTURE, new RequestParams()
                .put("image", new File(path))
                .putCid()
                .get(), UploadVo.class);
    }

    /**
     * 生成售后订单号
     */
    private void getAfterSaleOrderNo() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_APPLY_ORDER_NO, new RequestParams()
                .putCid()
                .get(), OrderNoVo.class);
    }

    /**
     * calculator price
     */
    private void calculatorPrice() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_AFTER_SALE_PRICE, new RequestParams()
                .putWithoutEmpty("piDetailed", getCalculatorParam())
                .put("onlineOrderno", applyBean.orderno)
                .putWithoutEmpty("aftersalestype", getSelectType())
                .putCid()
                .get(), AfterSaleCalculatorVo.class);
    }

    /**
     * 提交售后
     */
    private void applyAfterSale(int aftersalestype, double calculationPrice) {
        presenter.postData(ApiConfig.API_APPLY_AFTER_SALE, new RequestParams()
                .putCid()
                .putWithoutEmpty("onlineorderno", applyBean.orderno)
                .putWithoutEmpty("actualmoney", calculationPrice)
                .putWithoutEmpty("aftersalestype", aftersalestype)
                .putWithoutEmpty("orderno", applyOrderNo)
                .put("reason", etReason.getText().toString().trim())
//                .putWithoutEmpty("orderDetailId", orderDetailId)
                .putWithoutEmpty("customercode", App.getCustomer().customercode)
                .putWithoutEmpty("customername", App.getCustomer().customername)
                .putWithoutEmpty("imgurl", imageAdapter.getSelectPath())
                .putWithoutEmpty("piDetailed", getCalculatorParam())
                .get(), BaseVo.class);
    }

    /**
     * @param uploadVo
     */
    private void processUploadPicture(UploadVo uploadVo) {
        imageAdapter.insertPicture(uploadVo);
    }

    /**
     * @param noVo
     */
    private void processOrderNo(OrderNoVo noVo) {
        this.applyOrderNo = noVo.orderno;
        if (applyBean == null) {
            showToast(getString(R.string.a_config_error_tip));
            return;
        }
        handleGoods(true);
        handleTypeShow(applyBean.orderstate);
    }

    /**
     * @param dataVo
     */
    private void processCalculatorPrice(AfterSaleCalculatorVo dataVo) {
        this.actualmoney = dataVo.actualmoney;
        if (getSelectType() == AppConfig.REFUND_CHANGE_GOODS) {
            etAmount.setText(String.format("%1$.2f", 0.00));
        } else {
            etAmount.setText(String.format("%1$.2f", dataVo.actualmoney));
        }
        handleGoods(false);
    }

    @OnClick({R.id.tv_exit_money, R.id.tv_exit_all, R.id.tv_change, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit_money:
            case R.id.tv_exit_all:
            case R.id.tv_change:
                setAllSelect(view);
                calculatorPrice();
                break;
            case R.id.btn_apply:
                int selectType = getSelectType();
                if (selectType == -1) {
                    showToast(getString(R.string.a_please_choose_sale_after_type));
                    return;
                }
                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    showToast(getString(R.string.a_exit_amount_is_no_empty));
                    return;
                }
                applyAfterSale(selectType, actualmoney);
                break;
        }
    }

    /**
     * init picture adapter
     */
    private void initPictureAdapter() {
        List<GridImageVo> list = new ArrayList<>();
        list.add(new GridImageVo(true, ""));
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        imageAdapter = new GridImageAdapter(list, 5);
        mRecyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (imageAdapter.isCanAdd(position) && imageAdapter.canAddMax() > 0) {
                showPictureSelector(imageAdapter.canAddMax(), true, true, 0x02);
            }
        });
        imageAdapter.setOnItemChildClickListener((adapter, view, position) -> imageAdapter.deletePicture(position));
    }

    /**
     * handle goods
     */
    private void handleGoods(boolean isInitial) {
        layoutGoods.removeAllViews();
        if (applyBean.detailedList != null && applyBean.detailedList.size() > 0) {
            for (OrderDetailVo.DetailedListBean bean : applyBean.detailedList) {
                layoutGoods.addView(getGoodsView(layoutGoods, bean,isInitial));
            }
        }
    }

    /**
     * get goods view
     *
     * @param layoutGoods
     * @param item
     * @return
     */
    private View getGoodsView(LinearLayout layoutGoods, OrderDetailVo.DetailedListBean item,boolean isInitial) {
        return getHelperView(layoutGoods, R.layout.item_goods_order, helper -> {
            helper.setImageManager(mActivity, R.id.iv_image, item.previewimg, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, item.piname);
            helper.setText(R.id.tv_num, String.format("%1$s%2$s", "x", item.number));
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));

            helper.setViewGone(R.id.btn_apply);
            helper.setVisible(R.id.ll_operate, (getSelectType() != AppConfig.REFUND_EXIT_MONEY)
                    && (getSelectType() != -1));//仅退款不能修改数量
            if (isInitial){
                item.applyNumber = item.number;
            }
            helper.setText(R.id.tv_number, String.valueOf(item.applyNumber));
            helper.setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.btn_reduce:
                        if (item.applyNumber <= 1) {
                            return;
                        }
                        item.applyNumber--;
                        calculatorPrice();
                        break;
                    case R.id.btn_add:
                        if (item.applyNumber >= item.number) {
                            return;
                        }
                        item.applyNumber++;
                        calculatorPrice();
                        break;
                }
                helper.setText(R.id.tv_number, String.valueOf(item.applyNumber));
            }, R.id.btn_reduce, R.id.btn_add);
        });
    }

    /**
     * 2020/12/10 15:51:46
     * 商城订单状态：@iOS-许鸿桂 @android-史燕坤
     * 待支付：取消订单、支付
     * 待发货：申请售后（售后选项只有仅退款，且仅可整单申请售后）
     * 待收货：确认收货
     * 已取消：无按钮
     * 已完成：申请售后（售后选项有：退货退款、换货，可整单且未售后的商品可单独申请）
     * 支付后又取消的按钮：无按钮
     * 产品已退款：无按钮（若还有其他产品未售后的，可以单独选择申请售后）
     * 部分产品已退款：申请售后（售后选项有：退货退款、换货）2020年12月19日新增
     * handle type show
     */
    private void handleTypeShow(int orderstate) {
        if (orderstate == ShopOrderFragment.ORDER_STATE_WAIT_SEND) {
            tvExitMoney.setVisibility(View.VISIBLE);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_WAIT_RECEIVE) {
            tvExitMoney.setVisibility(View.VISIBLE);
            tvExitAll.setVisibility(View.VISIBLE);
            tvChange.setVisibility(View.VISIBLE);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_YET_COMPLETE ||
                orderstate == ShopOrderFragment.ORDER_STATE_YET_HALF_EXIT) {
            tvExitAll.setVisibility(View.VISIBLE);
            tvChange.setVisibility(View.VISIBLE);
        } else {
            tvExitMoney.setVisibility(View.GONE);
            tvExitAll.setVisibility(View.GONE);
            tvChange.setVisibility(View.GONE);
        }
    }

    /**
     * set all select
     *
     * @param view
     */
    private void setAllSelect(View view) {
        tvChange.setSelected(tvChange == view);
        tvExitMoney.setSelected(tvExitMoney == view);
        tvExitAll.setSelected(tvExitAll == view);
    }

    /**
     * 售后类型 1：仅退款 2：退货退款 3：换货
     *
     * @return
     */
    private int getSelectType() {
        if (tvExitMoney.isSelected()) {
            return AppConfig.REFUND_EXIT_MONEY;
        }
        if (tvExitAll.isSelected()) {
            return AppConfig.REFUND_EXIT_ALL;
        }
        if (tvChange.isSelected()) {
            return AppConfig.REFUND_CHANGE_GOODS;
        }
        return -1;
    }

    /**
     * get calculator param
     *
     * @return
     */
    private String getCalculatorParam() {
        List<TempAfterSaleBean> list = new ArrayList<>();
        if (applyBean != null && applyBean.detailedList != null && applyBean.detailedList.size() > 0) {
            for (OrderDetailVo.DetailedListBean bean : applyBean.detailedList) {
                list.add(new TempAfterSaleBean(bean.picode, bean.applyNumber, bean.id, bean.itemtype));
            }
        }
        return JSONObject.toJSONString(list);
    }

}
