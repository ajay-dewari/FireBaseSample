package com.ajaysinghdewari.firebasesample.models;

/**
 * Created by justdial on 23/5/17.
 */

public class UserInformation {
    private String name, address;

    public UserInformation(String name, String address){
        this.name=name;
        this.address=address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
