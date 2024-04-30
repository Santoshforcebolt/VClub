package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;
import com.sky.wrapper.core.model.BaseVo;

import java.io.Serializable;
import java.util.List;

/**
 * Author: sky on 2020/12/2 21:41
 * Email: xcode126@126.com
 * Desc:
 */
public class OrderDetailVo extends BaseVo {

    public double actualmoney;
    public String address;
    public int cancelable;
    public Object canceldate;
    public Object canceltime;
    public String channeltradeno;
    public int commentid;
    public int customercode;
    public String customergradeinfo;
    public String customername;
    public String customertel;
    public int deptcode;
    public String deptname;
    public double discountmoney;
    public String expresscode;
    public String expresscompany;
    public double expressmoney;
    public int extractmoney;
    public int itemnum;
    public String masterid;
    public int newmoneyrate;
    public String orderdate;
    public String orderno;
    public int orderstate;
    public String ordertime;
    public String outtradeno;
    public String paychannel;
    public String paytime;
    public String paytype;
    public int productnum;
    public String receiver;
    public String receivetime;
    public String remark;
    public int score;
    public String sendtime;
    public String tel;
    public double totalmoney;
    public List<DetailedListBean> detailedList;
    public List<?> employeeAchievementCommissionDetails;

    public static class DetailedListBean extends BaseTempBean {
        public double activitydiscount;
        public double actualmoney;
        public AfterSalesOrderBean afterSalesOrder;
        public int calcFlag;
        public double coupondiscount;
        public double empdiscount;
        public double groupdiscount;
        public String id;
        public int isItemAftersalesAble;
        public String itemtype;
        public String masterid;
        public int number;
        public int applyNumber;
        public String orderno;
        public double originalprice;
        public String picode;
        public String piname;
        public String previewimg;
        public double price;
        public String pricetype;
        public String promotionid;
        public String promotionname;
        public String promotiontype;
        public int storedcarddiscount;
        public double totaldiscount;
        public double totalmoney;
        public String usestoredcardsituation;
        public double vipdiscount;
        public List<?> usestoredcard;
        public static class AfterSalesOrderBean implements Serializable {
            public String itemimgurl;
            public double actualmoney;
            public String remark;
            public String itemcode;
            public int orderstate;
            public String reexpressreceivename;
            public String expressno;
            public String eid;
            public String itemname;
            public String reexpressno;
            public String ordertime;
            public String expressname;
            public int number;
            public String onlineorderno;
            public String reexpressreceivetel;
            public int customercode;
            public String imgurl;
            public String reexpressreceiveaddress;
            public String orderno;
            public String customername;
            public String reexpressname;
            public String reason;
            public int aftersalestype;
            public List<?> itemlist;
            public List<?> imgurllist;
            public List<?> afterSalesOrderDetailedList;
        }
    }
}
