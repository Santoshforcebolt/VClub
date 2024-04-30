package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

/**
 * Author: sky on 2020/12/9 17:29
 * Email: xcode126@126.com
 * Desc:
 */
public class RefundOrderDetailVo extends BaseVo {
    public AfterSalesOrderBean afterSalesOrder;
    public AfterSalesReturnAddress afterSalesReturnAddress;
    public ReturnDeptInfo returnDeptInfo;

    public static class AfterSalesOrderBean {
        public double actualmoney;
        public int aftersalestype;
        public int customercode;
        public String customername;
        public String eid;
        public String expressname;
        public String expressno;
        public String imgurl;
        public String itemcode;
        public String itemname;
        public int number;
        public String onlineorderno;
        public String orderno;
        public int orderstate;
        public String ordertime;
        public String reason;
        public String reexpressname;
        public String reexpressno;
        public String remark;
        public int returntype;
    }

    public static class AfterSalesReturnAddress {
        public String address;
        public String addresseetel;
        public String addresseename;
    }

    public static class ReturnDeptInfo {
        public String addr;
        public String deptname;
        public String tel;
    }
}
