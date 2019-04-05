package com.shuyun.easyimg.core;

public abstract class AbstractImageCache implements ImageCache {

    protected abstract ImageCache newImageCache();

    public void release(){

    }

}
