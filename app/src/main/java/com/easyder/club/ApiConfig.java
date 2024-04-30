package com.easyder.club;

/**
 * Auther:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:    API地址管理类
 */


public class ApiConfig {
    public static String HOST_F = BuildConfig.FLAVOR.equals("official") ? AppConfig.HOST : AppConfig.HOST_TEST;
    public static String HOST = HOST_F + "/vclub-web/";
    //会员登录
    public static final String API_LOGIN = "api/mobile/wxapplogin.ed";
    //退出登录
    public static final String API_LOGOUT = "api/mobile/quitlogin.ed";
    //会员注册
    public static final String API_REGISTER = "api/mobile/registerCustomer.ed";
    //通用发送验证码
    public static final String API_GET_CODE = "api/mobile/sendRegisterVerifyCode.ed";
    //会员中心
    public static final String API_PERSON_CENTER = "api/mobile/customerCenter.ed";
    //修改个人资料
    public static final String API_CHANGE_PERSON = "api/mobile/updateCustomerBaseInfo.ed";
    //修改头像
    public static final String API_CHANGE_HEADER = "api/mobile/updateHead.ed";
    //图片上传通用接口
    public static final String API_UPLOAD_PICTURE = "api/mobile/uploadImage.ed";
    //修改头像
    public static final String API_UPLOAD_HEADER = "api/mobile/updateHead.ed";
    //修改手机号
    public static final String API_CHANGE_TEL = "api/mobile/updatetel.ed";
    //修改支付-登录密码
    public static final String API_RESET_PASS = "api/mobile/updatepassword.ed";
    //获取用户的收货地址列表
    public static final String API_ADDRESS_LIST = "api/mobile/addresseslist.ed";
    //添加收货地址
    public static final String API_ADD_ADDRESS = "api/mobile/insaddresses.ed";
    //删除收货地址
    public static final String API_DELETE_ADDRESS = "api/mobile/deladdresses.ed";
    //设置默认收货地址
    public static final String API_SET_DEFAULT_ADDRESS = "api/mobile/updateDefaultFlag.ed";
    //修改收货地址
    public static final String API_CHANGE_ADDRESS = "api/mobile/updaddresses.ed";
    //获取国家
    public static final String API_GET_COUNTRY = "api/mobile/getCountryList.ed";
    //查询全部省、市、区或者根据省、市的id查找下级地区
    public static final String API_GET_PROVINCE_CITY = "api/mobile/commonregionlist.ed";
    //商城订单列表
    public static final String API_SHOP_ORDER_LIST = "api/mobile/myOnlineOrderList.ed";
    //取消订单
    public static final String API_CANCEL_ORDER = "api/mobile/cancelOnlineOrder.ed";
    //订单明细
    public static final String API_ORDER_DETAIL = "api/mobile/onlineOrderDetail.ed";
    //确认收货
    public static final String API_CONFIRM_ORDER = "api/mobile/confirmReceived.ed";
    //获取新闻频道分类下的文章列表
    public static final String API_NEWS_LIST = "api/mobile/consultationPageDetailed.ed";
    //新闻详情
    public static final String API_NEWS_DETAIL = "api/mobile/articleDetails.ed";
    //新闻点赞
    public static final String API_NEWS_LIKE = "api/mobile/articlePraise.ed";
    //我的预约列表
    public static final String API_APPOINT_LIST = "api/mobile/appointmentorderlist.ed";
    //获取可预约的门店
    public static final String API_CAN_APPOINT_STORE = "api/mobile/appointmentorderselectshop.ed";
    //申请预约
    public static final String API_APPLY_APPOINT = "api/mobile/appointmentordersubmit.ed";
    //修改预约
    public static final String API_CANCEL_APPOINT = "api/mobile/appointmentordercancel.ed";
    //消息列表
    public static final String API_MESSAGE_LIST = "api/mobile/messageCenterList.ed";
    //消息详情
    public static final String API_MESSAGE_DETAIL = "api/mobile/messageCenterDetailed.ed";
    //评鉴列表
    public static final String API_ENJOY_LIST = "api/mobile/evaluationlist.ed";
    //发布评鉴
    public static final String API_PUBLISH_ENJOY = "api/mobile/evaluationsubmit.ed";
    //评鉴明细
    public static final String API_ENJOY_DETAIL = "api/mobile/evaluationdetail.ed";
    //(最爱-心愿)添加删除
    public static final String API_CHANGE_WISH = "api/mobile/updateevaluation.ed";
    //意见反馈提交
    public static final String API_FEEDBACK = "api/mobile/feedbackAdd.ed";
    //余额明细
    public static final String API_BALANCE_LIST = "api/mobile/accountDetailedList.ed";
    //门店订单列表
    public static final String API_STORE_ORDER_LIST = "api/mobile/mySaleOrderList.ed";
    //门店订单详情
    public static final String API_STORE_ORDER_DETAIL = "api/mobile/saleOrderDetail.ed";
    //我的优惠券列表
    public static final String API_MY_TICKET = "api/mobile/couponCashInstanceList.ed";
    //领劵中心页面
    public static final String API_GET_TICKET_CENTER = "api/mobile/couponCashList.ed";
    //会员领取优惠劵
    public static final String API_MEMBER_GET_TICKET = "api/mobile/customerReceiveCouponCash.ed";
    //查询可领券列表
    public static final String API_CAN_GET_TICKET = "api/mobile/receiveCouponCashList.ed";
    //商城首页接口
    public static final String API_SHOP_INDEX = "api/mobile/index.ed";
    //精品推荐接口
    public static final String API_SHOP_RECOMMEND = "api/mobile/IndexRecommenLists.ed";
    //推广收益
    public static final String API_SPREAD = "api/mobile/getextension.ed";
    //创建二维码
    public static final String API_CREATE_CODE = "api/mobile/createrqcode.ed";
    //已提取收益明细
    public static final String API_YET_EARNINGS_DETAIL = "api/mobile/revenuExtractionOrderList.ed";
    //推广会员列表
    public static final String API_SPREAD_LIST = "api/mobile/extensionlist.ed";
    //积分收益明细-推广订单列表
    public static final String API_SPREAD_ORDER_LIST = "api/mobile/commisslist.ed";
    //积分兑换产品列表
    public static final String API_SCORE_PRODUCT_LIST = "api/mobile/scoreProductList.ed";
    //兑换商品操作
    public static final String API_INTEGRAL_EXCHANGE = "api/mobile/exchangeScoreProduct.ed";
    //积分兑换查看商家列表
    public static final String API_PARTNER_ADDRESS_LIST = "api/mobile/partnerAddressList.ed";
    //积分获取列表
    public static final String API_SCORE_DETAIL = "api/mobile/scoreDetailedList.ed";
    //获取限时抢购商品明细信息
    public static final String API_LIMIT_BUY = "api/mobile/getTimeLimitActivityDetailed.ed";
    //积分兑换列表
    public static final String API_SCORE_EXCHANGE_HISTORY = "api/mobile/exchangeOrderDetailedList.ed";
    //商品详情
    public static final String API_GOODS_DETAIL = "api/mobile/getShopProductDetail.ed";
    //商品列表
    public static final String API_GOODS_LIST = "api/mobile/getShopProduct.ed";
    //获取产品或者项目分类
    public static final String API_GET_KIND = "api/mobile/getProductOrProjectGroup.ed";
    //获取品牌列表
    public static final String API_GET_BRAND = "api/mobile/brandlist.ed";
    //加入购物车
    public static final String API_ADD_CAR = "api/mobile/addShopCart.ed";
    //查询购物车
    public static final String API_QUERY_CAR = "api/mobile/queryShopCart.ed";
    //更新购物车数量
    public static final String API_UPDATE_CAR = "api/mobile/updateShopCart.ed";
    //订单价格计算
    public static final String API_CALCULATE_PRICE = "api/mobile/onlineOrderCalcFee.ed";
    //删除购物车
    public static final String API_DELETE_CAR = "api/mobile/deleteShopCart.ed";
    //统计购物车商品总数量
    public static final String API_GET_CAR_TOTAL = "api/mobile/countShopCartTotal.ed";
    //获取单号
    public static final String API_GET_ORDER_NO = "api/mobile/createOnlineOrderNo.ed";
    //创建订单
    public static final String API_COMMIT_ORDER = "api/mobile/createOnlineOrder.ed";
    //订单支付
    public static final String API_ONLINE_ORDER_PAYMENT = "api/mobile/onlineOrderPayment.ed";
    //收藏列表
    public static final String API_COLLECT_LIST = "api/mobile/customerCollectionList.ed";
    //修改藏酒
    public static final String API_CHANGE_COLLECT = "api/mobile/updateCustomerCollection.ed";
    //获取套餐列表
    public static final String API_MEAL_LIST = "api/mobile/packageGroupList.ed";
    //套餐详情
    public static final String API_MEAL_DETAIL = "api/mobile/packageGroupDetailed.ed";
    //收益获取单号
    public static final String API_GET_BALANCE_NO = "api/mobile/revenuExtractionOrderNo.ed";
    //创建收益提取单
    public static final String API_EXTRACTION_EARNINGS = "api/mobile/revenuExtractionAddOrder.ed";
    //计算售后金额
    public static final String API_AFTER_SALE_PRICE = "api/mobile/calAfterSalesPrice.ed";
    //提交售后申请
    public static final String API_APPLY_AFTER_SALE = "api/mobile/addAfterSalesOrder.ed";
    //售后订单列表
    public static final String API_REFUND_ORDER_LIST = "api/mobile/afterSalesOrderList.ed";
    //售后订单详情
    public static final String API_GET_REFUND_ORDER_DETAIL = "api/mobile/getAfterSalesOrderDetail.ed";
    // 售后:接口列表 售后取消
    public static final String API_AFTER_SALES_CANCEL = "api/mobile/afterSalesCancel.ed";
    //会员寄回快递,填写单号
    public static final String API_COMMIT_REFUND_EXPRESS_INFO = "api/mobile/backToExpress.ed";
    //售后单确认收货
    public static final String API_AFTER_SALE_COMPLETE = "api/mobile/afterSalesComplete.ed";
    //获取售后单号
    public static final String API_GET_APPLY_ORDER_NO = "api/mobile/getAfterSalesOrderNo.ed";
    //充值接口
    public static final String API_RECHARGE = "api/mobile/recharge.ed";
    //获取充值单号
    public static final String API_GET_RECHARGE_NO = "api/mobile/rechargeorderno.ed";
    //获取版本信息
    public static final String API_APP_CHECK_VERSION = "api/mobile/versionUpdate.ed";
    //删除评鉴
    public static final String API_DELETE_ENJOY = "api/mobile/deleteEvaluation.ed";

}
