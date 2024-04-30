package com.easyder.club.module.enjoy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.adapter.GridImageAdapter;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.module.enjoy.vo.GridImageVo;
import com.easyder.club.module.enjoy.vo.TempEnjoyBean;
import com.easyder.club.module.enjoy.vo.UploadVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.luck.picture.lib.entity.LocalMedia;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperPickerActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/13 17:57
 * Email: xcode126@126.com
 * Desc:
 */
public class PublishEnjoyActivity extends WrapperPickerActivity<CommonPresenter>
        implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.et_product)
    EditText etProduct;
    @BindView(R.id.colorSeekBar)
    SeekBar colorSeekBar;
    @BindView(R.id.tv_color_lever)
    TextView tvColorLever;
    @BindView(R.id.et_color_evaluate)
    EditText etColorEvaluate;
    @BindView(R.id.smellSeekBar)
    SeekBar smellSeekBar;
    @BindView(R.id.tv_smell_lever)
    TextView tvSmellLever;
    @BindView(R.id.et_smell_evaluate)
    EditText etSmellEvaluate;
    @BindView(R.id.tasteSeekBar)
    SeekBar tasteSeekBar;
    @BindView(R.id.tv_taste_lever)
    TextView tvTasteLever;
    @BindView(R.id.et_taste_evaluate)
    EditText etTasteEvaluate;
    @BindView(R.id.rhymeSeekBar)
    SeekBar rhymeSeekBar;
    @BindView(R.id.tv_rhyme_lever)
    TextView tvRhymeLever;
    @BindView(R.id.et_rhyme_evaluate)
    EditText etRhymeEvaluate;
    @BindView(R.id.et_total_evaluate)
    EditText etTotalEvaluate;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_share_tip)
    TextView tvShareTip;

    private GridImageAdapter imageAdapter;
    private TempEnjoyBean tempEnjoyBean = new TempEnjoyBean();
    private int pictureType;

    public static Intent getIntent(Context context, GoodsListVo.ProductsBean item) {
        return new Intent(context, PublishEnjoyActivity.class).putExtra("item", item);
    }

    public static Intent getIntent(Context context) {
        return getIntent(context, null);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_publish_enjoy;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_publish_enjoy));
        tvShareTip.setText(Html.fromHtml(String.format("%1$s<font color = '#999999'>%2$s</font>", getString(R.string.a_show_picture), getString(R.string.a_picture_max_tip))));

        handleYetGoods(intent);
        colorSeekBar.setOnSeekBarChangeListener(this);
        tasteSeekBar.setOnSeekBarChangeListener(this);
        smellSeekBar.setOnSeekBarChangeListener(this);
        rhymeSeekBar.setOnSeekBarChangeListener(this);

        initPictureAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_UPLOAD_PICTURE)) {
            processUploadPicture((UploadVo) dataVo);
        } else if (url.contains(ApiConfig.API_PUBLISH_ENJOY)) {
            EventBus.getDefault().post(new AccountChanged(AccountIml.ACCOUNT_PUBLISH_ENJOY));
            showToast(getString(R.string.a_operate_success));
            finish();
        }
    }

    /**
     * @param uploadVo
     */
    private void processUploadPicture(UploadVo uploadVo) {
        if (pictureType == 0x02) {
//            UploadVo uploadVo = new UploadVo("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1658566166,42361211&fm=26&gp=0.jpg", "10");
            imageAdapter.insertPicture(uploadVo);
        } else {
//            tempEnjoyBean.imgurl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3382974621,141137018&fm=26&gp=0.jpg";
            tempEnjoyBean.imgurl = uploadVo.imgurl;
            ImageManager.load(mActivity, ivProduct, tempEnjoyBean.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        }
    }

    /**
     * publish enjoy
     */
    private void publishEnjoy() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_PUBLISH_ENJOY, new RequestParams()
                .putCid()
                .putWithoutEmpty("address", tempEnjoyBean.address)
                .putWithoutEmpty("productcode", tempEnjoyBean.productcode)//产品名称选择商品时传
                .putWithoutEmpty("productname", tempEnjoyBean.productname)//产品名称选择商品时传
//                .put("collection", tempEnjoyBean.collection)//是否收藏0否1是
//                .put("wish", "")//心愿单0否1是
                .putWithoutEmpty("colorcomment", tempEnjoyBean.colorcomment)//色泽评语
                .putWithoutEmpty("colorscore", tempEnjoyBean.colorscore)//色泽评分
                .putWithoutEmpty("endingcomment", tempEnjoyBean.endingcomment)//尾韵评语
                .putWithoutEmpty("endingscore", tempEnjoyBean.endingscore)//尾韵评分
                .putWithoutEmpty("smellcomment", tempEnjoyBean.smellcomment)//	嗅觉评语
                .putWithoutEmpty("smellscore", tempEnjoyBean.smellscore)//	嗅觉评分
                .putWithoutEmpty("tastecomment", tempEnjoyBean.tastecomment)//	味道评语
                .putWithoutEmpty("tastescore", tempEnjoyBean.tastescore)//	味道评分
                .putWithoutEmpty("images", tempEnjoyBean.images)//		嗮图多个逗号分隔
                .putWithoutEmpty("imgurl", tempEnjoyBean.imgurl)//	    产品图片
                .put("comment", tempEnjoyBean.comment)//总评语
//                .put("totalscore", "")//	总评分
//                .put("icourl", "")//	会员头像
//                .put("customercode", "")//会员编码
//                .put("customername", "")//会员名称
                .get(), BaseVo.class);
    }

    /**
     * upload file
     *
     * @param path
     */
    private void uploadFile(String path) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_UPLOAD_PICTURE, new RequestParams()
                .put("image", new File(path))
                .putCid()
                .get(), UploadVo.class);
    }

    @OnClick({R.id.iv_product, R.id.tv_location, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_product:
                pictureType = 0;
                showPictureSelector(imageAdapter.canAddMax(), true, true, 0x01);
                break;
            case R.id.tv_location:
                startActivity(LocationActivity.getIntent(mActivity));
                break;
            case R.id.btn_commit:
                String product = etProduct.getText().toString().trim();
                if (TextUtils.isEmpty(product)) {
                    showToast(getString(R.string.a_input_product_name));
                    return;
                }
                tempEnjoyBean.productname = product;

                tempEnjoyBean.colorscore = colorSeekBar.getProgress();
                tempEnjoyBean.colorcomment = etColorEvaluate.getText().toString().trim();

                tempEnjoyBean.smellscore = smellSeekBar.getProgress();
                tempEnjoyBean.smellcomment = etSmellEvaluate.getText().toString().trim();

                tempEnjoyBean.tastescore = tasteSeekBar.getProgress();
                tempEnjoyBean.tastecomment = etTasteEvaluate.getText().toString().trim();

                tempEnjoyBean.endingscore = rhymeSeekBar.getProgress();
                tempEnjoyBean.endingcomment = etRhymeEvaluate.getText().toString().trim();

                String totalEvaluate = etTotalEvaluate.getText().toString().trim();
                if (TextUtils.isEmpty(totalEvaluate)) {
                    showToast(getString(R.string.a_input_rhyme_evaluate));
                    return;
                }
                tempEnjoyBean.comment = totalEvaluate;
                tempEnjoyBean.images = imageAdapter.getSelectPath();
                publishEnjoy();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == colorSeekBar) {//色泽
            tvColorLever.setText(String.valueOf(progress));
            tempEnjoyBean.colorscore = progress;
        } else if (seekBar == smellSeekBar) {//嗅香
            tvSmellLever.setText(String.valueOf(progress));
            tempEnjoyBean.smellscore = progress;
        } else if (seekBar == tasteSeekBar) {//味道
            tvTasteLever.setText(String.valueOf(progress));
            tempEnjoyBean.tastescore = progress;
        } else if (seekBar == rhymeSeekBar) {//尾韵
            tvRhymeLever.setText(String.valueOf(progress));
            tempEnjoyBean.endingscore = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void obtainMediaResult(List<LocalMedia> list, int type) {
        this.pictureType = type;
        if (list != null && list.size() > 0) {
            LocalMedia localMedia = list.get(0);
            uploadFile(localMedia.getCompressPath());
        }
    }

    /**
     * init picture adapter
     */
    private void initPictureAdapter() {
        List<GridImageVo> list = new ArrayList<>();
        list.add(new GridImageVo(true, ""));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new GridImageAdapter(list, 3);
        mRecyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (imageAdapter.isCanAdd(position) && imageAdapter.canAddMax() > 0) {
                pictureType = 0;
                showPictureSelector(imageAdapter.canAddMax(), true, true, 0x02);
            }
        });
        imageAdapter.setOnItemChildClickListener((adapter, view, position) -> imageAdapter.deletePicture(position));
    }

    /**
     * 处理已经选择的产品
     *
     * @param intent
     */
    private void handleYetGoods(Intent intent) {
        GoodsListVo.ProductsBean item = (GoodsListVo.ProductsBean) intent.getSerializableExtra("item");
        if (item != null) {
            tempEnjoyBean.productname = item.productname;
            tempEnjoyBean.productcode = item.productcode;
            tempEnjoyBean.imgurl = OperateUtils.getFirstImage(item.imgurl);
            ivProduct.setEnabled(false);
            etProduct.setEnabled(false);
            ImageManager.load(mActivity, ivProduct, OperateUtils.getFirstImage(item.imgurl),
                    R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            etProduct.setText(item.productname);
        } else {
            ivProduct.setEnabled(true);
            etProduct.setEnabled(true);
        }
    }
}
