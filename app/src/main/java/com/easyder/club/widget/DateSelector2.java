package com.easyder.club.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.easyder.club.R;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.usage.wheel.WheelView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Author：sky on 2019/6/13 16:02.
 * Email：xcode126@126.com
 * Desc：日期选择器:年月
 */

public class DateSelector2 extends WrapperDialog {

    List<String> yearList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();

    public DateSelector2(Context context) {
        super(context, R.style.AlertTipsDialogTheme);

        setHelperCallback((dialog, helper) -> {
            final WheelView wvYear = helper.getView(R.id.wv_year);
            wvYear.setOffset(2);
            wvYear.setItems(getYearList());
            final int year = Calendar.getInstance().get(Calendar.YEAR);
            wvYear.setSeletion(yearList.indexOf(String.valueOf(year)));

            final WheelView wvMonth = helper.getView(R.id.wv_month);
            wvMonth.setOffset(2);
            wvMonth.setItems(getMonthList());
            final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            wvMonth.setSeletion(monthList.indexOf(String.format("%1$02d", month)));

            helper.setViewGone(R.id.wv_day);
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_date_selector;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        dialog.getWindow().setWindowAnimations(R.style.AlertTipsDialogTheme);
        setDialogParams(dialog, AutoUtils.getPercentHeightSize(650), AutoUtils.getPercentHeightSize(750), Gravity.CENTER);
    }

    @Override
    public void help(ViewHelper helper) {

    }

    public String getSelectedDate() {
        if (helper != null) {
            final WheelView wvYear = helper.getView(R.id.wv_year);
            final WheelView wvMonth = helper.getView(R.id.wv_month);
            return String.format("%1$s-%2$s", wvYear.getSeletedItem(), wvMonth.getSeletedItem());
        }
        return null;
    }

    private List<String> getYearList() {
        yearList.clear();
        for (int i = 1980; i <= 2050; i++) {
            yearList.add(String.valueOf(i));
        }
        return yearList;
    }

    private List<String> getMonthList() {
        monthList.clear();
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.format("%1$02d", i));
        }
        return monthList;
    }
}
