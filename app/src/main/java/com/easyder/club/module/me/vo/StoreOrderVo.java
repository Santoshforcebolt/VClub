package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/11 19:10.
 * Email：xcode126@126.com
 * Desc：门店定单
 */

public class StoreOrderVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public double actualmoney;
        public int commentstate;
        public String deptname;
        public int itemnum;
        public String orderno;
        public int orderstate;
        public int ordertotalmoney;
        public int productnum;
        public List<DetailedlistBean> detailedlist;

        public static class DetailedlistBean {
            public double actualmoney;
            public int id;
            public String imgurl;
            public String itemtype;
            public int number;
            public String piname;
            public int price;
            public int subtotal;
        }
    }
}
