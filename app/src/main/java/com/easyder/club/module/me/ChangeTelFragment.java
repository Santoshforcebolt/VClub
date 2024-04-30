package com.easyder.club.module.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.utils.RequestParams;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/6/19 10:48.
 * Email：xcode126@126.com
 * Desc：修改手机号
 */

public class ChangeTelFragment extends WrapperMvpFragment<CommonPresenter> {
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ll_tip)
    LinearLayout llTip;

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

    private String oldTel, oldCode;

    public static ChangeTelFragment newInstance(String oldTel, String oldCode) {
        ChangeTelFragment fragment = new ChangeTelFragment();
        Bundle bundle = new Bundle();
        bundle.putString("oldTel", oldTel);
        bundle.putString("oldCode", oldCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_change_tel;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        oldTel = getArguments().getString("oldTel");
        oldCode = getArguments().getString("oldCode");

        btnNext.setText(getString(R.string.a_confirm_change));
        llTip.setVisibility(View.GONE);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GET_CODE)) {
            handler.sendEmptyMessage(59);
        } else if (url.contains(ApiConfig.API_CHANGE_TEL)) {
            processChangeTelSuccess(dataVo);
        }
    }

    /**
     * 发送验证码成功
     *
     * @param dataVo
     */
    private void processChangeTelSuccess(BaseVo dataVo) {
        showToast(getString(R.string.a_operate_success));
        if (getActivity() instanceof ChangeTelActivity) {
            ChangeTelActivity activity = (ChangeTelActivity) getActivity();
            activity.changeTelSuccess();
        }
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
     * 提交新手机号
     *
     * @param tel
     * @param verifycode
     */
    private void commitNewTel(String tel, String verifycode) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_TEL, new RequestParams()
                .put("oldtel", oldTel)
                .put("oldverifycode", oldCode)
                .put("tel", tel)
                .put("verifycode", verifycode)
                .putCid()
                .get(), BaseVo.class);
    }

    @OnClick({R.id.tv_get_code, R.id.btn_next})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                String tel = etTel.getText().toString().trim();
                if (TextUtils.isEmpty(tel)) {
                    showToast(getString(R.string.a_input_right_tel));
                    return;
                }
                sendMobileCode(tel);
                break;
            case R.id.btn_next:
                String phone = etTel.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.a_input_right_tel));
                    return;
                }
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    showToast(getString(R.string.a_input_verify_code));
                    return;
                }
                commitNewTel(phone, code);
                break;
        }
    }
}
