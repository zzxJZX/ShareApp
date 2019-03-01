package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/29.
 */

public class ComplainListInstance {

    private int id;

    private String title;

    private String phone;

    private String oid;

    private int status;

    public ComplainListInstance(int id , String title , String phone , String oid , int status){
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.oid = oid;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getOid() {
        return oid;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
