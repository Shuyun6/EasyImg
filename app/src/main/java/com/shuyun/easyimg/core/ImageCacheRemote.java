package com.shuyun.easyimg.core;

import com.shuyun.easyimg.common.Optional;

/**
 * Get and put image to remote repo
 *
 * Consider that user has a customized remote repo for image. Obtain/get image from remote repo is
 * to post a request to service then parse response to image for caching. Cache/put image is to post
 * a image file to service for storage.
 *
 * These operations can be simplify to "url -> Image" & "Image -> Service"
 * The vital operation is Http request from an URL to service
 *
 * @Author shuyun
 * @Create 2019/2/21 0021 19:00
 * @Update 2019/2/21 0021 19:00
 */
public class ImageCacheRemote extends AbstractImageCache {

    private RemoteImageAdapter adapter;

    public void setAdapter(RemoteImageAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Optional<Image> obtainImage(String id) {
        Object object = adapter.convertFromRawData();
        Image image = new Image();
        image.setObject(object);
        return Optional.ofNullable(image);
    }

    @Override
    public Optional<Boolean> cacheImage(Image image) {
        return null;
    }

    @Override
    protected ImageCache newImageCache() {
        return new ImageCacheRemote();
    }

}
