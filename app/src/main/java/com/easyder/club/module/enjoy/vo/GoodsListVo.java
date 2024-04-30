package com.easyder.club.module.enjoy.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.io.Serializable;
import java.util.List;

/**
 * Author: sky on 2020/12/3 11:42
 * Email: xcode126@126.com
 * Desc:
 */
public class GoodsListVo extends BaseVo {

    public int total;
    public List<ProductsBean> products;

    public static class ProductsBean implements Serializable {
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
        public int storenum;
        public int tastescore;
        public Object timeLimitActivity;
        public int totalscore;
        public String volumetype;
        public int vsalenum;
        public String winery;

        public ProductsBean() {
        }

        public ProductsBean(String productcode, String productname, String imgurl) {
            this.productcode = productcode;
            this.productname = productname;
            this.imgurl = imgurl;
        }
    }
}
