package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/19 15:32
 * Email: xcode126@126.com
 * Desc:
 */
public class YetEarningsVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String bankcardno;
        public String bankname;
        public double commismoney;
        public int customercode;
        public String customername;
        public double extractionratio;
        public int extractiontype;
        public String masterid;
        public String orderdate;
        public String orderno;
        public int orderstate;
        public String ordertime;
        public Object scoreorwallet;
        public String tel;
        public String username;
    }
}
