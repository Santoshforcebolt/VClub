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
 * Desc：日期选择器
 */

public class DateTimeSelector extends WrapperDialog {

    List<String> yearList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<String> dayList = new ArrayList<>();
    List<String> hourList = new ArrayList<>();
    List<String> minuteList = new ArrayList<>();

    public DateTimeSelector(Context context) {
        super(context, R.style.AlertTipsDialogTheme);

        setHelperCallback(new HelperCallback() {
            @Override
            public void help(Dialog dialog, ViewHelper helper) {
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

                final WheelView wvHour = helper.getView(R.id.wv_hour);
                wvHour.setOffset(2);
                wvHour.setItems(getHourList());
                wvHour.setSeletion(0);

                final WheelView wvMinute = helper.getView(R.id.wv_minute);
                wvMinute.setOffset(2);
                wvMinute.setItems(getMinuteList());
                wvMinute.setSeletion(0);

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

//                wvDay.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
//                    @Override
//                    public void onSelected(int selectedIndex, String item) {
//
//                    }
//                });

            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_date_time_selector;
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
            final WheelView wvHour = helper.getView(R.id.wv_hour);
            final WheelView wvMinute = helper.getView(R.id.wv_minute);
            return String.format("%1$s-%2$s-%3$s %4$s:%5$s:00", wvYear.getSeletedItem(), wvMonth.getSeletedItem(),
                    wvDay.getSeletedItem(), wvHour.getSeletedItem(), wvMinute.getSeletedItem());
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

    private List<String> getDayList(int year, int month) {
        dayList.clear();
        int daySize = getDaySize(year, month);
        for (int i = 1; i <= daySize; i++) {
            dayList.add(String.format("%1$02d", i));
        }
        return dayList;
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

    private List<String> getHourList() {
        hourList.clear();
        for (int i = 0; i <= 23; i++) {
            hourList.add(String.format("%1$02d", i));
        }
        return hourList;
    }

    public List<String> getMinuteList() {
        minuteList.clear();
        for (int i = 1; i <= 59; i++) {
            minuteList.add(String.format("%1$02d", i));
        }
        return minuteList;
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
