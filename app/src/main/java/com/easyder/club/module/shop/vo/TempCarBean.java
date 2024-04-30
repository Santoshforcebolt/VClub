package com.easyder.club.module.shop.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

/**
 * Author: sky on 2020/11/26 18:00
 * Email: xcode126@126.com
 * Desc:
 */
public class TempCarBean extends BaseTempBean {
    public String itemcode;
    public int number;
    public String itemtype;

    public TempCarBean(String itemcode, int number, String itemtype) {
        this.itemcode = itemcode;
        this.number = number;
        this.itemtype = itemtype;
    }
}
