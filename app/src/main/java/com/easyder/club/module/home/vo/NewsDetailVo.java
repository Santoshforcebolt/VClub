package com.easyder.club.module.home.vo;


import com.sky.wrapper.core.model.BaseVo;

/**
 * Author: sky on 2020/11/24 10:51
 * Email: xcode126@126.com
 * Desc:
 */
public class NewsDetailVo extends BaseVo {

    public ArticleBean article;

    public static class ArticleBean {
        public String arparams;
        public String articlenewstype;
        public int articletype;
        public int collectionflag;
        public int configtask;
        public String content;
        public int evaluatenum;
        public int ipraise;
        public int praisenum;
        public int forwardnum;
        public int id;
        public String lang;
        public int mark;
        public String releaseemp;
        public String releasetime;
        public int sort;
        public int stick;
        public String title;
        public String titlecolor;
        public String titleimgs;
        public int views;
        public int weight;
    }
}
