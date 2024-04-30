package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.widget.DateTimeSelector;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.LogUtils;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 14:17
 * Email: xcode126@126.com
 * Desc: 立即订座
 */
public class AppointActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.view_dot_right)
    View viewDotRight;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.et_arrive_num)
    EditText etArriveNum;
    @BindView(R.id.iv_is_box)
    ImageView ivIsBox;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private int deptcode;
    private String deptname;

    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    public static Intent getIntent(Context context) {
        return new Intent(context, AppointActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_appoint;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        ImmersionBar.with(mActivity).statusBarView(statusBarView).statusBarDarkFont(true, 0.1f).init();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            deptcode = data.getIntExtra("deptcode", 0);
            deptname = data.getStringExtra("deptname");
            tvStore.setText(deptname);
        }
    }

    @OnClick({R.id.iv_left, R.id.tv_right, R.id.tv_store, R.id.iv_is_box, R.id.tv_time, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                startActivity(MyAppointActivity.getIntent(mActivity));
                break;
            case R.id.tv_store:
                startActivityForResult(StoreListActivity.getIntent(mActivity), 0x00);
                break;
            case R.id.iv_is_box:
                view.setSelected(!view.isSelected());
                break;
            case R.id.tv_time:
                showDatePicker();
//                tvTime.setText("2020-11-23 14:33");
                break;
            case R.id.btn_commit:
                goCommit();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_APPLY_APPOINT)) {
            processCommit(dataVo);
        }
    }

    /**
     * @param time
     * @param num
     * @param remark
     */
    private void requestCommit(int customercode, String customername, String time, String num, String remark) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_APPLY_APPOINT, new RequestParams()
                .putCid()
                .put("appointmenttime", time)
                .put("deptcode", deptcode)
                .put("deptname", deptname)
                .put("customercode", customercode)
                .put("customername", customername)
                .put("toshopnumber", num)
                .put("remark", remark)
                .put("box", ivIsBox.isSelected() ? 1 : 0)
                .get(), BaseVo.class);
    }

    /**
     * process commit
     *
     * @param dataVo
     */
    private void processCommit(BaseVo dataVo) {
        showToast(getString(R.string.a_operate_success));
        startActivity(MyAppointActivity.getIntent(mActivity));
        finish();
    }

    /**
     * go commit
     */
    private void goCommit() {
        String store = tvStore.getText().toString().trim();
        if (TextUtils.isEmpty(store)) {
            showToast(getString(R.string.a_input_appoint_store));
            return;
        }
        String num = etArriveNum.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            showToast(getString(R.string.a_input_arrive_num));
            return;
        }
        String time = tvTime.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            showToast(getString(R.string.a_input_arrive_time));
            return;
        }
        String remark = etRemark.getText().toString().trim();
        try {
            int customercode = App.getCustomer().customercode;
            String customername = App.getCustomer().customername;
            requestCommit(customercode, customername, time, num, remark);
        } catch (Exception e) {
            LogUtils.e("AppointActivity-goCommit-" + e.getMessage());
        }
    }

    /**
     * 展示日期选择
     */
    private void showDatePicker() {
        new DateTimeSelector(mActivity).addHelperAbsCallback((wrapper, dialog, helper) -> helper.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.tv_confirm:
                    tvTime.setText(((DateTimeSelector) wrapper).getSelectedDate());
                    break;
            }
            dialog.dismiss();
        }, R.id.tv_cancel, R.id.tv_confirm)).show();
    }

}
