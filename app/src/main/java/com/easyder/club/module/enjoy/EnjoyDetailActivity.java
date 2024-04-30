package com.easyder.club.module.enjoy;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.ConfirmDialog;
import com.easyder.club.module.enjoy.vo.EnjoyDetailVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.UpdateUtils;
import com.joooonho.SelectableRoundedImageView;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.HorizontalProgressBar;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/16 17:35
 * Email: xcode126@126.com
 * Desc:
 */
public class EnjoyDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_degree)
    TextView tvDegree;
    @BindView(R.id.tv_date)
    TextView tvDate;


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_color_lever)
    TextView tvColorLever;
    @BindView(R.id.tv_color_evaluate)
    TextView tvColorEvaluate;
    @BindView(R.id.tv_smell_lever)
    TextView tvSmellLever;
    @BindView(R.id.tv_smell_evaluate)
    TextView tvSmellEvaluate;
    @BindView(R.id.tv_taste_lever)
    TextView tvTasteLever;
    @BindView(R.id.tv_taste_evaluate)
    TextView tvTasteEvaluate;
    @BindView(R.id.tv_rhyme_lever)
    TextView tvRhymeLever;
    @BindView(R.id.tv_rhyme_evaluate)
    TextView tvRhymeEvaluate;
    @BindView(R.id.layout_picture)
    LinearLayout layoutPicture;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.ll_wish)
    LinearLayout llWish;
    @BindView(R.id.colorProgressBar)
    HorizontalProgressBar colorProgressBar;
    @BindView(R.id.smellProgressBar)
    HorizontalProgressBar smellProgressBar;
    @BindView(R.id.tasteProgressBar)
    HorizontalProgressBar tasteProgressBar;
    @BindView(R.id.rhymeProgressBar)
    HorizontalProgressBar rhymeProgressBar;

    private int enjoyId;
    private EnjoyDetailVo.EvaluaTionBean bean;
    private static final String TYPE_WISH = "wish";
    private static final String TYPE_COLLECT = "collection";

    public static Intent getIntent(Context context, int enjoyId) {
        return new Intent(context, EnjoyDetailActivity.class).putExtra("enjoyId", enjoyId);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_enjoy_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        enjoyId = intent.getIntExtra("enjoyId", 0);
        titleView.setTitle(getString(R.string.a_enjoy_detail));
        titleView.setText(R.id.tv_title_right, getString(R.string.a_delete));
        titleView.setChildClickListener(R.id.tv_title_right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(enjoyId);
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getEnjoyDetail();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ENJOY_DETAIL)) {
            processEnjoyDetail((EnjoyDetailVo) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_WISH)) {
            getEnjoyDetail();
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_ENJOY_CHANGE));
        }else if (url.contains(ApiConfig.API_DELETE_ENJOY)){
            showToast(getString(R.string.a_operate_success));
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_ENJOY_CHANGE));
            finish();
        }
    }

    /**
     * 获取评鉴详情
     */
    private void getEnjoyDetail() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ENJOY_DETAIL, new RequestParams().putCid().put("id", enjoyId).get(), EnjoyDetailVo.class);
    }

    /**
     * @param evalid
     * @param optiontype
     * @param optionvalue
     */
    private void changeCollect(int evalid, String optiontype, int optionvalue) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_WISH, new RequestParams()
                .putCid()
                .put("evalid", evalid)
                .put("optiontype", optiontype)
                .put("optionvalue", optionvalue)
                .get(), BaseVo.class);
    }

    /**
     * 删除评鉴
     *
     * @param evalid
     */
    private void deleteEnjoy(int evalid) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_DELETE_ENJOY, new RequestParams()
                .put("evalid", evalid)
                .putCid().get(), BaseVo.class);
    }

    /**
     * process enjoy detail
     *
     * @param dataVo
     */
    private void processEnjoyDetail(EnjoyDetailVo dataVo) {
        if (dataVo != null && dataVo.evaluaTion != null) {
            this.bean = dataVo.evaluaTion;
            ImageManager.load(mActivity, ivImage, OperateUtils.getFirstImage(bean.imgurl), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            tvName.setText(bean.productname);
            tvEvaluate.setText(bean.totalscore + "");
            tvDesc.setText(bean.comment);

            tvDegree.setText(String.format("%1$s%2$s", bean.alcoholic, "%vol"));
            tvDate.setText(TextUtils.isEmpty(bean.distillationyear) ? "0" : bean.distillationyear);
            tvVolume.setText(String.format("%1$s%2$s", bean.capacity, "ml"));

            tvDegree.setVisibility(bean.alcoholic > 0 ? View.VISIBLE : View.GONE);
            tvDate.setVisibility(!TextUtils.isEmpty(bean.distillationyear) ? View.VISIBLE : View.GONE);
            tvVolume.setVisibility(bean.capacity > 0 ? View.VISIBLE : View.GONE);


            colorProgressBar.setProgress(bean.colorscore);
            tvColorLever.setText(bean.colorscore + "");
            tvColorEvaluate.setText(bean.colorcomment);

            smellProgressBar.setProgress(bean.smellscore);
            tvSmellLever.setText(bean.smellscore + "");
            tvSmellEvaluate.setText(bean.smellcomment);

            tasteProgressBar.setProgress(bean.tastescore);
            tvTasteLever.setText(bean.tastescore + "");
            tvTasteEvaluate.setText(bean.tastecomment);

            rhymeProgressBar.setProgress(bean.endingscore);
            tvRhymeLever.setText(bean.endingscore + "");
            tvRhymeEvaluate.setText(bean.endingcomment);

            tvPlace.setText(bean.address);
            tvPlace.setVisibility(TextUtils.isEmpty(bean.address) ? View.GONE : View.VISIBLE);
            tvTime.setText(bean.createdate);
            llCollect.setSelected(bean.collection == 1);
            llWish.setSelected(bean.wish == 1);

            List<String> imageArray = OperateUtils.getImageArray(bean.images);
            if (imageArray != null && imageArray.size() > 0) {
                layoutPicture.removeAllViews();
                layoutPicture.setVisibility(View.VISIBLE);
                for (String url : imageArray) {
                    layoutPicture.addView(getImage(url, 200, 200));
                }
            } else {
                layoutPicture.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 确认是否删除评鉴弹窗
     *
     * @param evalid
     */
    private void deleteDialog(int evalid) {
        new WrapperDialog(mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_delete_enjoy;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogAbsParams(dialog, 586, 316, Gravity.CENTER);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setOnClickListener(v -> {
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            deleteEnjoy(evalid);
                            break;
                    }
                    dialog.dismiss();
                }, R.id.btn_confirm, R.id.btn_cancel);
            }
        }.show();
    }

    /**
     * get image
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    private SelectableRoundedImageView getImage(String url, int width, int height) {
        SelectableRoundedImageView imageView = new SelectableRoundedImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOval(false);
        imageView.setCornerRadiiDP(5, 5, 5, 5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(width), AutoUtils.getPercentWidthSize(height));
        params.setMargins(0, 0, AutoUtils.getPercentWidthSize(30), 0);
        imageView.setLayoutParams(params);
        ImageManager.load(mActivity, imageView, url, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        return imageView;
    }

    @OnClick({R.id.ll_collect, R.id.ll_wish})
    public void onViewClicked(View view) {
        if (bean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_collect:
                int c = bean.collection == 0 ? 1 : 0;
                changeCollect(bean.id, TYPE_COLLECT, c);
                break;
            case R.id.ll_wish:
                int w = bean.wish == 0 ? 1 : 0;
                changeCollect(bean.id, TYPE_WISH, w);
                break;
        }
    }
}
