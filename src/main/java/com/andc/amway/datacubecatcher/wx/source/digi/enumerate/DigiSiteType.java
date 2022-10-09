package com.andc.amway.datacubecatcher.wx.source.digi.enumerate;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/29.
 */
public enum DigiSiteType {
    nutrilite("wxapi.nutrilite.oneoone101.com"),
    artistry("wxapi.artistry.oneoone101.com");

    private String value;

    DigiSiteType(String value){
        this.value = value;
    }

    public static DigiSiteType fromSite(String site){
        if (Optional.ofNullable(site).isPresent()) {
            for (DigiSiteType type : values()) {
                if (type.toString().equals(site)) {
                    return type;
                }
            }
        }
        return null;
    }

    public static DigiSiteType fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (DigiSiteType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
        }
        return null;
    }

    public String toValue() {
        return value;
    }
}
