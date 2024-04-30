package com.easyder.club.module.me.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.vo.StoreOrderDetailVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;


/**
 * Author: sky on 2020/12/10 9:49
 * Email: xcode126@126.com
 * Desc: 门店订单详情
 */
public class StoreOrderDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.layout_goods)
    LinearLayout layoutGoods;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_actual)
    TextView tvActual;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;

    private String orderno;

    public static Intent getIntent(Context context, String orderno) {
        return new Intent(context, StoreOrderDetailActivity.class).putExtra("orderno", orderno);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_store_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_order_detail));
        orderno = intent.getStringExtra("orderno");
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getOrderDetail();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_STORE_ORDER_DETAIL)) {
            processOrderDetail((StoreOrderDetailVo) dataVo);
        }
    }

    /**
     * 获取订单详情
     */
    private void getOrderDetail() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_STORE_ORDER_DETAIL, new RequestParams()
                .putCid()
                .put("orderno", orderno)
                .get(), StoreOrderDetailVo.class);
    }

    /**
     * process order detail
     *
     * @param dataVo
     */
    private void processOrderDetail(StoreOrderDetailVo dataVo) {
        //价格信息
        tvTotalMoney.setText(String.format("%1$s%2$s", "£", dataVo.ordertotalmoney));
        tvDiscount.setText(String.format("%1$s%2$.2f", "-£", (dataVo.ordertotalmoney - dataVo.actualmoney)));
        tvActual.setText(String.format("%1$s%2$.2f", "£", dataVo.actualmoney));
        tvCreateTime.setText(dataVo.ordertime);
        tvRemark.setText(dataVo.remark);

        //商品
        layoutGoods.removeAllViews();
        if (dataVo.detailedlist != null && dataVo.detailedlist.size() > 0) {
            for (StoreOrderDetailVo.DetailedlistBean bean : dataVo.detailedlist) {
                layoutGoods.addView(getGoodsView(layoutGoods, bean));
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
    private View getGoodsView(LinearLayout layoutGoods, StoreOrderDetailVo.DetailedlistBean item) {
        return getHelperView(layoutGoods, R.layout.item_store_order_detail_goods, helper -> {
            helper.setImageManager(mActivity, R.id.iv_goods, item.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, item.piname);
//            helper.setText(R.id.tv_service_member, String.format("%1$s%2$s%3$s%4$d%5$s", getString(R.string.a_service_member_),
//                    item.empname, "  " + getString(R.string.a_count_), item.number, getString(R.string.a_unit_ci)));
            helper.setText(R.id.tv_service_member,String.format("%1$s%2$s",getString(R.string.a_service_member_),item.empname));
            helper.setText(R.id.tv_desc,String.format("%1$s%2$s",getString(R.string.a_count_),item.number));
//            if (item.consumelist != null && item.consumelist.size() > 0) {
//                StoreOrderDetailVo.DetailedlistBean.ConsumeListBean consumeListBean = item.consumelist.get(0);
//                helper.setText(R.id.tv_desc, String.format("%1$s%2$s", consumeListBean.vipcodeorvipitemname, consumeListBean.consumenum + getString(R.string.a_unit_ci)));
//            }
        });
    }
}
