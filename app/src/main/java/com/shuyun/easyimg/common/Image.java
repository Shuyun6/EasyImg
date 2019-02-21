package com.shuyun.easyimg.common;

import android.support.annotation.NonNull;

import java.io.Serializable;

final public class Image implements Serializable {

    public enum IMAGE_TYPE {
        BITMAP, DRAWABLE, FILE, BASE64, BYTES, OTHERS
    }

    @NonNull
    private String id;

    private IMAGE_TYPE type;

    private Object object;

    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IMAGE_TYPE getType() {
        return type;
    }

    public void setType(IMAGE_TYPE type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
