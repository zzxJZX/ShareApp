package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/29.
 */

public class CouponsShowInstance {

    private int active_id;

    private String free ;

    private String total;

    public CouponsShowInstance(int active_id , String free , String total){
        this.active_id = active_id;
        this.free = free;
        this.total = total;
    }

    public int getActive_id() {
        return active_id;
    }

    public String getFree() {
        return free;
    }

    public String getTotal() {
        return total;
    }

    public void setActive_id(int active_id) {
        this.active_id = active_id;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
