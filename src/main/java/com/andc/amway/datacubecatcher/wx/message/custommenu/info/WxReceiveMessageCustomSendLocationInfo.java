package com.andc.amway.datacubecatcher.wx.message.custommenu.info;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息下的推送事件
 * pic_sysphoto：弹出系统拍照发图的事件推送
 */
@Data
public class WxReceiveMessageCustomSendLocationInfo implements Serializable {

    @JacksonXmlProperty(localName = "Location_X")
    private Double Location_X;

    @JacksonXmlProperty(localName = "Location_Y")
    private Double Location_Y;

    @JacksonXmlProperty(localName = "Scale")
    private Integer scale;

    @JacksonXmlProperty(localName = "Label")
    private String label;

    @JacksonXmlProperty(localName = "Poiname")
    private String poiname;

}
