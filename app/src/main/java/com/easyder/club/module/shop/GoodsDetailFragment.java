package com.easyder.club.module.shop;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CarPresenter;
import com.easyder.club.module.common.ImagePreviewActivity;
import com.easyder.club.module.enjoy.EnjoyDetailActivity;
import com.easyder.club.module.enjoy.PublishEnjoyActivity;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.module.shop.vo.CarCountVo;
import com.easyder.club.module.shop.vo.GoodsDetailVo;
import com.easyder.club.module.shop.vo.TempCalculateVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.WebViewUtils;
import com.easyder.club.widget.RollingWebView;
import com.easyder.club.widget.SnapUpCountDownTimerView;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.HorizontalProgressBar;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Author: sky on 2020/11/25 18:26
 * Email: xcode126@126.com
 * Desc: 商品详情主界面
 */
public class GoodsDetailFragment extends WrapperStatusFragment<CarPresenter> {
    @BindView(R.id.mNestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.mBGABanner)
    BGABanner mBGABanner;
    @BindView(R.id.iv_sold_out)
    ImageView ivSoldOut;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_original_price)
    TextView tvOriginalPrice;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_repertory)
    TextView tvRepertory;

    @BindView(R.id.tv_total_enjoy)
    TextView tvTotalEnjoy;
    @BindView(R.id.tv_average_score)
    TextView tvAverageScore;
    @BindView(R.id.colorProgressBar)
    HorizontalProgressBar colorProgressBar;
    @BindView(R.id.tv_color_lever)
    TextView tvColorLever;
    @BindView(R.id.smellProgressBar)
    HorizontalProgressBar smellProgressBar;
    @BindView(R.id.tv_smell_lever)
    TextView tvSmellLever;
    @BindView(R.id.tasteProgressBar)
    HorizontalProgressBar tasteProgressBar;
    @BindView(R.id.tv_taste_lever)
    TextView tvTasteLever;
    @BindView(R.id.rhymeProgressBar)
    HorizontalProgressBar rhymeProgressBar;
    @BindView(R.id.tv_rhyme_lever)
    TextView tvRhymeLever;
    @BindView(R.id.ll_enjoy)
    LinearLayout llEnjoy;
    @BindView(R.id.layout_evaluate)
    LinearLayout layoutEvaluate;

    @BindView(R.id.layout_spec)
    LinearLayout layoutSpec;
    @BindView(R.id.ll_spec)
    LinearLayout llSpec;

    @BindView(R.id.mRollingWebView)
    RollingWebView mRollingWebView;
    @BindView(R.id.tv_order_dot)
    TextView tvOrderDot;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll_limit)
    LinearLayout llLimit;
    @BindView(R.id.mSnapUpCountDownTimerView)
    SnapUpCountDownTimerView mSnapUpCountDownTimerView;

    private String productcode;
    private GoodsDetailVo detailVo;

    public static GoodsDetailFragment newInstance(String productcode) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        Bundle args = new Bundle();
        args.putString("productcode", productcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        productcode = getArguments().getString("productcode");
        initRollingWebView();
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Subscribe
    public void OrderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_ORDER_COMMIT_SUCCESS:
                getActivity().finish();
                break;
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        getGoodsDetail();
        getEnjoyList();
        getCarCount();
    }

    @OnClick({R.id.fl_get_ticket, R.id.ll_car, R.id.btn_add, R.id.btn_buy, R.id.dtv_enjoy, R.id.tv_look_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_get_ticket:
                GetTicketFragment.newInstance().show(_mActivity.getFragmentManager(), "GetTicketFragment");
                break;
            case R.id.ll_car:
                startActivity(ShopCarActivity.getIntent(_mActivity));
                break;
            case R.id.btn_add:
                requestAddCar();
                break;
            case R.id.btn_buy:
                if (detailVo != null) {
                    showSpecDialog();
                }
//                startActivity(ConfirmOrderActivity.getIntent(_mActivity, getCalculateParam()));
                break;
            case R.id.dtv_enjoy:
                if (detailVo != null) {
                    GoodsListVo.ProductsBean bean = new GoodsListVo.ProductsBean(
                            detailVo.productcode, detailVo.productname, detailVo.imgurl);
                    startActivity(PublishEnjoyActivity.getIntent(_mActivity, bean));
                }
                break;
            case R.id.tv_look_more:
                notifyPageChange();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ADD_CAR)) {
            processAddCar();
        } else if (url.contains(ApiConfig.API_ENJOY_LIST)) {
            processEnjoyList((EnjoyListVo) dataVo);
        } else if (url.contains(ApiConfig.API_GET_CAR_TOTAL)) {
            processCarCount((CarCountVo) dataVo);
        } else if (url.contains(ApiConfig.API_GOODS_DETAIL)) {
            processGoodsDetail((GoodsDetailVo) dataVo);
        }
    }

    /**
     * add car
     */
    private void requestAddCar() {
        presenter.setNeedDialog(true);
        presenter.addCar(productcode, 1);
    }

    /**
     * get enjoy list
     */
    private void getEnjoyList() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ENJOY_LIST, new RequestParams()
                .putCid()
                .put("productcode", productcode)
                .put("page", 1)
                .put("rows", AppConfig.PAGE_SIZE)
