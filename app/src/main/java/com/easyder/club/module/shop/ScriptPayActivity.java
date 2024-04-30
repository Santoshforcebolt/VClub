package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.LogUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

/**
 * Author: sky on 2020/12/16 17:51
 * Email: xcode126@126.com
 * Desc:
 */
@Deprecated
public class ScriptPayActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.mCardInputWidget)
    CardInputWidget mCardInputWidget;

    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;

    public static Intent getIntent(Context context) {
        return new Intent(context, ScriptPayActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_script_pay;
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_recharge));
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
//        //获取输入框银行卡信息
//        Card card = new Card("4242424242424242", 12, 2023, "123");
//        CardInputWidget cardInputWidget = (CardInputWidget) findViewById(R.id.mCardInputWidget);
//        //验证是否错误
//        if (card.validateCard()) {
//            //创建stripe对象
//            Stripe stripe = new Stripe(mActivity, AppConfig.KEY_SECRET);
//            stripe.createToken(
//                    card,
//                    new TokenCallback() {
//                        public void onSuccess(Token token) {
//                            // Send token to your server
//                            //成功创建令牌
//                            //发起支付的请求接口
//                            Log.d("6666666", "123456");
//                            Log.d("6666666", token + "");
//                            String bankAccount = token.getId();
//                            Log.d("6666666", bankAccount);
//                        }
//
//                        public void onError(Exception error) {
//                            // Show localized error message
//                            Log.d("6666666", "12345678");
//                        }
//                    }
//            );
//        } else if (!card.validateNumber()) {
//            showToast("The card number that you entered is invalid");
//        } else if (!card.validateExpiryDate()) {
//            showToast("The expiration date that you entered is invalid");
//        } else if (!card.validateCVC()) {
//            showToast("The CVC code that you entered is invalid");
//        } else {
//            showToast("The card details that you entered are invalid");
//        }

//        CardInputWidget cardInputWidget = (CardInputWidget) findViewById(R.id.mCardInputWidget);
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            //验证错误
        } else {
            //创建stripe对象
            Stripe stripe = new Stripe(mActivity, AppConfig.KEY_SECRET);
            stripe.createToken(
                    cardToSave,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server
                            //成功创建令牌
                            //发起支付的请求接口
                            LogUtils.i("======>" + token);
                            String bankAccount = token.getId();
                            LogUtils.i("======>" + bankAccount);
                        }

                        public void onError(Exception error) {
                            LogUtils.i("======>" + error.getMessage());
                        }
                    }
            );
        }
    }
}
