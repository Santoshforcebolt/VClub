package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/24 11:51
 * Email: xcode126@126.com
 * Desc:
 */
public class BalanceVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public double afternewmoney;
        public double afteroldmoney;
        public String changetime;
        public String changetype;
        public int customercode;
        public String describes;
        public double frontnowmoney;
        public double frontoldmoney;
        public int id;
        public String masterid;
        public double newmoney;
        public double oldmoney;
        public String orderno;
        public String remark;
        public String showval;
    }
}
