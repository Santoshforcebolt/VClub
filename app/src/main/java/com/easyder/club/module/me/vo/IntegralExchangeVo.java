package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/26 17:12
 * Email: xcode126@126.com
 * Desc:
 */
public class IntegralExchangeVo extends BaseVo {
    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public int id;
        public String imgurl;
        public String itemcode;
        public String itemtype;
        public String masterid;
        public String number;
        public String orderno;
        public String ordertime;
        public String score;
        public int scoreproductcode;
        public String scoreproductname;
        public int totalscore;
    }
}
