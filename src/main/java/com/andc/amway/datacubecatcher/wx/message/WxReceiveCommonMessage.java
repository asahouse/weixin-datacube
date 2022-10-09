package com.andc.amway.datacubecatcher.wx.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * WX MP 平台下通用接收消息类
 */
@Data
@XmlRootElement
public class WxReceiveCommonMessage implements Serializable{

    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    @JacksonXmlProperty(localName = "MsgType")
    private MsgType msgType;

    public enum MsgType {
        text, image, voice, video, shortvideo, link, event, location
    }
}
