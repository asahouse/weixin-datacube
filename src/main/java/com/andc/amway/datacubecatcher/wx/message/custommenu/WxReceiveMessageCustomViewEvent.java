package com.andc.amway.datacubecatcher.wx.message.custommenu;

import com.andc.amway.datacubecatcher.wx.message.WxReceiveMessageEvent;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 消息下的推送事件
 * 点击菜单跳转链接时的事件推送
 */
@Data
public class WxReceiveMessageCustomViewEvent extends WxReceiveMessageEvent {
    @JacksonXmlProperty(localName = "MenuID")
    private String menuID;
}
