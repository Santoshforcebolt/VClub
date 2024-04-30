package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/4 17:56
 * Email: xcode126@126.com
 * Desc:
 */

public class MealDetailVo extends BaseVo {

    public String activityobject;
    public String activityobjectjson;
    public String channeltype;
    public int collectionId;
    public int commissionmoney;
    public String commissiontype;
    public String createtime;
    public String customergroupjson;
    public String effectiverange;
    public String effectiverangename;
    public String emp;
    public int empcommission;
    public String imgurl;
    public int iscollection;
    public int itemnum;
    public String masterid;
    public int packagecode;
    public String packagename;
    public int packagetypecode;
    public String packagetypename;
    public double preferentialamount;
    public double price;
    public int productnum;
    public int purchasenum;
    public String recommencode;
    public String remark;
    public int salesvolume;
    public String selectactivitytagobj;
    public int smallstate;
    public int state;
    public int totalmoney;
    public List<?> customergroup;
    public List<PackageGroupItemsBean> packageGroupItems;

    public static class PackageGroupItemsBean {
        public double achievement;
        public int brandstate;
        public int groupstate;
        public int id;
        public String imgurl;
        public String itemcode;
        public String itemname;
        public String itemtype;
        public int marketprice;
        public int packagecode;
        public double packageprice;
        public double price;
        public int quantity;
        public double saleprice;
        public int smallstate;
        public int stocknum;
    }
}
