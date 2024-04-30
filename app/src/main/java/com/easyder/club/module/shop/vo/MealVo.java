package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/4 17:02
 * Email: xcode126@126.com
 * Desc: 全部套餐
 */

public class MealVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String activityobject;
        public String activityobjectjson;
        public String channeltype;
        public int collectionId;
        public int commissionmoney;
        public String commissiontype;
        public String createtime;
        public String effectivedeptcodes;
        public String effectiverange;
        public String effectiverangename;
        public String emp;
        public String imgurl;
        public int iscollection;
        public int packagecode;
        public String packagename;
        public double preferentialamount;
        public double price;
        public int purchasenum;
        public String recommencode;
        public String remark;
        public int salesvolume;
        public String selectactivitytagobj;
        public int smallstate;
        public int state;
        public List<?> customergroup;
        public List<?> gradeList;
        public List<PackageGroupItemsBean> packageGroupItems;

        public static class PackageGroupItemsBean {
            public double achievement;
            public int effectivedaynum;
            public int id;
            public String imgurl;
            public String itemcode;
            public String itemname;
            public String itemtype;
            public int marketprice;
            public int packagecode;
            public double packageprice;
            public int price;
            public int quantity;
            public double saleprice;
        }
    }
}
