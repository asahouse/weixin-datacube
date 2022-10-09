package com.andc.amway.datacubecatcher.wx.source;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/7/2.
 */
public enum SourceType {
    Digi, Open;

    public static SourceType fromName(String name){
        if (Optional.ofNullable(name).isPresent()) {
            for (SourceType type : values()) {
                if (type.toString().equals(name)) {
                    return type;
                }
            }
        }
        return null;
    }
}
