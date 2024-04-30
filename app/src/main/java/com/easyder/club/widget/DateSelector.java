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
 * Desc：日期选择器：年月日
 */

public class DateSelector extends WrapperDialog {

    List<String> yearList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<String> dayList = new ArrayList<>();

    public DateSelector(Context context) {
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

            final WheelView wvDay = helper.getView(R.id.wv_day);
            wvDay.setOffset(2);
            wvDay.setItems(getDayList(year, month));
            final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            wvDay.setSeletion(dayList.indexOf(String.format("%1$02d", day)));

            wvYear.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

                @Override
                public void onSelected(int selectedIndex, String item) {
                    int year = Integer.parseInt(yearList.get(wvYear.getSeletedIndex()));
                    int month = Integer.parseInt(monthList.get(wvMonth.getSeletedIndex()));
                    int daySize = getDaySize(year, month);
                    if (daySize != dayList.size()) {
                        wvDay.setItems(getDayList(year, month));
                    }
                }
            });

            wvMonth.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    int year = Integer.parseInt(yearList.get(wvYear.getSeletedIndex()));
                    int month = Integer.parseInt(monthList.get(wvMonth.getSeletedIndex()));
                    int daySize = getDaySize(year, month);
                    if (daySize != dayList.size()) {
                        wvDay.setItems(getDayList(year, month));
                    }
                }
            });

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
            final WheelView wvDay = helper.getView(R.id.wv_day);
            return String.format("%1$s-%2$s-%3$s", wvYear.getSeletedItem(), wvMonth.getSeletedItem(), wvDay.getSeletedItem());
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

    private int getDaySize(int year, int month) {
        int daySize;
        if (month == 2) {
            daySize = isLeapYear(year) ? 29 : 28;
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            daySize = 31;
        } else {
            daySize = 30;
        }
        return daySize;
    }

    private List<String> getDayList(int year, int month) {
        dayList.clear();
        int daySize = getDaySize(year, month);
        for (int i = 1; i <= daySize; i++) {
            dayList.add(String.format("%1$02d", i));
        }
        return dayList;
    }

    /**
     * 判断是否时闰年
     *
     * @param year
     * @return
     */
    private boolean isLeapYear(int year) {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            return true;
        } else {
            return false;
        }
    }
}
