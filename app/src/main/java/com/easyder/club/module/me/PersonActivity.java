package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.me.vo.CountryListVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.widget.DateSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperPickerActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 11:30
 * Email: xcode126@126.com
 * Desc:
 */
public class PersonActivity extends WrapperPickerActivity<CommonPresenter> {

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.et_address)
    EditText etAddress;

    private String tel, customername;

    public static Intent getIntent(Context context) {
        return new Intent(context, PersonActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_person;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_person_msg));
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lines = etAddress.getLineCount();
                // 限制最大输入行数
                if (lines > 2) {
                    String str = s.toString();
                    int cursorStart = etAddress.getSelectionStart();
                    int cursorEnd = etAddress.getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                        str = str.substring(0, cursorStart-1) + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length()-1);
                    }
                    // setText会触发afterTextChanged的递归
                    etAddress.setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    etAddress.setSelection(etAddress.getText().length());
                }
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getPersonCenter();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_PERSON_CENTER)) {
            processPersonData((CustomerBean) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_PERSON)) {
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PERSON_CHANGE));
            finish();
        } else if (url.contains(ApiConfig.API_GET_COUNTRY)) {
            showCountryDialog((CountryListVo) dataVo);
        } else if (url.contains(ApiConfig.API_UPLOAD_HEADER)) {
            getPersonCenter();
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PERSON_CHANGE));
        }
    }

    /**
     * get person center
     */
    private void getPersonCenter() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_PERSON_CENTER, new RequestParams().putCid().get(), CustomerBean.class);
    }

    /**
     * @param sex           性别0：女1：男 2:未知
     * @param countryname
     * @param detailedaddre
     * @param email
     * @param nickname
     */
    private void changePerson(String sex, String countryname, String detailedaddre, String email, String nickname) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_PERSON, new RequestParams()
                .putCid()
                .put("customername", customername)
                .putWithoutEmpty("birthday", getBirthday())
                .putWithoutEmpty("birthdayyear", getBirthdayYear())
                .putWithoutEmpty("countryname", countryname)
                .putWithoutEmpty("detailedaddre", detailedaddre)
                .putWithoutEmpty("email", email)
                .putWithoutEmpty("nickname", nickname)
                .putWithoutEmpty("sex", (sex.equals(getString(R.string.a_man)) ? 1 : 0))
                .get(), BaseVo.class);
    }

    /**
     * get country
     */
    private void getCountry() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_COUNTRY, new RequestParams().putCid().get(), CountryListVo.class);
    }

    /**
     * upload picture
     *
     * @param image
     */
    private void requestUpload(String image) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_UPLOAD_HEADER, new RequestParams()
                .put("file", new File(image))
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * process person data
     *
     * @param customer
     */
    private void processPersonData(CustomerBean customer) {
        if (customer != null) {
            this.tel = customer.tel;
            this.customername = customer.customername;
            ImageManager.load(mActivity, ivHeader, customer.icourl, R.drawable.default_header, R.drawable.default_header);
            tvName.setText(customer.customername);
            etNick.setText(customer.nickname);
            etEmail.setText(customer.email);
            tvSex.setText(customer.sex == 0 ? getString(R.string.a_woman) : getString(R.string.a_man));
            tvBirthday.setText(String.format("%1$s-%2$s", customer.birthdayyear, customer.birthday));
            tvTel.setText(customer.tel);
            tvCountry.setText(customer.countryname);
            etAddress.setText(customer.detailedaddre);
//            tvLoginPassword.setText(customer.password);
//            tvPayPassword.setText(customer.paypassword);
        }
    }

    @OnClick({R.id.iv_header, R.id.tv_sex, R.id.tv_birthday, R.id.tv_tel,
            R.id.tv_login_change, R.id.tv_pay_change, R.id.tv_country, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                showPictureSelector(1, true, true);
                break;
            case R.id.tv_sex:
                showSexDialog();
                break;
            case R.id.tv_birthday:
                showDatePicker();
                break;
            case R.id.tv_tel:
                startActivity(ChangeTelActivity.getIntent(mActivity, tel));
                break;
            case R.id.tv_login_change:
                startActivity(ChangePassWordActivity.getIntent(mActivity, ChangePassWordActivity.TYPE_CHANGE_LOGIN_PASSWORD));
                break;
            case R.id.tv_pay_change:
                startActivity(ChangePassWordActivity.getIntent(mActivity, ChangePassWordActivity.TYPE_CHANGE_PAY_PASSWORD));
                break;
            case R.id.tv_country:
                getCountry();
                break;
            case R.id.btn_save:
                goSave();
                break;
        }
    }

    /**
     * go save
     */
    private void goSave() {
        String nick = etNick.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String country = tvCountry.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String sex = tvSex.getText().toString().trim();
        if (TextUtils.isEmpty(sex)) {
            showToast(getString(R.string.a_input_sex));
            return;
        }
        String birthday = tvBirthday.getText().toString().trim();
        if (TextUtils.isEmpty(birthday)) {
            showToast(getString(R.string.a_input_birthday));
            return;
        }
        changePerson(sex, country, address, email, nick);
    }

    @Override
    public void obtainMediaResult(List<LocalMedia> list, int type) {
        if (list != null && list.size() > 0) {
            LocalMedia localMedia = list.get(0);
            requestUpload(localMedia.getCompressPath());
        }
    }

    /**
     * @return
     */
    private String getBirthdayYear() {
        String birthday = tvBirthday.getText().toString().trim();
        if (!TextUtils.isEmpty(birthday) && birthday.length() >= 4) {
            return birthday.substring(0, 4);
        }
        return "";
    }

    /**
     * @return
     */
    private String getBirthday() {
        String birthday = tvBirthday.getText().toString().trim();
        if (!TextUtils.isEmpty(birthday) && birthday.length() >= 5) {
            return birthday.substring(5, birthday.length());
        }
        return "";
    }

    /**
     * 显示选择性别弹窗
     */
    private void showSexDialog() {
        new WrapperDialog(mActivity) {

            @Override
            public int getLayoutRes() {
                return R.layout.dialog_select_sex;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                dialog.setCanceledOnTouchOutside(false);
                setDialogAbsParams(dialog, 654, 430, Gravity.CENTER);
            }

            @Override
            public void help(final ViewHelper helper) {
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_man:
                                TextView tvMan = helper.getView(R.id.tv_man);
                                tvSex.setText(tvMan.getText());
                                break;
                            case R.id.tv_woman:
                                TextView tvWoman = helper.getView(R.id.tv_woman);
                                tvSex.setText(tvWoman.getText());
                                break;
                        }
                        dismiss();
                    }
                }, R.id.tv_man, R.id.tv_woman);
            }
        }.show();
    }

    /**
     * 展示日期选择
     */
    private void showDatePicker() {
        new DateSelector(mActivity).addHelperAbsCallback(new WrapperDialog.HelperAbsCallback() {
            @Override
            public void help(final WrapperDialog wrapper, final Dialog dialog, ViewHelper helper) {
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_confirm:
                                tvBirthday.setText(((DateSelector) wrapper).getSelectedDate());
                                break;
                        }
                        dialog.dismiss();
                    }
                }, R.id.tv_cancel, R.id.tv_confirm);
            }
        }).show();
    }

    /**
     * @param listVo
     */
    private void showCountryDialog(CountryListVo listVo) {
        new CountryDialog(mActivity, listVo, new CountryDialog.OnSelectListener() {
            @Override
            public void onResult(CountryListVo.RowsBean item) {
                if (item != null) {
                    tvCountry.setText(item.countryname);

                }
            }
        }).show();
    }
}
