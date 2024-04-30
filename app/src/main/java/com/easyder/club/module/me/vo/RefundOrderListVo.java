package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/9 16:38
 * Email: xcode126@126.com
 * Desc:
 */
public class RefundOrderListVo extends BaseVo {
    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public int actualmoney;
        public int aftersalestype;
        public int customercode;
        public String customername;
        public String eid;
        public String imgurl;
        public String itemcode;
        public String itemname;
        public int number;
        public String onlineorderno;
        public String orderno;
        public int orderstate;
        public String ordertime;
        public String reason;
        public String expressname;
        public String expressno;
        public String reexpressname;
        public String reexpressno;
        public String remark;
        public List<ItemlistBean> itemlist;

        public static class ItemlistBean {
            public int number;
            public String picode;
            public String piname;
        }
    }
}
