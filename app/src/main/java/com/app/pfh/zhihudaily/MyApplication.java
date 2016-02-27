package com.app.pfh.zhihudaily;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyApplication extends Application {
    private static RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(config);

    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static RequestQueue getQueue(){
        return queue;
    }
}
