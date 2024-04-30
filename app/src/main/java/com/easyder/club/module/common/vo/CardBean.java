package com.easyder.club.module.common.vo;

import com.stripe.android.model.Card;

/**
 * Author: sky on 2020/12/17 11:53
 * Email: xcode126@126.com
 * Desc:
 */
public class CardBean extends BaseTempBean{
    public String number;
    public int expMonth;
    public int expYear;
    public String cvc;

    public CardBean(String number, int expMonth, int expYear, String cvc) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = cvc;
    }

    public CardBean() {
    }
}
