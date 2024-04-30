package com.easyder.club.module.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.easyder.club.ApiConfig;
import com.easyder.club.Helper;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.MainActivity;
import com.easyder.club.module.common.RegisterActivity;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.common.vo.MemberVo;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:  winds
 * Data:    2018/7/26
 * Version: 1.0
 * Desc:
 */

public class LoginFragment extends WrapperMvpFragment<CommonPresenter> {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(statusBarView).init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        etAccount.setSelection(etAccount.getText().toString().trim().length());
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        String account = PreferenceUtils.getPreference(_mActivity, Helper.BASIC_TEL, null);
        if (!TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_LOGIN)) {
            processLoginData((MemberVo) dataVo);
        } else if (url.contains(ApiConfig.API_PERSON_CENTER)) {
            processPersonData((CustomerBean) dataVo);
        }
    }

    /**
     * login
     *
     * @param tel
     * @param password
     */
    private void requestLogin(String tel, String password) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_LOGIN, new RequestParams()
                .put("tel", tel)
                .put("password", password)
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
     * process login data
     *
     * @param dataVo
     */
    private void processLoginData(MemberVo dataVo) {
        App.setCid(dataVo.cid);
        getPersonCenter(dataVo.cid);
    }

    /**
     * @param dataVo
     */
    private void processPersonData(CustomerBean dataVo) {
        App.setCustomerBean(dataVo);
        Helper.saveTel(etAccount.getText().toString().trim());
        startActivity(MainActivity.getIntent(_mActivity));
        getActivity().finish();
    }

    @OnClick({R.id.btn_login, R.id.ll_register,R.id.tv_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String tel = etAccount.getText().toString().trim();
                if (TextUtils.isEmpty(tel)) {
                    showToast(getString(R.string.a_input_tel));
                    return;
                }
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.a_input_password));
                    return;
                }
                requestLogin(tel, password);
                break;
            case R.id.ll_register:
                startActivity(RegisterActivity.getIntent(_mActivity));
                break;
            case R.id.tv_forget:
                startActivity(ChangePassWordActivity.getIntent(_mActivity,ChangePassWordActivity.TYPE_RESET_LOGIN_PASSWORD));
                break;
        }
    }


}
