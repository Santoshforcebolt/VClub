package com.easyder.club.module.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import com.easyder.club.R;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;

/**
 * Author: sky on 2020/7/14 11:43.
 * Email:xcode126@126.com
 * Desc:
 */
public class ConfirmDialog extends WrapperDialog {

    public ConfirmDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        setDialogAbsParams(dialog, 586, 486, Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Override
    public void help(ViewHelper helper) {
        helper.setOnClickListener(v -> {
            dismiss();
        },  R.id.btn_cancel, R.id.iv_close);
    }
}
