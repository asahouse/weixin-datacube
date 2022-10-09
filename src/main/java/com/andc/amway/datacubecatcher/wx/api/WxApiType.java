package com.andc.amway.datacubecatcher.wx.api;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/6/30.
 */
public enum WxApiType {
    //图文分析接口
    GetArticleSummary("/getarticlesummary"),
    GetArticleTotal("/getarticletotal"),
    GetUserRead("/getuserread"),
    GetUserReadHour("/getuserreadhour"),
    GetUserShare("/getusershare"),
    GetUserShareHour("/getusersharehour"),

    //用户分析接口
    GetUserSummary("/getusersummary"),
    GetUserCumulate("/getusercumulate"),

    //消息分析接口
    GetUpStreamMsg("/getupstreammsg"),
    GetUpStreamMsgHour("/getupstreammsghour"),
    GetUpStreamMsgWeek("/getupstreammsgweek"),
    GetUpStreamMsgMonth("/getupstreammsgmonth"),
    GetUpStreamMsgDist("/getupstreammsgdist"),
    GetUpStreamMsgDistWeek("/getupstreammsgdistweek"),
    GetUpStreamMsgDistMonth("/getupstreammsgdistmonth"),

    //接口分析接口
    GetInterfaceSummary("/getinterfacesummary"),
    GetInterfaceSummaryHour("/getinterfacesummaryhour");

    private String value;

    WxApiType(String value){
        this.value = value;
    }

    public static WxApiType fromValue(String value){
        if (Optional.ofNullable(value).isPresent()) {
            for (WxApiType type : values()) {
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
