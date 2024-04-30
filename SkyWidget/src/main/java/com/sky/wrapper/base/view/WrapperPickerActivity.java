package com.sky.wrapper.base.view;

import android.content.Intent;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sky.widget.R;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.utils.GlideEngine;

import java.util.List;



/**
 * Auther:  winds
 * Data:    2017/4/19
 * Desc:    图片选择
 */

public abstract class WrapperPickerActivity<P extends MvpBasePresenter> extends WrapperSwipeActivity<P> {

    private int type;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                case PictureConfig.REQUEST_CAMERA:
                    obtainMediaResult(PictureSelector.obtainMultipleResult(data), type);
                    break;
            }
        }
    }

    /**
     * 选取图片
     *
     * @param maxSelectNum 选取图片的数量  多选时才生效
     * @param singleMode   是否单选
     * @param showCamera   是否显示拍照按钮
     * @param crop         是否剪裁
     * @param isCircleMode 剪裁是否显示圆形参考   用于裁剪圆形头像    开启裁剪时生效
     */
    public void showPictureSelector(int maxSelectNum, boolean singleMode, boolean showCamera, boolean crop, boolean isCircleMode) {
        PictureSelectionModel model = PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) //查看类型  仅图片,可选择其它或全部
                .theme(R.style.picture_WeChat_style)
                .maxSelectNum(maxSelectNum) //最大图片选择数量
                .isCompress(true)
                .compressQuality(60)// 图片压缩后输出质量
                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .compressMaxKB(500)
                .imageSpanCount(4)// 每行显示个数
                .minSelectNum(1)// 最小选择数量
                .selectionMode(singleMode ?
                        PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 单选 or 多选
                .imageEngine(GlideEngine.createGlideEngine())
                .isCamera(showCamera);// 是否显示拍照按钮
        if (crop) {
            model
                    .isEnableCrop(crop)             //开启裁剪
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .hideBottomControls(true)  //是否隐藏裁剪工具栏
                    .circleDimmedLayer(isCircleMode)    //显示圆形参考图层
//                    .setCircleDimmedBorderColor(UIUtils.getColor(R.color.colorRed))
                    .withAspectRatio(1, 1);     //裁剪比例
        }
        model.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 默认不压缩的选择图库
     *
     * @param maxSelectNum
     * @param singleMode
     * @param showCamera
     */
    public void showPictureSelector(int maxSelectNum, boolean singleMode, boolean showCamera, int type) {
        this.type = type;
        showPictureSelector(maxSelectNum, singleMode, showCamera);
    }

    /**
     * 默认不裁剪的选择图库
     *
     * @param maxSelectNum
     * @param singleMode
     * @param showCamera
     */
    public void showPictureSelector(int maxSelectNum, boolean singleMode,boolean crop, boolean showCamera) {
        showPictureSelector(maxSelectNum, singleMode, showCamera, crop, false);
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 默认不压缩的选择图库
     *
     * @param maxSelectNum
     * @param singleMode
     * @param showCamera
     */
    public void showPictureSelector(int maxSelectNum, boolean singleMode, boolean showCamera) {
        showPictureSelector(maxSelectNum, singleMode, showCamera, false, false);
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public void showCameraSelector(){
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .compressQuality(60)// 图片压缩后输出质量
                .isEnableCrop(true)             //开启裁剪
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .hideBottomControls(true)  //是否隐藏裁剪工具栏
                .circleDimmedLayer(false)    //显示圆形参考图层
                .withAspectRatio(1, 1)    //裁剪比例
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    public abstract void obtainMediaResult(List<LocalMedia> list, int type);

}

