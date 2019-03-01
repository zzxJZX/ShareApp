package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/21.
 */

public class RecommendInstance {
    private int id ;
    private String store_name;
    private int province;
    private int city;
    private String admin_phone;
    private String position;
    private String address;
    private String phone;
    private String image;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public RecommendInstance(int id, String store_name, int province, int city  , String admin_phone, String position, String address , String phone , String image) {
        this.id = id;
        this.store_name = store_name;
        this.province = province;
        this.city = city;
        this.admin_phone = admin_phone;
        this.position = position;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getStore_name() {
        return store_name;
    }

    public int getProvince() {
        return province;
    }

    public int getCity() {
        return city;
    }

    public String getAdmin_phone() {
        return admin_phone;
    }

    public String getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setAdmin_phone(String admin_phone) {
        this.admin_phone = admin_phone;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
