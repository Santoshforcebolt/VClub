package com.sky.wrapper.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.sky.widget.autolayout.utils.AutoUtils;

/**
 * Author: sky on 2021/6/30 19:58
 * Email: xcode126@126.com
 * Desc:
 */
public class StringUtils {
    /**
     * 指定第一个文本内容大小
     * @param txt
     * @param size
     * @return
     */
    public static SpannableString getFirstTxtSize(String txt,int size){
        SpannableString string = new SpannableString(txt);
        string.setSpan(new AbsoluteSizeSpan(AutoUtils.getPercentWidthSize(size)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }
}
