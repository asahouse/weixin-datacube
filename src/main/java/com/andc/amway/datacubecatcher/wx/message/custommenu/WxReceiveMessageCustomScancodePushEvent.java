package com.andc.amway.datacubecatcher.wx.message.custommenu;

import com.andc.amway.datacubecatcher.wx.message.WxReceiveMessageEvent;
import com.andc.amway.datacubecatcher.wx.message.custommenu.info.WxReceiveMessageCustomScanCodeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息下的推送事件
 * scancode_push：扫码推事件的事件推送
 */
@Data
@XmlRootElement
public class WxReceiveMessageCustomScancodePushEvent extends WxReceiveMessageEvent {
    @JacksonXmlProperty(localName = "ScanCodeInfo")
    private WxReceiveMessageCustomScanCodeInfo scanCodeInfo;
}
