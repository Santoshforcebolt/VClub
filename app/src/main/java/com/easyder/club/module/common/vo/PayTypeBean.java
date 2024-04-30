package com.easyder.club.module.common.vo;

/**
 * Author: sky on 2020/12/8 18:07
 * Email: xcode126@126.com
 * Desc:
 */
public class PayTypeBean extends BaseTempBean {

    public static final String PAY_TYPE_WALLET = "ewallet";
    public static final String PAY_TYPE_VISA = "visa";
    public static final String PAY_TYPE_CARD = "master";
    public static final String PAY_TYPE_CREDIT = "minipay";

    public int image;//图标
    public String payName;  //支付类型名称
    public String payCode;  //支付代号
    public double balacne;//钱包余额

    public PayTypeBean(int image, String payName, String payCode, double balacne) {
        this.image = image;
        this.payName = payName;
        this.payCode = payCode;
        this.balacne = balacne;
    }
}
