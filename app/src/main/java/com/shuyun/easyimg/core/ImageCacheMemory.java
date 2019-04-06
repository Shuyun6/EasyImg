package com.shuyun.easyimg.core;

import android.util.LruCache;

import com.shuyun.easyimg.common.Optional;

import java.lang.ref.SoftReference;

/**
 * An RAM cache via LruCache in Android which extend by LinkedHashMap
 * Use 1/8 of app's RAM space for caching Image
 * @Author shuyun
 * @Create 2019/2/21 0021 19:06
 * @Update 2019/2/21 0021 19:06
 */
final public class ImageCacheMemory extends AbstractImageCache {

    private LruCache<String, SoftReference<Image>> cache;

    private ImageCacheMemory() {
        final int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        cache = new LruCache<String, SoftReference<Image>>(cacheSize){
            @Override
            protected int sizeOf(String key, SoftReference<Image> value) {
                if (null == value || null == value.get()) {
                    return 0;
                }
                return value.get().getSize() / 1024;
            }
        };
    }

    @Override
    protected ImageCache newImageCache() {
        return new ImageCacheMemory();
    }

    @Override
    public void release() {
        super.release();
        cache = null;
    }

    @Override
    public Optional<Image> obtainImage(String id) {
        final SoftReference<Image> soft = cache.get(id);
        if (null == soft) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(soft.get());
        }
    }

    @Override
    public Optional<Boolean> cacheImage(Image image) {
        final SoftReference<Image> res = cache.put(image.getId(), new SoftReference<>(image));
        return Optional.of(null == res);
    }
}
