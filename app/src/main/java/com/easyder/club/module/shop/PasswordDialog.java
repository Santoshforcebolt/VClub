package com.easyder.club.module.shop;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatEditText;

import com.easyder.club.R;
import com.sky.widget.usage.ToastView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;

/**
 * Author: sky on 2020/11/30 15:59
 * Email: xcode126@126.com
 * Desc:
 */
public class PasswordDialog extends WrapperDialog {

    private Context context;

    public PasswordDialog(Context context) {
        super(context);
        this.context = context;
    }

    public PasswordDialog(Context context, PassWordListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_password;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        setDialogAbsParams(dialog, 622, 340, Gravity.CENTER);

    }

    @Override
    public void help(ViewHelper helper) {
        final AppCompatEditText etPassWord = helper.getView(R.id.et_password);
        helper.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    String trim = etPassWord.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        ToastView.showToast(context, context.getString(R.string.a_input_pay_password));
                        return;
                    }
                    if (listener != null) {
                        listener.onReturnPassWord(trim);
                    }
                    break;
            }
            dismiss();
        }, R.id.btn_cancel, R.id.btn_confirm);
    }

    public PassWordListener listener;

    public interface PassWordListener {
        void onReturnPassWord(String passWord);
    }
}