//                .putWithoutEmpty("optiontype", getOptionType())
                .get(), EnjoyListVo.class);
    }

    /**
     * get car count
     */
    private void getCarCount() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_CAR_TOTAL, new RequestParams().putCid().get(), CarCountVo.class);
    }

    /**
     * get goods detail
     */
    private void getGoodsDetail() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GOODS_DETAIL, new RequestParams().putCid().put("productcode", productcode).get(), GoodsDetailVo.class);
    }

    /**
     * process add car
     */
    private void processAddCar() {
        getCarCount();
        EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_CAR_CHANGE));
        showToast(getString(R.string.a_operate_success));
    }

    /**
     * process enjoy list
     *
     * @param dataVo
     */
    private void processEnjoyList(EnjoyListVo dataVo) {
        if (dataVo.rows != null && dataVo.rows.size() > 0) {
            layoutEvaluate.removeAllViews();
            llEnjoy.setVisibility(View.VISIBLE);
            int count = dataVo.rows.size() >= 3 ? 3 : dataVo.rows.size();
            for (int i = 0; i < count; i++) {
                EnjoyListVo.RowsBean bean = dataVo.rows.get(i);
                layoutEvaluate.addView(getEvaluateView(bean));
            }
        } else {
            llEnjoy.setVisibility(View.GONE);
        }
    }

    /**
     * process car count
     *
     * @param dataVo
     */
    private void processCarCount(CarCountVo dataVo) {
        if (dataVo != null) {
            tvOrderDot.setText(dataVo.total + "");
        }
    }

    /**
     * @param dataVo
     */
    private void processGoodsDetail(GoodsDetailVo dataVo) {
        this.detailVo = dataVo;

        tvName.setText(dataVo.productname);
        tvPrice.setText(String.format("%1$.2f", dataVo.saleprice));
        tvOriginalPrice.setText(String.format("%1$s%2$.2f", "£", dataVo.marketprice));
        tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线
        tvOriginalPrice.setVisibility(dataVo.marketprice>0?View.VISIBLE:View.GONE);
        tvSale.setText(String.format("%1$s%2$s", getString(R.string.a_yet_sale_), dataVo.salenum));
        tvRepertory.setText(String.format("%1$s%2$s", getString(R.string.a_repertory_), dataVo.stocknum));
//        ivSoldOut.setVisibility(dataVo.stocknum > 0 ? View.GONE : View.VISIBLE);
        handleBanner(dataVo);
        handleEnjoy(dataVo);
        handleSpec(dataVo);
        handleDetail();
        handleBottom(dataVo);

        if (dataVo.timeLimitActivity != null) {
            llLimit.setVisibility(View.VISIBLE);
            mSnapUpCountDownTimerView.startTime(dataVo.timeLimitActivity.remaintime);
            mSnapUpCountDownTimerView.start();
            mSnapUpCountDownTimerView.setOutOfTimeListener(() -> {
                //时间到期需要刷新
                getGoodsDetail();
            });
        } else {
            llLimit.setVisibility(View.GONE);
        }
    }

    /**
     * handle detail
     */
    private void handleDetail() {
        if (!TextUtils.isEmpty(detailVo.describes)) {
            mRollingWebView.loadData(WebViewUtils.formatFont(detailVo.describes), "text/html; charset=UTF-8", "UTF-8");
        }
    }

    /**
     * handle spec
     *
     * @param dataVo
     */
    private void handleSpec(GoodsDetailVo dataVo) {
        if (dataVo.specificationArray != null && dataVo.specificationArray.size() > 0) {
            layoutSpec.setVisibility(View.VISIBLE);
            llSpec.setVisibility(View.VISIBLE);
            layoutSpec.removeAllViews();
            for (GoodsDetailVo.SpecificationBean bean : dataVo.specificationArray) {
                layoutSpec.addView(getSpecView(bean));
            }
        } else {
            layoutSpec.setVisibility(View.GONE);
            llSpec.setVisibility(View.GONE);
        }
    }

    /**
     * handle enjoy
     *
     * @param dataVo
     */
    private void handleEnjoy(GoodsDetailVo dataVo) {
        tvTotalEnjoy.setText(String.format("%1$s (%2$s %3$s %4$s)", getString(R.string.a_buyer_enjoy),
                dataVo.evaluatenum, getString(R.string.a_unit_tiao), getString(R.string.a_enjoy)));
        tvAverageScore.setText(String.valueOf(dataVo.totalscore));
        tvColorLever.setText(String.valueOf(dataVo.colorscore));
        colorProgressBar.setProgress(dataVo.colorscore);
        tvSmellLever.setText(String.valueOf(dataVo.smellscore));
        smellProgressBar.setProgress(dataVo.colorscore);
        tvTasteLever.setText(String.valueOf(dataVo.tastescore));
        tasteProgressBar.setProgress(dataVo.tastescore);
        tvRhymeLever.setText(String.valueOf(dataVo.endingscore));
        rhymeProgressBar.setProgress(dataVo.endingscore);
    }

    /**
     * handle banner
     *
     * @param dataVo
     */
    private void handleBanner(GoodsDetailVo dataVo) {
        List<String> imageArray = OperateUtils.getImageArray(dataVo.imgurl);
        mBGABanner.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, item, position)
                -> ImageManager.load(_mActivity, itemView, item, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2));
        mBGABanner.setDelegate((BGABanner.Delegate<ImageView, String>) (banner, itemView, item, position) -> {
            if (imageArray != null && imageArray.size() > 0) {
                ArrayList list = new ArrayList<String>();
                for (String str : imageArray) {
                    list.add(str);
                }
                startActivity(ImagePreviewActivity.getIntent(_mActivity, list, position));
            }
//            else {
//                ArrayList<String> pictureList = new ArrayList<>();
//                pictureList.add(model.albumImage);
//                startActivity(ImagePreviewActivity.getIntent(mActivity, pictureList, 0));
//            }
        });
        mBGABanner.setData(imageArray, null);
    }

    /**
     * rows.stocknum > 0 && rows.issale == 1    立即购买
     * rows.stocknum == 0   已售罄
     * rows.issale == 0    暂不销售
     *
     * @param dataVo
     */
    private void handleBottom(GoodsDetailVo dataVo) {
        if (dataVo.stocknum == 0) {//已售罄
            btnBuy.setVisibility(View.GONE);
            btnAdd.setText(getText(R.string.a_sold_out));
            btnAdd.setEnabled(false);
            llBottom.setVisibility(View.VISIBLE);
        } else if (dataVo.stocknum > 0 && dataVo.issale == 1) {//立即购买
            btnBuy.setVisibility(View.VISIBLE);
            btnAdd.setText(getText(R.string.a_add_car));
            btnAdd.setEnabled(true);
            llBottom.setVisibility(View.VISIBLE);
        } else {//不可购买
            llBottom.setVisibility(View.GONE);
        }
    }

    /**
     * @param bean
     * @return
     */
    private View getEvaluateView(EnjoyListVo.RowsBean bean) {
        return getHelperView(layoutEvaluate, R.layout.item_evaluate_list, helper -> {
            helper.setImageManager(_mActivity, R.id.iv_header, bean.icourl, R.drawable.default_header, R.drawable.default_header);
//            String content = String.format("<font color='#000000'>%1$s </font>%2$s <font color='#000000'>%3$s </font>%4$s",
//                    bean.customername, "scored this whisky", bean.totalscore, "points");
//            helper.setText(R.id.tv_content, Html.fromHtml(content));
            helper.setText(R.id.tv_name, bean.customername);
            helper.setText(R.id.tv_score, bean.totalscore + "");
            helper.setText(R.id.tv_time, bean.createdate);
            helper.setText(R.id.tv_place, bean.address);
            helper.setVisible(R.id.tv_place, !TextUtils.isEmpty(bean.address));
            helper.setOnClickListener(R.id.layout_root, v ->
                    startActivity(EnjoyDetailActivity.getIntent(_mActivity, bean.id)));
        });
    }

    /**
     * get spec view
     *
     * @param bean
     * @return
     */
    private View getSpecView(GoodsDetailVo.SpecificationBean bean) {
        return getHelperView(layoutSpec, R.layout.item_goodes_spec, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                helper.setText(R.id.tv_name, bean.name);
                helper.setText(R.id.tv_value, bean.value);
            }
        });
    }

    /**
     * 通知父界面更改界面
     */
    private void notifyPageChange() {
        if (getActivity() instanceof GoodsDetailActivity) {
            GoodsDetailActivity activity = (GoodsDetailActivity) getActivity();
            activity.showPageIndex(1);
        }
    }

    /**
     * 通知父界面标题栏需要更改
     *
     * @param isFloat
     */
    private void notifyTitleChange(boolean isFloat) {
        if (getActivity() instanceof GoodsDetailActivity) {
            GoodsDetailActivity activity = (GoodsDetailActivity) getActivity();
            activity.showTitleType(!isFloat);
        }
    }

    /**
     * 监听滚动
     */
    private void initRollingWebView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRollingWebView.setLayoutParams(params);
        mRollingWebView.setOnCustomScrollChangeListener((l, t, oldl, oldt) -> {
        });
        WebViewUtils.initWebView(_mActivity, mRollingWebView);

        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY)
                        -> notifyTitleChange(scrollY > 102));
    }

    /**
     * show spec dialog
     */
    private void showSpecDialog() {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_spec;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                dialog.getWindow().setWindowAnimations(R.style.TransDialogAnim);
                setDialogParams(dialog, ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(500), Gravity.BOTTOM);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setImageManager(_mActivity, R.id.iv_goods, OperateUtils.getFirstImage(detailVo.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
                helper.setText(R.id.tv_name, detailVo.productname);
                helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", detailVo.saleprice));
                helper.setText(R.id.tv_num, String.valueOf(detailVo.tempNumber = 1));
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_add:
                                if (detailVo.tempNumber >= detailVo.stocknum) {
                                    showToast(getString(R.string.a_no_repertory_tip));
                                    return;
                                }
                                detailVo.tempNumber++;
                                helper.setText(R.id.tv_num, String.valueOf(detailVo.tempNumber));
                                break;
                            case R.id.btn_reduce:
                                if (detailVo.tempNumber <= 1) {
                                    return;
                                }
                                detailVo.tempNumber--;
                                helper.setText(R.id.tv_num, String.valueOf(detailVo.tempNumber));
                                break;
                            case R.id.iv_close:
                                dismiss();
                                break;
                            case R.id.tv_confirm:
                                startActivity(ConfirmOrderActivity.getIntent(_mActivity, getCalculateParam()));
                                dismiss();
                                break;
                        }
                    }

                }, R.id.btn_add, R.id.btn_reduce, R.id.iv_close, R.id.tv_confirm);
            }
        }.show();
    }

    /**
     * 获取计算价格的参数
     *
     * @return
     */
    public String getCalculateParam() {
        TempCalculateVo calculateVo = new TempCalculateVo();
        calculateVo.detailedList = new ArrayList<>();
        calculateVo.detailedList.add(new TempCalculateVo.DetailedListBean(productcode, detailVo.tempNumber));
        return JSONObject.toJSONString(calculateVo);
    }

    /**
     * get ticket dialog
     */
    private void getTicketDialog() {
        GetTicketFragment fragment = GetTicketFragment.newInstance();
        fragment.show(_mActivity.getFragmentManager(), "GetTicketFragment");
    }

}
