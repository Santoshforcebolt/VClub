package com.easyder.club;

import android.os.Environment;

import java.io.File;

/**
 * Auther:  winds
 * Data:    2017/7/26
 * Version: 1.0
 * Desc:    APP设置信息管理类
 */

public class AppConfig {

    public final static String HOST = "https://admin.vclubs.co";
    public final static String HOST_TEST = "http://vclub.meiyeyi.com";

    /*******************************************************************************************************************/
    public final static String SHARE_URL = ApiConfig.HOST_F + "/share/signUp.html?promotercode=";
    public final static String SHARE_DETAIL_URL = "http://vclub.meiyeyi.com/share/shopDetail.html?promotercode=";//&product=
    /*******************************************************************************************************************/
    public final static String DEFAULT_APP_FOLDER = Environment.getExternalStorageDirectory() + File.separator
            + App.getInstance().getPackageName();
    public final static String DEFAULT_IMAGE_PATH = DEFAULT_APP_FOLDER + File.separator + "image";
    public final static String DEFAULT_LOG_PATH = DEFAULT_APP_FOLDER + File.separator + "log";
    public static final String DEFAULT_DOWNLOAD_APP = DEFAULT_APP_FOLDER + File.separator + "app"; //保存下载app的默认路径
    /*******************************************************************************************************************/

    //Glide图片缓存路径
    public static final String DEFAULT_CACHE_PATH = DEFAULT_APP_FOLDER + File.separator + "cache";
    public static final int DISK_CACHE_SIZE = 200 * 1024 * 1024;
    public static final int MAX_MEMORY_CACHE_SIZE = 5 * 1024 * 1024;
    public static final int PAGE_SIZE = 10;

    /*******************************************************************************************************************/
    public static final String PRODUCT_LIST = "/pages/index/goodList/index";//产品
    public static final String PROJECT_LIST = "/pages/index/projectList/index";//项目
    public static final String RECEIVE_TICKET_CENTER = "/pages/index/receiveCoupon/index";//领券中心
    public static final String INTEGRAL_SHOP = "/pages/user/integral/index";//积分商城
    public static final String LIMIT_BUY = "/pages/index/qiangBuy/index";//限时购
    public static final String ALL_MEAL = "/pages/index/packageList/index";//全部套餐
    public static final String APPOINT = "/pages/yuyue/yuyue";//预约
    /*******************************************************************************************************************/
    //售后类型 1：仅退款 2：退货退款 3：换货
    public static final int REFUND_EXIT_MONEY = 1;
    public static final int REFUND_EXIT_ALL = 2;
    public static final int REFUND_CHANGE_GOODS = 3;
    /*******************************************************************************************************************/
//    public static final String KEY_SECRET = "pk_test_51HkNoyJTGr8oEWSJhbiGpfNPUczU2CuGh0H3FQWVW8J7NQN4Uh1nDAkloYldxuUnj8pgJva6xkGP3fplxktuRdUN00zXrgCQN7";
    public static final String KEY_SECRET = "pk_live_vzI7JDIe2zz8FNwt5v0EyhJw00NHGhMXbN";
    public final static String BUGLY_ID = "21f58922bf";//bugly
    /*******************************************************************************************************************/


}
