package com.andc.amway.datacubecatcher.wx.api;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/30.
 */
public enum WxApiInterface {
    datacube("/datacube");

    private String value;

    WxApiInterface(String value){
        this.value = value;
    }

    public static WxApiInterface fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (WxApiInterface inter : values()) {
                if (inter.value.equals(value)) {
                    return inter;
                }
            }
        }
        return null;
    }

    public String toValue() {
        return value;
    }

}
