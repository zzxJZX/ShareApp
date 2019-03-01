package com.zwxt.shareApp.instance;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/29.
 */

public class CouponsListInstance implements Serializable {

    private int active_id;

    private String created_at;

    private int id;

    private String money_free;

    private float money_total;

    private String phone;

    private int status;

    private String time_end;

    private String time_start;

    private String type;

    private String updated_at;

    public CouponsListInstance(int active_id , String created_at , int id , String money_free , float money_total , String phone , int status , String time_end , String time_start , String type , String updated_at){
        this.active_id = active_id;
        this.created_at = created_at;
        this.id = id;
        this.money_free = money_free;
        this.money_total = money_total;
        this.phone = phone;
        this.status = status;
        this.time_end = time_end;
        this.time_start = time_start;
        this.type = type;
        this.updated_at = updated_at;
    }

    public int getActive_id() {
        return active_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }

    public String getMoney_free() {
        return money_free;
    }

    public float getMoney_total() {
        return money_total;
    }

    public String getPhone() {
        return phone;
    }

    public int getStatus() {
        return status;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getType() {
        return type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setActive_id(int active_id) {
        this.active_id = active_id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMoney_free(String money_free) {
        this.money_free = money_free;
    }

    public void setMoney_total(float money_total) {
        this.money_total = money_total;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
