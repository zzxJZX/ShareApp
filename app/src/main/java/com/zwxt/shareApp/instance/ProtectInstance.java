package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/23.
 */

public class ProtectInstance {

    private int id;

    private int aid;

    private int bid;

    private int cid;

    private int days;

    private float price;

    private String content;

    public ProtectInstance(int id , int aid, int bid,int cid,int days,float price,String content){
        this.id = id;
        this.aid = aid;
        this.bid = bid;
        this.cid = cid;
        this.days = days;
        this.price = price;
        this.content = content;
    }

    public int getId() {
        return id;
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

    public int getDays() {
        return days;
    }

    public float getPrice() {
        return price;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setDays(int days) {
        this.days = days;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
