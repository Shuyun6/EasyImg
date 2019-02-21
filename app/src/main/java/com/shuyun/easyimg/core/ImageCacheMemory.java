package com.shuyun.easyimg.core;

import android.util.LruCache;

import com.shuyun.easyimg.common.Image;
import com.shuyun.easyimg.common.Optional;

/**
 * An RAM cache via LruCache in Android which extend by LinkedHashMap
 * Use 1/8 of app's RAM space for caching Image
 * @Author shuyun
 * @Create 2019/2/21 0021 19:06
 * @Update 2019/2/21 0021 19:06
 */
final public class ImageCacheMemory extends AbstractImageCache {

    LruCache<String, Image> cache;

    @Override
    public AbstractImageCache newInstance() {
        return new ImageCacheMemory();
    }

    private ImageCacheMemory() {
        final int maxMemory = (int) (Runtime.getRuntime().totalMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        cache = new LruCache<String, Image>(cacheSize){
            @Override
            protected int sizeOf(String key, Image value) {
                if (null == value) {
                    return 0;
                }
                return value.getSize() / 1024;
            }
        };
    }

    @Override
    public void release() {
        super.release();
        cache = null;
    }

    @Override
    public Optional<Image> obtainImage(String id) {
        final Image image = cache.get(id);
        if (null == image) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(image);
        }
    }

    @Override
    public Optional<Boolean> cacheImage(Image image) {
        final Image res = cache.put(image.getId(), image);
        return Optional.of(null == res);
    }
}
