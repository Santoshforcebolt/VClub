package com.easyder.club.module.enjoy.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

/**
 * Author: sky on 2020/12/1 17:27
 * Email: xcode126@126.com
 * Desc:
 */
public class TempEnjoyBean extends BaseTempBean {

    public String address;
    public String productcode;//产品编码选择商品时传
    public String productname;//产品名称选择商品时传
    //    public int collection;//是否收藏0否1是
//    public int wish;//心愿单0否1是
    public String colorcomment;//色泽评语
    public int colorscore;//色泽评分
    public String endingcomment;//尾韵评语
    public int endingscore;//尾韵评分
    public String smellcomment;//嗅觉评语
    public int smellscore;//嗅觉评分
    public String tastecomment;//味道评语
    public int tastescore;//味道评分
    public String images;//嗮图多个逗号分隔
    public String imgurl;//产品图片
    public String comment;//总评语
//    public int totalscore;//总评分
//    public int icourl;//会员头像
//    public int customercode;//会员编码
//    public int customername;//会员名称
}
