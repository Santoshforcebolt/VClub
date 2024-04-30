package com.easyder.club.module.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.Helper;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CardBean;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.common.vo.PayTypeBean;
import com.easyder.club.module.me.vo.TempPayBean;
import com.easyder.club.module.shop.vo.OrderPayVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;
import com.sky.wrapper.utils.LogUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 11:51
 * Email: xcode126@126.com
 * Desc: 收银台
 */
public class PayActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.iv_wallet_select)
    ImageView ivWalletSelect;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_card_select)
    ImageView ivCardSelect;

    private TempPayBean payBean;
    private double newbalance;
    private Card tempCard;
    private String tempToken;

    /**
     * @param context
     * @param payBean
     * @return
     */
    public static Intent getIntent(Context context, TempPayBean payBean) {
        return new Intent(context, PayActivity.class).putExtra("payBean", payBean);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_cashier_desk));
        payBean = (TempPayBean) intent.getSerializableExtra("payBean");
        tvPrice.setText(String.format("%1$s%2$.2f", "£", payBean.needAmount));
        setCardShow(Helper.getCardInfo());
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        if (App.getCustomer() != null) {
            CustomerBean customerBean = App.getCustomer();
            this.newbalance = customerBean.newbalance;
            tvBalance.setText(String.format("%1$s%2$.2f", "£", newbalance));
            //需要付款的金额小于余额
            if (payBean != null && payBean.needAmount < newbalance) {
                ivWalletSelect.setVisibility(View.VISIBLE);
            } else {
                ivWalletSelect.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_ONLINE_ORDER_PAYMENT)) {
            processOrderPay(dataVo);
        }
    }

    /**
     * 订单支付 余额支付
     *
     * @param orderno
     * @param paytype
     * @param paypassword
     */
    private void requestOrderPay(String orderno, String paytype, String paypassword) {
        if (presenter == null) {
            return;
        }
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_ONLINE_ORDER_PAYMENT, new RequestParams()
                .put("orderno", orderno)
                .put("paytype", paytype)
                .putWithoutEmpty("paypassword", paypassword)
                .putWithoutEmpty("token", tempToken)
                .putCid()
                .get(), OrderPayVo.class);
    }

    /**
     * process order pay success
     *
     * @param dataVo
     */
    private void processOrderPay(BaseVo dataVo) {
        if (dataVo instanceof OrderPayVo) {
            OrderPayVo orderPayVo = (OrderPayVo) dataVo;
            EventBus.getDefault().post(new OrderChanged(OrderIml.SIGN_ORDER_PAY_SUCCESS));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PERSON_CHANGE));
            startActivity(PaySuccessActivity.getIntent(mActivity, payBean.orderNo, orderPayVo.actualmoney, orderPayVo.score));
            this.finish();
        }
    }

    /**
     * 处理购物车下单支付逻辑
     */
    private void handleOrderPay() {
        if (ivWalletSelect.isSelected()) {
            if (newbalance < payBean.needAmount) {
                showToast(getString(R.string.a_balance_lack));
                return;
            }
            //输入密码窗口
            new PasswordDialog(mActivity, passWord ->
                    requestOrderPay(payBean.orderNo, PayTypeBean.PAY_TYPE_WALLET, passWord)).show();
            return;
        }

        if (ivCardSelect.isSelected()) {
            createToken();
            return;
        }
        showToast(getString(R.string.a_choose_pay_type));
    }

    /**
     * set card show
     */
    private void setCardShow(CardBean cardInfo) {
        if (cardInfo != null && !TextUtils.isEmpty(cardInfo.number)) {
            tvCard.setText(CommonTools.splitNumber(cardInfo.number));
            ivEdit.setVisibility(View.VISIBLE);
            tempCard = new Card(cardInfo.number, cardInfo.expMonth, cardInfo.expYear, cardInfo.cvc);
        } else {
            ivEdit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_commit, R.id.iv_wallet_select, R.id.iv_card_select, R.id.iv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                handleOrderPay();
                break;
            case R.id.iv_wallet_select:
                ivCardSelect.setSelected(false);
                ivWalletSelect.setSelected(true);
                break;
            case R.id.iv_card_select:
                ivCardSelect.setSelected(true);
                ivWalletSelect.setSelected(false);
                if (ivEdit.getVisibility() != View.VISIBLE) {
                    showEditDialog();
                }
                break;
            case R.id.iv_edit:
                showEditDialog();
                break;
        }
    }

    /**
     * @param
     */
    private void showEditDialog() {
        new WrapperDialog(mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_input_card;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 690, 340, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                CardInputWidget mCardInputWidget = helper.getView(R.id.mCardInputWidget);
                CardBean cardBean = Helper.getCardInfo();
                if (cardBean != null && !TextUtils.isEmpty(cardBean.number)) {
                    mCardInputWidget.setCardNumber(cardBean.number);
                    mCardInputWidget.setExpiryDate(cardBean.expMonth, cardBean.expYear);
                    mCardInputWidget.setCvcCode(cardBean.cvc);
                }
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.tv_confirm:
                                Card cardToSave = mCardInputWidget.getCard();
                                if (cardToSave == null) {
                                    showToast(getString(R.string.a_credit_card_error_tip));
                                    return;
                                }
//                                if (!cardToSave.validateNumber()) {
//                                    showToast("The card number that you entered is invalid");
//                                    return;
//                                }
//                                if (!cardToSave.validateExpiryDate()) {
//                                    showToast("The expiration date that you entered is invalid");
//                                    return;
//                                }
//                                if (!cardToSave.validateCVC()) {
//                                    showToast("The CVC code that you entered is invalid");
//                                    return;
//                                }
//                                if (!cardToSave.validateCard()) {
//                                    showToast("The card details that you entered are invalid");
//                                    return;
//                                }
                                tempCard = cardToSave;
                                CardBean tempCardBean = new CardBean(tempCard.getNumber(), tempCard.getExpMonth(),
                                        tempCard.getExpYear(), tempCard.getCVC());
                                Helper.saveCardInfo(tempCardBean);
                                setCardShow(tempCardBean);
                                dismiss();
                                break;
                            case R.id.tv_cancel:
                                dismiss();
                                break;
                        }
                    }
                }, R.id.tv_confirm, R.id.tv_cancel);
            }
        }.show();
    }

    /**
     * create token
     */
    private void createToken() {
        showLoadingView();
        Stripe stripe = new Stripe(mActivity, AppConfig.KEY_SECRET);
        stripe.createToken(tempCard, new TokenCallback() {
                    public void onSuccess(Token token) {
                        onStopLoading();
                        // Send token to your server
                        //成功创建令牌
                        //发起支付的请求接口
                        LogUtils.i("======>" + token);
                        String bankAccount = token.getId();
                        LogUtils.i("======>" + bankAccount);
                        tempToken = token.getId();
                        requestOrderPay(payBean.orderNo, PayTypeBean.PAY_TYPE_CREDIT, "");
                    }

                    public void onError(Exception error) {
                        onStopLoading();
                        LogUtils.i("======>" + error.getMessage());
                    }
                }
        );
    }


}
