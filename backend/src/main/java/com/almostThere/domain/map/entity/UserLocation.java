package com.almostThere.domain.map.entity;

import java.io.Serializable;

public class UserLocation implements Serializable {

    private String nickName;
    private double lat;
    private double lng;

    public UserLocation(String nickName, double lat, double lng){
        this.nickName = nickName;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
            "nickName='" + nickName + '\'' +
            ", lat=" + lat +
            ", lng=" + lng +
            '}';
    }
}

