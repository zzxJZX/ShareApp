package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/22.
 */

public class MailInstance {

    private int id;

    private String phone;

    private int province;

    private int city;

    private String address;

    private String name;

    private String order_phone;

    public MailInstance(int id,String phone,int province,int city,String address,String name,String order_phone){
        this.id = id;
        this.phone = phone;
        this.province =province;
        this.city = city;
        this.address = address;
        this.name = name;
        this.order_phone = order_phone;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public int getProvince() {
        return province;
    }

    public int getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getOrder_phone() {
        return order_phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder_phone(String order_phone) {
        this.order_phone = order_phone;
    }
}
