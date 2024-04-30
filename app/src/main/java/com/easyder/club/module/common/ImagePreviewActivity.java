package com.easyder.club.module.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.SolveViewPager;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.base.view.WrapperMvpActivity;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.scheduler.Task;
import com.sky.wrapper.core.scheduler.TaskScheduler;
import com.sky.wrapper.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/12/17 14:19.
 * Email：xcode126@126.com
 * Desc：
 */
public class ImagePreviewActivity extends WrapperMvpActivity<MvpBasePresenter> {

    @BindView(R.id.mViewPager)
    SolveViewPager mViewPager;
    @BindView(R.id.layout_guide)
    LinearLayout layout_guide;

    private ArrayList<String> list;
    private int position;
    private ImageAdapter adapter;
    private String imgUrl;

    public static Intent getIntent(Context context, ArrayList<String> list, int position) {
        return new Intent(context, ImagePreviewActivity.class).putStringArrayListExtra("list", list).putExtra("position", position);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        setInterceptable(false);
        getContainerView().setBackgroundColor(ContextCompat.getColor(mActivity, R.color.textSuper));
        list = intent.getStringArrayListExtra("list");
        position = intent.getIntExtra("position", 0);
        if (list.size() > 0) {
            imgUrl = list.get(0).toString();
        }
        adapter = new ImageAdapter(mActivity, list);
        mViewPager.setAdapter(adapter);
        addIndicatorView(layout_guide, position, list != null ? list.size() : 0);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                setSwipeBackEnable(position == 0);
                setIndicator(layout_guide, position);
                imgUrl = list.get(position);
            }
        });
        mViewPager.setCurrentItem(position, false);

    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 初始化指示器效果
     *
     * @param parent
     * @param index
     * @param size
     */
    private void addIndicatorView(LinearLayout parent, int index, int size) {
        if (size > 0) {
            parent.removeAllViews();
            for (int i = 0; i < size; i++) {
                ImageView view = new ImageView(mActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(15),
                        AutoUtils.getPercentWidthSize(15));
                params.leftMargin = AutoUtils.getPercentWidthSize(6);
                params.rightMargin = AutoUtils.getPercentWidthSize(6);
                view.setBackgroundResource(R.drawable.selector_banner_point);
                view.setEnabled(i == index);
                parent.addView(view, params);
            }
        }
    }

    /**
     * 設置指示器选中效果
     *
     * @param parent
     * @param index
     */
    private void setIndicator(LinearLayout parent, int index) {
        if (parent != null && parent.getChildCount() > 0) {
            int count = parent.getChildCount();
            if (count > index) {
                for (int i = 0; i < count; i++) {
                    View view = parent.getChildAt(i);
                    if (view instanceof ImageView) {
                        view.setBackgroundResource(R.drawable.selector_banner_point);
                        view.setEnabled(i == index);
//                        ((ImageView) view).setImageResource(i == index ? R.drawable.shape_dot_checked : R.drawable.shape_dot_normal);
                    }
                }
            }
        }
    }

    @OnClick({R.id.img_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(imgUrl)) {
                    taskScheduler(imgUrl);
                }
                break;
        }
    }

    /**
     * 将网络图片URL转化成bitmap形式
     *
     * @param url
     * @return bitmap type
     */
    public Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 下载并保存图片
     *
     * @param imgurl 图片地址
     */
    private void taskScheduler(final String imgurl) {
        TaskScheduler.execute(new Task<Integer>() {
            @Override
            public Integer doInBackground() throws InterruptedException {
                Bitmap bmp = returnBitMap(imgurl);
                // 首先保存图片
                File appDir = new File(AppConfig.DEFAULT_IMAGE_PATH);
                if (!appDir.exists()) {
                    boolean mkdir = appDir.mkdirs();
                    if (!mkdir) {
                        LogUtils.e("TAG", "文件夹创建失败");
                    } else {
                        LogUtils.e("TAG", "文件夹创建成功");
                    }
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                return 0;
            }

            @Override
            public void onSuccess(Integer result) {
                switch (result) {
                    case 0:
                        showToast("保存成功");
                        break;
                    case 1:
                        showToast("保存失败");
                        break;
                }
            }
        });
    }


    private static class ImageAdapter extends PagerAdapter {
        private List<String> datas;
        private LayoutInflater inflater;
        private Context context;

        public ImageAdapter(Context context, List<String> datas) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_image_details, container, false);
            if (view != null) {
                final ImageView imageView = view.findViewById(R.id.image);

                //loading
//                final ProgressBar loading = new ProgressBar(context);
//                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                loadingLayoutParams.gravity = Gravity.CENTER;
//                loading.setLayoutParams(loadingLayoutParams);
//                ((FrameLayout) view).addView(loading);

                final String imgurl = datas.get(position);
//                Glide.with(context).load(imgurl).thumbnail(0.1f).load(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        if (imageView instanceof ImageView) {
//                            ((ImageView) imageView).setImageDrawable(resource);
//                        } else {
//                            imageView.setBackground(resource);
//                        }
//                    }
//                });
                ImageManager.load(context, imageView, imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
//                Glide.with(context)
//                        .load(imgurl)
//                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
//                        .into(new GlideDrawableImageViewTarget(imageView) {
//                            @Override
//                            public void onLoadStarted(Drawable placeholder) {
//                                super.onLoadStarted(placeholder);
//                                loading.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                loading.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                                super.onResourceReady(resource, animation);
//                                loading.setVisibility(View.GONE);
//
//                            }
//                        });

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
