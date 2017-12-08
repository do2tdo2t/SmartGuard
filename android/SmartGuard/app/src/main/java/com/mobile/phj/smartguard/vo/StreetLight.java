package com.mobile.phj.smartguard.vo;

/**
 * Created by user on 2017-12-08.
 */

public class StreetLight {
    public String id;
    public int code;
    public double lat;
    public double lon;
    public String info;
    public int alram;

    @Override
    public String toString() {
        return "StreetLight" +
                "id='" + id + '\'' +
                ", code=" + code +
                ", lat=" + lat +
                ", lon=" + lon +
                ", info='" + info + '\'' +
                ", alram=" + alram ;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getAlram() {
        return alram;
    }

    public void setAlram(int alram) {
        this.alram = alram;
    }
}
