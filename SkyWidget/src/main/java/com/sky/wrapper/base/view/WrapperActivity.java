package com.sky.wrapper.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.R;
import com.sky.widget.autolayout.AutoFrameLayout;
import com.sky.widget.autolayout.AutoLinearLayout;
import com.sky.widget.autolayout.AutoRelativeLayout;
import com.sky.widget.autolayout.widget.AutoCardView;
import com.sky.widget.autolayout.widget.AutoRadioGroup;
import com.sky.widget.usage.TitleView;
import com.sky.widget.usage.ToastView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.utils.LanguageUtils;

import java.util.Locale;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

import static com.sky.wrapper.utils.SupportLanguageUtil.getSupportLanguage;


/**
 * Auther:  winds
 * Data:    2017/4/11
 * Desc:
 */

public abstract class WrapperActivity extends SupportActivity {
    private boolean focus = true;  //自动显示和隐藏输入法
    private long lastTime;
    private boolean interceptable;  //是否拦截快速点击事件

    protected LinearLayout root;
    protected LinearLayout layoutTitle;
    protected TitleView titleView;
    protected FrameLayout container;
    protected Activity mActivity;
    protected View contentView;
    public View statusBarView;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_RADIOGROUP = "RadioGroup";
    private static final String LAYOUT_CARDVIEW = "CardView";

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        String currentLanguage = LanguageUtils.getCurrentLanguage(newBase);
//        super.attachBaseContext(LanguageUtils.attachBaseContext(newBase, currentLanguage));
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        Integer language = SpUtil.getInt(newBase, Cons.SP_KEY_OF_CHOOSED_LANGUAGE, -1);
        String language = LanguageUtils.getCurrentLanguage(newBase);
        Context context = attachBaseContext(newBase, language);
        final Configuration configuration = context.getResources().getConfiguration();
        // 此处的ContextThemeWrapper是androidx.appcompat.view包下的
        // 你也可以使用android.view.ContextThemeWrapper，但是使用该对象最低只兼容到API 17
        // 所以使用 androidx.appcompat.view.ContextThemeWrapper省心
        final ContextThemeWrapper wrappedContext = new ContextThemeWrapper(context,
                R.style.Theme_AppCompat_Empty) {
            @Override
            public void applyOverrideConfiguration(Configuration overrideConfiguration) {
                if (overrideConfiguration != null) {
                    overrideConfiguration.setTo(configuration);
                }
                super.applyOverrideConfiguration(overrideConfiguration);
            }
        };
        super.attachBaseContext(wrappedContext);
    }

    // 下面是多语言切换方法
    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            return context;
        }
    }

    /**
     *
     * @param context
     * @param language
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Resources resources = context.getResources();
        final Configuration configuration = resources.getConfiguration();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        Locale locale;
        if (TextUtils.isEmpty(language)) {
            // 如果没有指定语言使用系统首选语言
            locale = getSystemPreferredLanguage();
        } else {
            // 指定了语言使用指定语言，没有则使用首选语言
            locale = getSupportLanguage(language);
        }
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, dm);
        return context;
    }

    /**
     *
     * @param context
     * @param newLanguage
     */
    public static void applyLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getSupportLanguage(newLanguage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DisplayMetrics dm = resources.getDisplayMetrics();
            // apply locale
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, dm);
        } else {
            // updateConfiguration
            DisplayMetrics dm = resources.getDisplayMetrics();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, dm);
        }
    }

    /**
     * 获取系统首选语言
     *
     * @return Locale
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Locale getSystemPreferredLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        } else if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        } else if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        } else if (name.equals(LAYOUT_RADIOGROUP)) {
            view = new AutoRadioGroup(context, attrs);
        } else if (name.equals(LAYOUT_CARDVIEW)) {
            view = new AutoCardView(context, attrs);
        }
//        else if (name.equals(LAYOUT_COORDINATORLAYOUT)){
//            view = new AutoCoordinatorLayout(context, attrs);
//        }
        if (view != null) {
            return view;
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeOnCreate();
        setContentView(R.layout.activity_wrapper);

        initView();
        initTitleView();
        initContentView();
        initImmersionBar();
        initView(savedInstanceState, titleView, getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ImmersionBar.with(this).destroy();
    }

    private void initView() {
        mActivity = this;
        root = findViewById(R.id.root);
        layoutTitle = findViewById(R.id.layout_title);
        titleView = findViewById(R.id.mTitleView);
        container = findViewById(R.id.container);
        statusBarView = findViewById(R.id.status_bar_view);
    }

    private void initTitleView() {
        if (!isUseTitle()) {
            titleView.setVisibility(View.GONE);
            layoutTitle.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
            AutoLinearLayout.LayoutParams layoutParams = (AutoLinearLayout.LayoutParams) container.getLayoutParams();
            layoutParams.topMargin = 0;
        } else {
            layoutTitle.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
            titleView.setChildClickListener(R.id.iv_title_left, onBackClick());
        }
    }

    protected boolean isImmersionBarEnable() {
        return true;
    }

    protected void initImmersionBar() {
        if (isImmersionBarEnable()) {
            setImmersionBar();
        }
    }

    protected void setImmersionBar() {
        if (isUseTitle()) {
            ImmersionBar.with(this)
                    .statusBarView(R.id.status_bar_view)
                    .statusBarDarkFont(true)
                    .init();
        } else {
            ImmersionBar.with(this).init();
        }
//
//        ImmersionBar.with(this)
//                .statusBarView(statusBarView)
//                .statusBarDarkFont(true)
//                .init();

//        if (ImmersionBar.isSupportStatusBarDarkFont()) {
//            ImmersionBar.with(this)
//                    .statusBarView(R.id.status_bar_view)
//                    .statusBarDarkFont(true)
//                    .init();
//        } else {
//            View view = findViewById(R.id.status_bar_view);
//            view.setBackgroundResource(R.drawable.shape_bg_title);
//            ImmersionBar.with(this)
//                    .statusBarView(view).init();
//        }
    }

    /**
     * 标题返回按钮 如需重写 覆盖此方法即可
     *
     * @return
     */
    protected View.OnClickListener onBackClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        };
    }

    protected void initContentView() {
        if (getViewLayout() != 0 && getViewLayout() != -1) {
            contentView = View.inflate(this, getViewLayout(), null);
            addContentView(contentView);
        }

        ButterKnife.bind(this);
    }

    protected View getContentView() {
        return contentView;
    }

    protected void addContentView(View view) {
        container.addView(view);
    }

    protected LinearLayout getRootView() {
        return root;
    }

    protected boolean isUseTitle() {
        return true;
    }

    protected void setTitleDivider(boolean isShow) {
        layoutTitle.setShowDividers(isShow ? LinearLayout.SHOW_DIVIDER_MIDDLE : LinearLayout.SHOW_DIVIDER_NONE);
    }

    protected void beforeOnCreate() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //去除默认actionbar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//统一管理竖屏
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决键盘覆盖输入框问题,可能需要具体写到每个界面
    }

    protected View getContainerView() {
        return container;
    }

    protected TitleView getTitleView() {
        return titleView;
    }

    /**
     * 返回值为0或者-1时不添加
     *
     * @return
     */
    protected abstract int getViewLayout();

    protected abstract void initView(Bundle savedInstanceState, TitleView titleView, Intent intent);

    /**
     * 设置是否拦截快速点击
     *
     * @param interceptable 默认拦截   设置不拦截请设置为 false
     */
    protected void setInterceptable(boolean interceptable) {
        this.interceptable = !interceptable;
    }

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    public boolean isInvalidClick() {
        long time = System.currentTimeMillis();
        long duration = time - lastTime;
        if (duration < 400) {
            return true;
        } else {
            lastTime = time;
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //快速点击拦截
        if (!interceptable && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isInvalidClick()) {
                return true;
            }
        }
        //键盘拦截判断
        if (focus) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    hideKeyboard(v);
                }
                return super.dispatchTouchEvent(ev);
            }
            // 其他组件响应点击事件
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 判断键盘是否应该隐藏
     * 点击除EditText的区域隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                ((EditText) v).setCursorVisible(true);
                return false;
            } else {
                ((EditText) v).setCursorVisible(false);  //隐藏光标
                return true;
            }
        }
        return false;
    }

    /**
     * 设置键盘显否
     *
     * @param v
     * @param visible
     */
    protected void setKeyboardVisible(View v, boolean visible) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (visible) {
                imm.showSoftInput(v, 0);
            } else {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    /**
     * 实例化对应layoutId的view同时生成ViewHelper
     * 使用此方法 需要考虑context的生命周期 避免内存泄漏 此方式默认使用的activity的context对象
     *
     * @param group    可为null
     * @param layoutId
     * @param listener
     * @return
     */
    protected View getHelperView(ViewGroup group, int layoutId, OnViewHelper listener) {
        ViewHelper helper = new ViewHelper(getLayoutInflater().inflate(layoutId, group == null ? null : group instanceof RecyclerView ? (ViewGroup) group.getParent() : group, false));
        if (listener != null) {
            listener.help(helper);
        }
        return helper.getItemView();
    }

    /**
     * 通过context 实例化对应layoutId的view同时生成ViewHelper  使用此方法 需要考虑context的生命周期 避免内存泄漏
     * 对生命周期和activity不一致的 使用application的context对象 其余可直接使用 (link#getHelperView的重载方法，如上)
     *
     * @param context
     * @param group
     * @param layoutId
     * @param listener
     * @return
     */
    protected View getHelperView(Context context, ViewGroup group, int layoutId, OnViewHelper listener) {
        ViewHelper helper = new ViewHelper(LayoutInflater.from(context).inflate(layoutId, group == null ? null : group instanceof RecyclerView ? (ViewGroup) group.getParent() : group, false));
        if (listener != null) {
            listener.help(helper);
        }
        return helper.getItemView();
    }


    /**
     * 设置自动隐藏输入法
     *
     * @param focus 默认 true 自动隐藏
     */
    protected void setAutoFocus(boolean focus) {
        this.focus = focus;
    }

    /**
     * 普通Toast提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastView.showToastInCenter(mActivity, msg, Toast.LENGTH_LONG);
    }

    /**
     * 图文样式的Toast提示
     *
     * @param msg
     * @param resId
     */
    protected void showToast(String msg, int resId) {
        ToastView.showVerticalToast(mActivity, msg, resId);
    }
}
