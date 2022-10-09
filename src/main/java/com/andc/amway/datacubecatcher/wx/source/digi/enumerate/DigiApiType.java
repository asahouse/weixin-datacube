package com.andc.amway.datacubecatcher.wx.source.digi.enumerate;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/29.
 */
public enum DigiApiType {

    authorize("/api/authorize/"),
    signature("/api/signature/"),
    access_token("/api/access_token/");

    private String value;

    DigiApiType(String value){
        this.value = value;
    }

    public static DigiApiType fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (DigiApiType type : values()) {
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
