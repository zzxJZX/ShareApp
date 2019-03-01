package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/24.
 */

public class LogisticsListInstance {

    private int aid;

    private int bid;

    private int cancel_status;

    private int cid;

    private int city;

    private int contain;

    private String courier_name;

    private String courier_phone;

    private int days;

    private int goods_protect_rules_id;

    private int goods_tid;

    private int id;

    private String logistics_id;

    private String mark;

    private String member_address;

    private String member_name;

    private String member_phone;

    private String oid;

    private String price;

    private int province;

    private int status;

    private int stock_id;

    private int store_id;

    public LogisticsListInstance(int aid , int bid, int cancel_status,int cid , int city , int contain, String courier_name,
                                 String courier_phone,int days,int goods_protect_rules_id,int goods_tid,int id,String logistics_id,String mark,String member_address
    ,String member_name,String member_phone,String oid,String price,int province,int status,int stock_id,int store_id){
        this.aid = aid;
        this.bid = bid;
        this.cancel_status = cancel_status;
        this.cid = cid;
        this.city = city;
        this.contain = contain;
        this.courier_name = courier_name;
        this.courier_phone = courier_phone;
        this.days = days;
        this.goods_protect_rules_id = goods_protect_rules_id;
        this.goods_tid = goods_tid;
        this.id = id;
        this.logistics_id = logistics_id;
        this.mark = mark;
        this.member_address = member_address;
        this.member_name = member_name;
        this.member_phone = member_phone;
        this.oid = oid;
        this.price = price;
        this.province = province;
        this.status = status;
        this.stock_id = stock_id;
        this.store_id = store_id;


    }


    public int getAid() {
        return aid;
    }

    public int getBid() {
        return bid;
    }

    public int getCancel_status() {
        return cancel_status;
    }

    public int getCid() {
        return cid;
    }

    public int getCity() {
        return city;
    }

    public int getContain() {
        return contain;
    }

    public String getCourier_name() {
        return courier_name;
    }

    public String getCourier_phone() {
        return courier_phone;
    }

    public int getDays() {
        return days;
    }

    public int getGoods_protect_rules_id() {
        return goods_protect_rules_id;
    }

    public int getGoods_tid() {
        return goods_tid;
    }

    public int getId() {
        return id;
    }

    public String getLogistics_id() {
        return logistics_id;
    }

    public String getMark() {
        return mark;
    }

    public String getMember_address() {
        return member_address;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getMember_phone() {
        return member_phone;
    }

    public String getOid() {
        return oid;
    }

    public String getPrice() {
        return price;
    }

    public int getProvince() {
        return province;
    }

    public int getStatus() {
        return status;
    }

    public int getStock_id() {
        return stock_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setCancel_status(int cancel_status) {
        this.cancel_status = cancel_status;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }

    public void setCourier_name(String courier_name) {
        this.courier_name = courier_name;
    }

    public void setCourier_phone(String courier_phone) {
        this.courier_phone = courier_phone;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setGoods_protect_rules_id(int goods_protect_rules_id) {
        this.goods_protect_rules_id = goods_protect_rules_id;
    }

    public void setGoods_tid(int goods_tid) {
        this.goods_tid = goods_tid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogistics_id(String logistics_id) {
        this.logistics_id = logistics_id;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setMember_address(String member_address) {
        this.member_address = member_address;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setMember_phone(String member_phone) {
        this.member_phone = member_phone;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
