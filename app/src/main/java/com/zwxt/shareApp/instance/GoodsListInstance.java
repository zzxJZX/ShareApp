package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/23.
 */

public class GoodsListInstance {
    private int aid ;
    private int bid ;
    private int cid ;
    private int discount_store_month ;
    private String goods_name ;
    private int id ;
    private String image ;
    private int level ;
    private int price_rent ;
    private int price_sale ;
    private int price_store ;
    private int tid ;

    public GoodsListInstance(int aid , int bid , int cid,int discount_store_month , String goods_name , int id , String image , int level , int price_rent , int price_sale , int price_store ,int tid){
        this.aid = aid;
        this.bid = bid;
        this.cid = cid;
        this.discount_store_month = discount_store_month;
        this.goods_name = goods_name;
        this.id = id;
        this.image = image;
        this.level = level;
        this.price_sale = price_sale;
        this.price_rent = price_rent;
        this.price_store = price_store;
        this.tid = tid;
    }


    public int getAid() {
        return aid;
    }

    public int getBid() {
        return bid;
    }

    public int getCid() {
        return cid;
    }

    public int getDiscount_store_month() {
        return discount_store_month;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice_rent() {
        return price_rent;
    }

    public int getPrice_sale() {
        return price_sale;
    }

    public int getPrice_store() {
        return price_store;
    }

    public int getTid() {
        return tid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setDiscount_store_month(int discount_store_month) {
        this.discount_store_month = discount_store_month;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPrice_rent(int price_rent) {
        this.price_rent = price_rent;
    }

    public void setPrice_sale(int price_sale) {
        this.price_sale = price_sale;
    }

    public void setPrice_store(int price_store) {
        this.price_store = price_store;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
