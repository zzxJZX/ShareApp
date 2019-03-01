package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/23.
 */

public class StoreTypeInstance {

    private int id;

    private String type_name;

    public StoreTypeInstance(int id,String type_name){
        this.id = id;
        this.type_name = type_name;
    }

    public int getId() {
        return id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
