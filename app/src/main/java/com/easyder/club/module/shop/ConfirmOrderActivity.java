package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CarPresenter;
import com.easyder.club.module.me.AddressListActivity;
import com.easyder.club.module.me.vo.CommitOrderVo;
import com.easyder.club.module.me.vo.TempCommitOrderBean;
import com.easyder.club.module.me.vo.TempPayBean;
import com.easyder.club.module.shop.vo.CalculateOrderVo;
import com.easyder.club.module.shop.vo.OrderNoVo;
import com.easyder.club.module.shop.vo.TempCalculateVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.ResponseInfo;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 15:43
 * Email: xcode126@126.com
 * Desc: 确认订单
 */
public class ConfirmOrderActivity extends WrapperStatusActivity<CarPresenter> {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_no_address)
    TextView tvNoAddress;
    @BindView(R.id.layout_goods)
    LinearLayout layoutGoods;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_ticket)
    TextView tvTicket;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    private String jsonStr, tempAddressCode, tempInstanceCode,tempTicketName;
    private CalculateOrderVo calculateOrderVo;

    public static Intent getIntent(Context context, String jsonStr) {
        return new Intent(context, ConfirmOrderActivity.class).putExtra("jsonStr", jsonStr);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_confirm_order));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        jsonStr = intent.getStringExtra("jsonStr");
        calculatePrice(jsonStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 0x01) {
                tempAddressCode = data.getStringExtra("id");
            } else if (requestCode == 0x02) {
                tempInstanceCode = data.getStringExtra("instancecode");
                tempTicketName = data.getStringExtra("showname");
            }
            calculatePrice(getCalculateParam());
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_CALCULATE_PRICE)) {
            processOrderData((CalculateOrderVo) dataVo);
        } else if (url.contains(ApiConfig.API_GET_ORDER_NO)) {
            processGetOrderNo((OrderNoVo) dataVo);
        } else if (url.contains(ApiConfig.API_COMMIT_ORDER)) {
            processCommitOrder((CommitOrderVo) dataVo);
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        super.onError(responseInfo);
        if (responseInfo != null && responseInfo.url != null) {
            if (responseInfo.url.contains(ApiConfig.API_CALCULATE_PRICE)) {
                tempInstanceCode = "";
                tvTicket.setText("");
            }
        }
    }

    @OnClick({R.id.ll_address, R.id.tv_no_address, R.id.tv_ticket, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
            case R.id.tv_no_address:
                startActivityForResult(AddressListActivity.getIntent(mActivity, true), 0x01);
                break;
            case R.id.tv_ticket:
                if (calculateOrderVo != null && calculateOrderVo.couponList != null && calculateOrderVo.couponList.size() > 0) {
                    startActivityForResult(ChooseTicketActivity.getIntent(mActivity, tempInstanceCode), 0x02);
                }
                break;
            case R.id.btn_pay:
                if (TextUtils.isEmpty(tempAddressCode)) {
                    showToast(getString(R.string.a_input_address));
                    return;
                }
                getOrderNo();
                break;
        }
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
     * 获取订单号
     */
    private void getOrderNo() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_ORDER_NO, new RequestParams()
                .putCid()
                .get(), OrderNoVo.class);
    }

    /**
     * 提交订单
     *
     * @param orderJsonStr
     */
    private void commitOrder(String orderJsonStr) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_COMMIT_ORDER, new RequestParams()
                .put("orderJsonStr", orderJsonStr)
                .putCid()
                .get(), CommitOrderVo.class);
    }

    /**
     * @param dataVo
     */
    private void processOrderData(CalculateOrderVo dataVo) {
        this.calculateOrderVo = dataVo;
        //收货地址
        if (dataVo.address != null) {
            CalculateOrderVo.AddressBean address = dataVo.address;
            this.tempAddressCode = address.id;
            llAddress.setVisibility(View.VISIBLE);
            tvNoAddress.setVisibility(View.GONE);
            tvName.setText(String.format("%1$s %2$s", address.receivername, address.receivertel));
            tvAddress.setText(String.format("%1$s%2$s", address.addressname, address.detailedaddre));
        } else {
            llAddress.setVisibility(View.GONE);
            tvNoAddress.setVisibility(View.VISIBLE);
        }

        //商品
        layoutGoods.removeAllViews();
        if (dataVo.detailedList != null && dataVo.detailedList.size() > 0) {
            for (CalculateOrderVo.DetailedListBean bean : dataVo.detailedList) {
                layoutGoods.addView(getGoodsView(layoutGoods, bean));
            }
        }

        //优惠券
        if (dataVo.selectedCoupon != null && dataVo.selectedCoupon.size() > 0) {
            tempInstanceCode = dataVo.selectedCoupon.get(0);
            if (!TextUtils.isEmpty(tempTicketName)){
                tvTicket.setText(tempTicketName);
            }
        }

        //价格信息
        tvTotalMoney.setText(String.format("%1$s%2$s", "£", dataVo.totalmoney));
        tvFreight.setText(String.format("%1$s%2$s", "£", dataVo.expressmoney));
        tvDiscount.setText(String.format("%1$s%2$s", "-£", dataVo.discountmoney));
        tvPrice.setText(String.format("%1$s%2$s", "£", dataVo.actualmoney));
    }

    /**
     * 处理提交订单成功
     *
     * @param dataVo
     */
    private void processCommitOrder(CommitOrderVo dataVo) {
        EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_ORDER_COMMIT_SUCCESS));
        if (dataVo.actualmoney <= 0) {//当支付金额小于等于0时 直接支付成功
            startActivity(PaySuccessActivity.getIntent(mActivity, dataVo.orderno, dataVo.actualmoney, dataVo.score));
        } else {
            TempPayBean payBean = new TempPayBean(TempPayBean.TEMP_PAY_SHOP_CAR, dataVo.orderno, dataVo.actualmoney);
            startActivity(PayActivity.getIntent(mActivity, payBean));
        }
        finish();
    }

    /**
     * get goods view
     *
     * @param layoutGoods
     * @param item
     * @return
     */
    private View getGoodsView(LinearLayout layoutGoods, CalculateOrderVo.DetailedListBean item) {
        return getHelperView(layoutGoods, R.layout.item_goods_order, helper -> {
            helper.setImageManager(mActivity, R.id.iv_image, item.previewimg, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, item.piname);
            helper.setText(R.id.tv_num, String.format("%1$s%2$s", "x", item.number));
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", item.price));
            helper.setViewGone(R.id.btn_apply);
            helper.setViewGone(R.id.ll_operate);
        });
    }

    /**
     * 获取计算价格的参数
     *
     * @return
     */
    public String getCalculateParam() {
        if (calculateOrderVo == null) {
            return "";
        }

        //商品
        TempCalculateVo calculateVo = new TempCalculateVo();
        calculateVo.detailedList = new ArrayList<>();
        for (CalculateOrderVo.DetailedListBean bean : calculateOrderVo.detailedList) {
            calculateVo.detailedList.add(new TempCalculateVo.DetailedListBean(bean.picode, bean.number));
        }

        //优惠券
        if (calculateVo.selectedCoupon == null) {
            calculateVo.selectedCoupon = new ArrayList<>();
        }
        if (!TextUtils.isEmpty(tempInstanceCode)) {
            calculateVo.selectedCoupon.add(tempInstanceCode);
        }

        //收货地址
        calculateVo.addressid = tempAddressCode;
        return JSONObject.toJSONString(calculateVo);
    }

    /**
     * 处理获取订单号准备提交订单
     *
     * @param dataVo
     */
    private void processGetOrderNo(OrderNoVo dataVo) {
        if (calculateOrderVo != null) {
            TempCommitOrderBean orderVo = new TempCommitOrderBean(dataVo.orderno);
            //订单实付金额, 这个必须跟计算订单金额接口返回的金额一样
            orderVo.actualmoney = String.valueOf(calculateOrderVo.actualmoney);
            //收货地址id, 如果订单中购买了产品, 这个必传
            orderVo.addressid = calculateOrderVo.address.id;
            //备注信息
            orderVo.remark = etRemark.getText().toString().trim();
            //订单商品列表
            if (calculateOrderVo.orderJson != null &&calculateOrderVo.orderJson.detailedList!=null
                    && calculateOrderVo.orderJson.detailedList.size() > 0) {
                List<TempCommitOrderBean.DetailedListBean> list = new ArrayList<>();
                for (CalculateOrderVo.OrderJsonBan.DetailedListBean rowsBean : calculateOrderVo.orderJson.detailedList) {
                    list.add(new TempCommitOrderBean.DetailedListBean(rowsBean.code, rowsBean.number));
                }
                orderVo.detailedList = list;
            }
            //选中的优惠券编码列表, 数组的顺序要和选中券的顺序一致
            orderVo.selectedCoupon = calculateOrderVo.selectedCoupon;
            //如果是购买组合套餐的时候
            Map<String, Object> map = JSONObject.parseObject(jsonStr);
            if (map.get("selectedPackageGroup") != null) {
                List<Integer> selectedPackageGroupInteger = (List<Integer>) map.get("selectedPackageGroup");
                orderVo.selectedPackageGroup = selectedPackageGroupInteger;
            }
            //选择购买的套餐id, 如果从套餐详情页面点击购买, 需要传这个参数
//            orderVo.selectedPackageGroup = calculateOrderVo.sel
//            orderVo.pickuptype = calculateVo.pickuptype;
//            if (orderVo.pickuptype == AppConfig.SELF_CONTAINED_STORES) {
//                orderVo.pickupdeptcode = storeAddressVo.deptcode;
//            }
//            orderVo.deduction = deductionType;
            commitOrder(JSONObject.toJSONString(orderVo));
        }
    }
}
