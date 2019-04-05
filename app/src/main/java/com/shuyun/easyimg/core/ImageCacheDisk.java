package com.shuyun.easyimg.core;

import android.os.Environment;

import com.shuyun.easyimg.common.Image;
import com.shuyun.easyimg.common.Logg;
import com.shuyun.easyimg.common.Optional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An Image cache in local disk. Use DiskLruCache to cache Image in device ROM
 *
 * @Author shuyun
 * @Create 2019/2/21 0021 19:07
 * @Update 2019/2/21 0021 19:07
 */
public class ImageCacheDisk extends AbstractImageCache {

    private DiskLruCache cache;
    private final String dir = Environment.getDataDirectory()+"/cache/image";
    /**
     * Use 10MB size for image caching
     */
    private final long maxCacheSize = 10*1024*1024;
    private final long tryLockTimeout = 100;
    /**
     * index for valueCount
     */
    private final int index = 0;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
//    private Condition readCdt = readLock.newCondition();
//    private Condition writeCdt = writeLock.newCondition();

    private ImageCacheDisk(){
        try {
            cache = DiskLruCache.open(new File(dir), 1, 1, maxCacheSize);
        } catch (IOException e) {
            Logg.e(e.getMessage());
        }
    }

    @Override
    protected ImageCache newImageCache() {
        return new ImageCacheDisk();
    }

    @Override
    public void release() {
        super.release();
        try {
            cache.close();
        } catch (IOException e) {
            Logg.e(e.getMessage());
        }
    }

    @Override
    public Optional<Image> obtainImage(String id) {
        Image image = null;
        try {
            boolean res = readLock.tryLock(tryLockTimeout, TimeUnit.MILLISECONDS);
            if (res) {
                DiskLruCache.Editor editor = cache.edit(id);
                InputStream is = editor.newInputStream(index);
                if (null == is) {
                    return Optional.empty();
                }
                ObjectInputStream ois = new ObjectInputStream(is);
                image = (Image) ois.readObject();
            }
        } catch (Exception e) {
            Logg.e(e.getMessage());
        } finally {
            readLock.unlock();
        }
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<Boolean> cacheImage(Image image) {
        try {
            boolean res = writeLock.tryLock(tryLockTimeout, TimeUnit.MILLISECONDS);
            if (res) {
                DiskLruCache.Editor editor = cache.edit(image.getId());
                OutputStream os = editor.newOutputStream(index);
                if (null == os) {
                    return Optional.of(false);
                }
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(image);
                oos.close();
            }
        } catch (Exception e) {
            Logg.e(e.getMessage());
        } finally {
            writeLock.unlock();
        }
        return Optional.of(true);
    }
}
