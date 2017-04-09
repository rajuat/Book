package com.itservz.bookex.android.model;

import java.io.Serializable;

/**
 * Created by Raju on 12/28/2016.
 */

public class Location implements Serializable{
    public double latitude;
    public double longitude;

    public Location(){
        //firebase
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
