package com.easyder.club.module.me;

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
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/6/19 10:36.
 * Email：xcode126@126.com
 * Desc：验证手机号
 */

public class VerifyTelFragment extends WrapperMvpFragment<CommonPresenter> {

    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;

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

    public static VerifyTelFragment newInstance(String tel) {
        VerifyTelFragment fragment = new VerifyTelFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tel", tel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_change_tel;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String tel = getArguments().getString("tel");
        etTel.setText(tel);
        etTel.setEnabled(false);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroyView();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GET_CODE)) {
            processVerifyCode(dataVo);
        }
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

    @OnClick({R.id.tv_get_code, R.id.btn_next})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                String tel = etTel.getText().toString().trim();
                if (TextUtils.isEmpty(tel)){
                    return;
                }
                sendMobileCode(tel);
                break;
            case R.id.btn_next:
                handleNext();
                break;
        }
    }

    /**
     * 处理下一步
     */
    private void handleNext() {
        String tel = etTel.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            return;
        }
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a_input_verify_code));
            return;
        }
        handleVerifyTel(tel, code);
    }

    /**
     * 处理验证手机号
     *
     * @param oldTel
     * @param code
     */
    private void handleVerifyTel(String oldTel, String code) {
        if (getActivity() instanceof ChangeTelActivity) {
            ChangeTelActivity activity = (ChangeTelActivity) getActivity();
            activity.loadChangePage(oldTel, code);
        }
    }
}
