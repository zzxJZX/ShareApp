package com.zwxt.shareApp.instance;

/**
 * Created by Administrator on 2018/8/22.
 */

public class ProvinceInstance {

    private int id;

    private String area_name;

    private int fid;

    private int level;

    private String position;

    public ProvinceInstance(int id,String area_name,int fid,int level,String position){
        this.id = id;
        this.area_name = area_name;
        this.fid = fid;
        this.level = level;
        this.position = position;
    }


    public int getId() {
        return id;
    }

    public String getArea_name() {
        return area_name;
    }

    public int getFid() {
        return fid;
    }

    public int getLevel() {
        return level;
    }

    public String getPosition() {
        return position;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
