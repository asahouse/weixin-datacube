package com.andc.amway.datacubecatcher.wx.message.custommenu;

import com.andc.amway.datacubecatcher.wx.message.WxReceiveMessageEvent;
import com.andc.amway.datacubecatcher.wx.message.custommenu.info.WxReceiveMessageCustomSendLocationInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息下的推送事件
 */
@Data
@XmlRootElement
public class WxReceiveMessageCustomLocationSelectEvent extends WxReceiveMessageEvent {
    @JacksonXmlProperty(localName = "SendLocationInfo")
    private WxReceiveMessageCustomSendLocationInfo sendLocationInfo;
}
