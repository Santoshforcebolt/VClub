package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/25 18:50
 * Email: xcode126@126.com
 * Desc:
 */
public class GoodsDetailVo extends BaseVo {

    public String adddatetime;
    public String addemp;
    public int alcoholic;
    public int audit;
    public String barcode;
    public String barrelshape;
    public String bottled;
    public String bottler;
    public String bottleseries;
    public String bottling;
    public int branchearlywarning;
    public int brandcode;
    public String brandname;
    public int calculatetheyear;
    public int capacity;
    public String casknumber;
    public int collectionId;
    public int collectnumber;
    public int colorscore;
    public int commissionmoney;
    public String commisstype;
    public int costprice;
    public boolean deleted;
    public int delflag;
    public String deptcode;
    public String describes;
    public String distillationyear;
    public String effectiverange;
    public String effectiverangename;
    public int empprice;
    public int endingscore;
    public String gradeextract;
    public int groupcode;
    public String groupname;
    public int headearlywarning;
    public String imgurl;
    public int iscollection;
    public int issale;
    public Object itemComment;
    public String kind;
    public double marketprice;
    public String masterid;
    public String modifydatetime;
    public String modifyemp;
    public int notdiscount;
    public String oldproductcode;
    public int online;
    public Object packageGroup;
    public String productcode;
    public String productname;
    public String recommencode;
    public int salenum;
    public double saleprice;
    public String scarpy;
    public int shelflife;
    public int shopcostprice;
    public int smallstate;
    public int smellscore;
    public int stocknum;
    public int tempNumber;
    public int storenum;
    public int evaluatenum;
    public int tastescore;
    public TimeLimitActivityBean timeLimitActivity;
    public int totalscore;
    public String volumetype;
    public int vsalenum;
    public String winery;
    public List<SpecificationBean> specificationArray;

    public static class TimeLimitActivityBean {
        public int activitycode;
        public String activityimgurl;
        public String activityname;
        public String activityobject;
        public String activityobjectjson;
        public String activityremark;
        public String activitytag;
        public String addemp;
        public String addtime;
        public String channeltype;
        public int deptcode;
        public String edatetime;
        public String effectiverange;
        public String effectiverangename;
        public int empcommission;
        public int everyonelimitnum;
        public int isindexshow;
        public String limittype;
        public String masterid;
        public String modifyemp;
        public String modifytime;
        public int prioritylevel;
        public long remaintime;
        public String sdatetime;
        public String selectactivitytagobj;
        public String showtime;
        public int state;
        public TimeLimitActivityThemeTemplateBean timeLimitActivityThemeTemplate;
        public String timeLimitActivityThemeTemplateStr;
        public List<?> customergroup;
        public List<?> taglists;
        public List<TimeLimitActivityDetailedsBean> timeLimitActivityDetaileds;

        public static class TimeLimitActivityThemeTemplateBean {
            public int activitycode;
            public String bgurl;
            public int id;
            public String masterid;
            public String minititle;
            public int templatecode;
            public String title;
        }

        public static class TimeLimitActivityDetailedsBean {
            public int activitycode;
            public String describes;
            public String discounttype;
            public int discountvalue;
            public int edatetime;
            public int id;
            public String imgurl;
            public String itemcode;
            public String itemname;
            public String itemtype;
            public int leftnum;
            public int marketprice;
            public String masterid;
            public int price;
            public int salenum;
            public int saleprice;
            public int stock;
            public int stocknum;
            public int totalnum;
            public String totalstock;
            public List<?> imgurls;
        }
    }

    public static class SpecificationBean{
        public String name;
        public String value;
        public String msgCode;
    }
}
