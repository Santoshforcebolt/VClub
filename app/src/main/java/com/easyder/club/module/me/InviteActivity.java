package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.me.vo.QrCodeVo;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.scheduler.Task;
import com.sky.wrapper.core.scheduler.TaskScheduler;


import butterknife.BindView;

/**
 * Author: sky on 2020/11/18 16:46
 * Email: xcode126@126.com
 * Desc: 邀请好友
 */
public class InviteActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.mSelectableRoundedImageView)
    ImageView mSelectableRoundedImageView;
    private CustomerBean customer;

    public static Intent getIntent(Context context) {
        return new Intent(context, InviteActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_invite;
    }

    @Override
    protected boolean isUseTitle() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setBackgroundColor(Color.parseColor("#303642"));
        titleView.setImageResource(R.id.iv_title_right,R.drawable.inviate_share);
        titleView.setChildClickListener(R.id.iv_title_right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
        layoutTitle.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        ImmersionBar.with(mActivity).statusBarColor("#303642").init();

        customer = App.getCustomer();
//        ivCode.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showShareDialog();
//                return false;
//            }
//        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        if (customer != null) {
            createCode(String.format("%1$s%2$s", AppConfig.SHARE_URL, customer.recommencode));
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_CREATE_CODE)) {
            processCodeData((QrCodeVo) dataVo);
        }
    }

    /**
     * 创建二维码
     *
     * @param content
     */
    private void createCode(String content) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_CREATE_CODE, new RequestParams()
                .put("content", content)
                .putCid()
                .get(), QrCodeVo.class);
    }

    /**
     * @param dataVo
     */
    private void processCodeData(QrCodeVo dataVo) {
        if (dataVo != null && !TextUtils.isEmpty(dataVo.base64Stream)) {
            byte[] decodedString = Base64.decode(dataVo.base64Stream, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivCode.setImageBitmap(decodedByte);
//            String s="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2749480438,453442932&fm=26&gp=0.jpg";
//            ImageManager.load(mActivity, ivCode, s, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        }
        if (customer != null) {
            ImageManager.load(mActivity, mSelectableRoundedImageView, customer.icourl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            String s = TextUtils.isEmpty(customer.nickname) ? customer.customername : customer.nickname;
            String format = String.format("%1$s<font color='#C5995B'>%2$s</font>", getString(R.string.a_me_is), s);
            tvName.setText(Html.fromHtml(format));
        }
    }

    /**
     * show share
     */
    private void showShareDialog() {
        new WrapperDialog(mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_share;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogParams(dialog, WindowManager.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(348), Gravity.BOTTOM);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.ll_facebook:
                                break;
                            case R.id.ll_save:
                                save(layoutContainer);
                                break;
                        }
                        dismiss();
                    }
                }, R.id.iv_close, R.id.ll_facebook, R.id.ll_save);
            }
        }.show();
    }

    /**
     * 保存
     *
     * @param v
     */
    public void save(View v) {
        showLoadingView();
//        int h = 0;
//        for (int i = 0; i < layoutContainer.getChildCount(); i++) {
//            h += layoutContainer.getChildAt(i).getHeight();
//            layoutContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
//        }
        final Bitmap bmp = Bitmap.createBitmap(v.getWidth(),v.getHeight(), Bitmap.Config.ARGB_8888);
        v.draw(new Canvas(bmp));
        saveImage(bmp);
    }

    /**
     * save image
     * @param bitmap
     */
    private void saveImage(final Bitmap bitmap) {
        showLoadingView();
        TaskScheduler.execute(new Task<Integer>() {
            @Override
            public Integer doInBackground() {
                try {
                    String fileName1 = System.currentTimeMillis() + ".jpg";
                    MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap,
                            fileName1, "Vclub"); // 名字和描述没用，系统会自动更改
                    return 1;
                } catch (Exception e) {
                    return 0;
                }


//                try {
//                    File folder = new File(AppConfig.DEFAULT_IMAGE_PATH);
//                    if (!folder.exists()) {
//                        folder.mkdirs();
//                    }
//                    String fileName = System.currentTimeMillis() + ".jpg";
//                    File file = new File(folder, fileName);
//                    FileOutputStream fos = new FileOutputStream(file);
//                    if (bitmap != null) {
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        fos.flush();
//                        fos.close();
//                        MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
//                                file.getAbsolutePath(), fileName, null);
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                                Uri.fromFile(new File(file.getPath()))));
//                        return 1;
//                    }
//                } catch (Exception e) {
//
//                }
//                return 0;
            }

            @Override
            public void onSuccess(Integer result) {
                onStopLoading();
                switch (result) {
                    case 1:
                        showToast(getString(R.string.a_save_image_success));
                        break;
                    case 0:
                        showToast(getString(R.string.a_save_image_fail));
                        break;
                }
            }
        });
    }


}
