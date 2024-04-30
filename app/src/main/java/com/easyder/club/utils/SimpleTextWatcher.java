package com.easyder.club.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Author: sky on 2020/11/18 17:39
 * Email: xcode126@126.com
 * Desc:
 */

public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
