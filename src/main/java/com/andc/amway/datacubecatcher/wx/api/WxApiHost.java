package com.andc.amway.datacubecatcher.wx.api;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/30.
 */
public enum WxApiHost {
    official("api.weixin.qq.com");

    private String value;

    WxApiHost(String value){
        this.value = value;
    }

    public static WxApiHost fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (WxApiHost type : values()) {
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
