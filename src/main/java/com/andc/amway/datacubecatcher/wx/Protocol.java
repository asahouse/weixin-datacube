package com.andc.amway.datacubecatcher.wx;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/29.
 */
public enum Protocol {
    http("http://"),https("https://");

    private String value;

    Protocol(String value){
        this.value = value;
    }

    public static Protocol fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (Protocol protocol : values()) {
                if (protocol.value.equals(value)) {
                    return protocol;
                }
            }
        }
        return null;
    }

    public String toValue() {
        return value;
    }
}
