package com.sky.wrapper.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sky.widget.R;
import com.sky.widget.usage.ToastView;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTools {


//    public static void setEnvironment(Context context) {
//        Context base = context.getApplicationContext();
//        String name = PreferenceUtils.getPreference(base, AppConfig.LANG_NAME, AppConfig.LANG_VALUE_DEFAULT);
//        Locale locale = name.equals(AppConfig.LANG_VALUE_SIMPLE_CHINESE) ? Locale.SIMPLIFIED_CHINESE : Locale.ENGLISH;
//        DisplayMetrics dm = base.getResources().getDisplayMetrics();
//        Configuration config = base.getResources().getConfiguration();
//        // 应用用户选择语言
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            config.setLocale(locale);
//        } else {
//            config.locale = locale;
//        }
//        base.getResources().updateConfiguration(config, dm);
//    }

    /**
     * 隐藏电话中间4位
     *
     * @param mobile
     * @return
     */
    public static String replaceMobile(String mobile) {
        if (mobile.length() == 11) {
            return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return mobile;
    }

    /**
     * 判斷文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExist(String path) {
        try {
            File file = new File(path);
            if (file != null && file.exists()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 拼接字符串
     *
     * @param arg
     * @param args
     * @return 拼接完成的字符串
     */
    public static String join(Object arg, Object... args) {
        StringBuffer buffer = new StringBuffer(arg == null ? "" : arg.toString());
        for (Object s : args) {
            buffer.append(s == null ? "" : s.toString());
        }
        return buffer.toString();
    }

    /**
     * 截取目标数组指定长度的的数据
     *
     * @param data   目标数组
     * @param i      截取起始位置
     * @param length 截取长度
     * @return 截取到的数据
     */
    public static byte[] arrayCopy(byte[] data, int i, int length) {
        byte[] temp = new byte[length];
        System.arraycopy(data, i, temp, 0, length);
        return temp;
    }


    public String format2f(Double d) {
        return String.format("%1$.2f", d);
    }

    /**
     * 拼接多个数组
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 设置多种颜色的字体
     *
     * @param msg
     * @param color1
     * @param color2
     * @param pos    两种颜色的分隔位置
     * @return
     */
    public static SpannableStringBuilder setColorful(String msg, int color1, int color2, int pos) {
        SpannableStringBuilder builder = new SpannableStringBuilder(msg);
        builder.setSpan(new ForegroundColorSpan(color1), 0, pos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color2), pos + 1, msg.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    /**
     * 匹配手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean matcherMobile(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.length() >= 5;
    }

    public static boolean matcherEmail(String email) {
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 获取当前的页面
     *
     * @param count    总数
     * @param pagesize 一页含有多少
     * @return
     */
    public static int getTotalPage(int count, int pagesize) {
        return count % pagesize == 0 ? count / pagesize : count / pagesize + 1;
    }

    /**
     * 吐司提示
     */
    public static Toast mToast;

    public static void showTips(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }

        mToast.setText(text);
        mToast.show();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获得格式化后的当前时间
     *
     * @param time
     * @return
     */
    public static SimpleDateFormat sdf;

    public static String getFormatTime(long time) {
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern("yyyy-MM-dd");
        String format = sdf.format(time);
        return format;
    }

    /**
     * 获得格式化后的当前时间
     *
     * @return
     */
    public static String getFormatTime(String format) {
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(format);
        return sdf.format(System.currentTimeMillis());
    }


    public static String getFormatTime(String pattern, Object time) {
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(pattern);
        return sdf.format(Long.parseLong(String.valueOf(time)));
    }

    /**
     * 把已经格式化好的时间转换成其他格式的时间
     *
     * @param oldPattern
     * @param newPattern
     * @param time
     * @return
     */
    public static String getFormatTime(String oldPattern, String newPattern, String time) {
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        try {
            sdf.applyPattern(oldPattern);
            Date date = sdf.parse(time);
            sdf.applyPattern(newPattern);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * String类型时间转长整型
     *
     * @param pattern
     * @param time
     * @return
     */
    public static long getLongTime(String pattern, String time) {
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        sdf.applyPattern(pattern);
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Date转化为String
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(date);
    }


    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    /**
     * 判断当前是否是有效Integer类型数据
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[1-9][\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断当前网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param clazz   要判断的服务的class
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> clazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningServiceInfo> services = manager.getRunningServices(100);
        for (int i = 0; i < services.size(); i++) {
            String className = services.get(i).service.getClassName();
            if (className.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备编号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // 显示或者隐藏输入法
    private void toggleSoftInput(Context context) {
        InputMethodManager imm = null;
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param view
     */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * copy content
     *
     * @param context
     * @param content
     */
    public static void copyContent(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager cManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cManager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**校验过程：
     1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * 检查网络是否已经连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * @param bankCard
     * @return
     */
    public static String splitNumber(String bankCard) {
        if (TextUtils.isEmpty(bankCard)) {
            return bankCard;
        }
        String regex = "(.{4})";
        return bankCard.replaceAll(regex, "$1 ");
    }

    /**
     * 检查是否安装了xxx
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo n = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * @param phonenum
     * @return
     */
    public static String getEncryptTel(String phonenum) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(phonenum) && phonenum.length() > 6) {
            for (int i = 0; i < phonenum.length(); i++) {
                char c = phonenum.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString().trim();
    }

    /**
     * 跳转浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            LogUtils.d("sky = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, UIUtils.getString(context, R.string.a_please_choose_browser)));
        } else {
            ToastView.showToast(context, UIUtils.getString(context, R.string.a_skip_browser_error_tip));
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}
