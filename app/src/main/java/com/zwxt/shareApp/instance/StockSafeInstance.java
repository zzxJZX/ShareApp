package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/27.
 */

public class StockSafeInstance {

    private int id ;

    private float price ;

    private String safe_name;

    public StockSafeInstance(int id , float price , String safe_name){
        this.id = id;
        this.price = price;
        this.safe_name = safe_name;
    }


    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public String getSafe_name() {
        return safe_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSafe_name(String safe_name) {
        this.safe_name = safe_name;
    }
}
