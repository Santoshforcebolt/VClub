package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

/**
 * Author: sky on 2020/12/9 15:42
 * Email: xcode126@126.com
 * Desc: 计算售后价格的bean
 */
public class TempAfterSaleBean extends BaseTempBean {
    public String picode;
    public String pitype;
    public int number;
    public String orderdetailed;

    public TempAfterSaleBean(String picode, int number, String orderdetailed,String pitype) {
        this.picode = picode;
        this.number = number;
        this.orderdetailed = orderdetailed;
        this.pitype = pitype;
    }
}
