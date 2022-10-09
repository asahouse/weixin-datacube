package com.andc.amway.datacubecatcher.wx.source.open.enumerate;

import java.util.Optional;

public enum OpenAuthType {
    oauth_client("/auth/oauth/token?grant_type=client_credentials");

    private String value;

    OpenAuthType(String value){
        this.value = value;
    }

    public static OpenAuthType fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (OpenAuthType type : values()) {
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
