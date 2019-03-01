package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/27.
 */

public class PositionListInstance {

    private int id;

    private String store_name;

    private String position;

    public PositionListInstance(int id , String store_name , String position){
        this.id = id;
        this.store_name = store_name;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getPosition() {
        return position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
