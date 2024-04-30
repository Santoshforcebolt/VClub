package com.easyder.club.module.home.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/21 18:02
 * Email: xcode126@126.com
 * Desc:
 */
public class NewsListVo extends BaseVo {

    public int total;
    public List<ArticlelistBean> articlelist;
    public List<NewsBannerListBean> newsBannerList;

    public static class ArticlelistBean {
        public String arparams;
        public String articlenewstype;
        public int articletype;
        public String articletypename;
        public int collectionflag;
        public int configtask;
        public int evaluatenum;
        public int id;
        public String lang;
        public int mark;
        public int praisenum;
        public String releaseemp;
        public String releasetime;
        public int sort;
        public int stick;
        public String sticktime;
        public String title;
        public String titlecolor;
        public String titleimgs;
        public int views;
        public int weight;
    }

    public static class NewsBannerListBean {
        public String bannerimg;
        public String bannerposition;
        public String bannertitle;
        public long gmtcreate;
        public long gmtmodified;
        public int id;
        public String jumpid;
        public String masterid;
        public int sort;
    }

}
