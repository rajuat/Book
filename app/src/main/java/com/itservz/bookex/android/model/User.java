package com.itservz.bookex.android.model;

import java.io.Serializable;

/**
 * Created by Raju on 12/7/2016.
 */

public class User implements Serializable {
    public String name;
    public String phone;
    public String email;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
