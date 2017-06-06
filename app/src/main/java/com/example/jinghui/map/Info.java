package com.example.jinghui.map;

import java.io.Serializable;

/**
 * Created by zhaoxin on 2017-05-03.
 */

public class Info implements Serializable {
    private double latitude;
    private double longitude;
    private String name;
    public Info(double latitude, double longitude, String name
                ) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;

        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
