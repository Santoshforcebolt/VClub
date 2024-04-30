package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/24 17:05
 * Email: xcode126@126.com
 * Desc: 商城首页
 */
public class ShopIndexVo extends BaseVo {

    public ExpandCardActivityRewardListBean expandCardActivityRewardList;
    public int showshop;
    public List<IndexRecommensOfCardBean> indexRecommensOfCard;
    public List<?> newCustomerActivityList;
    public List<ListIndexMenusBean> listIndexMenus;
    public List<?> groupBuyActivities;
    public List<ListAdvertsBean> listAdverts;
    public List<TimeLimitActivitieBean> timeLimitActivitie;
    public List<?> expandCardActivities;
    public List<ListIndexRecommensBean> listIndexRecommens;

    public static class ExpandCardActivityRewardListBean {
    }

    public static class IndexRecommensOfCardBean {
        public String gmtcreate;
        public String gmtmodified;
        public int id;
        public String imgurl;
        public int marketprice;
        public String masterid;
        public String piccode;
        public String picname;
        public int picstate;
        public String pictype;
        public int salenum;
        public int saleprice;
        public int sort;
        public int state;
        public int stocknum;
    }

    public static class ListIndexMenusBean {
        public String gmtcreate;
        public String gmtmodified;
        public String masterid;
        public int menucode;
        public String menuimg;
        public String menuname;
        public int sort;
        public int state;
        public String url;
    }

    public static class ListAdvertsBean {
        public String advertimg;
        public String advertposition;
        public String adverttitle;
        public String adverturl;
        public String gmtcreate;
        public String gmtmodified;
        public int id;
        public String jumpid;
        public String jumpname;
        public String jumptype;
        public String masterid;
        public int sort;
    }

    public static class TimeLimitActivitieBean {
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
        public int remaintime;
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
            public double marketprice;
            public String masterid;
            public double price;
            public int salenum;
            public double saleprice;
            public int stock;
            public int stocknum;
            public int totalnum;
            public String totalstock;
            public List<?> imgurls;
        }
    }

    public static class ListIndexRecommensBean {
        public String gmtcreate;
        public String gmtmodified;
        public int id;
        public String imgurl;
        public int marketprice;
        public String masterid;
        public String piccode;
        public String picname;
        public int picstate;
        public String pictype;
        public int salenum;
        public int saleprice;
        public int sort;
        public int state;
        public int stocknum;
    }
}
