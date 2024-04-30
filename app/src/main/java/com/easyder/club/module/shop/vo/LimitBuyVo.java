package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/25 16:46
 * Email: xcode126@126.com
 * Desc:
 */
public class LimitBuyVo extends BaseVo {
    public int activitycode;
    public int totalGoodsNum;
    public TimeLimitActivityThemeTemplateBean timeLimitActivityThemeTemplate;
    public int remaintime;
    public String activityobject;
    public String activityimgurl;
    public List<DetailedListBean> detailedList;
    public List<ActivityTagsBean> activityTags;

    public static class TimeLimitActivityThemeTemplateBean {
        public int activitycode;
        public String bgurl;
        public int id;
        public String masterid;
        public String minititle;
        public int templatecode;
        public String title;
    }

    public static class DetailedListBean {
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
        public List<String> imgurls;
    }

    public static class ActivityTagsBean {
        public int activitycode;
        public String activitytag;
        public int templatecode;
    }
}
