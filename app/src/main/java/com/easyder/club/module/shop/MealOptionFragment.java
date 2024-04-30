package com.easyder.club.module.shop;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.wrapper.base.view.WrapperDialogFragment;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/7 14:20
 * Email: xcode126@126.com
 * Desc:
 */
public class MealOptionFragment extends WrapperDialogFragment<CommonPresenter> {

    @BindView(R.id.et_min_price)
    EditText etMinPrice;
    @BindView(R.id.et_max_price)
    EditText etMaxPrice;
    @BindView(R.id.et_keyword)
    EditText etKeyword;

    public static MealOptionFragment newInstance() {
        return new MealOptionFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.dialog_option_meal;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setWindowAnimations(R.style.TransDialogAnim);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                int minPrice = 0;
                int maxPrice = 0;
                String min = etMinPrice.getText().toString().trim();
                String max = etMaxPrice.getText().toString().trim();
                if (!TextUtils.isEmpty(max)) {
                    maxPrice = Integer.parseInt(max);
                }
                if (!TextUtils.isEmpty(min)) {
                    minPrice = Integer.parseInt(min);

                }
                String keyword = etKeyword.getText().toString().trim();
                if (optionResultCallback != null) {
                    optionResultCallback.optionResult(minPrice, maxPrice, keyword);
                }
                dismissAllowingStateLoss();
                break;
        }
    }

    public void setOptionResultCallback(OptionResultCallback optionResultCallback) {
        this.optionResultCallback = optionResultCallback;
    }

    private OptionResultCallback optionResultCallback;

    public interface OptionResultCallback {
        void optionResult(int tempMinPrice, int tempMaxPrice, String temKeyword);
    }
}
