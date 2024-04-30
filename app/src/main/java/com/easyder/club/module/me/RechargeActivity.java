package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.Helper;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.BalanceBean;
import com.easyder.club.module.common.vo.CardBean;
import com.easyder.club.module.shop.vo.OrderNoVo;
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
 * Author: sky on 2020/11/17 17:59
 * Email: xcode126@126.com
 * Desc: 充值
 */
public class RechargeActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.tv200)
    TextView tv200;
    @BindView(R.id.tv500)
    TextView tv500;
    @BindView(R.id.tv1000)
    TextView tv1000;
    @BindView(R.id.tv5000)
    TextView tv5000;
    @BindView(R.id.tv10000)
    TextView tv10000;
    @BindView(R.id.tv38000)
    TextView tv38000;

    private Card tempCard;
    private String tempToken;
    private int amount;

    public static Intent getIntent(Context context) {
        return new Intent(context, RechargeActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_balance_recharge));
        setCardShow(Helper.getCardInfo());
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GET_RECHARGE_NO)) {
            processOrderNo((OrderNoVo) dataVo);
        } else if (url.contains(ApiConfig.API_RECHARGE)) {
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_WALLET_CHANGE, new BalanceBean(amount)));
            finish();
        }
    }

    /**
     * 获取充值订单
     */
    private void getRechargeNo() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_RECHARGE_NO, new RequestParams()
                .putCid()
                .get(), OrderNoVo.class);
    }

    /**
     * @param orderno
     * @param rechargemoney
     * @param token
     */
    private void requestRecharge(String orderno, int rechargemoney, String token) {
        if (presenter == null) {
            return;
        }
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_RECHARGE, new RequestParams()
                .put("orderno", orderno)
                .put("rechargemoney", rechargemoney)
                .put("stripemoney", rechargemoney)
                .put("token", token)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * 处理获取订单号
     *
     * @param orderNoVo
     */
    private void processOrderNo(OrderNoVo orderNoVo) {
        amount = getSelectNumber();
        if (amount == 0) {
            String trim = etAmount.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                showToast(getString(R.string.a_please_select_amount));
            } else {
                int amount = Integer.parseInt(trim);
                requestRecharge(orderNoVo.orderno, amount, tempToken);
            }
        } else {
            requestRecharge(orderNoVo.orderno, amount, tempToken);
        }
    }

    /**
     * set card show
     */
    private void setCardShow(CardBean cardInfo) {
        if (cardInfo != null && !TextUtils.isEmpty(cardInfo.number)) {
            tvCard.setText(CommonTools.splitNumber(cardInfo.number));
            ivEdit.setVisibility(View.VISIBLE);
            ivSelect.setSelected(true);
            tempCard = new Card(cardInfo.number, cardInfo.expMonth, cardInfo.expYear, cardInfo.cvc);
        } else {
            ivEdit.setVisibility(View.GONE);
            ivSelect.setSelected(false);
        }
    }

    @OnClick({R.id.btn_commit, R.id.iv_edit, R.id.iv_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                int selectNumber = getSelectNumber();
                String trim = etAmount.getText().toString().trim();
                if (selectNumber == 0 && TextUtils.isEmpty(trim)) {
                    showToast(getString(R.string.a_please_select_amount));
                    return;
                }
                createToken();
                break;
            case R.id.iv_select:
//                ivSelect.setSelected(true);
                showEditDialog();
                break;
            case R.id.iv_edit:
                showEditDialog();
                break;
        }
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
                        getRechargeNo();
                    }

                    public void onError(Exception error) {
                        onStopLoading();
                        LogUtils.i("======>" + error.getMessage());
                    }
                }
        );
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
                                CardBean tempCardBean = new CardBean(tempCard.getNumber(), tempCard.getExpMonth(), tempCard.getExpYear(), tempCard.getCVC());
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

    @OnClick({R.id.tv200, R.id.tv500, R.id.tv1000, R.id.tv5000, R.id.tv10000, R.id.tv38000})
    public void onNumberClicked(View view) {
        switch (view.getId()) {
            case R.id.tv200:
            case R.id.tv500:
            case R.id.tv1000:
            case R.id.tv5000:
            case R.id.tv10000:
            case R.id.tv38000:
                setAllSelected(view);
                break;
        }
    }

    /**
     * set all selected
     *
     * @param view
     */
    private void setAllSelected(View view) {
        tv200.setSelected(tv200 == view);
        tv500.setSelected(tv500 == view);
        tv1000.setSelected(tv1000 == view);
        tv5000.setSelected(tv5000 == view);
        tv10000.setSelected(tv10000 == view);
        tv38000.setSelected(tv38000 == view);
    }

    /**
     * get select number
     *
     * @return
     */
    private int getSelectNumber() {
        if (tv200.isSelected()) {
            return 200;
        }
        if (tv500.isSelected()) {
            return 500;
        }
        if (tv1000.isSelected()) {
            return 1000;
        }
        if (tv5000.isSelected()) {
            return 5000;
        }
        if (tv10000.isSelected()) {
            return 10000;
        }
        if (tv38000.isSelected()) {
            return 38000;
        }
        return 0;
    }
}
