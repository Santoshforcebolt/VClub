package com.easyder.club.module.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.common.vo.MemberVo;
import com.easyder.club.module.me.LoginActivity;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/12 16:47
 * Email: xcode126@126.com
 * Desc:
 */
public class RegisterActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_pay_password)
    EditText etPayPassword;
    @BindView(R.id.et_recommend_code)
    EditText etRecommendCode;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what <= 0) {
                tvCode.setEnabled(true);
                tvCode.setText(getString(R.string.a_get_msg));
                handler.removeCallbacksAndMessages(null);
            } else {
                tvCode.setEnabled(false);
                tvCode.setText(String.format("%sS", msg.what));
                handler.sendEmptyMessageDelayed(--msg.what, 1000);
            }
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_register));
        etPhone.setSelection(etPhone.getText().toString().trim().length());
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GET_CODE)) {
            processVerifyCode(dataVo);
        } else if (url.contains(ApiConfig.API_REGISTER)) {
            processRegisterData((MemberVo) dataVo);
        } else if (url.contains(ApiConfig.API_PERSON_CENTER)) {
            processPersonData((CustomerBean) dataVo);
        }
    }

    /**
     * send code
     *
     * @param tel
     */
    private void sendCode(String tel) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_CODE, new RequestParams().put("tel", tel).get(), BaseVo.class);
    }

    /**
     * 注册账号
     *
     * @param tel
     * @param verifycode
     * @param password
     * @param paypassword
     * @param recommencode
     */
    private void requestRegister(String tel, String verifycode, String password, String paypassword, String recommencode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_REGISTER, new RequestParams()
                .put("tel", tel)
                .put("verifycode", verifycode)
                .put("password", password)
                .put("paypassword", paypassword)
                .put("recommencode", recommencode)
                .get(), MemberVo.class);
    }

    /**
     * get person center
     */
    private void getPersonCenter(String cid) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_PERSON_CENTER, new RequestParams()
                .put("cid", cid)
                .get(), CustomerBean.class);
    }

    /**
     * process send msg success
     *
     * @param dataVo
     */
    private void processVerifyCode(BaseVo dataVo) {
        handler.sendEmptyMessage(59);
    }

    /**
     * process login data
     *
     * @param dataVo
     */
    private void processRegisterData(MemberVo dataVo) {
        App.setCid(dataVo.cid);
        getPersonCenter(dataVo.cid);
    }

    /**
     * @param dataVo
     */
    private void processPersonData(CustomerBean dataVo) {
        App.setCustomerBean(dataVo);
        startActivity(LoginActivity.getIntent(mActivity));
        finish();
    }

    @OnClick({R.id.tv_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                String tel = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(tel)) {
                    showToast(getString(R.string.a_input_tel));
                    return;
                }
                sendCode(tel);
                break;
            case R.id.btn_register:
                goRegister();
                break;
        }
    }

    /**
     * go register
     */
    private void goRegister() {
        String tel = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            showToast(getString(R.string.a_input_tel));
            return;
        }
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a_input_verify_code));
            return;
        }
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showToast(getString(R.string.a_set_login_password));
            return;
        }
        String payPassword = etPayPassword.getText().toString().trim();
        if (TextUtils.isEmpty(payPassword)) {
            showToast(getString(R.string.a_set_pay_password));
            return;
        }
        String recommendCode = etRecommendCode.getText().toString().trim();
        requestRegister(tel, code, password, payPassword, recommendCode);
    }
}
