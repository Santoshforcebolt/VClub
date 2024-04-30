package com.easyder.club.module.me;

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
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/5/30 18:03.
 * Email：xcode126@126.com
 * Desc：更改支付密码，登录密码
 */

public class ChangePassWordActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_password)
    EditText etPassWord;

    public static final int TYPE_CHANGE_LOGIN_PASSWORD = 1;//修改登录密码
    public static final int TYPE_CHANGE_PAY_PASSWORD = 2;//修改支付密码
    public static final int TYPE_RESET_LOGIN_PASSWORD = 3;//重置登陆密码
    private int type;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what <= 0) {
                tvGetCode.setEnabled(true);
                tvGetCode.setText(getString(R.string.a_get_msg));
                handler.removeCallbacksAndMessages(null);
            } else {
                tvGetCode.setEnabled(false);
                tvGetCode.setText(String.format("%sS", msg.what));
                handler.sendEmptyMessageDelayed(--msg.what, 1000);
            }
        }
    };

    public static Intent getIntent(Context mContext, int type, String tel) {
        return new Intent(mContext, ChangePassWordActivity.class)
                .putExtra("type", type)
                .putExtra("tel", tel);
    }

    public static Intent getIntent(Context mContext, int type) {
        return getIntent(mContext, type, null);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        setMultiTitle(titleView, intent);
        etTel.setSelection(etTel.getText().toString().trim().length());
    }

    /**
     * 设置复杂标题和接受数据处理
     *
     * @param titleView
     * @param intent
     */
    private void setMultiTitle(TitleView titleView, Intent intent) {
        type = intent.getIntExtra("type", TYPE_CHANGE_LOGIN_PASSWORD);
//        String tel = intent.getStringExtra("tel");
//        if (TextUtils.isEmpty(tel)) {
//            etTel.setEnabled(true);
//        } else {
//            etTel.setText(tel);
//            etTel.setEnabled(false);
//        }
        switch (type) {
            case TYPE_CHANGE_LOGIN_PASSWORD:
                titleView.setTitle(getString(R.string.a_change_login_password));
                etPassWord.setHint(getString(R.string.a_set_new_login_password));
                break;
            case TYPE_CHANGE_PAY_PASSWORD:
                titleView.setTitle(getString(R.string.a_change_pay_password));
                etPassWord.setHint(getString(R.string.a_set_new_pay_password));
                break;
            case TYPE_RESET_LOGIN_PASSWORD:
                titleView.setTitle(getString(R.string.a_reset_password));
                etPassWord.setHint(getString(R.string.a_set_new_pay_password));
                break;
        }
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
        } else if (url.contains(ApiConfig.API_RESET_PASS)) {
            processResetSuccess(dataVo);
        }
    }

    /**
     * 重置密码成功
     *
     * @param dataVo
     */
    private void processResetSuccess(BaseVo dataVo) {
        showToast(getString(R.string.a_operate_success));
        finish();
    }

    /**
     * 发送验证码成功
     *
     * @param dataVo
     */
    private void processVerifyCode(BaseVo dataVo) {
//        showToast("验证码发送成功");
        handler.sendEmptyMessage(59);
    }

    /**
     * 发送手机验证码
     *
     * @param tel
     */
    private void sendMobileCode(String tel) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_CODE, new RequestParams()
                .put("tel", tel)
                .get(), BaseVo.class);

    }

    /**
     * 充值密码
     *
     * @param tel
     * @param verifycode
     * @param newpassword
     */
    private void resetPassWord(String tel, String verifycode, String newpassword) {
        presenter.setNeedDialog(true);
        String s = type == TYPE_CHANGE_PAY_PASSWORD ? "paypassword" : "password";
        presenter.postData(ApiConfig.API_RESET_PASS, new RequestParams()
                .put("tel", tel)
                .put("verifycode", verifycode)
                .put("newpassword", newpassword)
                .put("passwordtype",s )
                .putCid()
                .get(), BaseVo.class);
    }

    @OnClick({R.id.tv_get_code, R.id.btn_next})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                handleGetCode();
                break;
            case R.id.btn_next:
                handleNext();
                break;
        }
    }

    /**
     * 处理获取验证码
     */
    private void handleGetCode() {
        String tel = etTel.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            showToast(getString(R.string.a_input_right_tel));
            return;
        }
        sendMobileCode(tel);
    }

    /**
     * 处理接下来
     */
    private void handleNext() {
        String tel = etTel.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            showToast(getString(R.string.a_input_right_tel));
            return;
        }
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a_input_verify_code));
            return;
        }
        String passWord = etPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            showToast(getString(R.string.a_input_password));
            return;
        }
        resetPassWord(tel, code, passWord);
    }
}
