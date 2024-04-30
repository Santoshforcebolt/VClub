package com.easyder.club.module.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;

import com.easyder.club.R;
import com.easyder.club.module.me.LoginActivity;
import com.sky.wrapper.base.view.BaseActivity;
import com.sky.wrapper.utils.LanguageUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:  winds
 * Data:    2018/3/9
 * Version: 1.0
 * Desc:
 */


public class AlertActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, AlertActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        String currentLanguage = LanguageUtils.getCurrentLanguage(newBase);
//        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase, currentLanguage));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //去除默认actionbar
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
    }

//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
//
//    @Subscribe
//    public void loginChanged(LoginChanged changed) {
//        if (changed.login) {
//            finish();
//        }
//    }

    @OnClick({R.id.tv_login})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(LoginActivity.getIntent(this,true));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressedSupport();
    }

}
