package com.easyder.club.widget;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


/**
 * Author：sky on 2019/5/30 15:40.
 * Email：xcode126@126.com
 * Desc：裁剪图片底部
 */

public class GlideScaleBottomTransform extends BitmapTransformation {
    private float radio;

    public GlideScaleBottomTransform(float radio) {
        this.radio = radio;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cropBitmap(pool, toTransform);
    }

    private Bitmap cropBitmap(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        float w = result.getWidth();
        float h = result.getHeight();
        float v = h / w;
        if (h > w && radio > 0 && v > radio) {
            float diff = v - radio;
            float start = diff * w;
            result = Bitmap.createBitmap(source, 0, (int) start, (int) w, (int) (h - start));
        }
        return result;
    }

    public String getId() {
        return getClass().getName() + Math.round(radio);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
