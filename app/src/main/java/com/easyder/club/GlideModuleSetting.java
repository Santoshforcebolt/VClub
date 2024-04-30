package com.easyder.club;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Auther:  winds
 * Data:    2017/11/6
 * Version: 1.0
 * Desc:
 */

@GlideModule
public class GlideModuleSetting extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, AppConfig.DEFAULT_CACHE_PATH, AppConfig.DISK_CACHE_SIZE));
        builder.setMemoryCache(new LruResourceCache(AppConfig.MAX_MEMORY_CACHE_SIZE));
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
    }
}
