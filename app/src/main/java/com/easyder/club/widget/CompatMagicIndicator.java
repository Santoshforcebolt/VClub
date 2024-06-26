package com.easyder.club.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.easyder.club.utils.IndicatorHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * Auther:  winds
 * Data:    2018/3/3
 * Version: 1.0
 * Desc:
 */


public class CompatMagicIndicator extends MagicIndicator {

    protected IndicatorHelper helper;

    public CompatMagicIndicator(Context context) {
        this(context, null);
    }

    public CompatMagicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    void init() {
        helper = new IndicatorHelper(this);
    }

    public IndicatorHelper getIndicatorHelper() {
        return helper;
    }

    public CompatBedgeTitleView getCompatBedgeTitleView(int index) {
        IPagerNavigator navigator = getNavigator();
        if (navigator != null && navigator instanceof CommonNavigator) {
            IPagerTitleView view = ((CommonNavigator) navigator).getPagerTitleView(index);
            if (view != null && view instanceof CompatBedgeTitleView) {
                return (CompatBedgeTitleView) view;
            }
        }
        return null;
    }
}
