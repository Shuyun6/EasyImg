package com.shuyun.easyimg.common;

import java.io.Serializable;

final public class Image implements Serializable {

    public enum TYPE {
        //
        BITMAP(0),
        DRAWABLE(1),
        FILE(2),
        BASE64(3),
        BYTES(4),
        OTHERS(99);
        int value;

        TYPE(int value) {
            this.value = value;
        }
    }

    private String id;

    private int type;

    private Object object;

    private int size;

    public Image(){

    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
