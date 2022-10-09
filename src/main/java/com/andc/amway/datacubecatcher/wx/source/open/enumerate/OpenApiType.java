package com.andc.amway.datacubecatcher.wx.source.open.enumerate;

import java.util.Optional;

public enum OpenApiType {
    detail("/resource/{appId}/detail"),
    token("/resource/{appId}/token");

    private String value;

    OpenApiType(String value){
        this.value = value;
    }

    public static OpenApiType fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (OpenApiType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
        }
        return null;
    }

    public String toValue(String appId) {
        return value.replace("{appId}", appId);
    }
}
