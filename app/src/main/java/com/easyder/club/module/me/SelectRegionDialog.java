package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.easyder.club.R;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;

/**
 * Author：sky on 2019/6/12 14:34.
 * Email：xcode126@126.com
 * Desc：选择省市区
 */

public class SelectRegionDialog extends WrapperDialog {

    public SelectRegionDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_select_address;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        setDialogParams(dialog, WindowManager.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(750), Gravity.BOTTOM);
    }

    @Override
    public void help(ViewHelper helper) {
        helper.setOnClickListener(R.id.iv_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public WrapperDialog show() {
        Window window = dialog.getWindow();
        if (window!=null){
            window.setWindowAnimations(R.style.TransDialogAnim);
        }
        return super.show();
    }
}
