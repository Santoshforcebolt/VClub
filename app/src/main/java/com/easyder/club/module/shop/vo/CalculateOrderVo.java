package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/27 16:34
 * Email: xcode126@126.com
 * Desc:
 */
public class CalculateOrderVo extends BaseVo {

    public double actualmoney;
    public AddressBean address;
    public double discountmoney;
    public double expressmoney;
    public double totalmoney;
    public List<CouponListBean> couponList;
    public List<DetailedListBean> detailedList;
    public List<?> packageGroupList;
    public List<PromotionDetailsBean> promotionDetails;
    public List<String> selectedCoupon;
    public List<?> selectedPackageGroup;
    public OrderJsonBan orderJson;

    public static class AddressBean {
        public String addflag;
        public String addresscode;
        public String addressname;
        public int customercode;
        public int defaultflag;
        public String detailedaddre;
        public String id;
        public String receivername;
        public String receivertel;
        public String zipcode;
    }

    public static class CouponListBean {
        public int activitycode;
        public String adddate;
        public String addtime;
        public String brand;
        public String brandname;
        public int couponcode;
        public String couponname;
        public String coupontype;
        public int customercode;
        public String customername;
        public int denomination;
        public int deptcode;
        public String deptname;
        public int donation;
        public int effectivedaynum;
        public String effectiverange;
        public String effectiverangename;
        public String enddate;
        public int fullmoney;
        public int givecustomercode;
        public String givecustomername;
        public Object givetime;
        public String groupname;
        public String instancecode;
        public boolean isselected;
        public String itemname;
        public String masterid;
        public String newcardorderno;
        public String orderno;
        public String ordertype;
        public String picode;
        public String pictype;
        public String productgroup;
        public String productname;
        public String rechargeorderno;
        public String rewardjson;
        public String showname;
        public String source;
        public int state;
        public int superposition;
        public String tel;
        public String usedate;
        public int usedeptcode;
        public String usedeptname;
        public String usetime;
    }

    public static class DetailedListBean {
        public int activitydiscount;
        public int actualmoney;
        public Object afterSalesOrder;
        public int calcFlag;
        public int coupondiscount;
        public int empdiscount;
        public int groupdiscount;
        public int id;
        public int isItemAftersalesAble;
        public String itemtype;
        public String masterid;
        public int number;
        public String orderno;
        public int originalprice;
        public String picode;
        public String piname;
        public String previewimg;
        public double price;
        public String pricetype;
        public String promotionid;
        public String promotionname;
        public String promotiontype;
        public int storedcarddiscount;
        public int totaldiscount;
        public int totalmoney;
        public String usestoredcardsituation;
        public int vipdiscount;
        public List<?> usestoredcard;
    }

    public static class PromotionDetailsBean {
        public int discountmoney;
        public int id;
        public String masterid;
        public String orderno;
        public String promotionid;
        public String promotionname;
        public String promotiontype;
    }

    public static class OrderJsonBan{

        public List<DetailedListBean> detailedList;

        public static class DetailedListBean {
            public int number;
            public String code;
            public String type;
        }
    }
}
