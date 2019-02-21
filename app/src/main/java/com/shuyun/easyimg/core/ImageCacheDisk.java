package com.shuyun.easyimg.core;

import android.graphics.Path;
import android.os.Environment;

import com.shuyun.easyimg.common.Image;
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

public class ImageCacheDisk extends AbstractImageCache {

    private DiskLruCache cache;
    private final String dir = Environment.getDataDirectory()+"/cache";
    private final long maxCacheSize = 10*1024*1024;//Use 10MB size for image caching
    private final long tryLockTimeout = 100;//ms
    private final int index = 0;//index for valueCount

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private Condition readCdt = readLock.newCondition();
    private Condition writeCdt = writeLock.newCondition();

    @Override
    public AbstractImageCache newInstance() {
        return new ImageCacheDisk();
    }

    private ImageCacheDisk(){
        try {
            cache = DiskLruCache.open(new File(dir), 1, 1, maxCacheSize);
        } catch (IOException e) {

        }
    }

    @Override
    public void release() {
        super.release();
        try {
            cache.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        } finally {
            readLock.unlock();
        }
        return Optional.of(true);
    }
}
