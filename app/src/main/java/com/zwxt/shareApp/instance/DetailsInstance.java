package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/22.
 */

public class DetailsInstance {

    private int free;

    private String store_type_name;

    private int total;

    private float price;

    private int store_id;

    private int store_type_id;

    private String create_at;

    private float discount_store_month;

    private int id;

    private String updated_at;

    public DetailsInstance(int free , String store_type_name , int total , float price , int store_id , int store_type_id , String create_at, float discount_store_month, int id, String updated_at){
        this.free = free;
        this.store_type_name = store_type_name;
        this.total = total;
        this.price = price;
        this.store_id = store_id;
        this.store_type_id = store_type_id;
        this.create_at = create_at;
        this.discount_store_month = discount_store_month;
        this.id = id;
        this.updated_at = updated_at;
    }

    public int getFree() {
        return free;
    }

    public String getStore_type_name() {
        return store_type_name;
    }

    public int getTotal() {
        return total;
    }

    public float getPrice() {
        return price;
    }

    public int getStore_id() {
        return store_id;
    }

    public int getStore_type_id() {
        return store_type_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public float getDiscount_store_month() {
        return discount_store_month;
    }

    public int getId() {
        return id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public void setStore_type_name(String store_type_name) {
        this.store_type_name = store_type_name;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setStore_type_id(int store_type_id) {
        this.store_type_id = store_type_id;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public void setDiscount_store_month(float discount_store_month) {
        this.discount_store_month = discount_store_month;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
