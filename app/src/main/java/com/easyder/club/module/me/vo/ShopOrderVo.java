package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;
import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/10 17:46.
 * Email：xcode126@126.com
 * Desc：商城订单
 */

public class ShopOrderVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean extends BaseTempBean {
        public double actualmoney;
        public String address;
        public int cancelable;
        public int commentid;
        public int customercode;
        public String customergradeinfo;
        public String customername;
        public String customertel;
        public int deptcode;
        public String deptname;
        public int discountmoney;
        public String expresscode;
        public String expresscompany;
        public int expressmoney;
        public int extractmoney;
        public int itemnum;
        public int newmoneyrate;
        public String orderdate;
        public String orderno;
        public int orderstate;
        public String ordertime;
        public String paytime;
        public String paytype;
        public String productnum;
        public String receiver;
        public String receivetime;
        public String remark;
        public int score;
        public String sendtime;
        public String tel;
        public int totalmoney;
        public List<DetailedListBean> detailedList;
        public boolean isOnlyItem = true;

        public static class DetailedListBean extends BaseTempBean{
            public int activitydiscount;
            public double actualmoney;
            public int calcFlag;
            public int coupondiscount;
            public int empdiscount;
            public int groupdiscount;
            public String id;
            public String itemtype;
            public String number;
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
            public int totaldiscount;
            public int totalmoney;
            public int vipdiscount;
        }

        public void checkItemType(){
            for (int i = 0; i < detailedList.size(); i++) {
                DetailedListBean detailedListBean = detailedList.get(i);
                if (detailedListBean.itemtype.equals("product")) {
                    isOnlyItem = false;
                    break;
                }
            }
        }
    }
}
