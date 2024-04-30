package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.vo.ExtractOrderNoVo;
import com.easyder.club.utils.DoubleUtil;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.SimpleTextWatcher;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/18 17:39
 * Email: xcode126@126.com
 * Desc: 收益提取
 */
public class ExtractEarningsActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ratio)
    TextView tvRatio;
    @BindView(R.id.et_earnings)
    EditText etEarnings;
    @BindView(R.id.tv_extract_amount)
    TextView tvExtractAmount;
    @BindView(R.id.tv_extract)
    TextView tvExtract;

    private ExtractOrderNoVo noVo;
    private double earnings;

    public static Intent getIntent(Context context) {
        return new Intent(context, ExtractEarningsActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_extract_earnings;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_earnings_extract));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getBalanceNo();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_GET_BALANCE_NO)) {
            processNo((ExtractOrderNoVo) dataVo);
        } else if (url.contains(ApiConfig.API_EXTRACTION_EARNINGS)) {
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_INTEGRAL_CHANGE));
            showToast(getString(R.string.a_operate_success));
            finish();
        }
    }

    /**
     * get balance no
     */
    private void getBalanceNo() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_BALANCE_NO, new RequestParams()
                .putCid()
                .get(), ExtractOrderNoVo.class);
    }

    /**
     * @param commismoney
     * @param orderno
     */
    private void extractEarnings(double commismoney, String orderno) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_EXTRACTION_EARNINGS, new RequestParams()
                .put("commismoney", commismoney)
                .put("orderno", orderno)
                .putCid()
//                .put("bankcardno", bankcardno)
//                .put("bankname", bankname)
//                .put("username", username)
                .get(), BaseVo.class);
    }

    /**
     * process no
     *
     * @param noVo
     */
    private void processNo(ExtractOrderNoVo noVo) {
        this.noVo = noVo;
        tvExtractAmount.setText(String.format("%1$s%2$.2f%3$s", getString(R.string.a_can_extract_), noVo.balance, getString(R.string.a_unit_fen)));
        if (TextUtils.equals(noVo.type, "score")) {
            ivImage.setImageResource(R.drawable.earnings);
            tvTitle.setText(getString(R.string.a_integral));
            tvRatio.setText(String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s", "(1", getString(R.string.a_unit_fen), getString(R.string.a_earnings), "=", noVo.value, getString(R.string.a_integral), ")"));
        } else if (TextUtils.equals(noVo.type, "balance")) {
            ivImage.setImageResource(R.drawable.wallet_red);
            tvTitle.setText(getString(R.string.a_account_balance));
            tvRatio.setText(String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s", "(1", getString(R.string.a_unit_yuan), getString(R.string.a_earnings), "=", noVo.value, getString(R.string.a_integral), ")"));
        } else {
            ivImage.setImageResource(R.drawable.wallet_red);
        }
        initInputType();
    }

    /**
     * init input type
     */
    private void initInputType() {
        etEarnings.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().indexOf(".") != -1) {
                    if (s.toString().length() - s.toString().indexOf(".") > 3) {
                        s.delete(s.toString().indexOf(".") + 3, s.toString().length());
                        return;
                    }
                }
                if (s.toString().equals(".")) {
                    etEarnings.setText("");
                    return;
                }
                if (s.toString().equals("0")) {
                    return;
                }
                if (s.length() != 0) {
                    earnings = Double.valueOf(etEarnings.getText().toString());
                } else {
                    earnings = 0;
                }
                if (earnings <= Double.valueOf(noVo.balance) && earnings != 0.0) {
                    tvExtract.setEnabled(true);
                } else {
                    tvExtract.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.tv_extract_all, R.id.tv_extract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_extract_all:
                if (noVo != null) {
                    etEarnings.setText(DoubleUtil.formatTwo(noVo.balance));
                }
                break;
            case R.id.tv_extract:
                if (noVo == null) {
                    showToast(getString(R.string.a_config_error_tip));
                    return;
                }
                extractEarnings(earnings, noVo.orderno);
                break;
        }
    }
}
