package com.andc.amway.datacubecatcher.wx.api;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/8/8.
 */
public enum WxApiInvokeRange {
    GetArticleSummary(1L),
    GetArticleTotal(1L),
    GetUserRead(3L),
    GetUserReadHour(1L),
    GetUserShare(7L),
    GetUserShareHour(1L),

    GetUserSummary(7L),
    GetUserCumulate(7L),

    GetUpStreamMsg(7L),
    GetUpStreamMsgHour(1L),
    GetUpStreamMsgWeek(30L),
    GetUpStreamMsgMonth(30L),
    GetUpStreamMsgDist(15L),
    GetUpStreamMsgDistWeek(30L),
    GetUpStreamMsgDistMonth(30L),

    GetInterfaceSummary(30L),
    GetInterfaceSummaryHour(1L);

    private Long value;

    WxApiInvokeRange(Long value){
        this.value = value;
    }

    public static WxApiInvokeRange fromValue(Long value){
        if (Optional.ofNullable(value).isPresent()) {
            for (WxApiInvokeRange range : values()) {
                if (range.value.equals(value)) {
                    return range;
                }
            }
        }
        return null;
    }

    public Long toValue() {
        return value;
    }

    public static WxApiInvokeRange fromWxApiType(WxApiType type){
        if (Optional.ofNullable(type).isPresent()) {
            for (WxApiInvokeRange range : values()) {
                if (range.name().equals(type.name())) {
                    return range;
                }
            }
        }
        return null;
    }
}
