package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/10 10:09
 * Email: xcode126@126.com
 * Desc:
 */
public class StoreOrderDetailVo extends BaseVo {

    public String achieempnames;
    public double actualmoney;
    public String addr;
    public int alipaymoney;
    public int birthdaydiscount;
    public int birthdaydiscountmoney;
    public int cashmoney;
    public int commentstate;
    public int count;
    public int coupondiscountmoney;
    public Object customer;
    public int customercode;
    public String customergrade;
    public String customername;
    public String customertype;
    public String days;
    public int deptcode;
    public int deptcode2;
    public int deptdiscount;
    public String deptname;
    public String deptname2;
    public int deptscore;
    public int discount;
    public String editPriceToken;
    public Object editactualmoney;
    public String emp;
    public int emppricediscountmoney;
    public int extractmoney;
    public String greadname;
    public int isrefund;
    public int itemdiscount;
    public String itemnams;
    public int itemnum;
    public String itemtype;
    public String masterid;
    public boolean needEditPriceToken;
    public int newaccountmoney;
    public String nickname;
    public int oldaccountmoney;
    public String online;
    public String orderdate;
    public String orderno;
    public String ordersource;
    public int orderstate;
    public String ordertime;
    public double ordertotalmoney;
    public int originalactualmoney;
    public int palipaymoney;
    public String paytype;
    public Object priceMap;
    public int productdiscount;
    public String productname;
    public int productnum;
    public int proudctcount;
    public int pwechatmoney;
    public int receivablesmoney;
    public String remark;
    public int restmoney;
    public String reversedate;
    public String reverseemp;
    public String reversereason;
    public String reversetime;
    public int scorenum;
    public String serviceemployees;
    public String shopsname;
    public String signature;
    public String statement;
    public int storedcarddiscountmoney;
    public int storedcardusemoney;
    public String sumactualmoney;
    public String tel;
    public int totalconsume;
    public double totaldiscountmoney;
    public String totalpromotion;
    public int totle;
    public int unionpaymoney;
    public int vipdeptcode;
    public String vipdeptname;
    public String viptype;
    public int wechatmoney;
    public List<?> couponlist;
    public List<DetailedlistBean> detailedlist;
    public List<?> employeeAchievementCommissionDetails;
    public List<?> goodsImgList;
    public List<?> packageGroupList;
    public List<?> selectedcoupon;

    public static class DetailedlistBean {
        public String achieemp;
        public String achieempnames;
        public Object achierate;
        public double actualmoney;
        public String brandname;
        public int commissionmoney;
        public String commisstype;
        public String customername;
        public Object editactualmoney;
        public String empname;
        public String groupname;
        public int icenum;
        public int id;
        public String imgurl;
        public String itemtype;
        public String masterid;
        public int number;
        public String orderdate;
        public String orderno;
        public String ordersource;
        public int orderstate;
        public String ordertime;
        public int originalactualmoney;
        public String paytype;
        public String picode;
        public String piname;
        public int price;
        public String promotiondetail;
        public String remark;
        public String returnorderno;
        public int returnorderstate;
        public String saleacrate;
        public String saleemp;
        public String saleempnames;
        public Object salerate;
        public String servicerate;
        public int stocknum;
        public Object storedCardOrderUseSituations;
        public int storedcardcode;
        public int storedcarddiscount;
        public int storedcarddiscountmoney;
        public int storedcardnum;
        public int storedcardusemoney;
        public int storedcardusemoneychange;
        public int subtotal;
        public String tel;
        public int totalDiscountAmount;
        public int totalconsume;
        public int totalconsumenum;
        public int totleprice;
        public List<ConsumeListBean> consumelist;
        public List<?> emplist;
        public static class ConsumeListBean{
            public String vipcodeorvipitemname;
            public String consumenum;
        }
    }
}
