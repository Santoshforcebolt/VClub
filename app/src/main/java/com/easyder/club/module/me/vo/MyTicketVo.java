package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/18 18:28.
 * Email：xcode126@126.com
 * Desc：优惠券
 */

public class MyTicketVo extends BaseVo {
    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String adddate;
        public String addtime;
        public String brand;
        public int couponcode;
        public String couponname;
        public String coupontype;
        public int customercode;
        public String customername;
        public int denomination;
        public int donation;
        public int effectivedaynum;
        public String effectiverangename;
        public String enddate;
        public String instancecode;
        public String showname;
        public String source;
        public int state;
        public int superposition;
        public String tel;
    }
}
