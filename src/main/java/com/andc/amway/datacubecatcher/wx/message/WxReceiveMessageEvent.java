package com.andc.amway.datacubecatcher.wx.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息下的推送事件
 */
@Data
@XmlRootElement
public class WxReceiveMessageEvent extends WxReceiveCommonMessage {

    @JacksonXmlProperty(localName = "Event")
    private WxMessageEvent event;

    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;

}
