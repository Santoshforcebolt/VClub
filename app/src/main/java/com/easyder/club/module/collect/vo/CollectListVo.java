package com.easyder.club.module.collect.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/1 11:06
 * Email: xcode126@126.com
 * Desc:
 */
public class CollectListVo extends BaseVo {

    public List<RowsBean> rows;
    public int total;
    public int bottlecount;
    public int bucketcount;
    public double totalmarketprice;
    public int totalnum;
    public double totalsaleprice;

    public static class RowsBean {
        public boolean isExpand;
        public String addr;
        public int alcoholic;
        public String barrelshape;
        public String bottled;
        public String bottler;
        public String bottleseries;
        public String bottling;
        public int calculatetheyear;
        public int capacity;
        public String cardtype;
        public String casknumber;
        public int customercode;
        public String distillationyear;
        public String gmtcreate;
        public int id;
        public String imgurl;
        public double marketprice;
        public String masterid;
        public int nowprice;
        public int number;
        public String piccode;
        public String pictname;
        public String pictype;
        public int preferentialamount;
        public String purchasedat;
        public int salenum;
        public double saleprice;
        public int stocknum;
        public String winery;
        public List<CardBindItemsBean> cardBindItems;
        public List<GiveCardDetailedsBean> giveCardDetaileds;
        public List<PackageGroupItemsBean> packageGroupItems;

        public static class CardBindItemsBean {
            public int averageprice;
            public int cardcode;
            public String cardname;
            public int commissionmoney;
            public String commissiontype;
            public int countnum;
            public int id;
            public String imgurl;
            public int itemcode;
            public String itemname;
            public int monthlyachienum;
            public int remainnum;
            public int saleprice;
            public int usednum;
        }

        public static class GiveCardDetailedsBean {
            public int cardcode;
            public int effective;
            public String effectiverange;
            public int givdelflag;
            public int givstate;
            public int id;
            public int itemcode;
            public String itemimgurl;
            public String itemname;
            public int itemprice;
            public String masterid;
            public int number;
            public int seffective;
            public int sitemcode;
            public String sitemname;
        }

        public static class PackageGroupItemsBean {
            public int achievement;
            public int brandstate;
            public int groupstate;
            public int id;
            public String imgurl;
            public String itemcode;
            public String itemname;
            public String itemtype;
            public int marketprice;
            public int packagecode;
            public int packageprice;
            public int price;
            public int quantity;
            public int saleprice;
            public int smallstate;
            public int stocknum;
        }
    }
}
