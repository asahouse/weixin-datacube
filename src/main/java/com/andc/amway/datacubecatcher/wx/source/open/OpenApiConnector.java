package com.andc.amway.datacubecatcher.wx.source.open;

import com.andc.amway.datacubecatcher.wx.Protocol;
import com.andc.amway.datacubecatcher.wx.source.open.enumerate.OpenApiType;
import com.andc.amway.datacubecatcher.wx.source.open.enumerate.OpenAuthType;

public class OpenApiConnector {

    public static String getAuthPath(Protocol protocol,
                                     String path,
                                     OpenAuthType authType){
        StringBuffer sb = new StringBuffer(protocol.toValue());
        return sb.append(path).append(authType.toValue()).toString();
    }

    public static String getApiPath(Protocol protocol,
                                    String path,
                                    OpenApiType apiType,
                                    String appId){
        StringBuffer sb = new StringBuffer(protocol.toValue());
        return sb.append(path).append(apiType.toValue(appId)).toString();
    }
}
